package com.zgj.mps.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.model.UserType;

import java.util.List;

/**
 * 用户设备接口
 * @author GaoJunZhang
 */
public interface IUserTypeService extends IService<UserType> {
    List<UserType> findAll(UserType userType);
}