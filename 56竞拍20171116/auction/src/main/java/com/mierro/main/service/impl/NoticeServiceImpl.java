package com.mierro.main.service.impl;

import com.mierro.authority.dao.UserDao;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.Tool;
import com.mierro.common.common.VerifyException;
import com.mierro.main.common.NoticeState;
import com.mierro.main.common.NoticeType;
import com.mierro.main.dao.NoticeDao;
import com.mierro.main.dao.NoticeUserDao;
import com.mierro.main.entity.NoticeBean;
import com.mierro.main.entity.NoticeUserBean;
import com.mierro.main.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 通知模块业务实现
 * Created by tlseek on 2017/8/26.
 */
@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {

    @Resource
    UserDao userDao;
    @Resource
    NoticeDao noticeDao;
    @Resource
    NoticeUserDao noticeUserDao;

    @Override
    public Page<NoticeBean> findAnnouncementNotice(Integer pageNo, Integer pageSize) {
        if (pageNo<1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = new PageRequest(pageNo-1, pageSize, sort);
        return noticeDao.find(NoticeType.ANNOUNCEMENT, pageRequest);
    }

    @Override
    public Page<NoticeBean> findAnnouncementNotice(Long userId, Boolean isSend, Integer pageNo, Integer pageSize) {
        NoticeType type = NoticeType.ANNOUNCEMENT;
        return rawFindWithUserState(userId, isSend, pageNo, pageSize, type);
    }

    @Override
    public Page<NoticeBean> findPersonalNotice(Long userId, Boolean isSend, Integer pageNo, Integer pageSize) {
        NoticeType type = NoticeType.PERSONAL;
        return rawFindWithUserState(userId, isSend, pageNo, pageSize, type);
    }

    private Page<NoticeBean> rawFindWithUserState(Long userId, Boolean isSend, Integer pageNo, Integer pageSize, NoticeType type) {
        if (pageNo<1){
            throw new VerifyException("pageNo参数错误");
        }
        Page<NoticeBean> page ;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = new PageRequest(pageNo-1, pageSize, sort);
        if (userId == null) {
            if (isSend == null) {
                page = noticeDao.findWithUserState(type, pageRequest);
            } else {
                page = noticeDao.findWithUserState(type, isSend, pageRequest);
            }
        } else {
            if (isSend == null) {
                page = noticeDao.findWithUserState(type, userId, pageRequest);
            } else {
                page = noticeDao.findWithUserState(type, isSend, userId, pageRequest);
            }
        }
        return page;
    }

    @Override
    public NoticeBean find(Long noticeId) {
        return ResponseCode.business.notNull(noticeDao.findOne(noticeId), "找不到此通知");
    }

    @Override
    public NoticeBean find(Long noticeId, Long userId) {
        return ResponseCode.business.notNull(noticeDao.findWithUserState(noticeId, userId), "找不到此通知");
    }

    @Override
    public NoticeBean addAnnouncement(String title, String context) {
        NoticeBean noticeBean = new NoticeBean();
        noticeBean.setTime(new Date());
        noticeBean.setTitle(title);
        noticeBean.setContext(context);
        noticeBean.setIsSend(false);
        noticeBean.setType(NoticeType.ANNOUNCEMENT);
        return noticeDao.save(noticeBean);
    }

    @Transactional
    @Override
    public NoticeBean addPersonal(String title, String context, Long userId) {
        ResponseCode.business.notNull(userDao.findOne(userId), "找不到此用户");
        NoticeBean noticeBean = new NoticeBean();
        noticeBean.setTime(new Date());
        noticeBean.setTitle(title);
        noticeBean.setContext(context);
        noticeBean.setIsSend(true);
        noticeBean.setType(NoticeType.PERSONAL);
        noticeDao.save(noticeBean);
        NoticeUserBean noticeUserBean = new NoticeUserBean();
        noticeUserBean.setState(NoticeState.UNREAD);
        noticeUserBean.setUserId(userId);
        noticeUserBean.setNoticeId(noticeBean.getId());
        noticeUserDao.save(noticeUserBean);
        return noticeBean;
    }

    @Override
    public NoticeBean edit(NoticeBean notice) {
        ResponseCode.bad_request.notNull(notice.getId(), "id 为 null");
        NoticeBean _noticeBean = ResponseCode.business.notNull(noticeDao.findOne(notice.getId()), "找不到通知");
        if(notice.getTitle() != null) _noticeBean.setTitle(notice.getTitle());
        if(notice.getContext() != null) _noticeBean.setContext(notice.getContext());
        if(notice.getIsSend() != null) _noticeBean.setIsSend(notice.getIsSend());
        noticeDao.save(_noticeBean);
        return _noticeBean;
    }

    @Transactional
    @Override
    public NoticeBean changeState(Long noticeId, Long userId, NoticeState state) {
        NoticeBean _noticeBean = ResponseCode.business.notNull(noticeDao.findOne(noticeId), "找不到通知");
        ResponseCode.business.notNull(userDao.findOne(userId), "找不到此用户");

        NoticeUserBean noticeUserBean = Tool.getOrDefault(
                noticeUserDao.findByNoticeIdAndUserId(noticeId, userId),
                () -> new NoticeUserBean(state, userId, noticeId));
        noticeUserBean.setState(state);
        noticeUserDao.save(noticeUserBean);

        _noticeBean.setUserId(userId);
        _noticeBean.setState(state);
        return _noticeBean;
    }

    @Override
    public NoticeBean sendNotice(Long noticeId) {
        NoticeBean _noticeBean = ResponseCode.business.notNull(noticeDao.findOne(noticeId), "找不到通知");
        _noticeBean.setIsSend(true);
        return noticeDao.save(_noticeBean);
    }

    @Override
    public Long hasUnreadNotice(Long userId) {
        return Tool.getOrDefault(noticeDao.countUnreadNotice(userId), 0L);
    }

    @Transactional
    @Override
    public void delete(Long noticeId) {
        noticeDao.delete(noticeId);
        noticeUserDao.deleteByNoticeId(noticeId);
    }

    @Override
    public Long hasUnreadAnnouncementNotice(Long userId) {
        return Tool.getOrDefault(noticeDao.countUnreadNotice(userId, NoticeType.ANNOUNCEMENT), 0L);
    }

    @Override
    public Long hasUnreadPersonalNotice(Long userId) {
        return Tool.getOrDefault(noticeDao.countUnreadNotice(userId, NoticeType.PERSONAL), 0L);
    }
}
