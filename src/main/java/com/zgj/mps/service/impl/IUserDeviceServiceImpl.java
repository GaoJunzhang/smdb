package com.zgj.mps.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.bean.AturhResourceBean;
import com.zgj.mps.bean.ResourceAuthBean;
import com.zgj.mps.bean.UserDeviceBean;
import com.zgj.mps.dao.mapper.UserDeviceMapper;
import com.zgj.mps.model.UserDevice;
import com.zgj.mps.service.IUserDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户设备接口实现
 *
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IUserDeviceServiceImpl extends ServiceImpl<UserDeviceMapper, UserDevice> implements IUserDeviceService {

    @Autowired
    private UserDeviceMapper userDeviceMapper;

    public int batchSave(List<UserDevice> list) {
        return userDeviceMapper.batchSave(list);
    }

    public List<UserDeviceBean> userDeviceByUid(Long uid) {
        return userDeviceMapper.userDeviceByUid(uid);
    }

    public List<UserDeviceBean> deviceByUidAndTYpe(Long uid, String typeName) {
        return userDeviceMapper.deviceByUidAndTYpe(uid, typeName);
    }

    public List<ResourceAuthBean> resourceAuthByDeviceType(Long uid, String typeName) {
        return userDeviceMapper.resourceAuthByDeviceType(uid, typeName);
    }

    public List<AturhResourceBean> resourceAuthByDeviceTypeAndRid(Long uid, String typeName, String rid) {
        return userDeviceMapper.resourceAuthByDeviceTypeAndRid(uid, typeName, rid);
    }

    public List<AturhResourceBean> resourceAuthByDeviceMacAndRid(Long uid, String deviceMac, String rid) {
        return userDeviceMapper.resourceAuthByDeviceMacAndRid(uid, deviceMac, rid);
    }


    public List<UserDevice> findByUd(UserDevice userDevice) {
        QueryWrapper<UserDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0);
        if (userDevice.getUserId() != null) {
            queryWrapper.eq("user_id", userDevice.getUserId());
        }
        if (StrUtil.isNotBlank(userDevice.getDeviceId())) {
            queryWrapper.eq("device_id", userDevice.getDeviceId());
        }
        return userDeviceMapper.selectList(queryWrapper);
    }
}