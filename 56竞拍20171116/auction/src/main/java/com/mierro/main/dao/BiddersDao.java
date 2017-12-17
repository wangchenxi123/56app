package com.mierro.main.dao;

import com.mierro.main.entity.BiddersBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */
public interface BiddersDao extends JpaRepository<BiddersBean,Long>{

    @Query("select bidder from BiddersBean bidder where bidder.sealedId = ?1 ")
    Page<BiddersBean> findAllBySealedId(Long sealedId, Pageable pageable);

    @Query("select bidder from BiddersBean bidder where bidder.sealedId = ?1 group by bidder.userId")
    List<BiddersBean> findAllBySealedId(Long sealedId);

    @Query("select count (bidder.id) from BiddersBean bidder where bidder.sealedId = ?1 and bidder.userId = ?2")
    Integer findAllNumberBySealedId(Long sealedId, Long userId);

    @Modifying
    @Query("delete from BiddersBean bidder where bidder.sealedId = ?1")
    void deleteBySealedId(Long sealedId);
}
