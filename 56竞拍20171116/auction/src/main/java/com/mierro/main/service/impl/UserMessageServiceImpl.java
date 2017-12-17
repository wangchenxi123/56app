package com.mierro.main.service.impl;

import com.alibaba.fastjson.JSON;
import com.mierro.authority.common.AythorityType;
import com.mierro.authority.common.LoginType;
import com.mierro.authority.common.PasswordUtils;
import com.mierro.authority.dao.*;
import com.mierro.authority.entity.*;
import com.mierro.authority.shiro.CustomUsernamePasswordToken;
import com.mierro.common.common.*;
import com.mierro.main.common.*;
import com.mierro.main.dao.*;
import com.mierro.main.entity.*;
import com.mierro.main.global.ItemCache;
import com.mierro.main.service.SystemNoticeService;
import com.mierro.main.service.UserMessageService;
import com.mierro.main.util.PasswordCheck;
import com.mierro.main.util.SMSUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by 黄晓滨 simba on 2017/8/11.
 * Remarks：
 */
@Service("UserMessageService")
public class UserMessageServiceImpl implements UserMessageService {

    @Resource
    private UserMessageDao userMessageDao;
    @Resource
    private CoinDao coinDao;
    @Resource
    private IntegralDao integralDao;
    @Resource
    private UserRoleRelationshipDao userRoleRelationshipDao;
    @Resource
    private UserDao userDao;
    @Resource
    private AuthenticationInfoDao authenticationInfoDao;
    @Resource
    private OrderDao orderDao;
    @Resource
    private SealedDao sealedDao;
    @Resource
    private SystemNoticeService systemNoticeService;
    @Resource
    private AddressDao addressDao;
    @Resource
    private ConfigDao configDao;


    private static Logger logger = LogManager.getLogger(UserMessageServiceImpl.class.getName());

    @Override
    @Transactional(readOnly = true)
    public Page<UserMessage> findAll(Long userId, String name, String phone, Boolean disable, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        if (name == null){
            name = "%";
        }else{
            name = "%"+name+"%";
        }
        if (phone == null){
            phone = "%";
        }else{
            phone = "%"+phone+"%";
        }

        Page<UserMessage> page;
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);

        if (userId == null){
            if (disable == null){
                page = userMessageDao.findAllMembers(name,phone,pageable);
            }else{
                page = userMessageDao.findAllMembers(name,phone,disable,pageable);
            }
        }else{
            page = userMessageDao.findAllById(userId,pageable);
        }

