package com.mierro.robot.service.impl;

import com.alibaba.fastjson.JSON;
import com.mierro.authority.common.LoginType;
import com.mierro.authority.common.PasswordUtils;
import com.mierro.authority.dao.AuthenticationInfoDao;
import com.mierro.authority.dao.UserDao;
import com.mierro.authority.dao.UserRoleRelationshipDao;
import com.mierro.authority.entity.AuthenticationInfo;
import com.mierro.authority.entity.User;
import com.mierro.authority.entity.UserMessage;
import com.mierro.authority.entity.UserRoleRelationship;
import com.mierro.common.common.*;
import com.mierro.fileOperation.common.UploadType;
import com.mierro.fileOperation.po.UploadFile;
import com.mierro.fileOperation.service.IUploadService;
import com.mierro.main.common.CoinType;
import com.mierro.main.common.UsernameIdWorker;
import com.mierro.main.dao.CoinDao;
import com.mierro.main.dao.IntegralDao;
import com.mierro.main.global.UserCache;
import com.mierro.robot.dao.RobotConfigDao;
import com.mierro.robot.dao.RobotUserDao;
import com.mierro.robot.dao.RobotUserMessageDao;
import com.mierro.robot.entity.RobotUserBean;
import com.mierro.robot.entity.e.RobotConfig;
import com.mierro.robot.entity.e.RobotUserState;
import com.mierro.robot.entity.view.AddOrUpdateRobotUserView;
import com.mierro.robot.entity.view.RobotUserHeadImgView;
import com.mierro.robot.service.RobotUserService;
import com.mierro.robot.utils.Tool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 机器人用户模块
 * Created by tlseek on 2017/8/18.
 */
@SuppressWarnings("all")
@Service("robotUserService")
public class RobotUserServiceImpl  implements RobotUserService{

    private static Logger LOG = LogManager.getLogger(RobotUserServiceImpl.class.getName());

    @Resource
    private RobotConfigDao robotConfigDao;
    @Resource
    private RobotUserDao robotUserDao;
    @Resource
    private RobotUserMessageDao robotUserMessageDao;
    @Resource
    private CoinDao coinDao;
    @Resource
    private IntegralDao integralDao;
    @Resource
    IUploadService uploadService;

    @Resource
    private AuthenticationInfoDao authenticationInfoDao;
    @Resource
    private UserRoleRelationshipDao userRoleRelationshipDao;
    @Resource
    private UserDao userDao;

    /**机器人竞拍用户列表*/
    private Map<Long, RobotItemUsers> robotUserMap = new ConcurrentHashMap<>();

    /**获取头像图片ID转换器*/
    private Function<AddOrUpdateRobotUserView, Long> mapToFileId;
    ExecutorService threadPool = Executors.newFixedThreadPool(10);

    @PreDestroy
    public void destory() {
        threadPool.shutdown();
    }
    @Override
    public void loading() {

        // 加载图片到数据库
        loadHeadImg();

        // 重设机器人头像, 运行一次
        Boolean resetUserHeadimgs = RobotConfig.RESET_USER_HEADIMGES.deserialize(robotConfigDao.get(RobotConfig.RESET_USER_HEADIMGES));
        if (resetUserHeadimgs) {
            String configInfo = Tool.getOrDefault(robotConfigDao.get(RobotConfig.ROBOT_USER_HEADIMGS), "[]");
            List<AddOrUpdateRobotUserView> headImgViews = RobotConfig.ROBOT_USER_HEADIMGS.deserialize(configInfo);
            List<Long> robotIds = robotUserMessageDao.findRobotIds();
            if (!robotIds.isEmpty() && !headImgViews.isEmpty()) {
                Collections.shuffle(robotIds);
                int num = robotIds.size() / headImgViews.size();
                if (num == 0) num = 1;
                for (int i = 0, j = 0; i < robotIds.size()  && j < headImgViews.size(); i = i + num, j++) {
                    int end;
                    end = (( end = i + num) < robotIds.size()) ? end : robotIds.size();
                    if (j == headImgViews.size() - 1) end = robotIds.size();
                    List<Long> updateList = robotIds.subList(i, end);
                    if (!updateList.isEmpty()) {
                        robotUserMessageDao.updateImg(headImgViews.get(j).getHeadImageFileId(), updateList);
                    }
                }
            }
            robotConfigDao.put(RobotConfig.RESET_USER_HEADIMGES, "false");
        }


        // 检测机器人用户有没有添加对应机器用户表的信息
        List<RobotUserBean> robotUserList =  robotUserDao.findUserIdByRobotUserIsNull().stream()
                .map(userId -> new RobotUserBean(userId, RobotUserState.waiting)).collect(Collectors.toList());
        if (!robotUserList.isEmpty()) {
            robotUserDao.save(robotUserList);
        }

        // 加载默认机器人列表, 运行一次
        Boolean loadUsers = RobotConfig.LOAD_USERS.deserialize(robotConfigDao.get(RobotConfig.LOAD_USERS));
        if (loadUsers) {
            File robotListFile = FileUtils.getConfigFile("./robot/robot-list.json");
            List<AddOrUpdateRobotUserView> robotUserViews = Optional.ofNullable(robotListFile)
                    .map(Tool.function(f -> new String(Files.readAllBytes(f.toPath()), "UTF-8")))
                    // 转换成 AddOrUpdateRobotUserView 列表
                    .map(json -> JSON.parseArray(json, AddOrUpdateRobotUserView.class))
                    .orElse(Collections.EMPTY_LIST);
            if (!robotUserViews.isEmpty()) {
                addUser(robotUserViews);
            }
            robotConfigDao.put(RobotConfig.LOAD_USERS, "false");
        }

        // 初始化机器人用户状态
        robotUserDao.changeState(RobotUserState.waiting);
    }

