package com.zgj.mps.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.bean.DeviceResourceBean;
import com.zgj.mps.model.DeviceResource;

import java.util.List;

/**
 * 设备资源接口
 * @author GaoJunZhang
 */
public interface IDeviceResourceService extends IService<DeviceResource> {
    int batchSave(List<DeviceResource> list);

    List<DeviceResourceBean> deviceResourceByDid(String deviceId);

    List<DeviceResourceBean> deviceResourceByDeviceType(String deviceType);

}