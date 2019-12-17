package com.zgj.mps.controller.vstu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zgj.mps.model.*;
import com.zgj.mps.service.*;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.tool.ShiroSecurityUtil;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.Result;
import com.zgj.mps.vo.SearchVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "设备类型管理接口")
@RequestMapping("/deviceType")
@Transactional
public class DeviceTypeController {

    @Autowired
    private IDeviceTypeService iDeviceTypeService;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IResourceOrderService iResourceOrderService;

    @Autowired
    private IResourceService iResourceService;

    @Autowired
    private IUserResourceService iUserResourceService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DeviceType> get(@PathVariable String id) {

        DeviceType deviceType = iDeviceTypeService.getById(id);
        return new ResultUtil<DeviceType>().setData(deviceType);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DeviceType>> getAll() {

        List<DeviceType> list = iDeviceTypeService.list();
        return new ResultUtil<List<DeviceType>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo pageVo,
                                               @ModelAttribute DeviceType deviceType,
                                               @ModelAttribute SearchVo searchVo) {

        IPage<DeviceType> page = iDeviceTypeService.selectPage(deviceType, pageVo, searchVo);
        Map<String, Object> map = new HashMap<>();
        map.put("data", page.getRecords());
        map.put("pageNo", page.getPages());
        map.put("totalCount", page.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/addDeviceType", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DeviceType> addDeviceType(@ModelAttribute DeviceType deviceType) {
        if (deviceType.getCreateTime() == null) {

            deviceType.setCreateTime(new Timestamp(System.currentTimeMillis()));
            deviceType.setIsDelete((short) 0);
        }
        if (iDeviceTypeService.saveOrUpdate(deviceType)) {
            return new ResultUtil<DeviceType>().setData(deviceType);
        }
        return new ResultUtil<DeviceType>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
//            if (iDeviceService.deviceByTypeId(id) != null) {
//                return new ResultUtil<Object>().setErrorMsg("设备类型正在使用，无法删除");
//            }
            iDeviceTypeService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/typeResourceByType", method = RequestMethod.GET)
    @ApiOperation(value = "/根据设备类型，获取资源列表")
    public Result<String> typeResourceByType(@ModelAttribute ResourceOrder order) {
        User user = shiroSecurityUtil.getCurrentUser();
        Device deviceType = iDeviceService.getById(order.getDeviceId());
        if (deviceType == null) {
            return new ResultUtil<String>().setErrorMsg("无效设备Id");
        }
        Resource resource = iResourceService.getById(order.getResourceId());
        if (resource == null) {
            return new ResultUtil<String>().setErrorMsg("无效资源Id");
        }
        List<UserResource> userResources = iUserResourceService.findByUserIdAndResourceId(user.getId(),resource.getId());
        if (userResources.size() <= 0) {

            return new ResultUtil<String>().setErrorMsg("该用户无当前资源权限");
        }
        if (userResources.get(0).getIsForever() == 1) {
            Timestamp nowTime = new Timestamp(System.currentTimeMillis());
            if (nowTime.before(userResources.get(0).getStartTime()) || nowTime.after(userResources.get(0).getEndTime())) {
                return new ResultUtil<String>().setErrorMsg("资源不在有效期，有效期为：【" + userResources.get(0).getStartTime() + "-" + userResources.get(0).getEndTime() + "】");
            }
        }
/*        List<ResourceOrder> resourceOrders = iResourceOrderService.findByOrder(order);
        if (resourceOrders.size() <= 0) {

            return new ResultUtil<String>().setErrorMsg("资源无权限");
        }
        if (resourceOrders.get(0).getIsForever() == 1) {
            Timestamp nowTime = new Timestamp(System.currentTimeMillis());
            if (nowTime.before(resourceOrders.get(0).getStartTime()) || nowTime.after(resourceOrders.get(0).getEndTime())) {
                return new ResultUtil<String>().setErrorMsg("资源不在有效期，有效期为：【" + resourceOrders.get(0).getStartTime() + "-" + resourceOrders.get(0).getEndTime() + "】");
            }
        }*/
        return new ResultUtil<String>().setData(resource.getUrl());
    }
}
