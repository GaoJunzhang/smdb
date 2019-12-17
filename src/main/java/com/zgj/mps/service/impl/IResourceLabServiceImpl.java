package com.zgj.mps.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.dao.mapper.ResourceLabMapper;
import com.zgj.mps.model.ResourceLab;
import com.zgj.mps.service.IResourceLabService;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资源标签接口实现
 *
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IResourceLabServiceImpl extends ServiceImpl<ResourceLabMapper, ResourceLab> implements IResourceLabService {

    @Autowired
    private ResourceLabMapper resourceLabMapper;

    public IPage<ResourceLab> selectPage(ResourceLab resourceLab, PageVo pageVo, SearchVo searchVo) {
        Page<ResourceLab> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        QueryWrapper<ResourceLab> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(resourceLab.getName())) {
            queryWrapper.like("name", "%" + resourceLab.getName() + "%");
        }
        queryWrapper.eq("is_delete", 0);
        if (StrUtil.isNotBlank(searchVo.getStartDate())) {
            queryWrapper.gt("create_time", searchVo.getStartDate());
        }
        if (StrUtil.isNotBlank(searchVo.getEndDate())) {
            queryWrapper.lt("create_time", searchVo.getEndDate());
        }
        return resourceLabMapper.selectPage(page, queryWrapper);
    }

}