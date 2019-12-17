package com.zgj.mps.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.bean.DeviceDownloadBean;
import com.zgj.mps.bean.DevicePageBean;
import com.zgj.mps.bean.DownloadBean;
import com.zgj.mps.dao.mapper.DeviceDownloadMapper;
import com.zgj.mps.model.DeviceDownload;
import com.zgj.mps.service.IDeviceDownloadService;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 资源类型接口实现
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IDeviceDownloadServiceImpl extends ServiceImpl<DeviceDownloadMapper, DeviceDownload> implements IDeviceDownloadService {

    @Autowired
    private DeviceDownloadMapper deviceDownloadMapper;

    public List<DeviceDownloadBean> devicerDownList(Long userId, String deviceMac) {
        String ust = "";
        if (userId != null){
            ust = userId+"";
        }
        return deviceDownloadMapper.devicerDownList(ust, deviceMac);
    }

    public Page<DevicePageBean> pageDownload(DevicePageBean devicePageBean, PageVo pageVo, SearchVo searchVo){
        Page<DevicePageBean> pageDownload = new Page<DevicePageBean>(pageVo.getPageNo(), pageVo.getPageSize());
        return deviceDownloadMapper.deviceDownList(pageDownload,devicePageBean); //自定义方法，多表
    }
}