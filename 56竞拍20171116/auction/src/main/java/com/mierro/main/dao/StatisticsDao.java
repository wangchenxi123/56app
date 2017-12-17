package com.mierro.main.dao;

import com.mierro.main.entity.ItemBean;
import com.mierro.main.entity.StatisticsBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public interface StatisticsDao extends JpaRepository<StatisticsBean,Long>{

    @Query(value = "select new ItemBean (item.id,item.name,item.item_type,item.time,COUNT(sealed.itemId)) FROM SealedBean sealed " +
            "INNER JOIN ItemBean item on item.id=sealed.itemId GROUP BY sealed.itemId")
    Page<ItemBean> findItemHeat(Pageable pageable);

    @Query("select new ItemBean (item.id,item.name,item.item_type,item.time,COUNT(sealed.itemId)) FROM SealedBean sealed " +
            "INNER JOIN ItemBean item on item.id=sealed.itemId GROUP BY sealed.itemId ORDER BY COUNT(sealed.itemId) desc")
    Page<ItemBean> findItemHeatCountDesc(Pageable pageable);
}
