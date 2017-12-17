package com.mierro.authority.entity;

import com.mierro.authority.common.BaseEntity;
import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by lhb on 2016/11/25.
 * 角色权限实体类
 */
@Entity
@Table(name = "t_role_function",uniqueConstraints = {@UniqueConstraint(columnNames={"roleId","functionId"})})
public class RoleFunctionRelationship extends BaseEntity<Long> implements IValueObject {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 角色Id
     */
    private Long roleId;

    /**
     * 功能Id
     */
    private Long functionId;

    /**
     * 无参构造函数
     */
    public RoleFunctionRelationship() {}

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
