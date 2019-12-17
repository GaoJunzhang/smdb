package com.zgj.mps.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.dao.mapper.UserTypeMapper;
import com.zgj.mps.model.UserType;
import com.zgj.mps.service.IUserTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户设备接口实现
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IUserTypeServiceImpl extends ServiceImpl<UserTypeMapper, UserType> implements IUserTypeService {

    @Autowired
    private UserTypeMapper userTypeMapper;

    public List<UserType> findAll(UserType userType){
        QueryWrapper<UserType> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(userType.getTypeId())){
            queryWrapper.eq("type_id",userType.getTypeId());
        }
        if (StrUtil.isNotBlank(userType.getUserId())){
            queryWrapper.eq("user_id",userType.getUserId());
        }
        queryWrapper.eq("is_delete",0);
        return userTypeMapper.selectList(queryWrapper);
    }
}