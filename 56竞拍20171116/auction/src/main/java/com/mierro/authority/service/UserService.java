package com.mierro.authority.service;

import com.mierro.authority.entity.User;
import com.mierro.authority.entity.UserMessage;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/7/20.
 * Remarks：
 */
public interface UserService {

    /**
     * 查询管理员
     * @param userId 管理员id
     * @return 分页对象
     */
    UserMessage selectAdmin(Long userId);

    /**
     * 更新用户token
     * @param userId 用户id
     * @param token token
     * @param time 过期时间
     */
    void updateToken(Long userId, String token, Date time);

    /**
     * 根据token查找用户
     * @param token token
     * @return 用户信息
     */
    User findByToken(String token);
}
