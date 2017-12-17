package com.mierro.authority.entity;

import com.mierro.authority.common.AythorityType;
import com.mierro.authority.common.BaseEntity;
import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by lhb on 2016/11/24.
 * 角色实体类
 */
@Entity
@Table(name = "t_role")
public class Role extends BaseEntity<Long> implements IValueObject {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 角色标识（英文）
     */
    @NotNull(message = "必须要有角色标识参数")
    private String identification;
    /**
     * 角色描述
     */
    @NotNull(message = "必须要有角色描述参数")
    private String description;
    /**
     * 禁用标志位
     */
    private Boolean disable;

    /**
     * 角色归属
     */
    @NotNull(message = "必须要有角色归属参数")
    private AythorityType type;

    @Transient
    private List<Authority> authorities;
    /**
     * 无参构造函数
     */
    public Role() {
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AythorityType getType() {
        return type;
    }

    public void setType(AythorityType type) {
        this.type = type;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
}
