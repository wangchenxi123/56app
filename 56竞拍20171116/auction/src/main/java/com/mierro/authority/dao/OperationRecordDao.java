package com.mierro.authority.dao;


import com.mierro.authority.entity.OperationRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/2/22.
 * Remarks：
 */
public interface OperationRecordDao extends JpaRepository<OperationRecord,Long> {

}
