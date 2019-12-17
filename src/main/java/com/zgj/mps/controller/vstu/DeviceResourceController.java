package com.zgj.mps.controller.vstu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zgj.mps.bean.OrderResourceBean;
import com.zgj.mps.model.Device;
import com.zgj.mps.model.DeviceResource;
import com.zgj.mps.model.ResourceOrder;
import com.zgj.mps.service.IDeviceResourceService;
import com.zgj.mps.service.IDeviceService;
import com.zgj.mps.service.IResourceOrderService;
import com.zgj.mps.tool.PageUtil;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Api(description = "设备资源管理接口")
@RequestMapping("/deviceResource")
@Transactional
public class DeviceResourceController {

    @Autowired
    private IDeviceResourceService iDeviceResourceService;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IResourceOrderService iResourceOrderService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DeviceResource> get(@PathVariable String id){

        DeviceResource deviceResource = iDeviceResourceService.getById(id);
        return new ResultUtil<DeviceResource>().setData(deviceResource);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DeviceResource>> getAll(){

        List<DeviceResource> list = iDeviceResourceService.list();
        return new ResultUtil<List<DeviceResource>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo page){

        IPage<DeviceResource> data = iDeviceResourceService.page(PageUtil.initMpPage(page));
        Map<String, Object> map = new HashMap<>();
        map.put("data", data.getRecords());
        map.put("pageNo", data.getPages());
        map.put("totalCount", data.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/getDeviceResourceByDid",method = RequestMethod.GET)
    @ApiOperation(value = "/通过用户id获取用户设备")
    public Result<List<OrderResourceBean>> getDeviceResourceByDid(@ModelAttribute OrderResourceBean orderResourceBean){

        List<OrderResourceBean> list = iResourceOrderService.orders(orderResourceBean);
        return new ResultUtil<List<OrderResourceBean>>().setData(list);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DeviceResource> saveOrUpdate(Short isForever,String startDate, String endDate,@RequestParam(name = "rIds", required = true) String[] rIds,
                                               @RequestParam(name = "dIds", required = true) String[] dIds){

        List<String> ids = new ArrayList<String>(Arrays.asList(dIds));
        List<Device> devices = iDeviceService.devicesByids(ids);
        List<DeviceResource> list = new ArrayList<>(dIds.length*rIds.length);
        List<ResourceOrder> resourceOrders = new ArrayList<>(dIds.length*rIds.length);
        if (devices.size()<=0){
            return new ResultUtil<DeviceResource>().setErrorMsg("设备信息异常");
        }
        for (Device device : devices){
            for (int j = 0; j < rIds.length; j++) {
                DeviceResource deviceResource = new DeviceResource();
                deviceResource.setDeviceId(device.getId());
                deviceResource.setResourceId(rIds[j]);
                list.add(deviceResource);

                ResourceOrder resourceOrder = new ResourceOrder();
                resourceOrder.setDeviceId(device.getId());
                resourceOrder.setResourceId(rIds[j]);
                resourceOrder.setDeviceMac(device.getDeviceMac());
                resourceOrder.setTypeId(device.getDeviceTypeId());
                resourceOrder.setIsForever(isForever);
                resourceOrder.setIsDelete((short)0);
                if (isForever != 0) {
                    resourceOrder.setStartTime(Timestamp.valueOf(startDate));
                    resourceOrder.setEndTime(Timestamp.valueOf(endDate));
                }
                resourceOrders.add(resourceOrder);
            }
        }
        if (iDeviceResourceService.batchSave(list)==1){
            iResourceOrderService.saveBatch(resourceOrders);
            return new ResultUtil<DeviceResource>().setSuccessMsg("添加成功");
        }
        return new ResultUtil<DeviceResource>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids){

        for(String id : ids){
            iResourceOrderService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }
}
