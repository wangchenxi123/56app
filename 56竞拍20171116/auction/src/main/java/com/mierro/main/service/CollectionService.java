package com.mierro.main.service;

import com.mierro.main.entity.CollectionBean;
import com.mierro.main.entity.ItemBean;
import org.springframework.data.domain.Page;

/**
 * Created by 黄晓滨 simba on 2017/8/18.
 * Remarks：
 */
public interface CollectionService {

    void addCollection(Long userId, Long itemId);

    void deleteCollection(Long collectionId);

    Page<ItemBean> findAll(Long userId, Integer pageNo, Integer pageSize);

    CollectionBean findOne(Long userId, Long itemId);
}
