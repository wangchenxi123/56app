package com.mierro.main.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.mierro.common.common.IValueObject;
import com.mierro.main.common.RunningProgram;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/8.
 * Remarks：商品
 */
@Entity
@Table(name = "t_item")
public class ItemBean implements IValueObject {

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 商品名称
     */
    @Column(length = 190)
    @Length(max = 189,message = "商品名称不宜过长")
    private String name;

    /**
     * 商品类型
     */
    private String item_type;

    /**
     * 成本
     */
    @NotNull(message = "请传入商品成本")
    @Min(0)
    private BigDecimal cost;

    /**
     * 市场价
     */
    @NotNull(message = "请传入商品市场价")
    @Min(0)
    private BigDecimal price;

    /**
     * 票数控线
     */
    private Integer control_line;

    /**
     * 商品标签
     */
    @JSONField(serialize = false)
    private String label_str;

    /**
     * 商品小图
     */
    @NotNull(message = "商品小图不能为空")
    private Long small_picture;

    /**
     * 商品大图
     */
    @JSONField(serialize = false)
    private String big_picture;

    /**
     * 是否禁用
     */
    private Boolean disable;

    /**
     * 运行方案
     */
    private RunningProgram running_program;

    /**
     * 是否实物
     */
    private Boolean in_kind;

    /**
     * 排序
     */
    @Min(value = 0,message = "最小值为0")
    @Max(value = 999,message = "最大值为999")
    private Integer sort;

    @Transient
    private List<String> labels;

    @Transient
    private List<String> bigPictures;

    @Transient
    private Integer periods;

    @Transient
    private Long collectionId;

    @Transient
    private Long countNumber;

    /**
     * 每次加码数目
     */
    @NotNull(message = "每次加码数目不能为空")
    private Integer plus_code;

    /**
     * 每轮加价
     */
    private String increase_the_price;

    /**
     * 商品复制主id
     */
    @JSONField(serialize = false)
    private Long fatherItemId;

    /**
     * 是否前端展示
     */
//    @JSONField(serialize = false)
    private Boolean front_show;

    /**
     * 是否是新人商品
     */
//    @JSONField(serialize = false)
    private Boolean novice;

    /**
     * 上架时间
     */
    private Date time;

    /**
     * 商品购买地址
     */
    private String purchaseAddress;

    public ItemBean(){}

    public ItemBean(ItemBean itemBean, Long collectionId){
        this.id = itemBean.getId();
        this.collectionId = collectionId;
        this.small_picture = itemBean.getSmall_picture();
        this.name = itemBean.getName();
        this.price = itemBean.getPrice();
    }

    public ItemBean(Long itemId,String name,String type , Date time,Long count){
        this.id = itemId;
        this.name = name;
        this.item_type = type;
        this.time = time;
        this.countNumber = count;
    }

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

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getControl_line() {
        return control_line;
    }

    public void setControl_line(Integer control_line) {
        this.control_line = control_line;
    }

    public String getLabel_str() {
        return label_str;
    }

    public void setLabel_str(String label_str) {
        this.label_str = label_str;
    }

    public Long getSmall_picture() {
        return small_picture;
    }

    public void setSmall_picture(Long small_picture) {
        this.small_picture = small_picture;
    }

    public String getBig_picture() {
        return big_picture;
    }

    public void setBig_picture(String big_picture) {
        this.big_picture = big_picture;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public RunningProgram getRunning_program() {
        return running_program;
    }

    public void setRunning_program(RunningProgram running_program) {
        this.running_program = running_program;
    }

    public Boolean getIn_kind() {
        return in_kind;
    }

    public void setIn_kind(Boolean in_kind) {
        this.in_kind = in_kind;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getBigPictures() {
        return bigPictures;
    }

    public void setBigPictures(List<String> bigPictures) {
        this.bigPictures = bigPictures;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getPlus_code() {
        return plus_code;
    }

    public void setPlus_code(Integer plus_code) {
        this.plus_code = plus_code;
    }

    public String getIncrease_the_price() {
        return increase_the_price;
    }

    public void setIncrease_the_price(String increase_the_price) {
        this.increase_the_price = increase_the_price;
    }

    public Long getFatherItemId() {
        return fatherItemId;
    }

    public void setFatherItemId(Long fatherItemId) {
        this.fatherItemId = fatherItemId;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public Boolean getFront_show() {
        return front_show;
    }

    public void setFront_show(Boolean front_show) {
        this.front_show = front_show;
    }

    public Boolean getNovice() {
        return novice;
    }

    public void setNovice(Boolean novice) {
        this.novice = novice;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(Long countNumber) {
        this.countNumber = countNumber;
    }

    public String getPurchaseAddress() {
        return purchaseAddress;
    }

    public void setPurchaseAddress(String purchaseAddress) {
        this.purchaseAddress = purchaseAddress;
    }
}
