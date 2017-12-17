package com.mierro.authority.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/7/20.
 * Remarks：
 */
@Entity
@Table(name = "t_user_message")
public class UserMessage implements IValueObject {

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    @Column(length = 20,unique = true)
    @Size(max = 20,message = "名字最大20个字符")
    private String username;


    @Size(max = 11,min =  11,message = "手机必须是十一位")
    @Pattern(regexp = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$",message = "请输入正确手机号")
    @Column(length = 11,unique = true)
    private String iphone;

    @Column(unique = true)
    @Pattern(regexp = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])",message = "身份证输入不合法")
    private String id_card;

    @Email(message = "请输入正确邮箱地址")
    @Column(unique = true)
    private String email;

    @Column(updatable = false)
    private Date join_date;

    /**
     * 头像
     */
    private Long head_pic;

    /**
     * 是否管理员
     */
    @JSONField(serialize=false)
    private Boolean admin;

    /**
     * 是否机器人
     */
    @JSONField(serialize=false)
    private Boolean robot;

    /**
     * 地址
     */
    private String address;

    /**
     * 积分次数
     */
    @Min(value = 0)
    private Integer integral;

    /**
     * 角色集合
     */
    @Transient
    private List<Role> roles;

    /**
     * 最后登陆时间
     */
    @Transient
    private Date final_login;

    /**
     * 是否禁用
     */
    @Transient
    private Boolean disable;

    @JSONField(serialize = false)
    private Integer days;

    /**
     * 赠币
     */
    @Transient
    private Integer gift;

    /**
     * 中奖次数
     */
    private Integer number;

    /**
     * 拍币
     */
    @Transient
    private Integer coin;

    /**
     * 注册时间
     */
    @Transient
    private Date reportedTime;

    /**
     * 一级代理id
     */
    @JSONField(serialize = false)
    private Long Level_one_proxy;

    /**
     * 二级代理id
     */
    @JSONField(serialize = false)
    private Long Level_two_proxy;

    /**
     * 所属渠道商
     */
    @JSONField(serialize = false)
    private Long promotion;

    // ------------------------微信第三方信息------------------------------
    /**
     * 微信openid
     */
    @JSONField(serialize = false)
    private String wechat_openid;
    /** 微信第三方登录信息 */
    @JSONField(serialize = false)
    private String wechat_access_token;

    /** 微信第三方登录信息 */
    @JSONField(serialize = false)
    @Column(columnDefinition = "TEXT")
    private String wechat_userinfo;

    // ------------------------qq第三方信息------------------------------
    /**
     * QQopenid
     */
    @JSONField(serialize = false)
    private String qq_openid;
    /** QQ第三方登录信息 */
    @JSONField(serialize = false)
    private String qq_access_token;

    /** QQ  第三方登录信息 */
    @JSONField(serialize = false)
    @Column(columnDefinition = "TEXT")
    private String qq_userinfo;

    /**
     * 总充值额
     */
    @Transient
    private Integer total_recharge;
    /**
     * 余额
     */
    @Transient
    private Integer balanceNumber;
    /**
     * 收益
     */
    @Transient
    private Integer income;

    /**
     * 注册ip
     */
    private String ip;

    public UserMessage(){}

    public UserMessage(Long id,Object reportedTime){
        this.id = id;
        this.reportedTime = (Date) reportedTime;
    }

    public UserMessage(UserMessage userMessage, Object reportedTime){
        this.id = userMessage.getId();
        this.username = userMessage.getUsername();
        this.iphone = userMessage.getIphone();
        this.id_card = userMessage.getId_card();
        this.email = userMessage.getEmail();
        this.join_date = userMessage.getJoin_date();
        this.number = userMessage.getNumber();
        this.head_pic = userMessage.getHead_pic();
        this.admin = userMessage.getAdmin();
        this.robot = userMessage.getRobot();
        this.integral = userMessage.getIntegral();
        this.days = userMessage.getDays();
        this.address = userMessage.address;
        this.reportedTime = (Date) reportedTime;
    }

