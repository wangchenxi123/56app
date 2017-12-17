package com.mierro.main.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ServiceException;
import com.mierro.common.common.Tool;
import com.mierro.common.common.VerifyException;
import com.mierro.main.Listener.Thread.ItemBidThread;
import com.mierro.main.common.CoinType;
import com.mierro.main.common.RunningProgram;
import com.mierro.main.dao.BiddersDao;
import com.mierro.main.dao.CoinDao;
import com.mierro.main.dao.ItemDao;
import com.mierro.main.dao.SealedDao;
import com.mierro.main.entity.BiddersBean;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.global.ItemCache;
import com.mierro.main.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 黄晓滨 simba on 2017/8/8.
 * Remarks：
 */
@Service("ItemService")
public class ItemServiceImpl implements ItemService {

    private static Logger logger = LogManager.getLogger(ItemServiceImpl.class.getName());

    @Resource
    private ItemDao itemDao;

    @Resource
    private CoinDao coinDao;

    @Resource
    private BiddersDao biddersDao;

    @Resource
    private SealedDao sealedDao;

    @Override
    @Transactional
    public void addItem(ItemBean itemBean) throws JsonProcessingException {
        if (itemBean.getRunning_program() == null){
            throw new ServiceException("请选择商品运行方案");
        }
        if (itemBean.getControl_line() == null){
            itemBean.setControl_line(itemBean.getCost().intValue()+1);
        }
        if (itemBean.getBigPictures().isEmpty()){
            throw new ServiceException("至少需要一个大图");
        }else{
            ObjectMapper objectMapper = new ObjectMapper();
            itemBean.setBig_picture(objectMapper.writeValueAsString(itemBean.getBigPictures()));
            if (itemBean.getLabels() != null && !itemBean.getLabels().isEmpty()){
                itemBean.setLabel_str(objectMapper.writeValueAsString(itemBean.getLabels()));
            }
        }
        if (itemBean.getSort() == null){
            itemBean.setSort(0);
        }
        itemBean.setFront_show(false);
        itemBean.setDisable(true);
        itemBean.setNovice(false);
        itemBean.setTime(new Date());
        itemBean.setFront_show(false);
        itemDao.save(itemBean);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemBean> findAll(String name, String type, Boolean disable, RunningProgram runningProgram,
                                  Integer plusCode,Integer pageNo, Integer pageSize) {
        if (pageNo <1 ){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"sort","id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        if (name == null){
            name = "%";
        }else{
            name = "%"+name+"%";
        }
        if (type == null){
            type = "%";
        }else{
            type = "%"+type+"%";
        }
        Page<ItemBean> page ;
        if (disable == null){
            if (runningProgram == null){
                if (plusCode == null){
                    page = itemDao.findAll(name,type,pageable);
                }else{
                    page = itemDao.findAll(name,type,plusCode,pageable);
                }
            }else{
                if (plusCode == null){
                    page = itemDao.findAll(name,type,runningProgram,pageable);
                }else{
                    page = itemDao.findAll(name,type,runningProgram,plusCode,pageable);
                }
            }
        }else{
            if (runningProgram == null){
                if (plusCode == null){
                    page = itemDao.findAll(name,type,disable,pageable);
                }else{
                    page = itemDao.findAll(name,type,disable,plusCode,pageable);
                }
            }else{
                if (plusCode == null){
                    page = itemDao.findAll(name,type,disable,runningProgram,pageable);
                }else{
                    page = itemDao.findAll(name,type,disable,runningProgram,plusCode,pageable);
                }
            }
        }
        return page.map(itemBean -> {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType orderType = Tool.getCollectionType(List.class,String.class);
            if (itemBean.getLabel_str() != null){
                try {
                    itemBean.setLabels(objectMapper.readValue(itemBean.getLabel_str(),orderType));
                } catch (IOException ignored) {}
            }
            if (itemBean.getBig_picture() != null){
                try {
                    itemBean.setBigPictures(objectMapper.readValue(itemBean.getBig_picture(),orderType));
                } catch (IOException ignored) {}
            }
            return itemBean;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemBean> findAll(Boolean disable) throws IOException {
        List<ItemBean> list = itemDao.findAll(disable);
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType orderType = Tool.getCollectionType(List.class,String.class);
        for (ItemBean itemBean : list){
            if (itemBean.getLabel_str() != null){
                itemBean.setLabels(objectMapper.readValue(itemBean.getLabel_str(),orderType));
            }
            if (itemBean.getBig_picture() != null){
                itemBean.setBigPictures(objectMapper.readValue(itemBean.getBig_picture(),orderType));
            }
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemBean findOne(Long itemId) {
        ItemBean itemBean = itemDao.findOne(itemId);
        if (itemBean.getLabel_str() != null){
            itemBean.setLabels(JSON.parseArray(itemBean.getLabel_str(),String.class));
        }
        if (itemBean.getBig_picture() != null) {
            itemBean.setBigPictures(JSON.parseArray(itemBean.getBig_picture(), String.class));
        }
        return itemBean;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (ItemCache.itemMap.get(id) == null){
            itemDao.delete(id);
        }else{
            ItemCache.delete(id);
            throw new ServiceException("删除失败，检测到该商品正在进行竞拍活动，该期竞拍活动结束将自动删除");
        }
    }

    @Override
    @Transactional
    public void forcedDelete(Long id) {
        if (ItemCache.itemMap.get(id) == null){
            itemDao.delete(id);
        }else{
            ItemCache.delete(id);
            //检测到该商品正在进行竞拍活动,进行强制删除

            ItemCache.Item item = ItemCache.itemMap.get(id);
            //移除缓存

            //删除积分记录
            coinDao.deleteByPlace(item.getId());

            //删除投票信息
            biddersDao.deleteBySealedId(item.getId());

            //删除封存缓存记录
            sealedDao.delete(item.getId());

            //删除商品
            itemDao.delete(id);
        }
    }

    @Override
    @Transactional
    public void updateItem(ItemBean itemBean) throws JsonProcessingException {
        if (itemBean.getId() == null){
            throw new VerifyException("必须传入商品id");
        }
        if (itemBean.getBigPictures() != null){
            itemBean.setBig_picture(JSON.toJSONString(itemBean.getBigPictures()));
        }
        if (itemBean.getLabels() != null){
            itemBean.setLabel_str(JSON.toJSONString(itemBean.getLabels()));
        }
        ItemBean old = itemDao.findOne(itemBean.getId());
        if (itemBean.getDisable() != null && !itemBean.getDisable().equals(old.getDisable())){
            old.setDisable(!old.getDisable());
        }
        Tool.dataCheckout(itemBean,old);

        if (old.getPlus_code().equals(0) || old.getIncrease_the_price().equals("0")
                || old.getControl_line().equals(0) || old.getCost().toString().equals("0")
                || old.getPrice().toString().equals("0")){
            throw new VerifyException("成本价、市场价、票数控线、每轮加价数、每轮加码数 不能设置为0");
        }

        itemDao.save(old);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String,Object> clientFindAll(Integer pageNo, Integer pageSize) {
        if (pageNo<1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"sort","id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        Page<Long> itemIds  = itemDao.findAllId(pageable);
        List<ItemCache.Item> list = ItemCache.getItemMap(itemIds.getContent());
        Map<String,Object> map = new HashMap<>();
        map.put("content",list);
        map.put("first",itemIds.isFirst());
        map.put("last",itemIds.isLast());
        map.put("number",itemIds.getNumber());
        map.put("numberOfElements",itemIds.getNumberOfElements());
        map.put("size",itemIds.getSize());
        map.put("totalElements",itemIds.getTotalElements());
        map.put("totalPages",itemIds.getTotalPages());
        map.put("currentTime",new Date());
        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> clientFind(Integer pageNo, Integer pageSize) {
        if (pageNo<1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"sort","id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        Page<Long> itemIds  = itemDao.findAllIdInNovice(pageable);
        List<ItemCache.Item> list = ItemCache.getItemMap(itemIds.getContent());
        Map<String,Object> map = new HashMap<>();
        map.put("content",list);
        map.put("first",itemIds.isFirst());
        map.put("last",itemIds.isLast());
        map.put("number",itemIds.getNumber());
        map.put("numberOfElements",itemIds.getNumberOfElements());
        map.put("size",itemIds.getSize());
        map.put("totalElements",itemIds.getTotalElements());
        map.put("totalPages",itemIds.getTotalPages());
        map.put("currentTime",new Date());
        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemCache.Item clientFindOne(Long itemId,Integer number) {
        ItemCache.Item item= ItemCache.getItemMap(itemId);
        Sort sort = new Sort(Sort.Direction.DESC,"time");
        Pageable pageable = new PageRequest(0,number,sort);
        Page<BiddersBean> page = biddersDao.findAllBySealedId(item.getId(),pageable);
        item.getBiddersBeans().addAll(page.getContent());
        return item;
    }

    @Override
    @Transactional
    public void updateItemState(Long itemId) {
        ItemBean itemBean = itemDao.findOne(itemId);
        ItemCache.Item item = ItemCache.itemMap.get(itemId);
        if (itemBean.getDisable().equals(false)){
            //当前状态为启用..即将改为禁用
            if (item != null){
                item.setDisable(true);
                throw new ServiceException("禁用失败，检测到该商品正在进行竞拍活动，该期竞拍活动结束将自动禁用");
            }else{
                itemBean.setDisable(!itemBean.getDisable());
            }
        }else{
            //当前状态为禁用..即将改为启用
            if (item == null){
                itemBean.setDisable(!itemBean.getDisable());
                ItemCache.load(itemBean);
            }
        }
    }

    @Override
    @Transactional
    public void updateItemShow(Long itemId) {
        ItemBean itemBean = itemDao.findOne(itemId);
        itemBean.setFront_show(!itemBean.getFront_show());
    }

    @Override
    @Transactional
    public void updateItemNovice(Long itemId) {
        ItemBean itemBean = itemDao.findOne(itemId);
        itemBean.setNovice(!itemBean.getNovice());
    }

    @Override
    @Transactional
    public void auction(Long itemId,Integer periods, Long userId) {
        //需要扣除的钱币数
        ItemCache.Item item = ItemCache.itemMap.get(itemId);
        if (item == null){
            throw new ServiceException("活动失效,请检查后再参与");
        }
        if (!item.getNumber_periods().equals(periods)){
            throw new ServiceException(ResponseCode.ACTIVITY_ENDS,"该期已经结束，请前往下一期");
        }

        Integer plus_code = item.getPlus_code();
        Integer coin;
        //校验用户钱币
        if (item.getItemBean().getIn_kind()){
            //实物
            coin = coinDao.findByUserIdAndCoinType(userId, CoinType.GIFT,CoinType.RECHARGE);
        }else{
            coin = coinDao.findByUserIdAndCoinType(userId, CoinType.RECHARGE);
        }
        if (coin == null || coin < plus_code){
            throw new ServiceException(ResponseCode.INSUFFICIENT_COINS,"拍币不足，请进行充值");
        }

        try {
            ItemCache.auction(userId,itemId,false,false);
        } catch (IOException e) {
            logger.error("投票成功，通知发送失败");
        } catch (ServiceException e){
            throw new ServiceException("投票失败，"+e.getMessage());
        }
    }

    @Override
//    @Transactional
    public void auction(Long itemId, Integer periods, Long userId, Integer number) {

        //需要扣除的钱币数
        ItemCache.Item item = ItemCache.itemMap.get(itemId);
        if (item == null){
            throw new ServiceException("活动失效,请检查后再参与");
        }
        if (!item.getNumber_periods().equals(periods)){
            throw new ServiceException(ResponseCode.ACTIVITY_ENDS,"该期已经结束，请前往下一期");
        }

        Integer coin;
        //校验用户钱币
        if (item.getItemBean().getIn_kind()){
            //实物
            coin = coinDao.findByUserIdAndCoinType(userId, CoinType.GIFT,CoinType.RECHARGE);
        }else{
            coin = coinDao.findByUserIdAndCoinType(userId, CoinType.RECHARGE);
        }
        if (coin == null || coin < (number *item.getPlus_code())){
            throw new ServiceException(ResponseCode.INSUFFICIENT_COINS,"拍币不足，请进行充值");
        }

        if (ItemCache.user_item_thread.get(userId) != null){
            ConcurrentHashMap<Long,Thread> concurrentHashMap = ItemCache.user_item_thread.get(userId);
            for (Map.Entry<Long,Thread> entry : concurrentHashMap.entrySet()){
                if (entry.getKey().equals(itemId)){
                    throw new ServiceException(ResponseCode.BUSINESS,"该商品您已经在排队竞拍了");
                }
            }
        }

        ItemBidThread itemBidThread = new ItemBidThread(userId,itemId,periods,number);

        ConcurrentHashMap<Long,Thread> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put(itemId,itemBidThread);
        ItemCache.user_item_thread.put(userId,concurrentHashMap);
        itemBidThread.start();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BiddersBean> findBidders(Long itemId, Long sealedId, Integer pageNo, Integer pageSize) {
        if(pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable  = new PageRequest(--pageNo,pageSize,sort);
        ItemCache.Item item;
        if (sealedId != null){
            item = new ItemCache.Item();
            item.setId(sealedId);
        }else {
            item = ItemCache.getItemMap(itemId);
        }
        return biddersDao.findAllBySealedId(item.getId(),pageable);
    }
}
