package com.zgj.mps.controller.vstu;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DownloadBean;
import com.zgj.mps.bean.DownloadTopBean;
import com.zgj.mps.bean.FollowBean;
import com.zgj.mps.controller.request.DownloadRequest;
import com.zgj.mps.model.*;
import com.zgj.mps.service.FollowService;
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

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by user on 2019/12/4.
 */
@Slf4j
@RestController
@Api(description = "关注管理接口")
@RequestMapping("/follow")
@Transactional
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @Autowired
    private IUserResourceService iUserResourceService;

    @Autowired
    private IResourceService iResourceService;

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo pageVo,
                            @ModelAttribute SearchVo searchVo,
                            @ModelAttribute FollowBean followBean) {

        Page<FollowBean> followBeanPage = followService.pageFollow(followBean, pageVo, searchVo);
        Map<String, Object> map = new HashMap<>();
        map.put("data", followBeanPage.getRecords());
        map.put("pageNo", followBeanPage.getPages());
        map.put("totalCount", followBeanPage.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @PostMapping(value = "/addFollow")
    @ApiOperation(value = "修改用户关注资源列表")
    public Object addFollow(@RequestBody DownloadRequest downloadRequest){
        JSONArray jsonArray = JSONUtil.parseArray(downloadRequest.getParams());
        User user = shiroSecurityUtil.getCurrentUser();
        if (jsonArray.size()<=0){
            return new ResultUtil<>().setErrorMsg("资源list不可用");
        }
        List<Follow> follows = new ArrayList<>();
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
                list.add(map);
                continue;
            }
            if (userResources.get(0).getIsForever() == 1) {
                Timestamp nowTime = new Timestamp(System.currentTimeMillis());
                if (nowTime.before(userResources.get(0).getStartTime()) || nowTime.after(userResources.get(0).getEndTime())) {
                    map.put("msg","资源不在有效期，有效期为：【" + userResources.get(0).getStartTime() + "-" + userResources.get(0).getEndTime() + "】");
                    list.add(map);
                    continue;
                }
            }
            List<Follow> followList = followService.findByUserIdAndRid(user.getId(),rid);
            Resource resource = iResourceService.getById(rid);
            map.put("resourceName", resource.getName());
            map.put("url", resource.getUrl());
            if (followList.size()>0){
                if ("1".equals(job.get("follow"))){
                    followService.removeById(followList.get(0).getId());
                    map.put("msg","取消关注成功");
                    list.add(map);
                    continue;
                }else {
                    map.put("msg","已关注");
                    list.add(map);
                    continue;
                }
            }
            map.put("msg", "关注成功");
            Follow follow= new Follow();
            follow.setResourceId(rid);
            follow.setCreateTime(new Timestamp(System.currentTimeMillis()));
            follow.setUserId(user.getId());
            follows.add(follow);
            list.add(map);
        }
        if (followService.saveBatch(follows)){
            return new ResultUtil<>().setData(list);
        }
        return new ResultUtil<>().setErrorMsg("请求失败");
    }

    @GetMapping(value = "/getFollows")
    @ApiOperation(value = "获取用户要关注资源")
    public Object getFollows(){
        User user = shiroSecurityUtil.getCurrentUser();
        return new ResultUtil<>().setData(followService.userFollowList(user.getId()));
    }

    @GetMapping(value = "/top10")
    @ApiOperation(value = "下载排行", tags = "下载排行")
    public Result<List<DownloadTopBean>> top10(){
        return new ResultUtil<List<DownloadTopBean>>().setData(followService.downloadTop10());
    }

    @GetMapping(value = "/getMonthDownload")
    @ApiOperation(value = "月下载量", tags = "月下载量")
    public Result<Map> getMonthDownload(){
        return new ResultUtil<Map>().setData(followService.getMonthDownload(DateUtil.year(new Date())));
    }
}
