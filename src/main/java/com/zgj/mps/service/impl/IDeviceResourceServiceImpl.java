package com.zgj.mps.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.bean.DeviceResourceBean;
import com.zgj.mps.dao.mapper.DeviceResourceMapper;
import com.zgj.mps.model.DeviceResource;
import com.zgj.mps.service.IDeviceResourceService;
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
public class IDeviceResourceServiceImpl extends ServiceImpl<DeviceResourceMapper, DeviceResource> implements IDeviceResourceService {

    @Autowired
    private DeviceResourceMapper deviceResourceMapper;

    public int batchSave(List<DeviceResource> list){
        return deviceResourceMapper.batchSave(list);
    }

    public List<DeviceResourceBean> deviceResourceByDid(String deviceId){
        return deviceResourceMapper.deviceResourceByDid(deviceId);
    }

    public List<DeviceResourceBean> deviceResourceByDeviceType(String deviceType){
        return deviceResourceMapper.deviceResourceByDeviceType(deviceType);
    }
}