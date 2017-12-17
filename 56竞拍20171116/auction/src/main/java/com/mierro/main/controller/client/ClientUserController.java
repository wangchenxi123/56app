package com.mierro.main.controller.client;

import com.alibaba.fastjson.JSON;
import com.mierro.authority.common.AythorityType;
import com.mierro.authority.common.LoginType;
import com.mierro.authority.controller.SystemModelHandler;
import com.mierro.authority.entity.AuthenticationInfo;
import com.mierro.authority.entity.User;
import com.mierro.authority.entity.UserMessage;
import com.mierro.authority.service.AuthorityService;
import com.mierro.authority.service.UserService;
import com.mierro.authority.shiro.CustomUsernamePasswordToken;
import com.mierro.common.common.*;
import com.mierro.main.common.SealType;
import com.mierro.main.entity.AddressBean;
import com.mierro.main.global.SystemCache;
import com.mierro.main.service.AddressService;
import com.mierro.main.service.CollectionService;
import com.mierro.main.service.UserMessageService;
import com.mierro.main.util.SMSUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by 黄晓滨 simba on 2017/8/11.
 * Remarks：
 */
@RestController
@RequestMapping("/client")
public class ClientUserController {

    @Resource
    private UserMessageService userMessageService;
    @Resource
    private UserService userService;
    @Resource
    private AuthorityService authorityService;
    @Resource
    private AddressService addressService;
    @Resource
    private CollectionService collectionService;

    /**
     * 获取个人资料
     * @return 个人资料信息
     */
    @GetMapping("/user")
    public ResultMessage getOrdinaryMembers(@ModelAttribute(SystemModelHandler.CURRENT_USER) User user){
        UserMessage userMessage = userMessageService.findOne(user.getId());
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",userMessage);
    }

    /**
     * 前台登录
     * @param identifier 用户名或者手机号等识别码
     * @param password 密码
     * @param sms 短信验证码
     * @param refreshToken token
     * @return 新的token值
     */
    @PostMapping("/login")
    public ResultMessage login(@RequestParam(value = "identifier",required = false)String identifier,
                               @RequestParam(value = "password",required = false) String password,
                               @RequestParam(value = "sms",required = false) String sms,
                               @RequestParam(value = "refreshToken",required = false) String refreshToken,
                               HttpServletRequest httpServletRequest){
        AuthenticationInfo authenticationInfo;
        if (refreshToken != null){
            //校验Token
            User user = userService.findByToken(refreshToken);
            if (user == null ||user.getExpirationTime().getTime()< new Date().getTime()){
                return new ResultMessage(ResponseCode.BAD_REQUEST,"refreshToken已经失效");
            }

            authenticationInfo = authorityService.findByUserIdAndLongType(user.getId(), LoginType.ACCOUNT);
            if (authenticationInfo == null) {
                throw new ServiceException(ResponseCode.BUSINESS, "该用户没有设置用户名，登陆失败，请联系管理员");
            }

            //进行授权登陆
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated() || subject.isRemembered()) {
                subject.logout();
            }
            //采用默认密码
            String pas = "123456";
            CustomUsernamePasswordToken token = new CustomUsernamePasswordToken(
                    authenticationInfo.getIdentifier(), pas, AythorityType.CLIENT, LoginType.ACCOUNT, "AUTO");
            subject.login(token);
        }else{
            if (sms == null){
                //检测是否已经登录，是--进行退出登录
                Subject subject = SecurityUtils.getSubject();
                if (subject.isAuthenticated() || subject.isRemembered()) {
                    subject.logout();
                }
                LoginType loginType = LoginType.ACCOUNT;
                authenticationInfo = authorityService.selectLoginMessageByIdentifier(identifier, LoginType.ACCOUNT);
                if (authenticationInfo == null){
                    loginType = LoginType.TELEPHONE;
                }
                CustomUsernamePasswordToken token = new CustomUsernamePasswordToken(
                        identifier.trim(), password, AythorityType.CLIENT, loginType);
                subject.login(token);
            }else{//手机验证码登陆
                //验证验证码是否正确
                String code = SMSUtils.checkCode(identifier, SMSUtils.SendType.LOGIN);
                if (code == null){
                    return new ResultMessage(ResponseCode.BUSINESS,"验证码不存在，请重新获取验证码!");
                }else if(code.equals("")){
                    return new ResultMessage(ResponseCode.BUSINESS,"验证码已过期，请重新获取!");
                }else if(!code.equals(sms)){
                    return new ResultMessage(ResponseCode.BUSINESS,"验证码错误!");
                }
                //进行授权登陆
                Subject subject = SecurityUtils.getSubject();
                if (subject.isAuthenticated() || subject.isRemembered()) {
                    subject.logout();
                }
                authenticationInfo = authorityService.selectLoginMessageByIdentifier(identifier, LoginType.TELEPHONE);
                if (authenticationInfo == null) {
                    throw new ServiceException(ResponseCode.BUSINESS, "登陆失败，没有查到该登陆信息");
                }
                //采用默认密码
                String pas = "123456";
                CustomUsernamePasswordToken token = new CustomUsernamePasswordToken(
                        identifier, pas, AythorityType.CLIENT, LoginType.TELEPHONE, "AUTO");
                subject.login(token);
                //清除验证码缓存
                SMSUtils.removeCodeCache(identifier);
            }
        }
        
