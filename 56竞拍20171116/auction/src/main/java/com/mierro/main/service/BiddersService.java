package com.mierro.main.service;

import com.mierro.main.entity.BiddersBean;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/19.
 * Remarks：
 */
public interface BiddersService {

    void save(List<BiddersBean> biddersBeans);

    Integer findBySealedNumber(Long sealed, Long userId);

    Page<BiddersBean> findBySealed(Long sealed, Integer pageNo, Integer pageSize);
}
