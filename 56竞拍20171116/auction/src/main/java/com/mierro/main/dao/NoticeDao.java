package com.mierro.main.dao;

import com.mierro.main.common.NoticeType;
import com.mierro.main.entity.NoticeBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
public interface NoticeDao extends JpaRepository<NoticeBean,Long> {

    @Query("SELECT new NoticeBean(n, nu.state, nu.userId) FROM NoticeBean n " +
            " LEFT JOIN NoticeUserBean nu ON n.id=nu.noticeId " +
            " WHERE n.id=?1 AND (nu.userId=?2 OR nu.userId IS NULL ) ")
    NoticeBean findWithUserState(Long noticeId, Long userId);

    @Query("SELECT new NoticeBean(n, nu.state, nu.userId) FROM NoticeBean n " +
            " LEFT JOIN NoticeUserBean nu ON n.id=nu.noticeId " +
            " WHERE n.type=?1 AND n.isSend=?2 AND (nu.userId=?3 OR nu.userId IS NULL )")
    Page<NoticeBean> findWithUserState(NoticeType type, Boolean isSend, Long userId, Pageable pageable);
    @Query("SELECT new NoticeBean(n, nu.state, nu.userId) FROM NoticeBean n " +
            " LEFT JOIN NoticeUserBean nu ON n.id=nu.noticeId " +
            " WHERE n.type=?1 AND n.isSend=?2 ")
    Page<NoticeBean> findWithUserState(NoticeType type, Boolean isSend, Pageable pageable);
    @Query("SELECT new NoticeBean(n, nu.state, nu.userId) FROM NoticeBean n " +
            " LEFT JOIN NoticeUserBean nu ON n.id=nu.noticeId " +
            " WHERE n.type=?1 AND (nu.userId=?2 OR nu.userId IS NULL ) ")
    Page<NoticeBean> findWithUserState(NoticeType type, Long userId, Pageable pageable);
    @Query("SELECT new NoticeBean(n, nu.state, nu.userId) FROM NoticeBean n " +
            " LEFT JOIN NoticeUserBean nu ON n.id=nu.noticeId " +
            " WHERE n.type=?1 ")
    Page<NoticeBean> findWithUserState(NoticeType type, Pageable pageable);

    @Query("SELECT n FROM NoticeBean n  WHERE n.type=?1 ")
    Page<NoticeBean> find(NoticeType type, Pageable pageable);

    @Query("SELECT COUNT(n.id) FROM NoticeBean n " +
            " LEFT JOIN NoticeUserBean nu ON n.id=nu.noticeId " +
            " WHERE n.isSend=true AND ( nu.id IS NULL  OR nu.userId=?1 AND nu.state=0)")
    Long countUnreadNotice(Long userId);

    @Query("SELECT COUNT(n.id) FROM NoticeBean n " +
            " LEFT JOIN NoticeUserBean nu ON n.id=nu.noticeId " +
            " WHERE n.isSend=true AND ( nu.id IS NULL  OR nu.userId=?1 AND nu.state=0) AND n.type=?2")
    Long countUnreadNotice(Long userId , NoticeType type);

}
