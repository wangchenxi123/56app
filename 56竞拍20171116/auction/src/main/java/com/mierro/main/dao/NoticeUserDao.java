package com.mierro.main.dao;

import com.mierro.main.entity.NoticeUserBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by tlseek on 2017/8/25.
 */
public interface NoticeUserDao extends JpaRepository<NoticeUserBean,Long> {

    @Query("SELECT nu FROM NoticeUserBean nu WHERE nu.noticeId=?1 AND nu.userId=?2")
    NoticeUserBean findByNoticeIdAndUserId(Long noticeId, Long userId);


    @Modifying
    @Transactional
    @Query("DELETE FROM NoticeUserBean WHERE noticeId=?1")
    int deleteByNoticeId(Long noticeId);
}
