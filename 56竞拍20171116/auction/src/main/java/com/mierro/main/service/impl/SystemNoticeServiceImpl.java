package com.mierro.main.service.impl;

import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.VerifyException;
import com.mierro.main.common.NoticeState;
import com.mierro.main.dao.SystemNoticeDao;
import com.mierro.main.entity.SystemNoticeBean;
import com.mierro.main.service.SystemNoticeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 系统通知实现
 * Created by tlseek on 2017/8/26.
 */
@Service("systemNoticeService")
public class SystemNoticeServiceImpl implements SystemNoticeService{
    @Resource
    SystemNoticeDao systemNoticeDao;

    @Override
    public Page<SystemNoticeBean> find(NoticeState state, Integer pageNo, Integer pageSize) {
        if (pageNo<1){
            throw new VerifyException("pageNo参数错误");
        }
        Page<SystemNoticeBean> page ;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = new PageRequest(pageNo-1, pageSize, sort);

        if (state == null) {
            page = systemNoticeDao.findAll(pageRequest);
        } else {
            page = systemNoticeDao.findAll(state, pageRequest);
        }

        return page;
    }

    @Override
    public SystemNoticeBean add(String title, String context) {
        SystemNoticeBean noticeBean = new SystemNoticeBean();
        noticeBean.setState(NoticeState.UNREAD);
        noticeBean.setTitle(title);
        noticeBean.setContext(context);
        noticeBean.setTime(new Date());
        return systemNoticeDao.save(noticeBean);
    }

    @Override
    public SystemNoticeBean changeState(Long noticeId, Long userId, NoticeState state) {
        SystemNoticeBean _noticeBean = ResponseCode.business.notNull(systemNoticeDao.findOne(noticeId), "找不到此通知");
        _noticeBean.setState(state);
        _noticeBean.setUserId(userId);
        return systemNoticeDao.save(_noticeBean);
    }
}
