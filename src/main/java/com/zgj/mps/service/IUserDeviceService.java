package com.zgj.mps.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.bean.AturhResourceBean;
import com.zgj.mps.bean.ResourceAuthBean;
import com.zgj.mps.bean.UserDeviceBean;
import com.zgj.mps.model.UserDevice;

import java.util.List;

/**
 * 用户设备接口
 *
 * @author GaoJunZhang
 */
public interface IUserDeviceService extends IService<UserDevice> {
    int batchSave(List<UserDevice> list);

    List<UserDeviceBean> userDeviceByUid(Long uid);

    List<UserDeviceBean> deviceByUidAndTYpe(Long uid, String typeName);

    List<ResourceAuthBean> resourceAuthByDeviceType(Long uid, String typeName);

    List<AturhResourceBean> resourceAuthByDeviceTypeAndRid(Long uid, String typeName, String rid);

    List<AturhResourceBean> resourceAuthByDeviceMacAndRid(Long uid, String deviceMac, String rid);


    List<UserDevice> findByUd(UserDevice userDevice);
}