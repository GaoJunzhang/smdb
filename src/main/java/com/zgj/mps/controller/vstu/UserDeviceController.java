package com.zgj.mps.controller.vstu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zgj.mps.bean.AturhResourceBean;
import com.zgj.mps.bean.ResourceAuthBean;
import com.zgj.mps.bean.UserDeviceBean;
import com.zgj.mps.model.Device;
import com.zgj.mps.model.DeviceType;
import com.zgj.mps.model.User;
import com.zgj.mps.model.UserDevice;
import com.zgj.mps.service.IDeviceService;
import com.zgj.mps.service.IDeviceTypeService;
import com.zgj.mps.service.IUserDeviceService;
import com.zgj.mps.service.IUserResourceService;
import com.zgj.mps.tool.PageUtil;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.tool.ShiroSecurityUtil;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "用户设备管理接口")
@RequestMapping("/userDevice")
@Transactional
public class UserDeviceController {

    @Autowired
    private IUserDeviceService iUserDeviceService;

    @Autowired
    private IUserResourceService iUserResourceService;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IDeviceTypeService iDeviceTypeService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<UserDevice> get(@PathVariable String id) {

        UserDevice userDevice = iUserDeviceService.getById(id);
        return new ResultUtil<UserDevice>().setData(userDevice);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<UserDevice>> getAll() {

        List<UserDevice> list = iUserDeviceService.list();
        return new ResultUtil<List<UserDevice>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo page) {

        IPage<UserDevice> data = iUserDeviceService.page(PageUtil.initMpPage(page));
        Map<String, Object> map = new HashMap<>();
        map.put("data", data.getRecords());
        map.put("pageNo", data.getPages());
        map.put("totalCount", data.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<UserDeviceBean> edit(@ModelAttribute UserDevice userDevice) {
        if (iUserDeviceService.saveOrUpdate(userDevice)) {
            return new ResultUtil<UserDeviceBean>().setSuccessMsg("操作成功");
        }
        return new ResultUtil<UserDeviceBean>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/getUserDeviceByUid/{uId}", method = RequestMethod.GET)
    @ApiOperation(value = "/通过用户id获取用户设备")
    public Result<List<UserDeviceBean>> getUserDeviceByUid(@PathVariable Long uId) {
        List<UserDeviceBean> list = iUserDeviceService.userDeviceByUid(uId);
        return new ResultUtil<List<UserDeviceBean>>().setData(list);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<UserDevice> saveOrUpdate(Short isForever,
                                           @RequestParam(name = "uIds", required = true) String[] uIds,
                                           @RequestParam(name = "dIds", required = true) String[] dIds,
                                           String startDate, String endDate) {

        List<UserDevice> list = new ArrayList<>();
        for (int i = 0; i < uIds.length; i++) {
            for (int j = 0; j < dIds.length; j++) {
                UserDevice userDevice = new UserDevice();
//                userDevice.setIsForever(isForever);
                userDevice.setIsDelete((short) 0);
                userDevice.setCreateTime(new Timestamp(System.currentTimeMillis()));
               /* if (isForever != 0) {
                    userDevice.setStartTime(Timestamp.valueOf(startDate));
                    userDevice.setEndTime(Timestamp.valueOf(endDate));
                }*/
                userDevice.setDeviceId(dIds[j]);
                if (checkDevice(userDevice)) {
                    userDevice.setUserId(Long.parseLong(uIds[i]));
                    list.add(userDevice);
                }
            }
        }
        if (list.size() <= 0) {
            return new ResultUtil<UserDevice>().setErrorMsg("无可用设备");
        }
        if (iUserDeviceService.batchSave(list) == 1) {
            return new ResultUtil<UserDevice>().setSuccessMsg("添加成功");
        }
        return new ResultUtil<UserDevice>().setErrorMsg("操作失败");
    }

    public boolean checkDevice(UserDevice userDevice) {
        List<UserDevice> userDevices = iUserDeviceService.findByUd(userDevice);
        if (userDevices.size() > 0) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iUserDeviceService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/deviceByUidAndType", method = RequestMethod.GET)
    @ApiOperation(value = "/通过用户id获取用户设备")
    public Result<List<Device>> deviceByUidAndType(@RequestParam(name = "type", required = true) String type) {
        User user = shiroSecurityUtil.getCurrentUser();
        DeviceType deviceType = iDeviceTypeService.findByName(type);
        if (deviceType == null) {
            return new ResultUtil<List<Device>>().setErrorMsg("无效设备类型");
        }
        List<Device> devices = iDeviceService.deviceByTypeId(deviceType.getId());
        if (devices.size() <= 0) {
            return new ResultUtil<List<Device>>().setErrorMsg(deviceType.getName() + "类型下无设备，请配置");
        }
        UserDevice userDevice = new UserDevice();
        userDevice.setUserId(user.getId());
        List<UserDevice> userDevices = iUserDeviceService.findByUd(userDevice);
        List<Device> newDeivce = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for (UserDevice userDevice1 : userDevices) {
            map.put(userDevice1.getDeviceId(), userDevice1.getUserId()+"");
        }
        for (Device device : devices) {
            if (map.containsKey(device.getId())) {
                newDeivce.add(device);
            }
        }
        return new ResultUtil<List<Device>>().setData(newDeivce);
    }

    @RequestMapping(value = "/resourceAuthByDeviceType", method = RequestMethod.GET)
    @ApiOperation(value = "/根据设备类型，获取用户权限")
    public Result<List<ResourceAuthBean>> resourceAuthByDeviceType(@RequestParam(name = "type", required = true) String type) {
        User user = shiroSecurityUtil.getCurrentUser();
        List<ResourceAuthBean> list = iUserDeviceService.resourceAuthByDeviceType(user.getId(), type);
        return new ResultUtil<List<ResourceAuthBean>>().setData(list);
    }

    @RequestMapping(value = "/resourceAuthByDeviceTypeAndRid", method = RequestMethod.GET)
    @ApiOperation(value = "/根据设备类型，获取用户权限")
    public Result<Map<String,String>> resourceAuthByDeviceTypeAndRid(@RequestParam(name = "type", required = true) String type, @RequestParam(name = "rid") String rid) {
        User user = shiroSecurityUtil.getCurrentUser();
        Map<String,String> map = new HashMap<>();
        List<UserDeviceBean> userDeviceBeans = iUserDeviceService.deviceByUidAndTYpe(user.getId(), type);
        if (userDeviceBeans.size() <= 0) {
            return new ResultUtil<Map<String,String>>().setErrorMsg("用户无该类型设备");
        }

        List<AturhResourceBean> list = iUserResourceService.selectUrByUidAndRid(user.getId(), rid);
        AturhResourceBean aturhResourceBean = new AturhResourceBean();
        if (list.size() > 0) {
            aturhResourceBean = list.get(0);
            Timestamp nowTime = new Timestamp(System.currentTimeMillis());
            map.put("url", aturhResourceBean.getUrl());
            map.put("resourceName", aturhResourceBean.getResourceName());
            map.put("isForever", aturhResourceBean.getIsForever());
            if ("1".equals(aturhResourceBean.getIsForever())) {
                if (nowTime.before(aturhResourceBean.getStartTime()) || nowTime.after(aturhResourceBean.getEndTime())) {
                    return new ResultUtil<Map<String,String>>().setErrorMsg("资源不在有效期，有效期为：【" + aturhResourceBean.getStartTime() + "-" + aturhResourceBean.getEndTime() + "】");
                }
                map.put("startTime", aturhResourceBean.getStartTime()+"");
                map.put("endTime", aturhResourceBean.getEndTime()+"");

            }else {
                map.put("startTime", "");
                map.put("endTime", "");
            }
            return new ResultUtil<Map<String,String>>().setData(map);
        } else {
            return new ResultUtil<Map<String,String>>().setData(map);
        }
    }

    @RequestMapping(value = "/resourceAuthByDeviceMacAndRid", method = RequestMethod.GET)
    @ApiOperation(value = "/根据设备类型，获取用户权限")
    public Result<AturhResourceBean> resourceAuthByDeviceMacAndRid(@RequestParam(name = "deviceMac", required = true) String deviceMac, @RequestParam(name = "rid") String rid) {
        User user = shiroSecurityUtil.getCurrentUser();
        List<AturhResourceBean> list = iUserDeviceService.resourceAuthByDeviceMacAndRid(user.getId(), deviceMac, rid);
        AturhResourceBean aturhResourceBean = new AturhResourceBean();
        if (list.size() > 0) {
            aturhResourceBean = list.get(0);
            Timestamp nowTime = new Timestamp(System.currentTimeMillis());
            if ("1".equals(aturhResourceBean.getIsForever())) {
                if (nowTime.before(aturhResourceBean.getStartTime()) || nowTime.after(aturhResourceBean.getEndTime())) {
                    return new ResultUtil<AturhResourceBean>().setErrorMsg("资源不在有效期，有效期为：【" + aturhResourceBean.getStartTime() + "-" + aturhResourceBean.getEndTime() + "】");
                }
            }
            return new ResultUtil<AturhResourceBean>().setData(aturhResourceBean);
        } else {
            return new ResultUtil<AturhResourceBean>().setData(aturhResourceBean);
        }
    }
}