    /**
     * 加载图片到数据库
     */
    @Override
    public void loadHeadImg() {
        Map<String,String> contentTypeMap = new HashMap<>(5);
        contentTypeMap.put("png", "image/x-png");
        contentTypeMap.put("jpg", "image/jpeg");

        String configInfo = Tool.getOrDefault(robotConfigDao.get(RobotConfig.ROBOT_USER_HEADIMGS), "[]");
        List<AddOrUpdateRobotUserView> headImgViews = RobotConfig.ROBOT_USER_HEADIMGS.deserialize(configInfo);
        File file = FileUtils.getConfigFile("./avatar/");
        List<String> imgNames = headImgViews.stream().map(AddOrUpdateRobotUserView::getHeadImageName).collect(Collectors.toList());

        File[] imgfiles ;
        if(file != null && (imgfiles = file.listFiles()) != null){
            Stream.of(imgfiles).filter(img -> !imgNames.contains(img.getName())).forEach(img -> {
                UploadFile uploadVo = new UploadFile();
                String[] name= img.getName().split("\\.");
                String suffix = name[name.length -1];
                uploadVo.setContentType(contentTypeMap.get(suffix));
                uploadVo.setType(UploadType.IMAGE);
                uploadVo.setUrl(UploadType.IMAGE.getRelativeURL(img.getName()));
                uploadVo.setFileName(img.getName());
                uploadVo = uploadService.save(uploadVo);

                try {
                    FileUtils.create(new FileInputStream(img), FileUtils.getResourcePath(uploadVo.getUrl()));
                } catch (IOException e) {}

                AddOrUpdateRobotUserView view = new AddOrUpdateRobotUserView();
                view.setHeadImageFileId(uploadVo.getId());
                view.setHeadImageName(img.getName());
                headImgViews.add(view);
            });
            robotConfigDao.put(RobotConfig.ROBOT_USER_HEADIMGS, JSON.toJSONString(headImgViews));
        }

        // 设置获取头像图片ID转换器
        mapToFileId = userView -> {
            AddOrUpdateRobotUserView find = null;
            // 已经文件ID
            if (userView.getHeadImageFileId() != null){
                return userView.getHeadImageFileId();
            } else
            // 头像名存在于资源文件中
            if(userView.getHeadImageName() != null && (find =  headImgViews.stream().filter(userView1 -> userView1.getHeadImageName().equals(userView.getHeadImageName())).findFirst().orElse(null)) != null) {
                return find.getHeadImageFileId();
            } else
            // 头像目录下文件数量大于0
            if(!headImgViews.isEmpty()) {
                return headImgViews.get(new Random().nextInt(headImgViews.size())).getHeadImageFileId();
            }
            return 1L;
        };
    }

    @Override
    public Page<UserMessage> findAll(String name, Boolean disable, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        name = name == null ? "%" : "%" + name + "%";
        Page<UserMessage> page;
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);

