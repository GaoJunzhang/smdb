package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zgj.mps.bean.AturhResourceBean;
import com.zgj.mps.bean.ResourceAuthBean;
import com.zgj.mps.bean.UserDeviceBean;
import com.zgj.mps.model.UserDevice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户设备数据处理层
 *
 * @author GaoJunZhang
 */
public interface UserDeviceMapper extends BaseMapper<UserDevice> {
    int batchSave(@Param("list") List<UserDevice> list);

    List<UserDeviceBean> userDeviceByUid(@Param("uid") Long uid);

    List<UserDeviceBean> deviceByUidAndTYpe(@Param("uid") Long uid, @Param("typeName") String typeName);

    List<ResourceAuthBean> resourceAuthByDeviceType(@Param("uid") Long uid, @Param("typeName") String typeName);

    List<AturhResourceBean> resourceAuthByDeviceTypeAndRid(@Param("uid") Long uid, @Param("typeName") String typeName, @Param("rid") String rid);

    List<AturhResourceBean> resourceAuthByDeviceMacAndRid(@Param("uid") Long uid, @Param("deviceMac") String deviceMac, @Param("rid") String rid);
}