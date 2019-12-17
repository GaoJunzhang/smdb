package com.zgj.mps.controller.vstu;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DeviceStatusBean;
import com.zgj.mps.bean.FollowBean;
import com.zgj.mps.tool.PageUtil;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.Result;
import com.zgj.mps.model.DeviceStatus;
import com.zgj.mps.service.IDeviceStatusService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "在线设备管理接口")
@RequestMapping("/deviceStatus")
@Transactional
public class DeviceStatusController {

    @Autowired
    private IDeviceStatusService iDeviceStatusService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DeviceStatus> get(@PathVariable String id){

        DeviceStatus deviceStatus = iDeviceStatusService.getById(id);
        return new ResultUtil<DeviceStatus>().setData(deviceStatus);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DeviceStatus>> getAll(){

        List<DeviceStatus> list = iDeviceStatusService.list();
        return new ResultUtil<List<DeviceStatus>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(String deviceName,String deviceMac,@ModelAttribute PageVo pageVo){

        Page<DeviceStatusBean> beanPage = iDeviceStatusService.pageDeviceStatus(deviceName,deviceMac,pageVo);
        Map<String, Object> map = new HashMap<>();
        map.put("data", beanPage.getRecords());
        map.put("pageNo", beanPage.getPages());
        map.put("totalCount", beanPage.getTotal());
        return new ResultUtil<>().setData(map);
    }


    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids){

        for(String id : ids){
            iDeviceStatusService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @Scheduled(fixedRate = 5000)
    public void checkDeviceStatus() {
        log.info("定时校验设备状态");
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, -10);
        List<DeviceStatus> deviceStatuses = iDeviceStatusService.deviceByTime(new Timestamp(nowTime.getTime().getTime()));
        if (deviceStatuses.size()>0){
            log.info("五分钟前的数据，开始删除{}",deviceStatuses.size());
            List<String> ids = new ArrayList<>(deviceStatuses.size());
            for (DeviceStatus deviceStatus: deviceStatuses){
                ids.add(deviceStatus.getId());
            }
            iDeviceStatusService.removeByIds(ids);
        }
    }
}
