package com.mierro.authority.service.impl;


import com.mierro.authority.common.AythorityType;
import com.mierro.authority.common.LoginType;
import com.mierro.authority.common.PasswordUtils;
import com.mierro.authority.dao.*;
import com.mierro.authority.entity.*;
import com.mierro.authority.service.UserService;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ServiceException;
import com.mierro.common.common.StringBuilderUtils;
import com.mierro.common.common.VerifyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by 黄晓滨 simba on 2017/7/20.
 * Remarks：
 */
@Service("UserService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private AuthenticationInfoDao authenticationInfoDao;

    @Resource
    private UserMessageDao userMessageDao;

    @Resource
    private UserRoleRelationshipDao userRoleRelationshipDao;

    @Resource
    private RoleDao roleDao;

    @Override
    @Transactional(readOnly = true)
    public UserMessage selectAdmin(Long userId) {
        UserMessage userMessage;
        if (userId.equals(1L)){
            userMessage = new UserMessage();
            userMessage.setId(1L);
            userMessage.setUsername("超级管理员");
        }else{
            userMessage = userMessageDao.findOne(userId);
        }
        List<Role> list  = roleDao.findRolesByUserId(userId);
        userMessage.setRoles(list);
        return userMessage;
    }

    @Transactional
    public void awardRolesByUserId(Long createById, Long userId, List<Long> rolesId) {
        List<UserRoleRelationship> list = new ArrayList<>();
        if (!userDao.exists(userId)){
            throw new VerifyException(ResponseCode.BAD_REQUEST,"用户不存在");
        }
        for (Long aRolesId : rolesId) {
            //检测是否存在该记录
            if (userRoleRelationshipDao.findByUserIdAndRoleId(userId, aRolesId) != null) {
                continue;
            }
            UserRoleRelationship u = new UserRoleRelationship();
            u.setLastModifyBy(createById);
            u.setCreateTime(new Date());
            u.setCreateBy(createById);
            if (roleDao.exists(aRolesId)) {
                u.setRoleId(aRolesId);
            } else {
                continue;
            }
            u.setUserId(userId);
            u.setLastModifyTime(new Date());
            list.add(u);
        }
        userRoleRelationshipDao.save(list);
    }

    @Override
    @Transactional
    public void updateToken(Long userId,String token, Date time) {
        User user = userDao.findOne(userId);
        user.setRefreshToken(token);
        user.setExpirationTime(time);
        userDao.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByToken(String token) {
        return userDao.findByRAndRefreshToken(token);
    }
}