        if (disable == null){
            page = robotUserMessageDao.findAllMembers(name,pageable);
        }else{
            page = robotUserMessageDao.findAllMembers(name,disable,pageable);
        }
        return page.map(userMessage -> {
            userMessage.setGift(Tool.getOrDefault(coinDao.findByUserIdAndCoinType(userMessage.getId(), CoinType.GIFT), 0));
            userMessage.setCoin(Tool.getOrDefault(coinDao.findByUserIdAndCoinType(userMessage.getId(), CoinType.RECHARGE), 0));
            userMessage.setIntegral(Tool.getOrDefault(integralDao.findByUserId(userMessage.getId()), 0));
            return userMessage;
        });
    }

    @Override
    public List<RobotUserHeadImgView> findDefaultRobotUserHeadImg() {
        String configInfo = Tool.getOrDefault(robotConfigDao.get(RobotConfig.ROBOT_USER_HEADIMGS), "[]");
        return RobotConfig.ROBOT_USER_HEADIMGS.deserialize(configInfo);
    }

    @Override
    public UserMessage find(Long userId) {
        UserMessage userMessage = ResponseCode.business.notNull(robotUserMessageDao.getOne(userId), "找不到机器人");
        userMessage.setGift(Tool.getOrDefault(coinDao.findByUserIdAndCoinType(userMessage.getId(), CoinType.GIFT), 0));
        userMessage.setCoin(Tool.getOrDefault(coinDao.findByUserIdAndCoinType(userMessage.getId(), CoinType.RECHARGE), 0));
        userMessage.setIntegral(Tool.getOrDefault(integralDao.findByUserId(userMessage.getId()), 0));
        return userMessage;
    }


    @Transactional
    @Override
    public void addUser(AddOrUpdateRobotUserView userView) {
        //创建用户
        String username = userView.getUsername() == null ?  Long.toString(UsernameIdWorker.nextId()) : userView.getUsername();
        if (authenticationInfoDao.findByIdentifierAndVoucherType(username, LoginType.ACCOUNT) != null) {
            throw new ServiceException(ResponseCode.BUSINESS,"用户名已经被注册");
        }
        User user = new User();
        user.setLastModifyTime(new Date());
        user.setLastModifyBy(1L);
        user.setDisable(false);
        user.setSalt(StringBuilderUtils.getRandomString(16));
        user.setCreateBy(1L);
        user.setFinalLogin(new Date());
        user.setCreateTime(new Date());
        user.setRefreshToken(UUID.randomUUID().toString());
        Date data = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.HOUR_OF_DAY,2);
        user.setExpirationTime(calendar.getTime());
        user = userDao.save(user);
        //创建登陆方式
        PasswordUtils passwordUtils = new PasswordUtils();
        String[] passwordAndsalt =  passwordUtils.encryptPassword(UUID.randomUUID().toString());
        try {
            AuthenticationInfo authenticationInfo = new AuthenticationInfo();
            authenticationInfo.setUserId(user.getId());
            authenticationInfo.setLastModifyBy(1L);
            authenticationInfo.setLastModifyTime(new Date());
            authenticationInfo.setCreateTime(new Date());
            authenticationInfo.setCreateBy(1L);
            authenticationInfo.setCredential(passwordAndsalt[0]);
            authenticationInfo.setIdentifier(username);
            authenticationInfo.setVoucherType(LoginType.ACCOUNT);
            authenticationInfo.setSalt(passwordAndsalt[1]);
            authenticationInfoDao.save(authenticationInfo);
        }catch (Exception e){
            throw new ServiceException(ResponseCode.BUSINESS,"用户名已经被注册");
        }
        //创建用户信息
        UserMessage userMessageBean = new UserMessage();
        userMessageBean.setId(user.getId());
        userMessageBean.setAdmin(false);
        userMessageBean.setUsername(username);
        userMessageBean.setIphone(null);
        userMessageBean.setHead_pic(mapToFileId.apply(userView));
        userMessageBean.setJoin_date(new Date());
        userMessageBean.setRobot(true);
        userMessageBean.setIntegral(0);
        userMessageBean.setAddress(userView.getAddress());
        robotUserMessageDao.save(userMessageBean);
        //赋予用户角色权限
        UserRoleRelationship userRoleRelationship1 = new UserRoleRelationship();
        userRoleRelationship1.setCreateBy(1L);
        userRoleRelationship1.setUserId(userMessageBean.getId());
        userRoleRelationship1.setLastModifyTime(new Date());
        userRoleRelationship1.setRoleId(2L);
        userRoleRelationship1.setLastModifyBy(1L);
        userRoleRelationshipDao.save(userRoleRelationship1);
        // 添加机器用户
        RobotUserBean robotUserBean = new RobotUserBean();
        robotUserBean.setId(user.getId());
        robotUserBean.setState(RobotUserState.waiting);
        robotUserDao.save(robotUserBean);
    }


    @Override
    public void addUser(List<AddOrUpdateRobotUserView> user) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"));
        user.forEach(Tool.consumer(this::addUser, (u, e) -> LOG.error(timestamp+":机器人添加用户 "+JSON.toJSONString(u)+" 失败: ",e)));
    }

    @Transactional
    @Override
    public void updateUser(AddOrUpdateRobotUserView user) {
        UserMessage userMessageBean = ResponseCode.business.notNull(robotUserMessageDao.findOne(user.getId()), "找不到机器人信息");
        // 修改用户名
        if (!userMessageBean.getUsername().equals(user.getUsername())) {
            AuthenticationInfo authenticationInfo = ResponseCode.business.notNull(authenticationInfoDao.findByUserIdAndVoucherType(user.getId(), LoginType.ACCOUNT), "找不到机器人信息");
            try {
                authenticationInfo.setIdentifier(user.getUsername());
                authenticationInfo.setLastModifyBy(1L);
                authenticationInfo.setLastModifyTime(new Date());
                authenticationInfoDao.save(authenticationInfo);

                userMessageBean.setUsername(user.getUsername());
            }catch (Exception e){
                throw new ServiceException(ResponseCode.BUSINESS,"用户名已经被注册");
            }
        }
        // 修改头像
        userMessageBean.setHead_pic(mapToFileId.apply(user));
        // 修改禁用
        if(user.getDisable() != null) {
            User user1 = ResponseCode.business.notNull(userDao.findOne(user.getId()), "找不到用户信息");
            user1.setDisable(user.getDisable());
            userDao.save(user1);
        }
        // 修改禁用
        if(user.getAddress() != null) userMessageBean.setAddress(user.getAddress());

        robotUserMessageDao.save(userMessageBean);
    }

    @Override
    public Long getRandomRobotId(Long itemId) {
        RobotUserBean robotUserBean = Tool.getOrDefault(robotUserMap.get(itemId), () -> setNewRobotItemUsers(itemId)).next();
        try {
            UserCache.userCache.put(robotUserBean.getId(), robotUserBean.getUserMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return robotUserBean.getId();
    }

    @Override
    public void refreshRandomRobot(Long itemId) {
        // 清理原来的对象
        RobotItemUsers oldItemUsers = robotUserMap.get(itemId);
        if (oldItemUsers != null) {
            robotUserMap.remove(itemId);
            oldItemUsers.clear();
        }

        // 给商品增加新的机器人用户
        setNewRobotItemUsers(itemId);
    }

    /**
     * 给商品增加新的机器人用户
     * @param itemId
     */
    @Transactional
    private RobotItemUsers setNewRobotItemUsers(Long itemId) {
        // 给商品增加新的机器人用户
        Integer[] range = RobotConfig.ALLOC_USER_NUM_RANGE.deserialize(robotConfigDao.get(RobotConfig.ALLOC_USER_NUM_RANGE));
        Integer allocUserNum = range[1] < 2 ? 2 : ( range[0] < 2 ? Tool.randomBetweem(2, range[1]) : Tool.randomBetweem(range[0], range[1]) );

        List<Long> waitings = robotUserDao.findIds(RobotUserState.waiting);
        List<Long> selectedIds;
        if (waitings.size() > allocUserNum) {
            Collections.shuffle(waitings); // 打乱
            selectedIds =  waitings.subList(0, allocUserNum); // 获取前 allocUserNum 个用户
        } else if(waitings.size() > 0) {
            Collections.shuffle(waitings); // 打乱
            selectedIds = waitings;
        } else {
            selectedIds = new ArrayList<>();
        }
        // 如果等待状态机器人不够, 从竞拍中的机器人中选择
        if(selectedIds.size() < allocUserNum){
            List<Long> auctions = robotUserDao.findIds(RobotUserState.auction);
            Collections.shuffle(auctions); // 打乱
            selectedIds.addAll(auctions.subList(0, allocUserNum - selectedIds.size())); // 获取前 allocUserNum 个用户
        }
        List<RobotUserBean> withUserMessage = robotUserDao.findWithUserMessage(selectedIds);
        // 获取竞拍次数区间
        Integer[] allowAuctionRange = RobotConfig.ALLOW_USER_AUCTION_RANGE.deserialize(robotConfigDao.get(RobotConfig.ALLOW_USER_AUCTION_RANGE));
        for (RobotUserBean robotUserBean : withUserMessage) {
            // 随机竞拍次数
            Integer allowAuction = Tool.randomBetweem(allowAuctionRange[0], allowAuctionRange[1]);
            robotUserBean.setMaxAuctionCount(allowAuction);
        }
        RobotItemUsers itemUsers = new RobotItemUsers(allocUserNum, allowAuctionRange);
        itemUsers.add(withUserMessage);
        itemUsers.itemId = itemId;
        withUserMessage.forEach(itemUsers.deque::addLast);
        robotUserMap.put(itemId, itemUsers);

        return itemUsers;
    }


    private static Random random = new Random();

    /**
     * 竞拍商品中参与的机器人用户
     */
    class RobotItemUsers {
        // 此次竞拍商品参与机器人用户总数
        private Integer[] allowAuctionRange;
        // 机器人用户列表
        private List<RobotUserBean> robotUserList;

        private Deque<RobotUserBean> deque;

        private Long itemId;

        public RobotItemUsers(Integer allocUserNum, Integer[] allowAuctionRange) {
            this.allowAuctionRange = allowAuctionRange;
            this.robotUserList = new ArrayList<>(allocUserNum);
            deque = new LinkedBlockingDeque<>();
        }

        RobotUserBean next(){
            if(deque.size() <= 1){
                blockRandomUser();
            }
            threadPool.execute(() -> {
                blockRandomUser();
            });
            return deque.removeFirst();
        }

        private void blockRandomUser(){
            synchronized (this){
                try {
                    deque.addLast(RobotUserServiceImpl.this.randomUser(this));
                } catch (Exception e){
                    e.printStackTrace();
                    setNewRobotItemUsers(itemId);
                }
            }
        }


        void add(List<RobotUserBean> users){
            robotUserList.addAll(users);
            robotUserDao.changeState(RobotUserState.auction, users.stream().map(RobotUserBean::getId).collect(Collectors.toList()));
        }
        void add(RobotUserBean user){
            robotUserList.add(user);
            robotUserDao.changeState(RobotUserState.auction, user.getId());
        }

        void clear(){
            List<Long> ids = new ArrayList<>(robotUserList.size());
            for (RobotUserBean robotUserBean : robotUserList) {
                UserCache.userCache.remove(robotUserBean.getId());
                ids.add(robotUserBean.getId());
            }
            robotUserDao.changeState(RobotUserState.waiting, ids);
        }

    }

    @Transactional
    RobotUserBean randomUser(RobotItemUsers  robotItemUsers){
        final RobotUserBean lastUser = robotItemUsers.deque.peekLast();
        RobotUserBean robotUserBean = null;
        try {
            int rand = new Random(random.nextLong()).nextInt(robotItemUsers.robotUserList.size() - 1);
            robotUserBean = robotItemUsers.robotUserList.stream().filter(u  -> lastUser == null || !u.getId().equals(lastUser.getId())).skip(rand).findFirst().get();
        } catch (Exception e){
            e.printStackTrace();
        }
        if (robotUserBean.getAuctionCount() < robotUserBean.getMaxAuctionCount()) {
            if (lastUser != null && lastUser.getId().equals(robotUserBean.getId()) ) {
                return randomUser(robotItemUsers);
            }
            robotUserBean.setAuctionCount(robotUserBean.getAuctionCount() + 1);
//                LOG.error(this.hashCode()+" "+robotUserBean.getId());
            return robotUserBean;
        } else {
            // 添加一个新机器人用户
            List<Long> ids = robotItemUsers.robotUserList.stream().map(RobotUserBean::getId).collect(Collectors.toList());
            List<Long> count = robotUserDao.findIdsEnableUserByStateNotIn(RobotUserState.waiting, ids);
            RobotUserState state = RobotUserState.waiting;
            if(count.size() < 2 ){
                state = RobotUserState.auction;
                count = robotUserDao.findIdsEnableUserByStateNotIn(RobotUserState.auction, ids);
            }
            RobotUserBean newUserBean = robotUserDao.findWithUserMessage(count.get(random.nextInt(count.size())));
            Integer allowAuction = Tool.randomBetweem(robotItemUsers.allowAuctionRange[0], robotItemUsers.allowAuctionRange[1]); // 随机竞拍次数
            newUserBean.setMaxAuctionCount(allowAuction);
            robotItemUsers.add(newUserBean);

            // 竞拍数已经达到最大, 清理
            UserCache.userCache.remove(robotUserBean.getId());
            robotUserDao.changeState(RobotUserState.waiting, robotUserBean.getId());
            robotItemUsers.robotUserList.remove(robotUserBean);
            return randomUser(robotItemUsers);
        }
    }
}
