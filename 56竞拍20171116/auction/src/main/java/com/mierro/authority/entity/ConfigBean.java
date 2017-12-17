package com.mierro.authority.entity;

import com.mierro.common.common.IValueObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by 黄晓滨 on 2017/3/23
 *
 * @message 备注
 */
@Entity
@Table(name = "t_config")
public class ConfigBean implements IValueObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private Integer keyId;

    @NotNull
    @Column(columnDefinition="TEXT")
    private String value;

    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getKeyId() {
        return keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }
}
