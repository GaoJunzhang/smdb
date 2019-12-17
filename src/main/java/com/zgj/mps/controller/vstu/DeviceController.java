package com.zgj.mps.controller.vstu;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DeviceBean;
import com.zgj.mps.model.Device;
import com.zgj.mps.model.DeviceType;
import com.zgj.mps.model.User;
import com.zgj.mps.model.UserDevice;
import com.zgj.mps.service.IDeviceService;
import com.zgj.mps.service.IDeviceTypeService;
import com.zgj.mps.service.IUserDeviceService;
import com.zgj.mps.service.UserService;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.Result;
import com.zgj.mps.vo.SearchVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "设备管理接口")
@RequestMapping("/device")
@Transactional
public class DeviceController {

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IDeviceTypeService iDeviceTypeService;

    @Autowired
    private IUserDeviceService iUserDeviceService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Device> get(@PathVariable String id) {

        Device device = iDeviceService.getById(id);
        return new ResultUtil<Device>().setData(device);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Device>> getAll() {

        List<Device> list = iDeviceService.list();
        return new ResultUtil<List<Device>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<Map> getByPage(@ModelAttribute PageVo pageVo,
                                 @ModelAttribute Device device,
                                 @ModelAttribute SearchVo searchVo, String typeId) {

        if (!StringUtils.isEmpty(typeId)) {
            device.setDeviceTypeId(typeId);
        }
        IPage<Device> data = iDeviceService.selectPage(device, pageVo, searchVo);
        Map<String, Object> map = new HashMap<>();
        List<DeviceBean> deviceBeans = new ArrayList<>(data.getRecords().size());
        List<DeviceType> deviceTypes = iDeviceTypeService.list();
        Map<String, String> map1 = new HashMap<>();
        for (DeviceType deviceType : deviceTypes) {
            map1.put(deviceType.getId(), deviceType.getName());
        }
        for (Device device1 : data.getRecords()) {
            DeviceBean deviceBean = new DeviceBean();
            deviceBean.inject(device1);
            deviceBean.setDeviceTypeName(map1.get(device1.getDeviceTypeId()));
            UserDevice userDevice = new UserDevice();
            userDevice.setDeviceId(device1.getId());
            List<UserDevice> userDevices = iUserDeviceService.findByUd(userDevice);
            if (userDevices.size()>0){
                User user = userService.get(userDevices.get(0).getUserId());
                deviceBean.setDeviceBy(user.getName());
            }
            deviceBeans.add(deviceBean);
        }
        map.put("data", deviceBeans);
        map.put("pageNo", pageVo.getPageNo());
        map.put("totalCount", data.getTotal());
        return new ResultUtil<Map>().setData(map);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Device> saveOrUpdate(@ModelAttribute Device device) {

        if (device.getCreateTime() == null) {
            device.setCreateTime(new Timestamp(System.currentTimeMillis()));
            device.setEnable((short) 0);
        }
        if (iDeviceService.saveOrUpdate(device)) {
            return new ResultUtil<Device>().setData(device);
        }
        return new ResultUtil<Device>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iDeviceService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/getVaildDevicePage",method = RequestMethod.GET)
    @ApiOperation(value = "获取未分配设备")
    public Object getVaildDevicePage(@ModelAttribute PageVo pageVo, String deviceName,String deviceMac) {

        Page<DeviceBean> page = iDeviceService.getVaildDevicePage(pageVo.getPageNo(), pageVo.getPageSize(), deviceName,deviceMac);
        Map<String, Object> map = new HashMap<>();
        map.put("data", page.getRecords());
        map.put("pageNo", page.getPages());
        map.put("totalCount", page.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @GetMapping(value = "/getMonthDevice")
    @ApiOperation(value = "获取月设备数量", tags = "获取没月设备数量")
    public Result<Map> getMonthDevice(){
        return new ResultUtil<Map>().setData(iDeviceService.getMonthDevice(DateUtil.year(new Date())));
    }

    @GetMapping(value = "/sumDevices")
    @ApiOperation(value = "设备总数", tags = "设备总数")
    public Result<Integer> sumDevices(){
        return new ResultUtil<Integer>().setData(iDeviceService.sumDevices());
    }
}
