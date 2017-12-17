package com.mierro.main.service;

import com.mierro.main.entity.AddressBean;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/18.
 * Remarks：
 */
public interface AddressService {

    /**
     * 添加一个收获地址
     * @param addressBean 收获地址id
     */
    void addAddress(AddressBean addressBean);

    /**
     * 查询某一个用户所有收获地址
     * @param userId 用户id
     * @return 收获地址集合
     */
    List<AddressBean> findAll(Long userId);

    /**
     * 删除某一个收获地址
     * @param addressId 收获地址id
     */
    void deleteAddress(Long addressId);

    /**
     * 更新收获地址信息
     * @param addressBean 收获地址id
     */
    void updateAddress(AddressBean addressBean);


}
