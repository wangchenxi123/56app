package com.mierro.main.dao;

import com.mierro.main.common.RunningProgram;
import com.mierro.main.entity.ItemBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/8.
 * Remarks：商品
 */
public interface ItemDao extends JpaRepository<ItemBean,Long> {

    @Query("select item from ItemBean item where item.name like ?1 and item.item_type like ?2")
    Page<ItemBean> findAll(String name, String type, Pageable pageable);

    @Query("select item from ItemBean item where item.name like ?1 and item.item_type like ?2 and item.plus_code = ?3")
    Page<ItemBean> findAll(String name, String type, Integer plus_code, Pageable pageable);

    @Query("select item from ItemBean item where item.name like ?1 and item.item_type like ?2 " +
            "and item.running_program = ?3")
    Page<ItemBean> findAll(String name, String type, RunningProgram runningProgram, Pageable pageable);

    @Query("select item from ItemBean item where item.name like ?1 and item.item_type like ?2 " +
            "and item.running_program = ?3 and item.plus_code = ?4")
    Page<ItemBean> findAll(String name, String type, RunningProgram runningProgram, Integer plus_code, Pageable pageable);

    @Query("select item from ItemBean item where item.name like ?1 and item.item_type like ?2 " +
            "and item.disable = ?3")
    Page<ItemBean> findAll(String name, String type, Boolean disable, Pageable pageable);

    @Query("select item from ItemBean item where item.name like ?1 and item.item_type like ?2 " +
            "and item.disable = ?3 and item.plus_code = ?4")
    Page<ItemBean> findAll(String name, String type, Boolean disable, Integer plus_code, Pageable pageable);

    @Query("select item from ItemBean item where item.name like ?1 and item.item_type like ?2 " +
            "and item.disable = ?3 and item.running_program = ?4")
    Page<ItemBean> findAll(String name, String type, Boolean disable, RunningProgram runningProgram,
                           Pageable pageable);

    @Query("select item from ItemBean item where item.name like ?1 and item.item_type like ?2 " +
            "and item.disable = ?3 and item.running_program = ?4 and item.plus_code = ?5")
    Page<ItemBean> findAll(String name, String type, Boolean disable, RunningProgram runningProgram,
                           Integer plus_code, Pageable pageable);

    @Query("select item from ItemBean item where item.disable = ?1")
    List<ItemBean> findAll(Boolean disable);

    @Query("select item.id from ItemBean item where item.disable = false and item.front_show = true ")
    Page<Long> findAllId(Pageable pageable);

    @Query("select item.id from ItemBean item where item.disable = false and item.front_show = true and item.novice = true ")
    Page<Long> findAllIdInNovice(Pageable pageable);

    @Query("select item.id from ItemBean item where item.fatherItemId = ?1 and item.plus_code = ?2 ")
    ItemBean findOneByFatherItemIdAndPlus_code(Long fatherItemId, Integer plus_code);

    @Query("select new ItemBean (item,collection.id) from CollectionBean collection inner join ItemBean item on collection.itemId = item.id " +
            "where collection.userId = ?1")
    Page<ItemBean> findAllByUserId(Long userId, Pageable pageable);
}
