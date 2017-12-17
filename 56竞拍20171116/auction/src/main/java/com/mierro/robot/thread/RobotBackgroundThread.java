package com.mierro.robot.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mierro.authority.common.SystemSettings;
import com.mierro.common.common.SpringTool;
import com.mierro.main.common.RunningProgram;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.global.ItemCache;
import com.mierro.robot.entity.RobotItemBean;
import com.mierro.robot.entity.e.RobotConfig;
import com.mierro.robot.entity.view.RechargeCoinConsume;
import com.mierro.robot.service.RobotService;
import com.mierro.robot.utils.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 机器人后台线程
 * Created by tlseek on 2017/8/12.
 */
public class RobotBackgroundThread extends Thread {

    private static Logger LOG = LogManager.getLogger(RobotBackgroundThread.class.getName());
    /**机器人当前期竞拍信息*/
    Map<Long, RobotItemTerm> robotItemTermMap = new ConcurrentHashMap<>();
    /**运行中*/
    private volatile boolean running = false;

    RobotService robotService;

    volatile boolean end = false;

    // 线程池
    ExecutorService auctionPool ;
    // 线程池
    ExecutorService executorPool ;

    public static ConcurrentLinkedQueue<RobotBackgroundThread> intances = new ConcurrentLinkedQueue<>();

    private Float coinCunsumeRate;
    private Float minMarketPrice;


    public void setCoinCunsumeRate(Float coinCunsumeRate) {
        this.coinCunsumeRate = coinCunsumeRate;
    }

    public void setMinMarketPrice(Float minMarketPrice) {
        this.minMarketPrice = minMarketPrice;
    }

