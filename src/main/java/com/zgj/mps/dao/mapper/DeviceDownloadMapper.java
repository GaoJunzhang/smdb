package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DeviceDownloadBean;
import com.zgj.mps.bean.DevicePageBean;
import com.zgj.mps.model.DeviceDownload;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源类型数据处理层
 * @author GaoJunZhang
 */
public interface DeviceDownloadMapper extends BaseMapper<DeviceDownload> {
    List<DeviceDownloadBean> devicerDownList(@Param("userId") String userId,@Param("deviceMac") String deviceMac);

    Page<DevicePageBean> deviceDownList(Page<DevicePageBean> page, @Param("devicePageBean") DevicePageBean devicePageBean);

}