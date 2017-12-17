package com.mierro.authority.common;


import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhb on 2016/11/24.
 * 实体基础抽象类
 */
@MappedSuperclass
public abstract class BaseEntity<ID_TYPE extends Serializable> {
    /**
     * 记录创建时间
     */
    @Column(updatable = false)
    private Date createTime = new Date();
    /**
     * 记录创建者
     */
    private ID_TYPE createBy;
    /**
     * 记录修改时间
     */
    private Date lastModifyTime;
    /**
     * 最后修改者
     */
    private ID_TYPE lastModifyBy;

    /**
     * 无参构造函数
     */
    public BaseEntity() {
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ID_TYPE getCreateBy() {
        return createBy;
    }

    public void setCreateBy(ID_TYPE createBy) {
        this.createBy = createBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public ID_TYPE getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(ID_TYPE lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }
}