        //修改用户最后登陆信息
        if (authenticationInfo != null){
            authorityService.updateFinalLoginByUserId(authenticationInfo.getUserId(),httpServletRequest);
        }

        //生成token
        String token = UUID.randomUUID().toString();
        Date data = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.DATE,3);
        //保存新的token
        userService.updateToken(SystemModelHandler.getCurrentUser().getId(),token,calendar.getTime());
        Map<String,String> m = new HashMap<>();
        m.put("token",token);
        m.put("expirationTime",(calendar.getTime().getTime()-new Date().getTime())+"");
        return new ResultMessage(ResponseCode.OK,"登录成功").putParam("resource",m);
    }

    /**
     * 申请注册普通用户
     * @param phone 手机
     * @param sms 短信验证码
     * @return 用户信息
     */
    @PostMapping(value = "/register")
    public ResultMessage add(@RequestParam("phone") String phone,
                             @RequestParam("sms") String sms,
                             @RequestParam("password") String password,
                             HttpServletRequest httpServletRequest){
//        //获取ip
//        String ip = Tool.getRealIp(httpServletRequest);
//        //获取定位
//        String position ;
//        System.out.println("http://restapi.amap.com/v3/ip?key="+ SystemCache.mapkey+"&ip="+ip);
//        String resource = HttpClient.get("http://restapi.amap.com/v3/ip?key="+SystemCache.mapkey+"&ip="+ip);
//        System.out.println(resource);
//        Map jsonObject= JSON.parseObject(resource);
//        if (jsonObject.get("status").equals("1")){
//            try {
//                String province = (String) jsonObject.get("province");
//                if (province == null || province.trim().equals("") || province.equals("局域网")){
//                    throw new ServiceException("无法获取定位");
//                }
//                String city = (String) jsonObject.get("city");
//                if (city == null || city.trim().equals("") || city.equals("局域网")){
//                    throw new ServiceException("无法获取定位");
//                }
//                province = province.substring(0,province.length()-1);
//                city = city.substring(0,city.length()-1);
//                position = province +" "+city;
//            }catch (Exception e){
//                throw new ServiceException("无法获取定位");
//            }
//        }else{
//            throw new ServiceException("无法获取定位");
//        }
        String position ="广东 广州";
        Map<String,String> m = userMessageService.addUser(phone,sms,null,password,position);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",m);
    }


    /**
     * 修改用户密码(修改自己的密码)
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     * @return 操作码
     */
    @PutMapping("/password")
    public ResultMessage updatePassword(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                        @RequestParam("newPassword") String newPassword,
                                        @RequestParam(value = "sms",required = false) String sms,
                                        @RequestParam(value = "oldPassword",required = false) String oldPassword){
        if (oldPassword == null && sms == null){
            throw new VerifyException("请使用旧密码验证或者手机号验证修改");
        }
        userMessageService.updatePassword(user.getId(),sms,newPassword,oldPassword);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 个人竞拍记录
     * @return 分页对象
     */
    @GetMapping("/auction/record")
    public ResultMessage auction_record(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                        @RequestParam("selete_type") SealType selete_type,
                                        @RequestParam("pageNo") Integer pageNo,
                                        @RequestParam("pageSize") Integer pageSize){
        Object object = userMessageService.auctionRecord(user.getId(),selete_type,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",object);
    }

    /**
     * 为中奖订单添加选定收获地址
     * @param orderId 订单id
     * @param addressId 地址id
     * @return 操作码
     */
    @PutMapping("/order/address")
    public ResultMessage setting_address(@RequestParam("orderId") Long orderId,
                                         @RequestParam("addressId")Long addressId,
                                         @ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user){
        userMessageService.setting_address(orderId,addressId,user.getId());
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 添加一条地址信息
     * @param userAddress 地址信息
     * @return 操作码
     */
    @PostMapping("/user/address")
    public ResultMessage addAddress(@ModelAttribute("userAddress") @Valid AddressBean userAddress,
                                    @ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                    BindingResult result){
        DataCheck.returnError(result);
        userAddress.setUserId(user.getId());
        addressService.addAddress(userAddress);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 获取用户所有地址
     * @return 地址集合
     */
    @GetMapping("/user/address")
    public ResultMessage getAddress(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user){
        List<AddressBean> addressBeans = addressService.findAll(user.getId());
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",addressBeans);
    }

    /**
     * 删除一个地址
     * @param addressId 地址id
     * @return 操作码
     */
    @DeleteMapping("/user/address")
    public ResultMessage deleteAddress(Long addressId){
        addressService.deleteAddress(addressId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 修改一个地址
     * @param userAddress 地址对象
     * @return 操作码
     */
    @PutMapping("/user/address")
    public ResultMessage addAddress(@ModelAttribute("userAddress") AddressBean userAddress){
        addressService.updateAddress(userAddress);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 查询用户收藏
     * @param pageNo 页码
     * @param pageSize 每页数目
     * @return 收藏记录分页对象
     */
    @GetMapping("/user/collection")
    public ResultMessage selectCollection(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                          @RequestParam("pageNo") Integer pageNo,
                                          @RequestParam("pageSize") Integer pageSize){
        Page page = collectionService.findAll(user.getId(),pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }

    /**
     * 添加一个收藏
     * @param itemId 商品id
     * @return 操作码
     */
    @PostMapping("/user/collection")
    public ResultMessage addCollection(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                       @RequestParam("itemId") Long itemId){
        collectionService.addCollection(user.getId(),itemId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 删除一个收藏
     * @param collectionId 收藏id
     * @return 操作码
     */
    @DeleteMapping("/user/collection")
    public ResultMessage deleteCollection(@RequestParam("collectionId") Long collectionId){
        collectionService.deleteCollection(collectionId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 更新头像
     * @param headPicId 头像文件地址id
     * @return 操作码
     */
    @PutMapping("/user/avatar")
    public ResultMessage updateUserHeadPic(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                           @RequestParam("headPicId") Long headPicId){
        UserMessage userMessage = new UserMessage();
        userMessage.setHead_pic(headPicId);
        userMessage.setId(user.getId());
        userMessageService.updateUserMessage(userMessage,null,null);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 修改用户名
     * @param name 用户名称
     * @return 操作码
     */
    @PutMapping("/user/name")
    public ResultMessage updateUsername(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                        @RequestParam("name") String name){
        UserMessage userMessage = new UserMessage();
        userMessage.setUsername(name);
        userMessage.setId(user.getId());
        userMessageService.updateUserMessage(userMessage,null,null);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 修改手机号
     * @param phone 手机号
     * @param oldPhoneSMS 旧手机号验证码
     * @param newPhoneSMS 新手机号验证码
     * @return 操作码
     */
    @PutMapping("/user/phone")
    public ResultMessage updatePhone(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                     @RequestParam("phone") String phone,
                                     @RequestParam(value = "oldPhoneSMS",required = false) String oldPhoneSMS,
                                     @RequestParam("newPhoneSMS") String newPhoneSMS){
        UserMessage userMessage = new UserMessage();
        userMessage.setIphone(phone);
        userMessage.setId(user.getId());
        userMessageService.updateUserMessage(userMessage,oldPhoneSMS,newPhoneSMS);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 获取积分流动详情
     * @return 积分流动分页情况
     */
    @GetMapping(value = "/integral/flow")
    public ResultMessage integral_flow(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                       @RequestParam("pageNo") Integer pageNo,
                                       @RequestParam("pageSize") Integer pageSize){
        Page page = userMessageService.integral_flow(user.getId(),pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }
}
