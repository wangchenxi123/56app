package com.mierro.main.dao;

import com.mierro.main.common.NoticeState;
import com.mierro.main.entity.SystemNoticeBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by tlseek on 2017/8/25.
 */
public interface SystemNoticeDao extends JpaRepository<SystemNoticeBean,Long> {


    @Query("SELECT  sn FROM SystemNoticeBean sn WHERE sn.state=?1")
    Page<SystemNoticeBean> findAll(NoticeState state, Pageable pageable);
}
