package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zgj.mps.bean.DeviceResourceBean;
import com.zgj.mps.model.DeviceResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备资源数据处理层
 *
 * @author GaoJunZhang
 */
public interface DeviceResourceMapper extends BaseMapper<DeviceResource> {
    int batchSave(@Param("list") List<DeviceResource> list);

    List<DeviceResourceBean> deviceResourceByDid(@Param("deviceId") String deviceId);

    List<DeviceResourceBean> deviceResourceByDeviceType(@Param("deviceType") String deviceType);
}