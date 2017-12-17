package com.mierro.main.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/5/31.
 * Remarks：标签实体类
 */
@Entity
@Table(name = "t_sort")
public class SortBean implements IValueObject {
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 标签名称
     */
    @NotNull(message = "标签名称不能为空")
    @Size(max = 12,min = 2,message = "标签名称在2个字符到12个字符之间")
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否显示
     */
    private Boolean disable;

    /**
     * 商品id集合json
     */
    @JSONField(serialize = false)
    @Column(columnDefinition="TEXT")
    private String commodities;

    /**
     * 商品集合
     */
    @Transient
    private List items;

    @Transient
    private Integer plus_code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public String getCommodities() {
        return commodities;
    }

    public void setCommodities(String commodities) {
        this.commodities = commodities;
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public Integer getPlus_code() {
        return plus_code;
    }

    public void setPlus_code(Integer plus_code) {
        this.plus_code = plus_code;
    }
}
