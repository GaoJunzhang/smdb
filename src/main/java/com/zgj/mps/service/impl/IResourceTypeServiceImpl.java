package com.zgj.mps.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.dao.mapper.ResourceTypeMapper;
import com.zgj.mps.model.ResourceType;
import com.zgj.mps.service.IResourceTypeService;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 资源类型接口实现
 *
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IResourceTypeServiceImpl extends ServiceImpl<ResourceTypeMapper, ResourceType> implements IResourceTypeService {

    @Autowired
    private ResourceTypeMapper resourceTypeMapper;

    public IPage<ResourceType> selectPage(ResourceType resourceType, PageVo pageVo, SearchVo searchVo) {
        Page<ResourceType> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        QueryWrapper<ResourceType> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(resourceType.getName())) {
            queryWrapper.like("name", "%" + resourceType.getName() + "%");
        }
        queryWrapper.eq("is_delete", 0);
        if (StrUtil.isNotBlank(searchVo.getStartDate())) {
            queryWrapper.gt("create_time", searchVo.getStartDate());
        }
        if (StrUtil.isNotBlank(searchVo.getEndDate())) {
            queryWrapper.lt("create_time", searchVo.getEndDate());
        }
        return resourceTypeMapper.selectPage(page, queryWrapper);
    }

    public List<ResourceType> findByName(String name) {
        QueryWrapper<ResourceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        queryWrapper.eq("is_delete", 0);
        return resourceTypeMapper.selectList(queryWrapper);
    }


    public List<ResourceType> findByIds(List<String> list) {
        QueryWrapper<ResourceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", list);
        return resourceTypeMapper.selectList(queryWrapper);
    }
}