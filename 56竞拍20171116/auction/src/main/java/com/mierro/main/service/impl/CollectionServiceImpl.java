package com.mierro.main.service.impl;

import com.mierro.common.common.VerifyException;
import com.mierro.main.dao.CollectionDao;
import com.mierro.main.dao.ItemDao;
import com.mierro.main.entity.CollectionBean;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.global.ItemCache;
import com.mierro.main.service.CollectionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/18.
 * Remarks：
 */
@Service("CollectionService")
public class CollectionServiceImpl implements CollectionService {

    @Resource
    private CollectionDao collectionDao;
    @Resource
    private ItemDao itemDao;

    @Override
    @Transactional
    public void addCollection(Long userId ,Long itemId) {
        if (collectionDao.findOne(userId,itemId) == null){
            CollectionBean collectionBean = new CollectionBean();
            collectionBean.setItemId(itemId);
            collectionBean.setTime(new Date());
            collectionBean.setUserId(userId);
            collectionDao.save(collectionBean);
        }
    }

    @Override
    @Transactional
    public void deleteCollection(Long collectionId) {
        collectionDao.delete(collectionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemBean> findAll(Long userId, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        Page<ItemBean> item = itemDao.findAllByUserId(userId,pageable);
        return item.map(itemBean -> {
            Integer periods = ItemCache.getItemMap(itemBean.getId()).getNumber_periods();
            itemBean.setPeriods(periods);
            return itemBean;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionBean findOne(Long userId, Long itemId) {
        return collectionDao.findOne(userId,itemId);
    }
}
