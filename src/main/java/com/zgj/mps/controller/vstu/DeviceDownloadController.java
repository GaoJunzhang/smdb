package com.zgj.mps.controller.vstu;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DevicePageBean;
import com.zgj.mps.bean.DownloadBean;
import com.zgj.mps.controller.request.DownloadRequest;
import com.zgj.mps.model.DeviceDownload;
import com.zgj.mps.model.Resource;
import com.zgj.mps.model.User;
import com.zgj.mps.model.UserResource;
import com.zgj.mps.service.IDeviceDownloadService;
import com.zgj.mps.service.IResourceService;
import com.zgj.mps.service.IUserResourceService;
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
@Api(description = "资源类型管理接口")
@RequestMapping("/deviceDownload")
@Transactional
public class DeviceDownloadController {

    @Autowired
    private IDeviceDownloadService iDeviceDownloadService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @Autowired
    private IUserResourceService iUserResourceService;

    @Autowired
    private IResourceService iResourceService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DeviceDownload> get(@PathVariable String id){

        DeviceDownload deviceDownload = iDeviceDownloadService.getById(id);
        return new ResultUtil<DeviceDownload>().setData(deviceDownload);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DeviceDownload>> getAll(){

        List<DeviceDownload> list = iDeviceDownloadService.list();
        return new ResultUtil<List<DeviceDownload>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo pageVo,
                            @ModelAttribute SearchVo searchVo,
                            @ModelAttribute DevicePageBean devicePageBean){

        Page<DevicePageBean> devicePageBeanPage = iDeviceDownloadService.pageDownload(devicePageBean, pageVo, searchVo);
        Map<String, Object> map = new HashMap<>();
        map.put("data", devicePageBeanPage.getRecords());
        map.put("pageNo", devicePageBeanPage.getPages());
        map.put("totalCount", devicePageBeanPage.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DeviceDownload> saveOrUpdate(@ModelAttribute DeviceDownload deviceDownload){

        if(iDeviceDownloadService.saveOrUpdate(deviceDownload)){
            return new ResultUtil<DeviceDownload>().setData(deviceDownload);
        }
        return new ResultUtil<DeviceDownload>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids){

        for(String id : ids){
            iDeviceDownloadService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @PostMapping(value = "/editDownLoadResource")
    @ApiOperation(value = "修改用户要下载的资源列表")
    public Object editDownLoadResource(@RequestBody DownloadRequest downloadRequest){
        JSONArray jsonArray = JSONUtil.parseArray(downloadRequest.getParams());
        User user = shiroSecurityUtil.getCurrentUser();
        if (jsonArray.size()<=0){
            return new ResultUtil<>().setErrorMsg("资源list不可用");
        }
        List<DeviceDownload> deviceDownloads = new ArrayList<>();
        List<Map<String,String>> list = new ArrayList<>();
        for (int i=0;i<jsonArray.size();i++){
            JSONObject job = jsonArray.getJSONObject(i);
            String rid = job.get("rid")+"";
            if (StrUtil.isEmpty(rid)){
                continue;
            }
            Map<String,String> map = new HashMap<>();
            List<UserResource> userResources = iUserResourceService.findByUserIdAndResourceId(user.getId(),rid);
            map.put("rid", rid);

            if (userResources.size()<=0){
                map.put("msg","资源无效");
                map.put("code","1");
                list.add(map);
                continue;
            }
            if (userResources.get(0).getIsForever() == 1) {
                Timestamp nowTime = new Timestamp(System.currentTimeMillis());
                if (nowTime.before(userResources.get(0).getStartTime()) || nowTime.after(userResources.get(0).getEndTime())) {
                    map.put("msg","资源不在有效期，有效期为：【" + userResources.get(0).getStartTime() + "-" + userResources.get(0).getEndTime() + "】");
                    map.put("code","1");
                    list.add(map);
                    continue;
                }
            }
            if (!"0".equals(job.get("downLoad"))){
                continue;
            }
            Resource resource = iResourceService.getById(rid);
            map.put("resourceName", resource.getName());
            map.put("url", resource.getUrl());
            map.put("msg", "正在下载");
            map.put("code","0");
            DeviceDownload deviceDownload = new DeviceDownload();
            deviceDownload.setResourceId(rid);
            deviceDownload.setIsDelete((short)0);
            deviceDownload.setCreateTime(new Timestamp(System.currentTimeMillis()));
            deviceDownload.setUserId(user.getId());
            deviceDownload.setStatus((short)0);
            deviceDownloads.add(deviceDownload);
            list.add(map);
        }
        if (iDeviceDownloadService.saveBatch(deviceDownloads)){
            return new ResultUtil<>().setData(list);
        }
        return new ResultUtil<>().setErrorMsg("请求失败");
    }

    @GetMapping(value = "/getDownLoads")
    @ApiOperation(value = "记录设备下载状态")
    public Object getDownLoads(){
        User user = shiroSecurityUtil.getCurrentUser();
        return new ResultUtil<>().setData(iDeviceDownloadService.devicerDownList(user.getId(),null));
    }

    @PostMapping(value = "/editDownloadResource")
    @ApiOperation(value = "记录设备下载状态")
    public Object editDownloadResource(@RequestBody DownloadRequest downloadRequest){
        JSONArray jsonArray = JSONUtil.parseArray(downloadRequest.getParams());
        String deviceMac = downloadRequest.getDeviceMac();
        if (StrUtil.isEmpty(deviceMac)){
            return new ResultUtil<>().setErrorMsg("设备号码不能为空");
        }
        User user = shiroSecurityUtil.getCurrentUser();
        if (jsonArray.size()<=0){
            return new ResultUtil<>().setErrorMsg("资源list不可用");
        }
        List<DeviceDownload> deviceDownloads = new ArrayList<>();
        List<Map<String,String>> list = new ArrayList<>();
        for (int i=0;i<jsonArray.size();i++){
            JSONObject job = jsonArray.getJSONObject(i);
            String rid = job.get("rid")+"";
            if (StrUtil.isEmpty(rid)){
                continue;
            }
            Map<String,String> map = new HashMap<>();
            List<UserResource> userResources = iUserResourceService.findByUserIdAndResourceId(user.getId(),rid);
            map.put("rid", rid);

            if (userResources.size()<=0){
                map.put("msg","资源无效");
                map.put("code","1");
                list.add(map);
                continue;
            }
            if (userResources.get(0).getIsForever() == 1) {
                Timestamp nowTime = new Timestamp(System.currentTimeMillis());
                if (nowTime.before(userResources.get(0).getStartTime()) || nowTime.after(userResources.get(0).getEndTime())) {
                    map.put("msg","资源不在有效期，有效期为：【" + userResources.get(0).getStartTime() + "-" + userResources.get(0).getEndTime() + "】");
                    map.put("code","1");
                    list.add(map);
                    continue;
                }
            }
            Resource resource = iResourceService.getById(rid);
            map.put("resourceName", resource.getName());
            map.put("url", resource.getUrl());
            map.put("msg", "正在下载");
            map.put("code","0");
            map.put("progress",job.get("progress")+"");
            map.put("deviceMac",deviceMac);
            DeviceDownload deviceDownload = new DeviceDownload();
            deviceDownload.setResourceId(rid);
            deviceDownload.setIsDelete((short)0);
            deviceDownload.setCreateTime(new Timestamp(System.currentTimeMillis()));
            deviceDownload.setUserId(user.getId());
            deviceDownload.setProgress(job.get("progress")+"");
            deviceDownload.setStatus((short)1);
            deviceDownload.setDeviceMac(deviceMac);
            deviceDownloads.add(deviceDownload);
            list.add(map);
        }
        if (iDeviceDownloadService.saveBatch(deviceDownloads)){
            return new ResultUtil<>().setData(list);
        }
        return new ResultUtil<>().setErrorMsg("请求失败");
    }

    @GetMapping(value = "/getDeviceDownload")
    @ApiOperation(value = "查询设备下载状态")
    public Object getDeviceDownload(@RequestParam(name = "deviceMac",required = true) String deviceMac){
        return new ResultUtil<>().setData(iDeviceDownloadService.devicerDownList(null,deviceMac));
    }
}
