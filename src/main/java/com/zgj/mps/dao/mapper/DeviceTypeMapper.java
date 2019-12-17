package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zgj.mps.model.DeviceType;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;

/**
 * 设备类型数据处理层
 *
 * @author GaoJunZhang
 */
public interface DeviceTypeMapper extends BaseMapper<DeviceType> {
    IPage<DeviceType> pageData(DeviceType deviceType, PageVo pageVo, SearchVo searchVo);
}