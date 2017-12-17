package com.mierro.main.controller.admin;

import com.mierro.authority.common.AythorityType;
import com.mierro.authority.common.LoginType;
import com.mierro.authority.controller.SystemModelHandler;
import com.mierro.authority.entity.AuthenticationInfo;
import com.mierro.authority.entity.User;
import com.mierro.authority.service.AuthorityService;
import com.mierro.authority.shiro.CustomUsernamePasswordToken;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.common.common.ServiceException;
import com.mierro.common.common.VerifyException;
import com.mierro.common.kaptcha.KaptchaCheckout;
import com.mierro.main.common.CoinType;
import com.mierro.main.entity.AddressBean;
import com.mierro.main.entity.CoinBean;
import com.mierro.main.entity.SealedBean;
import com.mierro.main.service.AddressService;
import com.mierro.main.service.CoinService;
import com.mierro.main.service.SealedService;
import com.mierro.main.service.UserMessageService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/8/11.
 * Remarks：用户管理
 */
@RestController
@RequestMapping("/admin")
public class AdminUserController {

    @Resource
    private UserMessageService userService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    private AddressService addressService;

    @Resource
    private SealedService sealedService;

    @Resource
    private CoinService coinService;


    /**
     * 分页查询所有用户
     * @param name 用户名称
     * @param phone 手机号
     * @param disable 是否禁用
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    @GetMapping("/users")
    public ResultMessage getOrdinaryMembers(@RequestParam(value = "userId",required = false) Long userId,
                                            @RequestParam(value = "name",required = false) String name,
                                            @RequestParam(value = "phone",required = false) String phone,
                                            @RequestParam(value = "disable",required = false) Boolean disable,
                                            @RequestParam("pageNo") Integer pageNo,
                                            @RequestParam("pageSize") Integer pageSize){
        Page page = userService.findAll(userId,name,phone,disable,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }

    /**
     * 会员关注栏目
     * @param userId 会员id
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 会员分页数目
     */
    @GetMapping("/users/attention")
    public ResultMessage getOrdinaryMembersAttention(@RequestParam(value = "userId",required = false) Long userId,
                                                     @RequestParam("pageNo") Integer pageNo,
                                                     @RequestParam("pageSize") Integer pageSize){
        Page page = userService.findAll(userId,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }

    /**
     * 登录接口
     * @param identifier 标识符
     * @param password 密码
     * @param validate 验证码
     * @return 操作码
     */
    @PostMapping("/login")
    public ResultMessage login(@RequestParam("identifier")String identifier,
                               @RequestParam(value = "password",required = false) String password,
                               @RequestParam(value = "validate") String validate,
                               @ApiIgnore HttpServletRequest request){
        AuthenticationInfo authenticationInfo;
        //校验验证码
        if (!KaptchaCheckout.checkout(request,validate)){
            return new ResultMessage(ResponseCode.BAD_REQUEST,"验证码错误");
        }
        //检测是否已经登录，是--进行退出登录
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered()) {
            subject.logout();
        }
        LoginType loginType = LoginType.ACCOUNT;
        authenticationInfo = authorityService.selectLoginMessageByIdentifier(identifier, LoginType.ACCOUNT);
        if (authenticationInfo == null){
            loginType = LoginType.TELEPHONE;
            authenticationInfo = authorityService.selectLoginMessageByIdentifier(identifier, LoginType.TELEPHONE);
        }
        if (authenticationInfo == null){
            return new ResultMessage(ResponseCode.BUSINESS,"没有查询到该账户，该登录方式只支持手机或者用户名登录");
        }
        if (authorityService.findAythorityTypeByUserId(authenticationInfo.getUserId(), AythorityType.INSIDER)){
            return new ResultMessage(ResponseCode.ACCESSDENIED,"拒绝登录");
        }
        CustomUsernamePasswordToken token = new CustomUsernamePasswordToken(
                identifier.trim(), password, AythorityType.INSIDER, loginType);
        subject.login(token);

        //修改用户最后登陆信息
        authorityService.updateFinalLoginByUserId(authenticationInfo.getUserId(),request);

        return new ResultMessage(ResponseCode.OK,"登录成功");
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
                                        @RequestParam("oldPassword") String oldPassword){
        if (user.getId() == null){
            throw new ServiceException(ResponseCode.UNAUTHORIZED,"请先登陆");
        }
        userService.updatePassword(user.getId(),null,newPassword,oldPassword);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 修改用户密码(修改会员的密码，进行密码重置)
     * @param userId 用户id
     * @param newPassword 新密码
     * @return 操作码
     */
    @PutMapping("/user/password")
    public ResultMessage updatePassword(@ApiIgnore @ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                        @RequestParam("userId") Long userId,
                                        @RequestParam("newPassword") String newPassword){
        if (user.getId().equals(userId)){
            throw new ServiceException(ResponseCode.ACCESSDENIED,"服务器拒绝操作，该接口不能修改自身");
        }
        userService.updatePassword(user.getId(),userId,newPassword);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 获取用户所有地址
     * @return 地址集合
     */
    @GetMapping("/user/address")
    public ResultMessage getAddress(@RequestParam("userId") Long userId){
        List<AddressBean> addressBeans = addressService.findAll(userId);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",addressBeans);
    }

    /**
     * 赠送赠币
     * @param userId 用户id
     * @param number 赠送数量
     * @param reason 操作说明
     * @return 操作码
     */
    @PostMapping("/user/gift")
    public ResultMessage giftCoin(@RequestParam("userId") Long userId,
                                  @RequestParam("number") Integer number,
                                  @RequestParam(value = "reason",required = false) String reason){
        userService.giftCoin(userId,number,reason);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 赠送拍币
     * @param userId 用户id
     * @param number 赠送数量
     * @param reason 操作说明
     * @return 操作码
     */
    @PostMapping("/user/coin")
    public ResultMessage giftRealCoin(@RequestParam("userId") Long userId,
                                      @RequestParam("number") Integer number,
                                      @RequestParam(value = "reason",required = false) String reason){
        userService.giftRealCoin(userId,number,reason);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 增加用户积分
     * @param userId 用户id
     * @param number 赠送数量
     * @param reason 操作说明
     * @return 操作码
     */
    @PostMapping("/user/integral")
    public ResultMessage giftIntegral(@RequestParam("userId") Long userId,
                                      @RequestParam("number") Integer number,
                                      @RequestParam(value = "reason",required = false) String reason){
        userService.giftIntegral(userId,number,reason);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 修改用户状态
     * @param userId 用户id
     * @return 操作码
     */
    @PutMapping("/user/start")
    public ResultMessage updateStart(@ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                     @RequestParam("userId") Long userId){
        if (user.getId().equals(userId)){
            throw new VerifyException(ResponseCode.ACCESSDENIED,"不允许操作自身");
        }
        userService.cancelOrDisableAdminByUserId(userId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 查看用户历史投票
     * @param userId 用户id
     * @return 历史投票记录
     */
    @GetMapping("/user/sealed")
    public ResultMessage findSealedByBidder(@RequestParam("userId") Long userId,
                                            @RequestParam("pageNo") Integer pageNo,
                                            @RequestParam("pageSize") Integer pageSize){
        Page<SealedBean> page = sealedService.findSealedByBidder(userId,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }

    /**
     * 查看用户金币流动情况
     * @param userId 用户id
     * @param pageNo 页码
     * @param pageSize 每页数目
     * @param coinType 金币类型
     * @return 流动情况
     */
    @GetMapping("/user/coin_flow")
    public ResultMessage findCoinFlow(@RequestParam("userId") Long userId,
                                      @RequestParam("pageNo") Integer pageNo,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam(value = "coinType",required = false) CoinType coinType){
        Page<CoinBean> coinBeans = coinService.findAll(userId,coinType,pageNo,pageSize) ;
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",coinBeans);
    }

    /**
     * 查看用户金币情况统计
     * @param userId 用户id
     * @param coinType 金币类型
     * @return 流动情况
     */
    @GetMapping("/user/coin_statistics")
    public ResultMessage findCoinStatistics(@RequestParam("userId") Long userId,
                                            @RequestParam(value = "coinType") CoinType coinType){
        Map coinBeans = coinService.findStatistics(userId,coinType) ;
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",coinBeans);
    }
}
