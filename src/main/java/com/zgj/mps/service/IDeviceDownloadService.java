package com.zgj.mps.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.bean.DeviceDownloadBean;
import com.zgj.mps.bean.DevicePageBean;
import com.zgj.mps.model.DeviceDownload;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;

import java.util.List;

/**
 * 资源类型接口
 * @author GaoJunZhang
 */
public interface IDeviceDownloadService extends IService<DeviceDownload> {
    List<DeviceDownloadBean> devicerDownList(Long userId,String deviceMac);

    Page<DevicePageBean> pageDownload(DevicePageBean devicePageBean, PageVo pageVo, SearchVo searchVo);
}