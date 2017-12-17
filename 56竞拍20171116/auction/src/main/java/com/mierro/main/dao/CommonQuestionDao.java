package com.mierro.main.dao;

import com.mierro.main.common.QuestionCategory;
import com.mierro.main.entity.CommonQuestionBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */

public interface CommonQuestionDao extends JpaRepository<CommonQuestionBean,Long>{

    @Query("select common from CommonQuestionBean common where common.title like ?1")
    Page<CommonQuestionBean> findAll(String title, Pageable pageable);

    @Query("select common from CommonQuestionBean common where common.title like ?1 and common.type = ?2")
    Page<CommonQuestionBean> findAll(String title, QuestionCategory questionCategory, Pageable pageable);

    @Query("select common from CommonQuestionBean common where common.disable = false ")
    List<CommonQuestionBean> findAll(Sort sort);

    @Query("select common from CommonQuestionBean common where common.disable = false and common.type = ?1")
    List<CommonQuestionBean> findAll(QuestionCategory questionCategory, Sort sort);
}
