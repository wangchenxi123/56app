package com.mierro.main.dao;

import com.mierro.main.entity.CollectionBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 黄晓滨 simba on 2017/8/18.
 * Remarks：
 */
public interface CollectionDao extends JpaRepository<CollectionBean,Long> {

    @Query("select collection from CollectionBean collection where collection.userId = ?1 and collection.itemId = ?2")
    CollectionBean findOne(Long userId, Long itemId);
}
