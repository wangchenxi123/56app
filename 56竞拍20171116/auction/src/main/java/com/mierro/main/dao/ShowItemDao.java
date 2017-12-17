package com.mierro.main.dao;

import com.mierro.main.entity.ShowItemBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 黄晓滨 simba on 2017/7/31.
 * Remarks：用户晒单
 */
public interface ShowItemDao extends JpaRepository<ShowItemBean,Long>{

    Page<ShowItemBean> findAll(Pageable pageable);

    @Query("select sa from ShowItemBean sa where sa.userId = ?1")
    Page<ShowItemBean> findAll(Long userId, Pageable pageable);

    @Query("select sa from ShowItemBean sa where sa.userId = ?1 and sa.itemId = ?2")
    Page<ShowItemBean> findAll(Long userId, Long itemId, Pageable pageable);

    @Query("select sa from ShowItemBean sa where sa.itemId = ?1")
    Page<ShowItemBean> findAllByItemId(Long itemId, Pageable pageable);

    @Query("select sa from ShowItemBean sa where sa.orderId = ?1")
    Page<ShowItemBean> findAllByOrderId(Long orderId, Pageable pageable);

    @Query("select sa from ShowItemBean sa where sa.orderId = ?1 and sa.itemId = ?2 and sa.userId = ?3")
    ShowItemBean findAllByOrderId(Long orderId, Long itemId, Long userId);
}
