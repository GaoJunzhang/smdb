package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DeviceStatusBean;
import com.zgj.mps.bean.FollowBean;
import com.zgj.mps.model.DeviceStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 在线设备数据处理层
 * @author GaoJunZhang
 */
public interface DeviceStatusMapper extends BaseMapper<DeviceStatus> {
    Page<DeviceStatusBean> pageDeviceStatus(Page<DeviceStatusBean> page, @Param("deviceName") String deviceName,@Param("deviceMac") String deviceMac);
}