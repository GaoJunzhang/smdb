package com.zgj.mps.controller.vstu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.OrderResourceBean;
import com.zgj.mps.bean.UserResourceDtypeBean;
import com.zgj.mps.model.DeviceType;
import com.zgj.mps.model.ResourceOrder;
import com.zgj.mps.model.RtypeDtype;
import com.zgj.mps.model.User;
import com.zgj.mps.service.IDeviceTypeService;
import com.zgj.mps.service.IResourceOrderService;
import com.zgj.mps.service.IRtypeDtypeService;
import com.zgj.mps.service.IUserResourceService;
import com.zgj.mps.tool.DateUtil;
import com.zgj.mps.tool.PageUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "资源标签管理接口")
@RequestMapping("/order")
@Transactional
public class ResourceOrderController {

    @Autowired
    private IResourceOrderService iResourceOrderService;

    @Autowired
    private IDeviceTypeService iDeviceTypeService;

    @Autowired
    private IRtypeDtypeService iRtypeDtypeService;

    @Autowired
    private IUserResourceService iUserResourceService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ResourceOrder> get(@PathVariable String id) {

        ResourceOrder resourceOrder = iResourceOrderService.getById(id);
        return new ResultUtil<ResourceOrder>().setData(resourceOrder);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ResourceOrder>> getAll() {

        List<ResourceOrder> list = iResourceOrderService.list();
        return new ResultUtil<List<ResourceOrder>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo page) {

        IPage<ResourceOrder> data = iResourceOrderService.page(PageUtil.initMpPage(page));
        Map<String, Object> map = new HashMap<>();
        map.put("data", data.getRecords());
        map.put("pageNo", data.getPages());
        map.put("totalCount", data.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ResourceOrder> saveOrUpdate(@ModelAttribute ResourceOrder resourceOrder) {

        if (iResourceOrderService.saveOrUpdate(resourceOrder)) {
            return new ResultUtil<ResourceOrder>().setData(resourceOrder);
        }
        return new ResultUtil<ResourceOrder>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iResourceOrderService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<OrderResourceBean>> orders(@ModelAttribute OrderResourceBean orderResourceBean) {

        List<OrderResourceBean> list = iResourceOrderService.orders(orderResourceBean);
        return new ResultUtil<List<OrderResourceBean>>().setData(list);
    }

    @RequestMapping(value = "/selectOrderPage", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<Page<OrderResourceBean>> selectOrderPage(@ModelAttribute PageVo pageVo, @ModelAttribute SearchVo searchVo, @ModelAttribute OrderResourceBean orderResourceBean) {

        Page<OrderResourceBean> page = iResourceOrderService.selectOrderPage(pageVo.getPageNo(), pageVo.getPageSize(), orderResourceBean);
        return new ResultUtil<Page<OrderResourceBean>>().setData(page);
    }

    @RequestMapping(value = "/addOrderTime", method = RequestMethod.POST)
    @ApiOperation(value = "资源加时")
    public Result<String> addOrderTime(@RequestParam(name = "id", required = false) String id, @RequestParam(name = "time", required = true) int time) {
        ResourceOrder resourceOrder = iResourceOrderService.getById(id);
        if (time == 99999) {
            resourceOrder.setIsForever((short) 0);
        } else {
            String newStartTime = "";
            String newEndTime = "";
            if (resourceOrder.getIsForever() == 0) {
                resourceOrder.setStartTime(null);
            }
            resourceOrder.setIsForever((short) 1);
            if (resourceOrder.getStartTime() != null) {

                newStartTime = resourceOrder.getStartTime().toString();
                newEndTime = DateUtil.getAfterMonth(resourceOrder.getEndTime().toString(), time);
            } else {
                newStartTime = DateUtil.getNowDateByFormate("yyyy-MM-dd HH:mm:ss");
                newEndTime = DateUtil.getAfterMonth(newStartTime, time);
            }
            resourceOrder.setStartTime(Timestamp.valueOf(newStartTime));
            resourceOrder.setEndTime(Timestamp.valueOf(newEndTime));
        }
        if (iResourceOrderService.saveOrUpdate(resourceOrder)) {

            return new ResultUtil<String>().setSuccessMsg("加时成功");
        }
        return new ResultUtil<String>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/updateIsforever", method = RequestMethod.POST)
    @ApiOperation(value = "资源加时")
    public Result<String> updateIsforever(@RequestParam(name = "ids", required = false) String[] ids, @RequestParam(name = "isForever", required = true) short isForever) {
        List<ResourceOrder> list = new ArrayList<>(ids.length);
        for (String id : ids) {
            ResourceOrder order = new ResourceOrder();
            order.setId(id);
            order.setIsForever(isForever);
            list.add(order);
        }
        if (iResourceOrderService.updateIsforever(list) == 1) {

            return new ResultUtil<String>().setSuccessMsg("操作成功");
        }
        return new ResultUtil<String>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ApiOperation(value = "资源加时")
    public Result<String> updateStatus(@RequestParam(name = "ids", required = false) String[] ids, @RequestParam(name = "status", required = true) short status) {
        List<ResourceOrder> list = new ArrayList<>(ids.length);
        for (String id : ids) {
            ResourceOrder order = new ResourceOrder();
            order.setId(id);
            order.setStatus(status);
            list.add(order);
        }
        if (iResourceOrderService.updateIsforever(list) == 1) {

            return new ResultUtil<String>().setSuccessMsg("操作成功");
        }
        return new ResultUtil<String>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/getResourceByDeviceType",method = RequestMethod.GET)
    @ApiOperation(value = "/根据设备类型，获取用户权限")
    public Result<List<UserResourceDtypeBean>> getResourceByDeviceType(@RequestParam(name = "deviceType") String deviceType){
        User user = shiroSecurityUtil.getCurrentUser();
        DeviceType deviceTypeObj = iDeviceTypeService.findByName(deviceType);
        if (deviceTypeObj==null){
            return new ResultUtil<List<UserResourceDtypeBean>>().setErrorMsg("无效设备类型");
        }
        List<RtypeDtype> rtypeDtypes = iRtypeDtypeService.findByDtypeName(deviceTypeObj.getId());
        if (rtypeDtypes.size()<=0){
            return new ResultUtil<List<UserResourceDtypeBean>>().setData(null);
        }
        String[] typeIds = new String[rtypeDtypes.size()];
        for (int i=0; i<rtypeDtypes.size();i++){
            typeIds[i] = rtypeDtypes.get(i).getRtId();
        }
        List<UserResourceDtypeBean> list = iUserResourceService.udtData(user.getId(),typeIds);
/*
        OrderResourceBean orderResourceBean = new OrderResourceBean();
        orderResourceBean.setTypeName(deviceType);
        List<OrderResourceBean> list = iResourceOrderService.orders(orderResourceBean);*/
        return new ResultUtil<List<UserResourceDtypeBean>>().setData(list);
    }
}