    public UserMessage(UserMessage userMessage, Long totalRecharge, Object reportedTime){
        this.id = userMessage.getId();
        this.username = userMessage.getUsername();
        this.iphone = userMessage.getIphone();
        this.id_card = userMessage.getId_card();
        this.email = userMessage.getEmail();
        this.join_date = userMessage.getJoin_date();
        this.number = userMessage.getNumber();
        this.head_pic = userMessage.getHead_pic();
        this.admin = userMessage.getAdmin();
        this.robot = userMessage.getRobot();
        this.integral = userMessage.getIntegral();
        this.days = userMessage.getDays();
        this.address = userMessage.address;
        this.total_recharge = totalRecharge.intValue();
        this.reportedTime = (Date) reportedTime;
    }

    public UserMessage(UserMessage userMessage, Date finalLong, Boolean disable){
        this.id = userMessage.getId();
        this.username = userMessage.getUsername();
        this.iphone = userMessage.getIphone();
        this.id_card = userMessage.getId_card();
        this.email = userMessage.getEmail();
        this.join_date = userMessage.getJoin_date();
        this.number = userMessage.getNumber();
        this.head_pic = userMessage.getHead_pic();
        this.final_login = finalLong;
        this.disable = disable;
        this.admin = userMessage.getAdmin();
        this.robot = userMessage.getRobot();
        this.integral = userMessage.getIntegral();
        this.days = userMessage.getDays();
        this.address = userMessage.address;
    }

    public Integer getGift() {
        return gift;
    }

    public void setGift(Integer gift) {
        this.gift = gift;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getJoin_date() {
        return join_date;
    }

    public void setJoin_date(Date join_date) {
        this.join_date = join_date;
    }

    public Long getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(Long head_pic) {
        this.head_pic = head_pic;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Date getFinal_login() {
        return final_login;
    }

    public void setFinal_login(Date final_login) {
        this.final_login = final_login;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public Boolean getRobot() {
        return robot;
    }

    public void setRobot(Boolean robot) {
        this.robot = robot;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Date getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(Date reportedTime) {
        this.reportedTime = reportedTime;
    }

    public Long getLevel_two_proxy() {
        return Level_two_proxy;
    }

    public void setLevel_two_proxy(Long level_two_proxy) {
        Level_two_proxy = level_two_proxy;
    }

    public Long getLevel_one_proxy() {
        return Level_one_proxy;
    }

    public void setLevel_one_proxy(Long level_one_proxy) {
        Level_one_proxy = level_one_proxy;
    }

    public Long getPromotion() {
        return promotion;
    }

    public void setPromotion(Long promotion) {
        this.promotion = promotion;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getWechat_openid() {
        return wechat_openid;
    }

    public void setWechat_openid(String wechat_openid) {
        this.wechat_openid = wechat_openid;
    }

    public String getWechat_access_token() {
        return wechat_access_token;
    }

    public void setWechat_access_token(String wechat_access_token) {
        this.wechat_access_token = wechat_access_token;
    }

    public String getWechat_userinfo() {
        return wechat_userinfo;
    }

    public void setWechat_userinfo(String wechat_userinfo) {
        this.wechat_userinfo = wechat_userinfo;
    }

    public String getQq_openid() {
        return qq_openid;
    }

    public void setQq_openid(String qq_openid) {
        this.qq_openid = qq_openid;
    }

    public String getQq_access_token() {
        return qq_access_token;
    }

    public void setQq_access_token(String qq_access_token) {
        this.qq_access_token = qq_access_token;
    }

    public String getQq_userinfo() {
        return qq_userinfo;
    }

    public void setQq_userinfo(String qq_userinfo) {
        this.qq_userinfo = qq_userinfo;
    }

    public Integer getTotal_recharge() {
        return total_recharge;
    }

    public void setTotal_recharge(Integer total_recharge) {
        this.total_recharge = total_recharge;
    }

    public Integer getBalanceNumber() {
        return balanceNumber;
    }

    public void setBalanceNumber(Integer balanceNumber) {
        this.balanceNumber = balanceNumber;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
