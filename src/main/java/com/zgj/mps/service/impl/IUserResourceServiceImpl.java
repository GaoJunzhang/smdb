package com.zgj.mps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.bean.AturhResourceBean;
import com.zgj.mps.bean.UserAuthBean;
import com.zgj.mps.bean.UserResourceBean;
import com.zgj.mps.bean.UserResourceDtypeBean;
import com.zgj.mps.dao.mapper.UserResourceMapper;
import com.zgj.mps.model.UserResource;
import com.zgj.mps.service.IUserResourceService;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 设备资源接口实现
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IUserResourceServiceImpl extends ServiceImpl<UserResourceMapper, UserResource> implements IUserResourceService {

    @Autowired
    private UserResourceMapper userResourceMapper;

    public int batchSave(List<UserResource> list){
        return userResourceMapper.batchSave(list);
    }

    public List<UserResourceBean> userResourceByUid(Long uid){
        return userResourceMapper.userResourceByUid(uid);
    }

    public Page<UserAuthBean> userAuthData(PageVo pageVo, SearchVo searchVo, String nickName, Short isForever){
        Page<UserAuthBean> page = new Page<UserAuthBean>(pageVo.getPageNo(), pageVo.getPageSize());
        return userResourceMapper.userAuthData(page,nickName,isForever); //自定义方法，多表
    }

    public int updateUrIsforever(Short isForever,Long userId){
        return userResourceMapper.updateUrIsforever(isForever,userId);
    }

    public int updateUrStatus(Short status,Long userId){
        return userResourceMapper.updateUrStatus(status,userId);
    }

    public List<UserResource> userResourceByUserId(Long userId){
        QueryWrapper<UserResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return userResourceMapper.selectList(queryWrapper);
    }

    public List<UserResourceDtypeBean> udtData(Long userId, String[] typeIds){
        return userResourceMapper.userResourceByDtype(userId,typeIds);
    }

    public List<UserResource> findByUserIdAndResourceId(Long userId,String resourceId){
        QueryWrapper<UserResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("resource_id",resourceId);
        return userResourceMapper.selectList(queryWrapper);
    }

    public List<AturhResourceBean> selectUrByUidAndRid(Long userId, String rid){
        return userResourceMapper.selectUrByUidAndRid(userId,rid);
    }
}