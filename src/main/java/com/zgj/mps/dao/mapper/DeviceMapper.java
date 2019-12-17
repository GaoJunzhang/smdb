package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DeviceBean;
import com.zgj.mps.model.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 设备数据处理层
 * @author GaoJunZhang
 */
public interface DeviceMapper extends BaseMapper<Device> {

    List<DeviceBean> getVaildDevice(Page page, @Param("deviceName") String deviceName, @Param("deviceMac") String deviceMac);

    Map getMonthDevice(@Param("year") int year);

    int sumDevices();
}