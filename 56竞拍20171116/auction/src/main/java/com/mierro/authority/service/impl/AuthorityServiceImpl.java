package com.mierro.authority.service.impl;


import com.mierro.authority.common.AythorityType;
import com.mierro.authority.common.LoginEquipment;
import com.mierro.authority.common.LoginType;
import com.mierro.authority.dao.*;
import com.mierro.authority.entity.*;
import com.mierro.authority.service.AuthorityService;
import com.mierro.authority.shiro.CustomPermissionCheckFilter;
import com.mierro.common.common.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by 黄晓滨 simba on 2017/7/20.
 * Remarks：
 */
@Service("AuthorityService")
public class AuthorityServiceImpl implements AuthorityService {
    @Resource
    private FunctionDao functionDao;
    @Resource
    private RoleFunctionRelationshipDao roleFunctionRelationshipDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private UserRoleRelationshipDao userRoleRelationshipDao;
    @Resource
    private AuthenticationInfoDao authenticationInfoDao;
    @Resource
    private UserDao userDao;

    @Transactional
    void awardAuthoritiesByRoleId(Long createById, Long roleId, List<Long> authoritiesId) {
        if (!roleDao.exists(roleId)){
            throw new VerifyException(ResponseCode.BAD_REQUEST,"没有找到该角色");
        }
        List<RoleFunctionRelationship> list = new ArrayList<>();
        for (Long anAuthoritiesId : authoritiesId) {
            if (roleFunctionRelationshipDao.findByRoleIdAndFunctionId(roleId,anAuthoritiesId) == null){
                RoleFunctionRelationship roleFunctionRelationship = new RoleFunctionRelationship();
                roleFunctionRelationship.setCreateTime(new Date());
                roleFunctionRelationship.setLastModifyBy(createById);
                roleFunctionRelationship.setCreateBy(createById);
                roleFunctionRelationship.setRoleId(roleId);
                roleFunctionRelationship.setLastModifyTime(new Date());
                roleFunctionRelationship.setFunctionId(anAuthoritiesId);
                list.add(roleFunctionRelationship);
            }
        }
        roleFunctionRelationshipDao.save(list);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Role> findAll(String identification, String description, Boolean disable,
                              AythorityType aythorityType, Integer pageNo, Integer pageSize) {
        if (pageNo<1){
            throw new VerifyException("pageNo参数异常");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        Page<Role> page ;
        if (identification == null|| identification.trim().equals("")){
            identification="%";
        }else {
            identification = "%"+identification+"%";
        }
        if (description == null|| description.trim().equals("")){
            description="%";
        }else {
            description = "%"+description+"%";
        }
        if (disable == null){
            if (aythorityType == null){
                page = roleDao.findRolesByCondition(identification,description,pageable);
            }else{
                page = roleDao.findRolesByCondition(identification,description,aythorityType,pageable);
            }
        }else {
            if (aythorityType == null){
                page = roleDao.findRolesByCondition(identification,description,disable,pageable);
            }else{
                page = roleDao.findRolesByCondition(identification,description,disable,aythorityType,pageable);
            }
        }
        return page;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> selectAllAuthority() {
        return functionDao.findUrl();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationInfo selectLoginMessageByIdentifier(String identifier, LoginType loginType) {
        return authenticationInfoDao.findByIdentifierAndVoucherType(identifier,loginType);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean judgeEnable(Long userId) {
        return authenticationInfoDao.judgeEnable(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> findPermsByUserPrincipal(Long userId, AythorityType LoginType) {
        //根据登录类型和用户id获取权限
        return authenticationInfoDao.findAythoritiesByUserId(userId,LoginType);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean findAythorityTypeByUserId(Long userId, AythorityType aythorityType) {
        List<Long> ids = roleDao.findAythorityTypeByUserId(userId,aythorityType);
        return ids.isEmpty();
    }

    @Override
    @Transactional
    public void updateFinalLoginByUserId(Long userId, HttpServletRequest httpServletRequest) {
        User user = userDao.findOne(userId);
        user.setFinalLogin(new Date());
        LoginEquipment equipment = DeviceUtils.judgmentEquipment(httpServletRequest);
        user.setLoginEquipment(equipment);
        userDao.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationInfo findByUserIdAndLongType(Long userId, LoginType loginType) {
        return authenticationInfoDao.findByUserIdAndVoucherType(userId,loginType);
    }

}
