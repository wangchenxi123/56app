package com.mierro.main.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mierro.common.common.ServiceException;
import com.mierro.common.common.Tool;
import com.mierro.common.common.VerifyException;
import com.mierro.main.dao.ItemDao;
import com.mierro.main.dao.SortDao;
import com.mierro.main.entity.SortBean;
import com.mierro.main.global.ItemCache;
import com.mierro.main.service.SortService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/6/1.
 * Remarks：
 */
@Service("SortService")
public class SortServiceImpl implements SortService {

    @Resource
    private SortDao sortDao;
    @Resource
    private ItemDao itemDao;

    @Override
    @Transactional(readOnly = true)
    public List<SortBean> findAll() {
        List<SortBean> labelList =  sortDao.findAll();
        for (SortBean labelBean : labelList){
            labelBean.setCommodities(null);
        }
        return labelList;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SortBean> findAll(Integer pageNo, Integer pageSize) {
        if (pageNo <1){
            throw new VerifyException("pageNo参数必须大于0");
        }
        Pageable pageable = new PageRequest(--pageNo,pageSize);
        return sortDao.findAll(pageable);
    }

    @Override
    public Page<SortBean> clientFindAll(Integer pageNo, Integer pageSize) {
        if (pageNo <1){
            throw new VerifyException("pageNo参数必须大于0");
        }
        Pageable pageable = new PageRequest(--pageNo,pageSize);
        return sortDao.clientFindAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public SortBean findOne(Long id) throws IOException {
        SortBean labelBean =  sortDao.findOne(id);
        // TODO: 2017/7/25 进行商品查找
        if (labelBean.getCommodities() != null){
            List<Long> itemIds = JSON.parseArray(labelBean.getCommodities(),Long.class);
            labelBean.setItems(itemDao.findAll(itemIds));
        }
        //清除某一列
        labelBean.setCommodities(null);
        return labelBean;
    }

    @Override
    @Transactional(readOnly = true)
    public SortBean ClientFindOne(Long id) throws IOException {
        SortBean labelBean =  sortDao.findOne(id);
        if (labelBean == null){
            throw new ServiceException("没有找到该分类");
        }
        // TODO: 2017/7/25 进行商品查找
        if (labelBean.getCommodities() != null){
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType = Tool.getCollectionType(List.class, Long.class);
            List<Long> itemIds = objectMapper.readValue(labelBean.getCommodities(),javaType);
            List<ItemCache.Item> items = ItemCache.getItemMap(itemIds);
            for (ItemCache.Item item :items){
                item.setPlus_code(null);
                item.setClick_number(null);
                item.setNumber_periods(null);
                item.setDelete(null);
                item.getItemBean().setPlus_code(null);
                item.getItemBean().setSort(null);
                item.getItemBean().setDisable(null);
                item.getItemBean().setFront_show(null);
                item.getItemBean().setIn_kind(null);
                item.getItemBean().setIncrease_the_price(null);
                item.getItemBean().setItem_type(null);
                item.getItemBean().setNovice(null);
            }
            labelBean.setItems(items);
        }
        //清除某一列
        labelBean.setCommodities(null);
        return labelBean;
    }

    @Override
    @Transactional(readOnly = true)
    public SortBean clientFindOne(Long id) throws IOException {
        SortBean labelBean =  sortDao.findOne(id);
        // TODO: 2017/7/25 进行商品查找
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = Tool.getCollectionType(List.class, Long.class);
        List<Long> itemIds = objectMapper.readValue(labelBean.getCommodities(),javaType);
        List<ItemCache.Item> items = ItemCache.getItemMap(itemIds);
        labelBean.setItems(items);
        return labelBean;
    }

    @Override
    @Transactional
    public void addSort(SortBean label) throws JsonProcessingException {
        if (label.getSort() == null){
            label.setSort(1);
        }
        if (label.getItems().isEmpty()){
            throw new VerifyException("分类至少要有一个商品");
        }
        label.setDisable(true);

        if (label.getItems() != null){
            if (!label.getItems().get(0).getClass().getName().equals(Long.class.getName())){
                try {
                    label.setItems((ArrayList<Long>) label.getItems());
                }catch (Exception e){
                    throw new VerifyException("传入的商品类型错误，传入商品id即可");
                }
            }
            label.setCommodities(JSON.toJSONString(label.getItems()));
        }
        sortDao.save(label);
    }

    @Override
    @Transactional
    public void deleteSort(Long id) {
        sortDao.delete(id);
    }

    @Override
    @Transactional
    public void updateSort(SortBean label){
        if (label.getId() == null){
            throw new VerifyException("请传入标签id");
        }
        SortBean old = sortDao.findOne(label.getId());

        if (label.getItems() != null){
            if (!label.getItems().get(0).getClass().getName().equals(Long.class.getName())){
                try {
                    label.setItems((ArrayList<Long>) label.getItems());
                }catch (Exception e){
                    throw new VerifyException("传入的商品类型错误，传入商品id即可");
                }
            }
            label.setCommodities(JSON.toJSONString(label.getItems()));
        }
        if (label.getDisable()!= null && !label.getDisable().equals(old.getDisable())){
            old.setDisable(!old.getDisable());
        }
        Tool.dataCheckout(label,old);

        if (old.getItems().isEmpty() && old.getCommodities().length()<5
                && label.getItems().get(0).getClass().getName().equals(Long.class.getName())){
            throw new ServiceException("至少要有一件商品");
        }
        sortDao.save(old);
    }

    @Override
    @Transactional
    public void updateState(Long id) {
        SortBean sortBean = sortDao.findOne(id);
        sortBean.setDisable(!sortBean.getDisable());
        sortDao.save(sortBean);
    }
}
