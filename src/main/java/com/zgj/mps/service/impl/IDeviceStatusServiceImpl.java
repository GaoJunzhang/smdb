package com.zgj.mps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.bean.DeviceStatusBean;
import com.zgj.mps.dao.mapper.DeviceStatusMapper;
import com.zgj.mps.model.DeviceStatus;
import com.zgj.mps.service.IDeviceStatusService;
import com.zgj.mps.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * 在线设备接口实现
 *
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IDeviceStatusServiceImpl extends ServiceImpl<DeviceStatusMapper, DeviceStatus> implements IDeviceStatusService {

    @Autowired
    private DeviceStatusMapper deviceStatusMapper;

    public List<DeviceStatus> deviceByTime(Timestamp time) {
        QueryWrapper<DeviceStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("time", time);
        return deviceStatusMapper.selectList(queryWrapper);
    }

    public List<DeviceStatus> findByDeviceId(String deviceId){
        QueryWrapper<DeviceStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        return deviceStatusMapper.selectList(queryWrapper);
    }

    public Page<DeviceStatusBean> pageDeviceStatus(String deviceName,String deviceMac, PageVo pageVo) {
        Page<DeviceStatusBean> pageFollow = new Page<DeviceStatusBean>(pageVo.getPageNo(), pageVo.getPageSize());
        return deviceStatusMapper.pageDeviceStatus(pageFollow, deviceName,deviceMac); //自定义方法，多表
    }
}