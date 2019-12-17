package com.zgj.mps.controller.vstu;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zgj.mps.bean.TypeResourceBean;
import com.zgj.mps.model.Device;
import com.zgj.mps.model.ResourceOrder;
import com.zgj.mps.model.TypeResource;
import com.zgj.mps.model.User;
import com.zgj.mps.service.IDeviceService;
import com.zgj.mps.service.IResourceOrderService;
import com.zgj.mps.service.ITypeResourceService;
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
@Api(description = "设备资源管理接口")
@RequestMapping("/typeResource")
@Transactional
public class TypeResourceController {

    @Autowired
    private ITypeResourceService iTypeResourceService;

    @Autowired
    private IResourceOrderService iResourceOrderService;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<TypeResource> get(@PathVariable String id) {

        TypeResource typeResource = iTypeResourceService.getById(id);
        return new ResultUtil<TypeResource>().setData(typeResource);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<TypeResource>> getAll() {

        List<TypeResource> list = iTypeResourceService.list();
        return new ResultUtil<List<TypeResource>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo page) {

        IPage<TypeResource> data = iTypeResourceService.page(PageUtil.initMpPage(page));
        Map<String, Object> map = new HashMap<>();
        map.put("data", data.getRecords());
        map.put("pageNo", data.getPages());
        map.put("totalCount", data.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iResourceOrderService.deleteByTypeResourceId(id);
            iTypeResourceService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<TypeResource> saveOrUpdate(@RequestParam(name = "isForever", defaultValue = "0") Short isForever,
                                             String startDate, String endDate,
                                             @RequestParam(name = "rIds", required = true) String[] rIds,
                                             @RequestParam(name = "tIds", required = true) String[] tIds) {
        User user = shiroSecurityUtil.getCurrentUser();

        List<ResourceOrder> resourceOrders = new ArrayList<ResourceOrder>(tIds.length * rIds.length);
        List<TypeResource> list = new ArrayList<TypeResource>(tIds.length * rIds.length);

        for (int i = 0; i < tIds.length; i++) {
            for (int j = 0; j < rIds.length; j++) {
                TypeResource typeResource = new TypeResource();
                typeResource.setTypeId(tIds[i]);
                typeResource.setResourceId(rIds[j]);
                typeResource.setCreateBy(user.getId());
                typeResource.setCreateTime(new Timestamp(System.currentTimeMillis()));
                typeResource.setIsDelete((short) 0);
                list.add(typeResource);
                List<Device> devices = iDeviceService.deviceByTypeId(tIds[i]);
                for (Device device:devices){

                    ResourceOrder resourceOrder = new ResourceOrder();
                    resourceOrder.setCreateBy(user.getId());
                    resourceOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    if (StrUtil.isNotBlank(startDate)) {

                        resourceOrder.setStartTime(Timestamp.valueOf(startDate));
                    }
                    if (StrUtil.isNotBlank(endDate)) {

                        resourceOrder.setEndTime(Timestamp.valueOf(endDate));
                    }
                    resourceOrder.setTypeResourceId(typeResource.getId());
                    resourceOrder.setDeviceId(device.getId());
                    resourceOrder.setDeviceMac(device.getDeviceMac());
                    resourceOrder.setIsDelete((short) 0);
                    resourceOrder.setIsForever(isForever);
                    resourceOrder.setTypeId(tIds[i]);
                    resourceOrder.setResourceId(rIds[j]);
                    resourceOrder.setStatus((short)0);
                    resourceOrders.add(resourceOrder);
                }
            }
        }
        if (iTypeResourceService.saveBatch(list)) {
            if (iResourceOrderService.saveBatch(resourceOrders)){
                return new ResultUtil<TypeResource>().setSuccessMsg("添加成功");
            }
        }
        return new ResultUtil<TypeResource>().setErrorMsg("操作失败");
    }

    //typeResourceByDid
    @RequestMapping(value = "/typeResourceByDid/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "/通过用户id获取用户设备")
    public Result<List<TypeResourceBean>> typeResourceByDid(@PathVariable String id) {
        List<TypeResourceBean> list = iTypeResourceService.typeResourceByDid(id);
        return new ResultUtil<List<TypeResourceBean>>().setData(list);
    }
}
