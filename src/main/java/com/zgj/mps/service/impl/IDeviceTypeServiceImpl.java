package com.zgj.mps.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.dao.mapper.DeviceTypeMapper;
import com.zgj.mps.model.DeviceType;
import com.zgj.mps.service.IDeviceTypeService;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 设备类型接口实现
 *
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IDeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements IDeviceTypeService {

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    public IPage<DeviceType> selectPage(DeviceType deviceType, PageVo pageVo, SearchVo searchVo) {
        Page<DeviceType> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        QueryWrapper<DeviceType> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(deviceType.getName())) {
            queryWrapper.like("name","%"+deviceType.getName()+"%");
        }
        queryWrapper.eq("is_delete",0);
        if (StrUtil.isNotBlank(searchVo.getStartDate())){
            queryWrapper.gt("create_time",searchVo.getStartDate());
        }
        if (StrUtil.isNotBlank(searchVo.getEndDate())){
            queryWrapper.lt("create_time",searchVo.getEndDate());
        }
        queryWrapper.eq("is_delete",0);
        return deviceTypeMapper.selectPage(page, queryWrapper);
    }

    public DeviceType findByName(String name){
        QueryWrapper<DeviceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        queryWrapper.eq("is_delete",0);
        return deviceTypeMapper.selectOne(queryWrapper);
    }
}