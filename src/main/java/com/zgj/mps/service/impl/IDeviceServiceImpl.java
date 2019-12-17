package com.zgj.mps.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.bean.DeviceBean;
import com.zgj.mps.dao.mapper.DeviceMapper;
import com.zgj.mps.model.Device;
import com.zgj.mps.service.IDeviceService;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 设备接口实现
 *
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IDeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    public IPage<Device> selectPage(Device device, PageVo pageVo, SearchVo searchVo) {
        Page<Device> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(device.getDeviceName())) {
            queryWrapper.like("device_name", "%" + device.getDeviceName() + "%");
        }
        if (StrUtil.isNotBlank(device.getDeviceMac())) {
            queryWrapper.like("device_mac", "%" + device.getDeviceMac() + "%");
        }
        if (device.getEnable() != null) {
            queryWrapper.eq("is_delete", device.getEnable());
        }
        if (device.getDeviceTypeId() != null) {
            queryWrapper.eq("device_type_id", device.getDeviceTypeId());
        }
        if (StrUtil.isNotBlank(searchVo.getStartDate())) {
            queryWrapper.gt("create_time", searchVo.getStartDate());
        }
        if (StrUtil.isNotBlank(searchVo.getEndDate())) {
            queryWrapper.lt("create_time", searchVo.getEndDate());
        }
        queryWrapper.eq("enable",0);
        return deviceMapper.selectPage(page, queryWrapper);
    }

    public List<Device> deviceByDeviceMac(String deviceMac){
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_mac",deviceMac);
        queryWrapper.eq("enable",0);
        return deviceMapper.selectList(queryWrapper);
    }

    public List<Device> deviceByTypeId(String typeId){
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_type_id",typeId);
        queryWrapper.eq("enable",0);
        return deviceMapper.selectList(queryWrapper);
    }

    public List<Device> devicesByTypeIds(List<Device> typeIds){
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable",0);
        queryWrapper.in("device_type_id",typeIds);
        return deviceMapper.selectList(queryWrapper);
    }

    public List<Device> devicesByids(List<String> ids){
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable",0);
        queryWrapper.in("id",ids);
        return deviceMapper.selectList(queryWrapper);
    }

    //
    public Page<DeviceBean> getVaildDevicePage(int pageNo, int pageSize, String deviceName, String deviceMac){
        Page<DeviceBean> page = new Page<DeviceBean>(pageNo, pageSize);
        return page.setRecords(this.baseMapper.getVaildDevice(page, deviceName,deviceMac));
    }

    public Map getMonthDevice(int year){
        return deviceMapper.getMonthDevice(year);
    }

    public int sumDevices(){
        return deviceMapper.sumDevices();
    }
}