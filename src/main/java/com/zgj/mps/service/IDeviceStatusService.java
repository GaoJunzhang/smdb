package com.zgj.mps.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.bean.DeviceStatusBean;
import com.zgj.mps.model.DeviceStatus;
import com.zgj.mps.vo.PageVo;

import java.sql.Timestamp;
import java.util.List;

/**
 * 在线设备接口
 * @author GaoJunZhang
 */
public interface IDeviceStatusService extends IService<DeviceStatus> {
    List<DeviceStatus> deviceByTime(Timestamp time);

    List<DeviceStatus> findByDeviceId(String deviceId);

    Page<DeviceStatusBean> pageDeviceStatus(String deviceName, String deviceMac, PageVo pageVo);
}