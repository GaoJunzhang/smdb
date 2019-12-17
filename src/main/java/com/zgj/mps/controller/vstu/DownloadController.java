package com.zgj.mps.controller.vstu;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DownStatusBean;
import com.zgj.mps.bean.DownloadBean;
import com.zgj.mps.bean.DownloadTopBean;
import com.zgj.mps.controller.request.DownloadRequest;
import com.zgj.mps.model.*;
import com.zgj.mps.service.IDeviceService;
import com.zgj.mps.service.IDownloadService;
import com.zgj.mps.service.IResourceService;
import com.zgj.mps.service.IUserResourceService;
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

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "下载资源管理接口")
@RequestMapping("/download")
@Transactional
public class DownloadController {

    @Autowired
    private IDownloadService iDownloadService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @Autowired
    private IResourceService iResourceService;

    @Autowired
    private IDeviceService iDeviceService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Download> get(@PathVariable String id) {

        Download download = iDownloadService.getById(id);
        return new ResultUtil<Download>().setData(download);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Download>> getAll() {

        List<Download> list = iDownloadService.list();
        return new ResultUtil<List<Download>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo pageVo,
                                                @ModelAttribute SearchVo searchVo,
                                                @ModelAttribute DownloadBean downloadBean) {

        Page<DownloadBean> downloadBeanPage = iDownloadService.pageDownload(downloadBean, pageVo, searchVo);
        Map<String, Object> map = new HashMap<>();
        map.put("data", downloadBeanPage.getRecords());
        map.put("pageNo", downloadBeanPage.getPages());
        map.put("totalCount", downloadBeanPage.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Download> saveOrUpdate(@ModelAttribute Download download) {

        if (download.getCreateTime() == null) {
            download.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (download.getStatus() == 1){
            download.setFinishTime(new Timestamp(System.currentTimeMillis()));
        }
        if (iDownloadService.saveOrUpdate(download)) {
            return new ResultUtil<Download>().setSuccessMsg("成功");
        }
        return new ResultUtil<Download>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iDownloadService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/downProcessByDevictType", method = RequestMethod.GET)
    @ApiOperation(value = "查询下载数据")
    public Result<List<DownStatusBean>> downProcessByDevictType(@RequestParam(value = "type",required = true) String type){
        List<DownStatusBean> list = iDownloadService.downProcessByDevictType(type);
        return new ResultUtil<List<DownStatusBean>>().setData(list);
    }

    @PostMapping(value = "/addDownLoad")
    @ApiOperation(value = "上传资源下载记录")
    public Object addDownLoad(HttpServletRequest request,
                              @RequestParam(name = "rid",required = true) String rid,
                              @RequestParam(name = "deviceMac", required = true) String deviceMac){
        Resource resource = iResourceService.getById(rid);
        if (resource == null){
            return new ResultUtil<>().setErrorMsg("资源无效");
        }
        String ip = getIPAddress(request);
        User user = shiroSecurityUtil.getCurrentUser();
        Download download = new Download();
        download.setResourceId(rid);
        download.setIsDelete((short)0);
        download.setCreateTime(new Timestamp(System.currentTimeMillis()));
        download.setUserId(user.getId());
        download.setDeviceMac(deviceMac);
        download.setIp(ip);
        download.setStatus((short)0);
       if (iDownloadService.save(download)){
           return new ResultUtil<>().setSuccessMsg("提交成功");
       }
        return new ResultUtil<>().setErrorMsg("请求失败");
    }

    @PostMapping(value = "/addResourceUsed")
    @ApiOperation(value = "上传资源使用记录")
    public Object addResourceUsed(HttpServletRequest request,
                                  @RequestParam(name = "rid",required = true) String rid,
                                  @RequestParam(name = "deviceMac", required = true) String deviceMac){
        Resource resource = iResourceService.getById(rid);
        if (resource == null){
            return new ResultUtil<>().setErrorMsg("资源无效");
        }
        String ip = getIPAddress(request);
        User user = shiroSecurityUtil.getCurrentUser();
        Download download = new Download();
        download.setResourceId(rid);
        download.setIsDelete((short)0);
        download.setCreateTime(new Timestamp(System.currentTimeMillis()));
        download.setUserId(user.getId());
        download.setDeviceMac(deviceMac);
        download.setIp(ip);
        download.setStatus((short)1);
        if (iDownloadService.save(download)){
            return new ResultUtil<>().setSuccessMsg("提交成功");
        }
        return new ResultUtil<>().setErrorMsg("请求失败");
    }

    @GetMapping(value = "/getDownLoads")
    @ApiOperation(value = "获取用户要下载的资源")
    public Object getDownLoads(@RequestParam(name = "deviceMac",required = true) String deviceMac){
        User user = shiroSecurityUtil.getCurrentUser();
        return new ResultUtil<>().setData(iDownloadService.userDownList(user.getId(),deviceMac));
    }

    @GetMapping(value = "/top10")
    @ApiOperation(value = "下载排行", tags = "下载排行")
    public Result<List<DownloadTopBean>> top10(){
        return new ResultUtil<List<DownloadTopBean>>().setData(iDownloadService.downloadTop10());
    }

    @GetMapping(value = "/getMonthDownload")
    @ApiOperation(value = "月下载量", tags = "月下载量")
    public Result<Map> getMonthDownload(){
        return new ResultUtil<Map>().setData(iDownloadService.getMonthDownload(DateUtil.year(new Date())));
    }

    private String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }
}
