package com.mierro.main.service.impl;

import com.mierro.common.common.Tool;
import com.mierro.common.common.VerifyException;
import com.mierro.main.common.QuestionCategory;
import com.mierro.main.dao.CommonQuestionDao;
import com.mierro.main.entity.CommonQuestionBean;
import com.mierro.main.service.CommonQuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */
@Service("CommonQuestionService")
public class CommonQuestionServiceImpl implements CommonQuestionService {

    @Resource
    private CommonQuestionDao commonQuestionDao;

    @Override
    @Transactional(readOnly = true)
    public List<CommonQuestionBean> findAll(QuestionCategory questionCategory) {
        Sort sort = new Sort(Sort.Direction.DESC,"type")
                .and(new Sort(Sort.Direction.DESC,"id"));
        List<CommonQuestionBean> commonQuestionBeans ;

        if (questionCategory != null){
            commonQuestionBeans = commonQuestionDao.findAll(questionCategory,sort);
        }else{
            commonQuestionBeans = commonQuestionDao.findAll(sort);
        }
        return commonQuestionBeans;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommonQuestionBean> findAll(String title, QuestionCategory questionCategory, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        if (title == null){
            title = "%";
        }else {
            title = "%" +title +"%";
        }
        Sort sort = new Sort(Sort.Direction.DESC,"type")
                .and(new Sort(Sort.Direction.DESC,"id"));
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        Page<CommonQuestionBean> commonQuestionBeans ;
        if (questionCategory == null){
            commonQuestionBeans = commonQuestionDao.findAll(title,pageable);
        }else {
            commonQuestionBeans = commonQuestionDao.findAll(title,questionCategory,pageable);
        }
        return commonQuestionBeans;
    }

    @Override
    @Transactional(readOnly = true)
    public CommonQuestionBean findOne(Long id) {
        return commonQuestionDao.findOne(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        commonQuestionDao.delete(id);
    }

    @Override
    @Transactional
    public void update(CommonQuestionBean commonQuestionBean) {
        if (commonQuestionBean.getId() == null){
            throw new VerifyException("必须传入id");
        }
        CommonQuestionBean old = commonQuestionDao.findOne(commonQuestionBean.getId());
        Tool.dataCheckout(commonQuestionBean,old);
        if (commonQuestionBean.getDisable() != null &&!commonQuestionBean.getDisable().equals(old.getDisable())){
            old.setDisable(!old.getDisable());
        }
        commonQuestionDao.save(old);
    }

    @Override
    @Transactional
    public void add(CommonQuestionBean commonQuestionBean) {
        commonQuestionBean.setDisable(false);
        commonQuestionBean.setTime(new Date());
        commonQuestionDao.save(commonQuestionBean);
    }

    @Override
    @Transactional
    public void updateState(Long id) {
        CommonQuestionBean commonQuestionBean = commonQuestionDao.findOne(id);
        commonQuestionBean.setDisable(!commonQuestionBean.getDisable());
        commonQuestionDao.save(commonQuestionBean);
    }

}