        return page.map(userMessage -> {
            userMessage.setGift(coinDao.findByUserIdAndCoinType(userMessage.getId(), CoinType.GIFT));
            userMessage.setCoin(coinDao.findByUserIdAndCoinType(userMessage.getId(), CoinType.RECHARGE));
            userMessage.setIntegral(integralDao.findByUserId(userMessage.getId()));
            userMessage.setTotal_recharge(coinDao.findConsumptionByUserIdAndCoinTypeAndUse
                    (userMessage.getId(),CoinType.RECHARGE, CoinSource.RECHARGE));

            if (userMessage.getGift() == null)userMessage.setGift(0);
            if (userMessage.getCoin() == null)userMessage.setCoin(0);
            if (userMessage.getIntegral() == null)userMessage.setIntegral(0);
            if (userMessage.getNumber() == null)userMessage.setNumber(0);
            if (userMessage.getTotal_recharge() == null)userMessage.setTotal_recharge(0);
            return userMessage;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserMessage> findAll(Long userId, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Page<UserMessage> page;
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);

        if (userId == null){
            page = userMessageDao.findAll(pageable);
        }else{
            page = userMessageDao.findAllById(userId,pageable);
        }

        return page.map(userMessage -> {

            //赠币余额
            userMessage.setGift(coinDao.findByUserIdAndCoinType(userMessage.getId(), CoinType.GIFT));

            //拍币余额
            userMessage.setCoin(coinDao.findByUserIdAndCoinType(userMessage.getId(), CoinType.RECHARGE));

            //总充值
            userMessage.setTotal_recharge(coinDao.findConsumptionByUserIdAndCoinTypeAndUse
                    (userMessage.getId(),CoinType.RECHARGE, CoinSource.RECHARGE));

            if (userMessage.getGift() == null)userMessage.setGift(0);
            if (userMessage.getCoin() == null)userMessage.setCoin(0);
            if (userMessage.getNumber() == null)userMessage.setNumber(0);
            if (userMessage.getTotal_recharge() == null)userMessage.setTotal_recharge(0);
            return userMessage;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public UserMessage findOne(Long userId) {
        UserMessage userMessage = userMessageDao.findOne(userId);
        if (userMessage != null){
            userMessage.setGift(coinDao.findByUserIdAndCoinType(userMessage.getId(), CoinType.GIFT));
            userMessage.setCoin(coinDao.findByUserIdAndCoinType(userMessage.getId(), CoinType.RECHARGE));
            userMessage.setIntegral(integralDao.findByUserId(userMessage.getId()));
            if (userMessage.getGift() == null)userMessage.setGift(0);
            if (userMessage.getCoin() == null)userMessage.setCoin(0);
            if (userMessage.getIntegral() == null)userMessage.setIntegral(0);
        }
        return userMessage;
    }

    @Override
    @Transactional
    public Map<String,String> addUser(String phone, String sms,String ip,String position) {
        return addUser(phone, sms,ip, UUID.randomUUID().toString(), position);
    }

    @Override
    public Map<String, String> addUser(String phone, String sms, String ip, String password, String position) {
        //验证验证码是否正确
        String code = SMSUtils.checkCode(phone, SMSUtils.SendType.UPDATA);
        if (code == null){
            throw new ServiceException(ResponseCode.BUSINESS,"验证码不存在，请重新获取验证码!");
        }else if(code.equals("")){
            throw new ServiceException(ResponseCode.BUSINESS,"验证码已过期，请重新获取!");
        }else if(!code.equals(sms)){
            throw new ServiceException(ResponseCode.BUSINESS,"验证码错误!");
        }
        //清除验证码缓存
        SMSUtils.removeCodeCache(phone);
        //创建用户
        //生成用户名
        String username = detection(LoginType.ACCOUNT,'A');
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
        calendar.add(Calendar.HOUR_OF_DAY,3);
        user.setExpirationTime(calendar.getTime());

        user = userDao.save(user);
        //创建登陆方式
        PasswordUtils passwordUtils = new PasswordUtils();
        String[] passwordAndsalt =  passwordUtils.encryptPassword(password);
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
        try {
            AuthenticationInfo authenticationInfo2 = new AuthenticationInfo();
            authenticationInfo2.setUserId(user.getId());
            authenticationInfo2.setLastModifyBy(1L);
            authenticationInfo2.setLastModifyTime(new Date());
            authenticationInfo2.setCreateTime(new Date());
            authenticationInfo2.setCreateBy(1L);
            authenticationInfo2.setCredential(passwordAndsalt[0]);
            authenticationInfo2.setSalt(passwordAndsalt[1]);
            authenticationInfo2.setIdentifier(phone);
            authenticationInfo2.setVoucherType(LoginType.TELEPHONE);
            authenticationInfoDao.save(authenticationInfo2);
        }catch (Exception e){
            throw new ServiceException(ResponseCode.BUSINESS,"手机号已经被注册");
        }
        //创建用户信息

        UserMessage userMessageBean = new UserMessage();
        userMessageBean.setId(user.getId());
        userMessageBean.setAdmin(false);
        userMessageBean.setUsername(username);
        userMessageBean.setIphone(phone);
        //随机分配头像
        Random random = new Random();
        userMessageBean.setHead_pic((long) (random.nextInt(16)%(16) + 1));
        userMessageBean.setAddress(position);
        userMessageBean.setLevel_one_proxy(1L);
        userMessageBean.setIp(ip);
        userMessageBean.setJoin_date(new Date());
        userMessageBean.setRobot(false);
        userMessageBean.setIntegral(0);

        userMessageDao.save(userMessageBean);
        //赋予基础用户角色权限
        UserRoleRelationship userRoleRelationship1 = new UserRoleRelationship();
        userRoleRelationship1.setCreateBy(1L);
        userRoleRelationship1.setUserId(userMessageBean.getId());
        userRoleRelationship1.setLastModifyTime(new Date());
        userRoleRelationship1.setRoleId(2L);
        userRoleRelationship1.setLastModifyBy(1L);
        userRoleRelationshipDao.save(userRoleRelationship1);

        //注册赠送拍币
        ConfigBean configBean = configDao.findByKeyId(ConfigSetting.gift_coin);
        int number;
        if (configBean == null){
            number = 0;
        }else{
            try {
                number = Integer.parseInt(configBean.getValue());
            }catch (Exception e){
                number = 0;
            }
        }

        CoinBean coinBean = new CoinBean();
        coinBean.setTime(new Date());
        coinBean.setCoinType(CoinType.GIFT);
        coinBean.setNumber(number);
        coinBean.setSource(CoinSource.Gift);
        coinBean.setUserId(user.getId());
        coinBean.setReason("注册赠送拍币");
        coinBean.setPromotion(false);
        coinDao.save(coinBean);

        //进行登录
        //进行授权登陆
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered()) {
            subject.logout();
        }
        //采用默认密码
        String pas = "123456";
        CustomUsernamePasswordToken token = new CustomUsernamePasswordToken(
                username, pas, AythorityType.CLIENT, LoginType.ACCOUNT, "AUTO");
        subject.login(token);

        //组装返回登录信息
        Map<String,String> map = new HashMap<>();
        map.put("token",user.getRefreshToken());
        map.put("expirationTime",(calendar.getTime().getTime()-new Date().getTime())+"");
        return map;
    }

    /**
     * 检测是否可以注册
     * @return 账号信息
     */
    private String detection(LoginType loginType, char lable){
//        if ((int)lable >= 90){
//            lable = 'A';
//        }else{
//            lable = (char)(lable+1);
//        }
//        IdWorker idWorker = new IdWorker();
//        String str = idWorker.nextId()+"";
//        str = lable+str.substring(0,2)+"*****"+str.substring(str.length()-4,str.length());
//        if (authenticationInfoDao.findByIdentifierAndVoucherType(str,loginType) != null) {
//            str = detection(loginType,lable);
//        }
        return Long.toString(UsernameIdWorker.nextId());
    }

    @Override
    @Transactional
    public void addUser(Long userId,String phone, String sms,String ip,String position) {
        //验证验证码是否正确
        String code = SMSUtils.checkCode(phone, SMSUtils.SendType.UPDATA);
        if (code == null){
            throw new ServiceException(ResponseCode.BUSINESS,"验证码不存在，请重新获取验证码!");
        }else if(code.equals("")){
            throw new ServiceException(ResponseCode.BUSINESS,"验证码已过期，请重新获取!");
        }else if(!code.equals(sms)){
            throw new ServiceException(ResponseCode.BUSINESS,"验证码错误!");
        }
        //清除验证码缓存
        SMSUtils.removeCodeCache(phone);
        //创建用户
        IdWorker idWorker = new IdWorker();
        String username = detection(LoginType.ACCOUNT,'A');
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
        try {
            AuthenticationInfo authenticationInfo2 = new AuthenticationInfo();
            authenticationInfo2.setUserId(user.getId());
            authenticationInfo2.setLastModifyBy(1L);
            authenticationInfo2.setLastModifyTime(new Date());
            authenticationInfo2.setCreateTime(new Date());
            authenticationInfo2.setCreateBy(1L);
            authenticationInfo2.setCredential(passwordAndsalt[0]);
            authenticationInfo2.setSalt(passwordAndsalt[1]);
            authenticationInfo2.setIdentifier(phone);
            authenticationInfo2.setVoucherType(LoginType.TELEPHONE);
            authenticationInfoDao.save(authenticationInfo2);
        }catch (Exception e){
            throw new ServiceException(ResponseCode.BUSINESS,"手机号已经被注册");
        }
        //创建用户信息
        UserMessage userMessageBean = new UserMessage();
        userMessageBean.setId(user.getId());
        userMessageBean.setAdmin(false);
        userMessageBean.setUsername(username);
        userMessageBean.setIphone(phone);
        userMessageBean.setHead_pic(1L);
        userMessageBean.setIp(ip);
        userMessageBean.setAddress(position);
        userMessageBean.setJoin_date(new Date());
        userMessageBean.setRobot(false);
        userMessageBean.setIntegral(0);

        if (userId == 1L){
            userMessageBean.setLevel_one_proxy(1L);
        }else{
           UserMessage userMessage =  userMessageDao.findOne(userId);
           if (userMessage.getLevel_one_proxy() == 1L){
                userMessageBean.setLevel_one_proxy(userId);
           }else{
               userMessageBean.setLevel_one_proxy(userMessage.getLevel_one_proxy());
               userMessageBean.setLevel_two_proxy(userId);
           }
        }

        userMessageDao.save(userMessageBean);
        //赋予基础用户角色权限
        UserRoleRelationship userRoleRelationship1 = new UserRoleRelationship();
        userRoleRelationship1.setCreateBy(1L);
        userRoleRelationship1.setUserId(userMessageBean.getId());
        userRoleRelationship1.setLastModifyTime(new Date());
        userRoleRelationship1.setRoleId(2L);
        userRoleRelationship1.setLastModifyBy(1L);
        userRoleRelationshipDao.save(userRoleRelationship1);
    }

    @Override
    @Transactional
    public void updatePassword(Long adminId, Long userId, String newPassword) {
        //进行检测
        if (!adminId.equals(1L)){
            UserMessage userMessageBean = userMessageDao.findOne(adminId);
            if (userMessageBean.getAdmin().equals(true)){
                throw new ServiceException(ResponseCode.ACCESSDENIED,"服务器拒绝操作，该用户为管理员，您无权操作");
            }
        }

        //查询用户登陆信息
        List<AuthenticationInfo> authenticationInfo = authenticationInfoDao.findByUserId(userId);
        if (authenticationInfo == null || authenticationInfo.size() == 0){
            throw new ServiceException(ResponseCode.BUSINESS,"未找到用户信息");
        }
        //修改密码
        PasswordUtils passwordUtils = new PasswordUtils();
        String[] newPasswordAndSalt = passwordUtils.encryptPassword(newPassword);
        for (AuthenticationInfo authenticationInfo1 : authenticationInfo){
            authenticationInfo1.setCredential(newPasswordAndSalt[0]);
            authenticationInfo1.setSalt(newPasswordAndSalt[1]);
        }
        authenticationInfoDao.save(authenticationInfo);
    }

    @Override
    public void updatePassword(Long userId, String sms, String newPassword, String oldPassword) {
        //查询用户登陆信息
        List<AuthenticationInfo> authenticationInfo = authenticationInfoDao.findByUserId(userId);
        if (authenticationInfo == null || authenticationInfo.size() == 0){
            throw new ServiceException(ResponseCode.BUSINESS,"未找到用户信息");
        }

        if (sms != null){
            for (AuthenticationInfo authenticationInfo1 :authenticationInfo){
                if (authenticationInfo1.getVoucherType().equals(LoginType.TELEPHONE)){
                    String code = SMSUtils.checkCode(authenticationInfo1.getIdentifier(), SMSUtils.SendType.UPDATA);
                    if (code == null){
                        throw new ServiceException(ResponseCode.ACCESSDENIED,"验证码不存在，请重新获取验证码!");
                    }else if(code.equals("")){
                        throw new ServiceException(ResponseCode.ACCESSDENIED,"验证码已过期，请重新获取!");
                    }else if(!code.equals(sms)){
                        throw new ServiceException(ResponseCode.ACCESSDENIED,"验证码错误!");
                    }
                }
            }
        }else{
            //校验旧密码
            String temp = PasswordCheck.check(oldPassword,authenticationInfo.get(0).getSalt());
            if (!temp.equals(authenticationInfo.get(0).getCredential())){
                throw new ServiceException(ResponseCode.ACCESSDENIED,"密码输入错误");
            }
        }
        //修改密码
        PasswordUtils passwordUtils = new PasswordUtils();
        String[] newPasswordAndSalt = passwordUtils.encryptPassword(newPassword);
        for (AuthenticationInfo authenticationInfo1 : authenticationInfo){
            authenticationInfo1.setCredential(newPasswordAndSalt[0]);
            authenticationInfo1.setSalt(newPasswordAndSalt[1]);
        }
        authenticationInfoDao.save(authenticationInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Object auctionRecord(Long userId, SealType type, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"time");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        Page<SealedBean> sealedBeans;
        if (type.equals(SealType.ALL)){//查询所有
            List<Long> list = new ArrayList<>();
            Map<Long,Integer> item = ItemCache.item_userId.get(userId);
            if (item != null && !item.isEmpty()){
                for (Map.Entry<Long,Integer> map :item.entrySet()){
                    list.add(map.getKey());
                }
                if (!list.isEmpty()){
                    sealedBeans = sealedDao.findAll(userId,list,pageable);
                }else{
                    sealedBeans = sealedDao.findAll_all(userId,pageable);
                }
            }else{
                sealedBeans = sealedDao.findAll_all(userId,pageable);
            }
        }else if(type.equals(SealType.IS_IN_PROGRESS)){//正在拍
            Map<Long, Integer> userItemCaches = ItemCache.item_userId.get(userId);
            if (userItemCaches == null || userItemCaches.isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("content", new ArrayList());
                map.put("number", 0);
                map.put("numberOfElements", 0);
                map.put("size", 0);
                map.put("totalElements", 0);
                map.put("totalPages", 0);
                map.put("first", true);
                map.put("last", true);
                return map;
            }
            //进行转换
            List<Long> userItemCacheList = new ArrayList<>();
            userItemCacheList.addAll(userItemCaches.keySet());
            sealedBeans = sealedDao.findAll(userItemCacheList, false, pageable);
        }else if(type.equals(SealType.WINNING)){//我拍中
            sealedBeans = sealedDao.findAll(userId, pageable);
        }else if(type.equals(SealType.FAILURE)){//未拍中
            sealedBeans = sealedDao.findAllByUserIdOnNoWinning(userId, true, pageable);
        }else if(type.equals(SealType.WAITING_PAYMENT)){//待付款
            OrderState orderState = OrderState.Conversion(type.toString());
            sealedBeans = sealedDao.findAll(userId,orderState,pageable);
        }else if(type.equals(SealType.WAITING_RECEIPT)){//待签收
            OrderState orderState = OrderState.Conversion(type.toString());
            sealedBeans = sealedDao.findAll(userId,orderState,pageable);
        }else if(type.equals(SealType.WAITING_SUN_ALONE)){//待晒单
            OrderState orderState = OrderState.Conversion(type.toString());
            sealedBeans = sealedDao.findAll(userId,orderState,pageable);
        }else if(type.equals(SealType.WAITING_CHOICE_ADDRESS)){//待填写收货地址
            OrderState orderState = OrderState.Conversion(type.toString());
            sealedBeans = sealedDao.findAll(userId,orderState,pageable);
        }else{
            throw new VerifyException("类型参数错误");
        }

        List<Long> orderId = new ArrayList<>();
        for (SealedBean sealedBean : sealedBeans.getContent()){
            sealedBean.setUserMessage(JSON.parseObject(sealedBean.getUser(),UserMessage.class));
            sealedBean.setItemMessage(JSON.parseObject(sealedBean.getItem(),ItemBean.class));
            if (sealedBean.getSealed() && sealedBean.getUserMessage().getId().equals(userId)){
                orderId.add(sealedBean.getId());
            }
        }
        //查询读取订单记录
        List<OrderBean> orderBeans = new ArrayList<>();
        if (!orderId.isEmpty()){
            orderBeans = orderDao.findAll(orderId);
        }

        List<ItemVo> itemVos = new ArrayList<>();
        for (SealedBean sealedBean : sealedBeans) {
            if (!sealedBean.getSealed() && sealedBean.getSealType().equals(SealType.IS_IN_PROGRESS)){
                //该记录现在正在运行
                ItemCache.Item item = ItemCache.getItemMap(sealedBean.getItemId());
                ItemBean itemBean = item.getItemBean();
                itemBean.setCost(null);
                itemBean.setControl_line(null);
                itemBean.setDisable(null);
                itemBean.setRunning_program(null);
                ItemVo itemVo = new ItemVo();
                itemVo.setId(sealedBean.getId());
                itemVo.setItemBean(itemBean);
                itemVo.setInstant_time(item.getInstant_time());
                itemVo.setNumber_periods(item.getNumber_periods());
                itemVo.setItemName(item.getItemBean().getName());
                itemVo.setPicture(item.getItemBean().getSmall_picture());
                itemVo.setSealType(SealType.IS_IN_PROGRESS);
                itemVo.setSettlement(item.isSettlement());
                itemVo.setItemId(item.getItemBean().getId());
                itemVo.setPrice(item.getPrice());
                itemVos.add(itemVo);
            }else{
                ItemBean itemBean = sealedBean.getItemMessage();
                ItemVo itemVo = new ItemVo();
                itemVo.setId(sealedBean.getId());
                itemVo.setItemBean(itemBean);
                itemVo.setUsername(sealedBean.getName());
                itemVo.setNumber_periods(sealedBean.getPeriods());
                itemVo.setItemName(sealedBean.getItemName());
                itemVo.setPicture(sealedBean.getPicture());
                itemVo.setSealType(SealType.FAILURE);
                itemVo.setItemId(sealedBean.getItemId());
                itemVo.setPrice(sealedBean.getMarket_price());
                itemVo.setSettlement(true);
                //判断设置状态，如果是用户自身拍中，插入订单信息
                if (!orderBeans.isEmpty()){
                    for (OrderBean order : orderBeans){
                        if (order.getId().equals(itemVo.getId())){
                            itemVo.setOrder(order);
                            SealType sealType = SealType.Conversion(order.getOrder_state().toString());
                            if (sealType == null){
                                logger.error("出现订单状态不一致");
                            }
                            itemVo.setSealType(sealType);
                            break;
                        }else {
                            itemVo.setSealType(SealType.FAILURE);
                        }
                    }
                }
                itemVos.add(itemVo);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("content", itemVos);
        map.put("number", sealedBeans.getNumber());
        map.put("numberOfElements", sealedBeans.getNumberOfElements());
        map.put("size", sealedBeans.getSize());
        map.put("totalElements", sealedBeans.getTotalElements());
        map.put("totalPages", sealedBeans.getTotalPages());
        map.put("first", sealedBeans.isFirst());
        map.put("last", sealedBeans.isLast());
        map.put("time", new Date());
        return map;
    }

    @Override
    @Transactional
    public void updateUserMessage(UserMessage userMessage, String oldPhoneSMS, String newPhoneSMS) {
        if (userMessage.getId() == null){
            throw new VerifyException("必须传入识别id");
        }
        userMessage.setRobot(null);
        UserMessage old = userMessageDao.findOne(userMessage.getId());

        if (userMessage.getIphone() != null && !old.getIphone().equals(userMessage.getIphone())){
            //需要修改手机号
            if (newPhoneSMS == null){
                throw new ServiceException("请传入新手机号验证码");
            }
            AuthenticationInfo authenticationInfo =
                    authenticationInfoDao.findByUserIdAndVoucherType(userMessage.getId(), LoginType.TELEPHONE);
            //验证验证码是否正确

            String code = SMSUtils.checkCode(userMessage.getIphone(), SMSUtils.SendType.UPDATA);
            if (code == null){
                throw new ServiceException(ResponseCode.BUSINESS,"新手机号验证码不存在，请重新获取验证码!");
            }else if(code.equals("")){
                throw new ServiceException(ResponseCode.BUSINESS,"新手机号验证码已过期，请重新获取!");
            }else if(!code.equals(newPhoneSMS)){
                throw new ServiceException(ResponseCode.BUSINESS,"新手机号验证码错误!");
            }

            //校验需要修改的手机号是否被注册
            if (authenticationInfoDao.findByIdentifierAndVoucherType(userMessage.getIphone(), LoginType.TELEPHONE) != null){
                throw new ServiceException(ResponseCode.BUSINESS,"手机号已经被注册!");
            }

            if (authenticationInfo == null){
                AuthenticationInfo authenticationInfo2 =
                        authenticationInfoDao.findByUserIdAndVoucherType(userMessage.getId(), LoginType.ACCOUNT);
                authenticationInfo = new AuthenticationInfo();
                authenticationInfo.setUserId(userMessage.getId());
                authenticationInfo.setLastModifyBy(1L);
                authenticationInfo.setLastModifyTime(new Date());
                authenticationInfo.setCreateTime(new Date());
                authenticationInfo.setCreateBy(1L);
                authenticationInfo.setCredential(authenticationInfo2.getCredential());
                authenticationInfo.setSalt(authenticationInfo2.getSalt());
                authenticationInfo.setIdentifier(userMessage.getIphone());
                authenticationInfo.setVoucherType(LoginType.TELEPHONE);
            }else{
                if (oldPhoneSMS == null){
                    throw new ServiceException("请传入旧手机号验证码");
                }
                code = SMSUtils.checkCode(old.getIphone(), SMSUtils.SendType.UPDATA);
                if (code == null){
                    throw new ServiceException(ResponseCode.BUSINESS,"旧手机号验证码不存在，请重新获取验证码!");
                }else if(code.equals("")){
                    throw new ServiceException(ResponseCode.BUSINESS,"旧手机号验证码已过期，请重新获取!");
                }else if(!code.equals(oldPhoneSMS)){
                    throw new ServiceException(ResponseCode.BUSINESS,"旧手机号验证码错误!");
                }
                authenticationInfo.setIdentifier(userMessage.getIphone());
            }
            authenticationInfoDao.save(authenticationInfo);
        }
        if (userMessage.getUsername() != null && !old.getUsername().equals(userMessage.getUsername())){
            //需要修改用户名

            //校验需要修改的手机号是否被注册
            if (authenticationInfoDao.findByIdentifierAndVoucherType(userMessage.getUsername(), LoginType.ACCOUNT) != null){
                throw new ServiceException(ResponseCode.BUSINESS,"用户名已经被注册!");
            }

            AuthenticationInfo authenticationInfo =
                    authenticationInfoDao.findByUserIdAndVoucherType(userMessage.getId(), LoginType.ACCOUNT);
            if (authenticationInfo == null){
                AuthenticationInfo authenticationInfo2 =
                        authenticationInfoDao.findByUserIdAndVoucherType(userMessage.getId(), LoginType.TELEPHONE);
                authenticationInfo = new AuthenticationInfo();
                authenticationInfo.setUserId(userMessage.getId());
                authenticationInfo.setLastModifyBy(1L);
                authenticationInfo.setLastModifyTime(new Date());
                authenticationInfo.setCreateTime(new Date());
                authenticationInfo.setCreateBy(1L);
                authenticationInfo.setCredential(authenticationInfo2.getCredential());
                authenticationInfo.setSalt(authenticationInfo2.getSalt());
                authenticationInfo.setIdentifier(userMessage.getUsername());
                authenticationInfo.setVoucherType(LoginType.ACCOUNT);
            }else{
                authenticationInfo.setIdentifier(userMessage.getUsername());
            }
            authenticationInfoDao.save(authenticationInfo);
        }
        Tool.dataCheckout(userMessage,old);
        userMessageDao.save(old);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> invitationToReward(Long userId) {
        Integer one_proxy = userMessageDao.findNumberToLevel_one_proxy(userId);
        Integer two_proxy = userMessageDao.findNumberToLevel_two_proxy(userId);
        Integer coinNumber = coinDao.findReward(userId);
        if (one_proxy == null)one_proxy = 0;
        if (two_proxy == null)two_proxy = 0;
        if (coinNumber == null)coinNumber = 0;
        LocalDate localDate = LocalDate.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        Date endTime = Date.from(zdt.toInstant());
        LocalDate stateTime = LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth()-1);
        zdt = stateTime.atStartOfDay(zoneId);
        Date stateDate = Date.from(zdt.toInstant());
        Integer yesterday = coinDao.findReward(userId,stateDate,endTime);
        if (yesterday == null)yesterday = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("proxy",one_proxy);
        map.put("son_proxy",two_proxy);
        map.put("total",coinNumber);
        map.put("yesterdayCoin",yesterday);
        return map;
    }

    @Override
    @Transactional
    public void setting_address(Long orderId, Long addressId, Long userId) {
        OrderBean orderBean = orderDao.findOne(orderId);
        if (orderBean.getReceipt_id()!= null){
            throw new ServiceException(ResponseCode.ACCESSDENIED,"一个订单只能设置一次");
        }
        if (orderBean.getUserId().equals(userId)){
            AddressBean addressBean = addressDao.findOne(addressId);
            orderBean.setReceipt_id(addressId);
            orderBean.setOrder_state(OrderState.WAITING_SHIP);
            orderBean.setReceiptString(JSON.toJSONString(addressBean));

            orderDao.save(orderBean);
            //发送通知
            String context = "订单编号：" + orderBean.getId() + "可以进行发货了，发货地址为" +
                    addressBean.getAddress() + " " + addressBean.getDetailed_address() +
                    ",发货商品为：" + orderBean.getItem_id() + ",商品id:" + orderBean.getId() +
                    ",中奖用户id：" + orderBean.getUserId();
            systemNoticeService.add("商品发货通知", context);
        }else{
            throw new ServiceException(ResponseCode.ACCESSDENIED,"操作失败");
        }
    }

    @Override
    @Transactional
    public void giftCoin(Long userId,Integer number,String reason) {
        if (number < 0 && (reason == null || reason.trim().equals(""))){
            throw new ServiceException("扣除赠币一定要写明扣除原因");
        }

        if (number < 0){
            Integer integral = coinDao.findByUserIdAndCoinType(userId,CoinType.RECHARGE);
            if (integral == null || Math.abs(number) > integral){
                throw new ServiceException("用户赠币不足于扣除，用户赠币为："+integral);
            }
        }

        CoinBean coinBean = new CoinBean();
        coinBean.setTime(new Date());
        coinBean.setCoinType(CoinType.GIFT);
        coinBean.setNumber(number);

        if (number <0){
            coinBean.setSource(CoinSource.SYSTEM_DEDUCTION);
        }else{
            coinBean.setSource(CoinSource.Gift);
        }

        coinBean.setUserId(userId);
        if (reason == null){
            coinBean.setReason("系统赠送");
        }else{
            coinBean.setReason(reason);
        }
        coinBean.setPromotion(false);
        coinDao.save(coinBean);
    }

    @Override
    @Transactional
    public void giftRealCoin(Long userId, Integer number, String reason) {

        if (number < 0 && (reason == null || reason.trim().equals(""))){
            throw new ServiceException("扣除拍币一定要写明扣除原因");
        }

        if (number < 0){
            Integer integral = coinDao.findByUserIdAndCoinType(userId,CoinType.RECHARGE);
            if (integral == null || Math.abs(number) > integral){
                throw new ServiceException("用户拍币不足于扣除，用户拍币为："+integral);
            }
        }
        CoinBean coinBean = new CoinBean();
        coinBean.setTime(new Date());
        coinBean.setCoinType(CoinType.RECHARGE);
        coinBean.setNumber(number);

        if (number <0){
            coinBean.setSource(CoinSource.SYSTEM_DEDUCTION);
        }else{
            coinBean.setSource(CoinSource.Gift);
        }

        coinBean.setUserId(userId);
        if (reason == null){
            coinBean.setReason("系统赠送");
        }else{
            coinBean.setReason(reason);
        }
        coinBean.setPromotion(false);
        coinDao.save(coinBean);
    }

    @Override
    @Transactional
    public void giftIntegral(Long userId, Integer number, String reason) {
        IntegralBean integralBean = new IntegralBean();
        if (number < 0 && (reason == null || reason.trim().equals(""))){
            throw new ServiceException("扣除积分一定要写明扣除原因");
        }

        if (number < 0){
            Integer integral = integralDao.findByUserId(userId);
            if (integral == null || Math.abs(number) > integral){
                throw new ServiceException("用户积分不足于扣除，用户积分为："+integral);
            }
        }

        if (reason == null){
            integralBean.setReason("系统赠送");
        }else{
            integralBean.setReason(reason);
        }
        integralBean.setUserId(userId);
        integralBean.setNumber(number);
        integralBean.setOperationalType(OperationalType.GIFT);
        integralBean.setTime(new Date());
        integralDao.save(integralBean);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IntegralBean> integral_flow(Long userId, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"time");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        return integralDao.findByUserId(userId,pageable);
    }

    @Override
    @Transactional
    public void cancelOrDisableAdminByUserId(Long userId) {
        if(userId.equals(1L)){
            throw new ServiceException(ResponseCode.ACCESSDENIED,"无法禁用系统默认超级管理员");
        }
        User user = userDao.findOne(userId);
        if (user == null){
            throw new VerifyException(ResponseCode.BAD_REQUEST,"没有找到该用户");
        }
        user.setDisable(!user.getDisable());
        userDao.save(user);
    }

    @Override
    @Transactional
    public void reportedReset(Long userId) {
        userMessageDao.reportedReset(userId,0);
    }
}