    public RobotBackgroundThread(RobotService robotService) {
        super("机器人线程");
        this.robotService = robotService;
        intances.add(this);
        System.out.println("RobotBackgroundThread.RobotBackgroundThread");
        coinCunsumeRate = RobotConfig.COIN_CONSUME_RATE.deserialize(robotService.getConfig(RobotConfig.COIN_CONSUME_RATE));
        minMarketPrice = RobotConfig.MIN_MARKET_PRICE.deserialize(robotService.getConfig(RobotConfig.MIN_MARKET_PRICE));
        auctionPool = new ThreadPoolExecutor(5, 20,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
        executorPool = new ThreadPoolExecutor(5, 20,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }

    @SuppressWarnings("all")
    @Override
    public void run() {
//        int[] count = {0};
        LOG.info("机器人竞拍后台开始运行.........");
        try {

            while (SpringTool.getApplicationContext() == null){
                Thread.sleep(500);
            }
            // 平台退出维护期
            SystemSettings.maintenance = false;
            // 正在运行的暂存列表
            Map<Long, Boolean> isEnd = new ConcurrentHashMap<>();
            List<RobotItemTerm> cache = Collections.synchronizedList(new ArrayList<>());
            while (running) {
                long start = System.currentTimeMillis();

                Log logMain = new Log();
                List<RobotItemTerm> auctionTerm = ItemCache.itemMap.entrySet().stream().map(entry -> {
                     Long itemId = entry.getKey();
                     ItemCache.Item item = entry.getValue();

                     Log log = new Log();
                     long start2 = System.currentTimeMillis();
                     // 商品结算中退出
                     if (item.isSettlement() || item.getInterrupted()) {
//                            term.stop = true;
                         robotItemTermMap.remove(itemId);
                         return null;
                     }
                     // 获取当前期信息
                     RobotItemTerm term = Optional.ofNullable(robotItemTermMap.get(itemId)).orElseGet(() -> createRobotItemTerm(itemId, item, null));
                     if (term == null) {
                         return null;
                     }
                     // 获取下一次竞拍时间
                     NextRobotAuctionTime record = term.getNextRobotAuctionTime();
                     if (record == null || record.instant_time.before(item.getInstant_time())) {
                         record = new NextRobotAuctionTime(item.getInstant_time());
                         term.setNextRobotAuctionTime(record);
                     }
//                        LOG.debug("机器人将要竞拍商品 {} {} 时间 {}", itemId,item.getItemBean().getRunning_program(), record);
//                                    Log.debug(itemId, String.format("%d 机器人将要竞拍商品  时间 %s", (System.currentTimeMillis()-start2), record.toString()));
                     log.buffer(String.format("%d 机器人将要竞拍商品  时间 %s", (System.currentTimeMillis() - start2), record.toString()));
                     cache.add(term);

                    // 下一次竞拍时间大于当前时间时退出
                    if (record.willDoTime.getTime() - (System.currentTimeMillis() + 200) > 0) {
                        log.write(itemId);
                        return null;
                    }
                    term.item = item;
                    return term;
                }).filter(Objects::nonNull).collect(Collectors.toList());

                // 并发竞拍
                Function<RobotItemTerm, Callable<Void>> function = term -> () -> {
                    ItemCache.Item item = term.item;
                    Long itemId = term.itemId;
                    Log log = new Log();
                    long start2 = System.currentTimeMillis();
                    // 下限 是否超过最小成交价
                    BooleanSupplier moreThanMinMarketPrice = () -> Float.valueOf(item.getPrice()) > minMarketPrice;
                    // 上限 预计成本价 > 竞拍价 + 实际竞拍收益(实际竞拍数*每轮加价)
                    BooleanSupplier lessThan = () -> term.expectCost.compareTo(new BigDecimal(item.getPrice()).add(new BigDecimal(item.getActual_click_number()).multiply(new BigDecimal(item.getIncrease_the_price())))) > 0;


                    // 竞拍
                    switch (item.getItemBean().getRunning_program()) {
                        case NOTROBOT: {
                            // 什么都不做
                            // 设置下一次竞拍时间
                            term.setNextRobotAuctionTime(null);
                            break;
                        }
                        case ROBOT_SUCCESS: {
                            long start_robot_success = System.currentTimeMillis();
                            boolean isAuction = isRobotSuccessAuction(item, term, moreThanMinMarketPrice, lessThan);
//                                            Log.debug(itemId, "start_robot_success " + (System.currentTimeMillis()-start_robot_success));
                            String message;
                            if (isAuction) {
                                auction(itemId);
                                message = "[[[[机器人竞拍商品 {} 下一次竞拍时间 {}]";
                            } else {
                                message = "[[[[机器人跳过商品 {} 下一次竞拍时间 {}]";
                            }
                            // 设置下一次竞拍时间
                            term.setNextRobotAuctionTime(new NextRobotAuctionTime(new Date()));
//                                LOG.debug(message ,itemId, term.nextRobotAuctionTime);
//                                            Log.debug(itemId, "start_robot_success " + (System.currentTimeMillis()-start_robot_success)+ String.format(message.replaceAll("\\{\\}", "%s"), itemId.toString(), term.nextRobotAuctionTime.toString()) +" "+ term.toString()+" "+JSON.toJSONString(item));
//                                            Log.debug(itemId, "start_robot_success ", String.valueOf((System.currentTimeMillis()-start_robot_success)));
                            log.buffer("start_robot_success ", System.currentTimeMillis() - start_robot_success, message);
                            break;
                        }
                        case CONTROL_LINE_FIX:
                        case CONTROL_LINE_RANGE: {
                            long start_control = System.currentTimeMillis();
                            boolean isAuction = isControlLineAuction(item, term, moreThanMinMarketPrice);
                            String message;
                            if (isAuction) {
                                auction(itemId);
                                message = "[[[[机器人竞拍商品 {} 下一次竞拍时间 {}]";
                            } else {
                                message = "[[[[机器人跳过商品 {} 下一次竞拍时间 {}]";
                            }
                            // 设置下一次竞拍时间
                            term.setNextRobotAuctionTime(new NextRobotAuctionTime(new Date()));
//                                LOG.debug(message,itemId, term.nextRobotAuctionTime);
//                                            Log.debug(itemId,"start_control " + (System.currentTimeMillis()-start_control)+ String.format(message.replaceAll("\\{\\}", "%s"), itemId.toString(), term.nextRobotAuctionTime.toString()) +" "+ term.toString()+" "+JSON.toJSONString(item));
                            log.buffer("start_control ", System.currentTimeMillis() - start_control, message);
                            break;
                        }

                    }
                    logMain.buffer(itemId);
                    log.buffer("start2 ", System.currentTimeMillis() - start2);
                    log.write(itemId);
                    return null;
                };
                List<Callable<Void>> callableList = auctionTerm.stream().map(function).collect(Collectors.toList());
                for (Future<Void> voidFuture : executorPool.invokeAll(callableList)) {
                    voidFuture.get();
                }

                Long next = cache.stream()
                .filter(Objects::nonNull)
                .filter(r -> !RunningProgram.NOTROBOT.equals(r.running_program))
                .filter(r -> {
                    if (r.nextRobotAuctionTime == null || r.nextRobotAuctionTime.willDoTime == null) {
                        LOG.error(r);
                        return false;
                    }
                    return true;
                })
                .min(Comparator.comparing(r -> r.nextRobotAuctionTime.willDoTime.getTime()))
                .map(r -> {
                    logMain.buffer(r, r.nextRobotAuctionTime);
                    return r.nextRobotAuctionTime.willDoTime.getTime() - System.currentTimeMillis();
                })
                .orElse(50L);
                // 清除缓存
                cache.clear();

                logMain.buffer("for ", System.currentTimeMillis()-start, "机器人后台进程睡觉: ", next);
                logMain.write();
                try {
//                    Log.debug("机器人后台进程睡觉: " + next);
                    if (next >= 50) {
                        Thread.sleep(next);
                        LOG.debug("机器人后台进程睡觉: " + next);
                    }
                } catch (InterruptedException e) {
                    LOG.error("机器人后台进程睡觉失败", e);
                }

//                if (count[0] == 15) {
//                    throw new RuntimeException();
//                }
//                count[0]++;

            }

        } catch (Exception e) {
            LOG.error("机器人后台进程意外中止", e);
            SystemSettings.maintenance = true;
            end();
        } finally {
            LOG.info("平台维护中.........");
            SystemSettings.maintenance = true;
            auctionPool.shutdown();
            executorPool.shutdown();
            end = true;
        }
        LOG.info("机器人竞拍后台进程停止.........");
    }

    /**
     * 是否竞拍
     * @param itemId
     * @return
     */
    public boolean isAuction(Long itemId){
        ItemCache.Item item = ItemCache.itemMap.get(itemId);
        // 获取当前期信息
        RobotItemTerm term = Optional.ofNullable(robotItemTermMap.get(itemId)).orElseGet(() -> createRobotItemTerm(itemId, item, null));
        // 下限 是否超过最小成交价
        BooleanSupplier moreThanMinMarketPrice = () -> Float.valueOf(item.getPrice()) > minMarketPrice;
        // 上限 预计成本价 > 竞拍价 + 实际竞拍收益(实际竞拍数*每轮加价)
        BooleanSupplier lessThan = () -> term.expectCost.compareTo(new BigDecimal(item.getPrice()).add(new BigDecimal(item.getActual_click_number()).multiply(new BigDecimal(item.getIncrease_the_price())))) > 0;

        switch (item.getItemBean().getRunning_program()) {
            case NOTROBOT: return false;
            case ROBOT_SUCCESS: {
                return isRobotSuccessAuction(item, term, moreThanMinMarketPrice, lessThan);
            }
            case CONTROL_LINE_FIX:
            case CONTROL_LINE_RANGE: {
                return isControlLineAuction(item, term, moreThanMinMarketPrice);
            }

        }
        return true;
    }

    private boolean isAverageMarketPriceAuction(ItemCache.Item item, RobotItemTerm term, BooleanSupplier moreThanMinMarketPrice, BooleanSupplier lessThan) {
        boolean isAuction = false;
        // 是否超过最小成交价
        if (moreThanMinMarketPrice.getAsBoolean()) {
            if (lessThan.getAsBoolean()) {
                if (term.averageMarketPrice == null
                        || term.averageMarketPrice.compareTo(new BigDecimal(item.getPrice())) >= 0) {
                    isAuction = true;
                }
            }
        } else {
            isAuction = true;
        }
        return isAuction;
    }

    private boolean isCoinConsumeAuction(Float coinCunsumeRate, ItemCache.Item item, RobotItemTerm term, BooleanSupplier moreThanMinMarketPrice, BooleanSupplier lessThan) {
        boolean isAuction = false; // 机器人是否竞拍
        // 是否超过最小成交价
        if (moreThanMinMarketPrice.getAsBoolean()) {
            if (lessThan.getAsBoolean()) {
                // 上一个不是机器人
                if (!item.getRobot()) {
                    RechargeCoinConsume coinConsume = robotService.userCoinCousumeInfo(item.getUserId());
//                                        System.out.println(coinConsume);
                    // (用户所有充值的拍币 - 剩余的拍币 + 排队竞拍预扣费) * 用户中奖的金额占消耗拍币的比率 - 用户目前中奖记录的总成本 > 商品的成本
                    // 是否满足用户中的条件
                    boolean isUserSuccess = (coinConsume.rechargeSum - coinConsume.current + coinConsume.withholding)*coinCunsumeRate.floatValue() - coinConsume.sealedCostSum > term.realCost.floatValue();
                    if (!isUserSuccess) {
                        isAuction = true;
                    }
                } else {
                    // 是否没过了控线
                    isAuction = item.getClick_number() < term.control_line;
                }
            }
        } else {
            isAuction = true;
        }
        return isAuction;
    }

    private boolean isControlLineAuction(ItemCache.Item item, RobotItemTerm term, BooleanSupplier moreThanMinMarketPrice) {
        boolean isAuction = false; // 机器人是否竞拍
//                                Consumer<Object> log = obj -> {if (item.getItemBean().getRunning_program() == RunningProgram.CONTROL_LINE_FIX) System.out.println(obj);};
        // 是否过了控线
//                                log.accept("moreThanMinMarketPrice "+moreThanMinMarketPrice.getAsBoolean());
        if (moreThanMinMarketPrice.getAsBoolean()) {
//                                    log.accept("item.getActual_click_number() " + item.getActual_click_number()
//                                            +", term.control_line = " + term.control_line
//                                            +",  " + (item.getActual_click_number() < term.control_line));
            if (item.getActual_click_number() < term.control_line){
                // 预计成本价 > 竞拍价 + 实际竞拍收益
//                                        log.accept("term.expectCost = " + term.expectCost
//                                                +", item.getPrice() = " + item.getPrice()
//                                                +", item.getActual_click_number() = " + item.getActual_click_number()
//                                                +", item.getIncrease_the_price() = " + item.getIncrease_the_price());
                if(term.expectCost.compareTo(new BigDecimal(item.getPrice()).add(new BigDecimal(item.getActual_click_number()))) > 0) {
                    isAuction = true;
                } else {
                    if (!item.getRobot()) { // 是机器人
                        isAuction = true;
                    }
                }
            }
        } else {
            isAuction = true;
        }
        return isAuction;
    }

    private boolean isRobotSuccessAuction(ItemCache.Item item, RobotItemTerm term, BooleanSupplier moreThanMinMarketPrice, BooleanSupplier lessThan) {
        boolean isAuction = false; // 机器人是否竞拍
        long start = System.currentTimeMillis();
        // 是否过了控线 && 上一个不是机器人
        if (moreThanMinMarketPrice.getAsBoolean()) {
//            Log.debug(term.itemId, "moreThanMinMarketPrice true "+(System.currentTimeMillis()-start));
            if(lessThan.getAsBoolean()){
//                Log.debug(term.itemId, "lessThan true "+(System.currentTimeMillis()-start));
                if (item.getClick_number() < term.control_line || !item.getRobot() ){
                    isAuction = true;
                }
            } else {
//                Log.debug(term.itemId, "lessThan false "+(System.currentTimeMillis()-start));
            }
        } else {
//            Log.debug(term.itemId, "moreThanMinMarketPrice false "+(System.currentTimeMillis()-start));
            isAuction = true;
        }
        return isAuction;
    }


    private void auction(Long itemId) {
        long start = System.currentTimeMillis();
        auctionPool.execute(new Runnable() {
            @Override
            public void run() {
                Long robotId = null;
                Log log = new Log();
                try {
                    ItemCache.Item item = ItemCache.itemMap.get(itemId);
                    robotId = robotService.getRandomRobotId(itemId);
                    if (robotId.equals(item.getUserId())){
                        robotId = robotService.getRandomRobotId(itemId);
                    }
                    long start1 = System.currentTimeMillis() - start;
                    if (!item.isSettlement()) { // 检测是否已结算
                        // 竞拍
                        ItemCache.auction(robotId, itemId, true,false);
                        log.buffer("auction start ", start1," end ",System.currentTimeMillis()-start);
                    }
                } catch (Exception e) {
                    log.buffer("auction failure ", e.getMessage());
                    LOG.error("机器人竞拍失败 "+itemId+", 用户 "+robotId,e);
                    robotId = robotService.getRandomRobotId(itemId);
                    try {
                        ItemCache.auction(robotId, itemId, true,false);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        log.buffer("auction failure2 ", e.getMessage());
                    }
                } finally {
                    log.write(itemId);
                }
            }
        });
        long end = System.currentTimeMillis();
//        Log.debug("auction "+(end-start));
    }


    @Override
    public synchronized void start() {
        super.start();
        running = true;
        LOG.info("机器人竞拍后台进程启动.........");
    }

    /**结束机器人线程*/
    public void end(){
        running = false;
        LOG.info("机器人竞拍后台进程停止中.........");
        while (!end);
    }

    /**是否运行中*/
    public boolean isRunning() {
        return running;
    }

    /**
     * 更新
     * @param
     */
    public void updateTerm(Long itemId){
        RobotItemTerm term1 = robotItemTermMap.get(itemId);
        createRobotItemTerm(itemId, ItemCache.itemMap.get(itemId), term1 != null ? term1.random : null);
    }

    private RobotItemTerm createRobotItemTerm(Long itemId, ItemCache.Item item, Random random) {
        RobotItemTerm term1 = null;
        ItemBean itemBean = item.getItemBean();
        RobotItemBean bean = robotService.getRobotItemBean(itemBean);

        switch (itemBean.getRunning_program()) {
            case NOTROBOT:
            case CONTROL_LINE_FIX: {
                term1 = new RobotItemTerm(itemBean.getRunning_program(), itemBean.getControl_line(), bean.getCrossingAuctionRate(), itemBean.getCost());
            } break;
            case ROBOT_SUCCESS:

            case CONTROL_LINE_RANGE: {
                term1 = new RobotItemTerm(itemBean.getRunning_program(), bean.getControlLineMin(), bean.getControlLineMax(), bean.getCrossingAuctionRate(), itemBean.getCost());
            } break;
            default: {
                // 此商品出现未知异常
                robotService.errorItemHandler(String.format("商品 %s 运行方式为 null \n %s", itemBean.getName(), JSON.toJSONString(item, SerializerFeature.PrettyFormat)), itemId);
                // 强行设置机器人
                term1 = new RobotItemTerm(RunningProgram.ROBOT_SUCCESS, bean.getControlLineMin(), bean.getControlLineMax(), bean.getCrossingAuctionRate(), itemBean.getCost());
            }
        }
        term1.itemId = itemId;
        term1.averageMarketPrice = bean.getAverageMarketPrice();
        robotItemTermMap.put(itemId,term1);
        return term1;
    }

    /**
     * 机器人某期商品信息
     */
    public static class RobotItemTerm {
        // 运行方案
        final RunningProgram running_program;
        // 实际控线
        final Integer control_line;
        /**商品越过控线后机器人竞拍概率, 默认95%*/
        final Integer crossingAuctionRate;
        // 随机种子
        static final Random random = new Random();
        // 预计成本
        final BigDecimal expectCost;
        // 真实成本
        final BigDecimal realCost;
        // 平均成交价
        BigDecimal averageMarketPrice;
        ItemCache.Item item;
        Long itemId;

        static DecimalFormat decimalFormat = new DecimalFormat(".00");
//        private boolean stop = false;

        private NextRobotAuctionTime nextRobotAuctionTime;

        public RobotItemTerm(RunningProgram running_program, Integer control_line, Integer crossingAuctionRate, BigDecimal cost) {
            this.running_program = running_program;
            this.control_line = control_line;
            this.crossingAuctionRate = crossingAuctionRate;
//            this.random = new Random(3);
            int costRate = new Random(random.nextLong()).nextInt(300)+700;
            this.expectCost = new BigDecimal(decimalFormat.format(cost.floatValue()*costRate/1000));
            this.realCost = cost;
        }
        public RobotItemTerm(RunningProgram running_program, Integer rangeMin, Integer rangeMax, Integer crossingAuctionRate, BigDecimal cost) {
            this.running_program = running_program;
//            this.random = random == null ? new Random(3) : random;
            int bound = rangeMax - rangeMin;
            this.control_line = bound > 0 ? new Random(random.nextLong()).nextInt(bound) + rangeMin : rangeMin;
            this.crossingAuctionRate = crossingAuctionRate;
            int costRate = new Random(random.nextLong()).nextInt(300)+700;
            this.expectCost = new BigDecimal(decimalFormat.format(cost.floatValue()*costRate/1000));
            this.realCost = cost;
        }

        public NextRobotAuctionTime getNextRobotAuctionTime() {
            return nextRobotAuctionTime;
        }

        public void setNextRobotAuctionTime(NextRobotAuctionTime nextRobotAuctionTime) {
            this.nextRobotAuctionTime = nextRobotAuctionTime;
        }

        @Override
        public String toString() {
            return "RobotItemTerm{" +
                    "running_program=" + running_program +
                    ", itemId=" + itemId +
                    ", control_line=" + control_line +
//                    ", crossingAuctionRate=" + crossingAuctionRate +
                    ", expectCost=" + expectCost +
                    ", realCost=" + realCost +
                    '}';
        }
    }

    /**
     * 下一次机器人竞拍时间
     */
    public static class NextRobotAuctionTime {
        // 随机种子
        static long seed = 0L;

        final Date instant_time;
        final Date willDoTime;
        final Random random;

        /**
         * @param instant_time 目前竞拍的时间
         */
        public NextRobotAuctionTime(Date instant_time) {
            this.instant_time = instant_time;
            if(seed  == Long.MAX_VALUE) seed = 0L;
            random = new Random(seed++);
            this.willDoTime = new Date(instant_time.getTime() + interval());
        }

        /**机器人竞拍时间间隔*/
        long interval(){
            return random.nextInt(3000) + 6000;
        }

        static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        @Override
        public String toString() {
            return "NextRobotAuctionTime{" +
                    "instant_time=" + formatter2.format(LocalDateTime.ofInstant(instant_time.toInstant(), ZoneId.systemDefault())) +
                    ", willDoTime=" + formatter2.format(LocalDateTime.ofInstant(willDoTime.toInstant(), ZoneId.systemDefault())) +
                    '}';
        }
    }

}
