package com.zgj.mps.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.bean.UserDResourceBean;
import com.zgj.mps.dao.mapper.ResourceMapper;
import com.zgj.mps.model.Resource;
import com.zgj.mps.service.IResourceService;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * 资源接口实现
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    public IPage<Resource> selectPage(Resource resource, PageVo pageVo, SearchVo searchVo){
        Page<Resource> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(resource.getName())) {
            queryWrapper.like("name",resource.getName());
        }
        if (StrUtil.isNotBlank(resource.getTitle())) {
            queryWrapper.like("title",resource.getTitle());
        }
        queryWrapper.eq("is_delete",0);
        if (StrUtil.isNotBlank(resource.getContent())){
            queryWrapper.like("content",resource.getContent());
        }
        if (StrUtil.isNotBlank(searchVo.getStartDate())){
            queryWrapper.gt("create_time",searchVo.getStartDate());
        }
        if (StrUtil.isNotBlank(searchVo.getEndDate())){
            queryWrapper.lt("create_time",searchVo.getEndDate());
        }
        if (StrUtil.isNotBlank(resource.getLabId())){
            queryWrapper.eq("lab_id",resource.getLabId());
        }
        return resourceMapper.selectPage(page, queryWrapper);
    }
    public List<UserDResourceBean> reourceListByUid(String uid){
        return resourceMapper.reourceListByUid(uid);
    }

    public List<Resource> findByIds(List<Resource> list){
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",0);
        queryWrapper.in("id",list);
        return resourceMapper.selectList(queryWrapper);
    }

    public List<Resource> findByTypeIds(List<String> list){
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",0);
        queryWrapper.in("type_id",list);
        return resourceMapper.selectList(queryWrapper);
    }

    public Map monthResource(int year){
        return resourceMapper.monthResource(year);
    }

    public int sumResource(){
        return resourceMapper.sumResource();
    }
}