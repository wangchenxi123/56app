package com.mierro.authority.service;

import com.mierro.authority.entity.OperationRecord;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/2/21.
 * Remarks：
 */

public interface OperationRecordService {

    /**
     * 添加一条操作记录
     * @param userId 用户id
     * @param request http请求的request
     * @param response http请求的response
     */
    void addOperationRecord(Long userId, HttpServletRequest request, HttpServletResponse response);

    /**
     * 删除操作记录
     * @param list 需要删除的操作记录集合
     */
    void delete(List<OperationRecord> list);

}
