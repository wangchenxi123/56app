package com.mierro.authority.Listener.AuthorityInitSerivce;

import com.alibaba.fastjson.JSON;
import com.google.common.io.Resources;
import com.mierro.authority.common.AythorityType;
import com.mierro.authority.common.LoginType;
import com.mierro.authority.common.PasswordUtils;
import com.mierro.authority.dao.*;
import com.mierro.authority.entity.*;
import com.mierro.common.common.StringBuilderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by 黄晓滨 simba on 2017/4/1.
 * Remarks：
 */
@Service("AuthorityInitSerivce")
public class AuthorityInitSerivce {

    @Resource
    private RoleDao roleDao;
    @Resource
    private RoleFunctionRelationshipDao roleFunctionRelationshipDao;
    @Resource
    private UserDao userDao;
    @Resource
    private UserRoleRelationshipDao userRoleRelationshipDao;
    @Resource
    private AuthenticationInfoDao authenticationInfoDao;
    @Resource
    private FunctionDao functionDao;

    private Set<Long> permissions = new HashSet<>();

    @Transactional
    public void addSuperAdmin(){
        if(authenticationInfoDao.findByUserId(1L).size() == 0) {
            //创建超级管理员角色
            Role role = new Role();
            role.setId(1L);
            role.setDisable(false);
            role.setCreateBy(1L);
            role.setLastModifyBy(1L);
            role.setLastModifyTime(new Date());
            role.setType(AythorityType.INSIDER);
            role.setDescription("超级管理员角色");
            role.setIdentification("超级管理员角色,拥有所有权限操作");
            roleDao.save(role);
            System.out.println("/************************创建超级管理员角色***************************/");
            //创建用户
            User user = new User();
            user.setId(1L);
            user.setUsername("mierro");
            user.setCreateBy(1L);
            user.setLastModifyBy(1L);
            user.setLastModifyTime(new Date());
            user.setDisable(false);
            user.setFinalLogin(new Date());
            user.setRefreshToken(UUID.randomUUID().toString());
            PasswordUtils passwordUtils = new PasswordUtils();
            user.setSalt(StringBuilderUtils.getRandomString(16));
            Date data = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(data);
            calendar.add(Calendar.HOUR_OF_DAY, 2);
            user.setExpirationTime(calendar.getTime());
            userDao.save(user);
            //创建用户角色关联
            List<UserRoleRelationship> rows = new ArrayList<>();
            List<Role> roles = roleDao.findAll();
            for (Role role1 : roles){
                if (userRoleRelationshipDao.findByUserIdAndRoleId(1L,role1.getId()) == null){
                    UserRoleRelationship userRoleRelationship = new UserRoleRelationship();
                    userRoleRelationship.setRoleId(role1.getId());
                    userRoleRelationship.setUserId(user.getId());
                    userRoleRelationship.setCreateBy(1L);
                    userRoleRelationship.setLastModifyBy(1L);
                    userRoleRelationship.setLastModifyTime(new Date());
                    rows.add(userRoleRelationship);
                }
            }
            userRoleRelationshipDao.save(rows);
            System.out.println("/************************创建超级管理员账号***************************/");
            //创建登录方式
            //密码:mierro13149980
            String password = "07a21341ab704f2b5ae04b892cfdf05e";
            String passwordAndsalt[] = passwordUtils.encryptPassword(password);
            AuthenticationInfo authenticationInfo = new AuthenticationInfo();
            authenticationInfo.setUserId(user.getId());
            authenticationInfo.setLastModifyBy(1L);
            authenticationInfo.setLastModifyTime(new Date());
            authenticationInfo.setCreateTime(new Date());
            authenticationInfo.setCreateBy(1L);
            authenticationInfo.setCredential(passwordAndsalt[0]);
            authenticationInfo.setIdentifier("mierro");
            authenticationInfo.setVoucherType(LoginType.ACCOUNT);
            authenticationInfo.setSalt(passwordAndsalt[1]);
            authenticationInfoDao.save(authenticationInfo);
            System.out.println("/***********************创建超级管理员登陆方式*************************/");
            addSuperAuthority();
            System.out.println("/***********************赋予超级管理员所有权限*************************/");
        }
    }

    @Transactional
    public void addSuperAuthority(){
        Set<Long> set;
        if (permissions.size() == 0){
            set = functionDao.findAllId();
        }else{
            set = functionDao.findByAll(permissions);
        }

        if (!roleDao.exists(1L)){
            Role role = new Role();
            role.setId(1L);
            role.setDisable(false);
            role.setCreateBy(1L);
            role.setLastModifyBy(1L);
            role.setLastModifyTime(new Date());
            role.setType(AythorityType.INSIDER);
            role.setDescription("超级管理员角色");
            role.setIdentification("超级管理员角色,拥有所有权限操作");
            roleDao.save(role);
        }
        Iterator it = set.iterator();
        List<RoleFunctionRelationship> list = new ArrayList<>();
        while (it.hasNext()){
            //检测是否存在该记录
            Long id = (Long) it.next();
            if (roleFunctionRelationshipDao.findByRoleIdAndFunctionId(1L,id ) != null){
                continue;
            }
            RoleFunctionRelationship roleFunctionRelationship = new RoleFunctionRelationship();
            roleFunctionRelationship.setCreateTime(new Date());
            roleFunctionRelationship.setLastModifyBy(1L);
            roleFunctionRelationship.setCreateBy(1L);
            roleFunctionRelationship.setRoleId(1L);
            roleFunctionRelationship.setLastModifyTime(new Date());
            roleFunctionRelationship.setFunctionId(id);
            list.add(roleFunctionRelationship);
        }
        roleFunctionRelationshipDao.save(list);
    }

    @Transactional
    public void addAuthority(){
        //读取文件
        try {
            BufferedInputStream bufferedInputStream =
                    (BufferedInputStream) Resources.getResource("basalAuthority.json").getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream,"UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String valueString ;
            while ((valueString=bufferedReader.readLine())!=null){
                stringBuilder.append(valueString);
            }
            //进行数据转换
            List<Authority> authorities = JSON.parseArray(stringBuilder.toString(),Authority.class);
            if (!authorities.isEmpty()){
                //刷新保存
                functionDao.save(authorities);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void delete(Long roleId,List<Long> all){
        if (all != null && all.size() != 0){
            roleFunctionRelationshipDao.delete(roleId,all);
        }
    }

    public Boolean exits(){
        List<AuthenticationInfo> list = authenticationInfoDao.findByUserId(1L);
        return !(list == null || list.size() == 0);
    }

    public Boolean detectionPermissionSet(){
        List<Authority> authorities = functionDao.findAll();
        return authorities.isEmpty();
    }

    public void addClientRols(){
        if (!roleDao.exists(2L)){
            Role role = new Role();
            role.setId(2L);
            role.setDisable(false);
            role.setCreateBy(1L);
            role.setLastModifyBy(1L);
            role.setLastModifyTime(new Date());
            role.setType(AythorityType.CLIENT);
            role.setDescription("普通会员");
            role.setIdentification("普通会员角色,拥有所有前段权限操作权限");
            roleDao.save(role);
        }
    }


}
