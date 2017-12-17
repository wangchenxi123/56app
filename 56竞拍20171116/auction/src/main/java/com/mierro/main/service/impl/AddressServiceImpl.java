package com.mierro.main.service.impl;

import com.mierro.common.common.ServiceException;
import com.mierro.common.common.Tool;
import com.mierro.common.common.VerifyException;
import com.mierro.main.dao.AddressDao;
import com.mierro.main.entity.AddressBean;
import com.mierro.main.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/18.
 * Remarks：
 */
@Service("AddressService")
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressDao addressDao;

    @Override
    @Transactional
    public void addAddress(AddressBean addressBean) {
        if (addressBean.getUserId() == null ){
            throw new VerifyException("缺少所属用户");
        }
        addressDao.save(addressBean);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressBean> findAll(Long userId) {
        return addressDao.findAll(userId);
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        addressDao.delete(addressId);
    }

    @Override
    @Transactional
    public void updateAddress(AddressBean addressBean) {
        if(addressBean.getId() == null){
            throw new ServiceException("缺少识别id");
        }
        AddressBean address = addressDao.findOne(addressBean.getId());
        Tool.dataCheckout(addressBean,address);
        addressDao.save(address);
    }
}
