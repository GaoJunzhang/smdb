package com.zgj.mps.controller.vstu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zgj.mps.bean.UserAuthBean;
import com.zgj.mps.bean.UserResourceBean;
import com.zgj.mps.model.UserResource;
import com.zgj.mps.service.IUserResourceService;
import com.zgj.mps.tool.DateUtil;
import com.zgj.mps.tool.PageUtil;
import com.zgj.mps.tool.ResultUtil;
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
@Api(description = "设备资源管理接口")
@RequestMapping("/userResource")
@Transactional
public class UserResourceController {

    @Autowired
    private IUserResourceService iUserResourceService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<UserResource> get(@PathVariable String id) {

        UserResource userResource = iUserResourceService.getById(id);
        return new ResultUtil<UserResource>().setData(userResource);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<UserResource>> getAll() {

        List<UserResource> list = iUserResourceService.list();
        return new ResultUtil<List<UserResource>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo page) {

        IPage<UserResource> data = iUserResourceService.page(PageUtil.initMpPage(page));
        Map<String, Object> map = new HashMap<>();
        map.put("data", data.getRecords());
        map.put("pageNo", data.getPages());
        map.put("totalCount", data.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/getUserResourceByUid/{uid}", method = RequestMethod.GET)
    @ApiOperation(value = "/通过用户id获取用户资源")
    public Result<List<UserResourceBean>> getUserResourceByUid(@PathVariable Long uid) {
        List<UserResourceBean> list = iUserResourceService.userResourceByUid(uid);
        return new ResultUtil<List<UserResourceBean>>().setData(list);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<UserResource> edit(@ModelAttribute UserResource userResource) {
        if (iUserResourceService.saveOrUpdate(userResource)) {
            return new ResultUtil<UserResource>().setSuccessMsg("操作成功");
        }
        return new ResultUtil<UserResource>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<UserResource> saveOrUpdate(@RequestParam(name = "isForever", defaultValue = "0") Short isForever,
                                             @RequestParam(name = "uIds", required = true) String[] uIds,
                                             @RequestParam(name = "rIds", required = true) String[] rIds,
                                             String startDate, String endDate) {

        List<UserResource> list = new ArrayList<>(uIds.length * rIds.length);
        for (int i = 0; i < uIds.length; i++) {
            for (int j = 0; j < rIds.length; j++) {
                UserResource userResource = new UserResource();
                userResource.setIsForever(isForever);
                userResource.setCreateTime(new Timestamp(System.currentTimeMillis()));
                if (isForever != 0) {

                    userResource.setStartTime(Timestamp.valueOf(startDate));
                    userResource.setEndTime(Timestamp.valueOf(endDate));
                }
                userResource.setUserId(Long.parseLong(uIds[i]));
                userResource.setResourceId(rIds[j]);
                list.add(userResource);
            }
        }
        System.out.println(list.size());
        if (iUserResourceService.batchSave(list) == 1) {
            return new ResultUtil<UserResource>().setSuccessMsg("添加成功");
        }
        return new ResultUtil<UserResource>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iUserResourceService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/userAuthData", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object userAuthData(@ModelAttribute PageVo pageVo,
                                                   @ModelAttribute SearchVo searchVo,
                                                   String nickName, Short isForever) {

        IPage<UserAuthBean> data = iUserResourceService.userAuthData(pageVo, searchVo, nickName, isForever);
        Map<String, Object> map = new HashMap<>();
        map.put("data", data.getRecords());
        map.put("pageNo", data.getPages());
        map.put("totalCount", data.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/updateUrIsforever", method = RequestMethod.POST)
    @ApiOperation(value = "资源加时")
    public Result<String> updateUrIsforever(@RequestParam(name = "isForever", required = true) short isForever, @RequestParam(name = "userIds", required = true) String[] userIds) {
        for (String userId : userIds) {
            iUserResourceService.updateUrIsforever(isForever, Long.parseLong(userId));
        }
        return new ResultUtil<String>().setSuccessMsg("操作成功");
    }

    @RequestMapping(value = "/updateUrStatus", method = RequestMethod.POST)
    @ApiOperation(value = "资源加时")
    public Result<String> updateUrStatus(@RequestParam(name = "status", required = true) short status, @RequestParam(name = "userIds", required = true) String[] userIds) {
        for (String userId : userIds) {
            iUserResourceService.updateUrStatus(status, Long.parseLong(userId));
        }
        return new ResultUtil<String>().setSuccessMsg("操作成功");
    }

    @RequestMapping(value = "/addUrTime", method = RequestMethod.POST)
    @ApiOperation(value = "资源加时")
    public Result<String> addUrTime(@RequestParam(name = "userId", required = false) Long userId, @RequestParam(name = "time", required = true) int time) {
        List<UserResource> userResources = iUserResourceService.userResourceByUserId(userId);
        if (userResources.size()<=0){
            return new ResultUtil<String>().setErrorMsg("用户数据异常");
        }
        for (UserResource userResource:userResources){
            if (time == 99999){
                userResource.setIsForever((short)0);
            }else {
                String newStartTime = "";
                String newEndTime = "";
                if (userResource.getIsForever() == 0) {
                    userResource.setStartTime(null);
                }
                userResource.setIsForever((short) 1);
                if (userResource.getStartTime() != null) {

                    newStartTime = userResource.getStartTime().toString();
                    newEndTime = DateUtil.getAfterMonth(userResource.getEndTime().toString(), time);
                } else {
                    newStartTime = DateUtil.getNowDateByFormate("yyyy-MM-dd HH:mm:ss");
                    newEndTime = DateUtil.getAfterMonth(newStartTime, time);
                }
                userResource.setStartTime(Timestamp.valueOf(newStartTime));
                userResource.setEndTime(Timestamp.valueOf(newEndTime));
            }
        }
        if (iUserResourceService.saveOrUpdateBatch(userResources)){
            return new ResultUtil<String>().setSuccessMsg("操作成功");
        }
        return new ResultUtil<String>().setErrorMsg("操作失败");
    }
}
