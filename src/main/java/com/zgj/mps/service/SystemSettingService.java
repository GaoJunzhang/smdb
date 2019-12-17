package com.zgj.mps.service;


import com.zgj.mps.generator.base.BaseService;
import com.zgj.mps.model.SystemSetting;

/**
 * 系统设置接口
 *
 * @author Wangj
 */
public interface SystemSettingService extends BaseService<SystemSetting, String> {

    String getSetting(String key, String defaultSeting);

    String setSetting(String key, String setting);
}