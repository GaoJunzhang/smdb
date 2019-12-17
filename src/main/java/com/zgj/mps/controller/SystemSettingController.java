package com.zgj.mps.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSSClient;
import com.zgj.mps.controller.request.SaveSystemSettingRequest;
import com.zgj.mps.generator.base.BaseController;
import com.zgj.mps.model.SystemSetting;
import com.zgj.mps.service.SystemSettingService;
import com.zgj.mps.tool.OSSClientUtil;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author Wangj
 */
@Slf4j
@RestController
@Api(description = "系统设置管理接口")
@RequestMapping("/systemSetting")
@Transactional
public class SystemSettingController extends BaseController<SystemSetting, String> {

    @Autowired
    private SystemSettingService systemSettingService;

    @Override
    public SystemSettingService getService() {
        return systemSettingService;
    }

    @RequiresPermissions("setting:aliyunOss")
    @RequestMapping(value = "/getAliyunOssSetting", method = RequestMethod.GET)
    @ApiOperation(value = "获取OSS设置")
    public Result getAliyunOssSetting() {
        return new ResultUtil<>().setData(systemSettingService.getSetting("aliyun_oss", "{}"));
    }

    @RequiresPermissions("setting:aliyunOss")
    @RequestMapping(value = "/setAliyunOssSetting", method = RequestMethod.POST)
    @ApiOperation(value = "设置OSS")
    public Object setAliyunOssSetting(@RequestBody SaveSystemSettingRequest saveSystemSettingRequest) {
        if (StrUtil.isNotEmpty(saveSystemSettingRequest.getSetting()) && !"{}".equals(saveSystemSettingRequest.getSetting())) {
            JSONObject jsonObject = JSONUtil.parseObj(saveSystemSettingRequest.getSetting());
            OSSClient ossClient = new OSSClient(jsonObject.get("endpoint") + "", jsonObject.get("key") + "", jsonObject.get("secret") + "");
            try {
                ossClient.listBuckets();
            } catch (Exception e) {
                return new ResultUtil<>().setErrorMsg("阿里oss参数配置错误");
            } finally {
                ossClient.shutdown();
            }
        }
        OSSClientUtil.clearInstance();
        return new ResultUtil().setSuccessMsg(systemSettingService.setSetting("aliyun_oss", saveSystemSettingRequest.getSetting()));
    }

    @RequiresPermissions("setting:base")
    @RequestMapping(value = "/getBaseSetting", method = RequestMethod.GET)
    @ApiOperation(value = "获取基本设置")
    public Result getBaseSetting() {
        HashMap<String, Object> settings = new HashMap<>();
        settings.put("adDuration", systemSettingService.getSetting("ad_duration", "15"));
        settings.put("connHeartbeat", systemSettingService.getSetting("conn_heartbeat", "30"));
        settings.put("serverUrl", systemSettingService.getSetting("server_url", "http://localhost:9999/"));

        return new ResultUtil<>().setData(settings);
    }

    @RequiresPermissions("setting:base")
    @RequestMapping(value = "/setAdDurationSetting", method = RequestMethod.POST)
    public Object setAdDurationSetting(@RequestBody SaveSystemSettingRequest saveSystemSettingRequest) {
        if (StrUtil.isEmpty(saveSystemSettingRequest.getSetting())) {
            return new ResultUtil().setErrorMsg("无效的参数");
        }
        return new ResultUtil().setSuccessMsg(systemSettingService.setSetting("ad_duration", saveSystemSettingRequest.getSetting()));
    }

    @RequiresPermissions("setting:base")
    @RequestMapping(value = "/setConnHeartbeatSetting", method = RequestMethod.POST)
    public Object setConnHeartbeatSetting(@RequestBody SaveSystemSettingRequest saveSystemSettingRequest) {
        if (StrUtil.isEmpty(saveSystemSettingRequest.getSetting())) {
            return new ResultUtil().setErrorMsg("无效的参数");
        }
        return new ResultUtil().setSuccessMsg(systemSettingService.setSetting("conn_heartbeat", saveSystemSettingRequest.getSetting()));
    }

    @RequiresPermissions("setting:base")
    @RequestMapping(value = "/setServerUrlSetting", method = RequestMethod.POST)
    public Object setServerUrlSetting(@RequestBody SaveSystemSettingRequest saveSystemSettingRequest) {
        if (StrUtil.isEmpty(saveSystemSettingRequest.getSetting())) {
            return new ResultUtil().setErrorMsg("无效的参数");
        }
        return new ResultUtil().setSuccessMsg(systemSettingService.setSetting("server_url", saveSystemSettingRequest.getSetting()));
    }

    @RequiresPermissions("setting:weather")
    @RequestMapping(value = "/getWeatherSetting", method = RequestMethod.GET)
    @ApiOperation(value = "获取天气设置")
    public Result getWeatherSetting() {
        return new ResultUtil<>().setData(systemSettingService.getSetting("weather", "{}"));
    }

    @RequiresPermissions("setting:weather")
    @RequestMapping(value = "/setWeatherSetting", method = RequestMethod.POST)
    @ApiOperation(value = "设置天气")
    public Object setWeatherSetting(@RequestBody SaveSystemSettingRequest saveSystemSettingRequest) {
        return new ResultUtil().setSuccessMsg(systemSettingService.setSetting("weather", saveSystemSettingRequest.getSetting()));
    }
}
