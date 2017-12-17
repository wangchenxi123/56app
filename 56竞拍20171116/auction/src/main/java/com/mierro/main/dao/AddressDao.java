package com.mierro.main.dao;

import com.mierro.main.entity.AddressBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
public interface AddressDao extends JpaRepository<AddressBean,Long> {

    @Query("select address from AddressBean address where address.userId = ?1 ")
    List<AddressBean> findAll(Long userId);
}
