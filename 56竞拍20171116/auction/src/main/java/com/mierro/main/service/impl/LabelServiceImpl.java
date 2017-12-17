package com.mierro.main.service.impl;

import com.mierro.common.common.ServiceException;
import com.mierro.common.common.Tool;
import com.mierro.common.common.VerifyException;
import com.mierro.main.dao.LabelDao;
import com.mierro.main.entity.LabelBean;
import com.mierro.main.service.LabelService;
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
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
@Service("LabelService")
public class LabelServiceImpl implements LabelService {

    @Resource
    private LabelDao labelDao;

    @Override
    @Transactional(readOnly = true)
    public Page<LabelBean> findAll(Integer pageNo, Integer pageSize) {
        if (pageNo<1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        return labelDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelBean> findAll() {
        List<LabelBean> labelBeans = labelDao.findAll();
        for(LabelBean labelBean : labelBeans){
            labelBean.setSetting_url(null);
        }
        return labelBeans;
    }

    @Override
    @Transactional(readOnly = true)
    public LabelBean findOne(Long labelId) {
        return labelDao.findOne(labelId);
    }

    @Override
    @Transactional
    public void delete(Long labelId) {
        labelDao.delete(labelId);
    }

    @Override
    @Transactional
    public void addLabel(LabelBean label) {
        label.setDisable(true);
        label.setTime(new Date());
        labelDao.save(label);
    }

    @Override
    @Transactional
    public void updateLabel(LabelBean label) {
        if (label.getId() == null){
            throw new VerifyException("必须传入标签的id");
        }
        LabelBean labelBean = labelDao.findOne(label.getId());

        if (label.getDisable()!= null && !label.getDisable().equals(labelBean.getDisable())){
            labelBean.setDisable(!labelBean.getDisable());
        }

        Tool.dataCheckout(label,labelBean);
        if (labelBean.getDisable().equals(false)){
            //查询是否已经超过4个
            Integer number = labelDao.countNumber();
            if (number == null || number<5){
                labelDao.save(labelBean);
            }else{
                throw new ServiceException("启动数目超过了4个，修改失败");
            }
        }
    }

    @Override
    @Transactional
    public void updateLabelState(Long id) {
        LabelBean labelBean = labelDao.findOne(id);
        labelBean.setDisable(!labelBean.getDisable());
        labelDao.save(labelBean);
    }
}
