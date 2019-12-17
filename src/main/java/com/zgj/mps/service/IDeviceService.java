package com.zgj.mps.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.bean.DeviceBean;
import com.zgj.mps.model.Device;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;

import java.util.List;
import java.util.Map;

/**
 * 设备接口
 * @author GaoJunZhang
 */
public interface IDeviceService extends IService<Device> {
    IPage<Device> selectPage(Device device, PageVo pageVo, SearchVo searchVo);

    List<Device> deviceByDeviceMac(String deviceMac);

    List<Device> deviceByTypeId(String typeId);

    List<Device> devicesByTypeIds(List<Device> typeIds);

    List<Device> devicesByids(List<String> ids);

    Page<DeviceBean> getVaildDevicePage(int pageNo, int pageSize, String deviceName, String deviceMac);

    Map getMonthDevice(int year);

    int sumDevices();
}