package com.mierro.main.entity;

import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
@Entity
@Table(name = "t_address")
public class AddressBean implements IValueObject {

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 地址所属用户id
     */
    private Long userId;

    /**
     * 收货人名称
     */
    @Column(length = 20)
    @Size(max = 20,message = "名字最大20个字符")
    private String name;

    /**
     * 收货人手机号码
     */
    @Size(max = 11,min =  11,message = "手机必须是十一位")
    @Pattern(regexp = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$",message = "请输入正确手机号")
    private String phone;

    /**
     * 收货人地址
     */
    @NotNull
    private String address;

    /**
     * 收货人详细地址
     */
    @NotNull
    private String detailed_address;

    /**
     * QQ号
     */
    @Length(max = 12,min = 5)
    private String penguin;

    /**
     * 支付宝账号
     */
    private String alipay;

    /**
     * 邮箱
     */
    @Email(message = "请输入正确邮箱地址")
    private String email;

    /**
     * 创建时间
     */
    private Date time;

    /**
     * 是否默认
     */
    private Boolean acquiescence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailed_address() {
        return detailed_address;
    }

    public void setDetailed_address(String detailed_address) {
        this.detailed_address = detailed_address;
    }

    public String getPenguin() {
        return penguin;
    }

    public void setPenguin(String penguin) {
        this.penguin = penguin;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAcquiescence() {
        return acquiescence;
    }

    public void setAcquiescence(Boolean acquiescence) {
        this.acquiescence = acquiescence;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
