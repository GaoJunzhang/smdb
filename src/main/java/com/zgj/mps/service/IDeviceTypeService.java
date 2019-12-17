package com.zgj.mps.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.model.DeviceType;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;

/**
 * 设备类型接口
 * @author GaoJunZhang
 */
public interface IDeviceTypeService extends IService<DeviceType> {
    IPage<DeviceType> selectPage(DeviceType deviceType, PageVo pageVo, SearchVo searchVo);

    DeviceType findByName(String name);
}