package com.mierro.authority.entity;


import com.mierro.authority.common.BaseEntity;
import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by lhb on 2016/11/25.
 * 用户角色实体类
 */
@Entity
@Table(name = "t_user_role",uniqueConstraints = {@UniqueConstraint(columnNames={"userId","roleId"})})
public class UserRoleRelationship extends BaseEntity<Long> implements IValueObject {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 角色Id
     */
    private Long roleId;

    /**
     * 无参构造函数
     */
    public UserRoleRelationship() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
