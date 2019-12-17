package com.zgj.mps.controller.vstu;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zgj.mps.bean.UserDResourceBean;
import com.zgj.mps.model.Resource;
import com.zgj.mps.service.IResourceService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "资源管理接口")
@RequestMapping("/resource")
@Transactional
public class ResourceController {

    @Autowired
    private IResourceService iResourceService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Resource> get(@PathVariable String id) {

        Resource resource = iResourceService.getById(id);
        return new ResultUtil<Resource>().setData(resource);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Resource>> getAll() {

        List<Resource> list = iResourceService.list();
        return new ResultUtil<List<Resource>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo pageVo,
                                             @ModelAttribute SearchVo searchVo,
                                             @ModelAttribute Resource resource) {

        IPage<Resource> page = iResourceService.selectPage(resource, pageVo, searchVo);
        Map<String, Object> map = new HashMap<>();
        map.put("data", page.getRecords());
        map.put("pageNo", page.getPages());
        map.put("totalCount", page.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Resource> saveOrUpdate(@ModelAttribute Resource resource) {
        if (resource.getCreateTime() == null) {
            resource.setCreateTime(new Timestamp(System.currentTimeMillis()));
            resource.setIsDelete((short) 0);
        }
        if (iResourceService.saveOrUpdate(resource)) {
            return new ResultUtil<Resource>().setData(resource);
        }
        return new ResultUtil<Resource>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iResourceService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/reourceListByUid/{uid}",method = RequestMethod.GET)
    @ApiOperation(value = "/通过用户id获取用户资源")
    public Result<List<UserDResourceBean>> reourceListByUid(@PathVariable String uid){
        List<UserDResourceBean> list = iResourceService.reourceListByUid(uid);
        return new ResultUtil<List<UserDResourceBean>>().setData(list);
    }

    @GetMapping(value = "/getMonthResource")
    @ApiOperation(value = "获取月资源数量", tags = "获取没月资源数量")
    public Result<Map> getMonthResource(){
        return new ResultUtil<Map>().setData(iResourceService.monthResource(DateUtil.year(new Date())));
    }

    @GetMapping(value = "/sumResource")
    @ApiOperation(value = "资源总数", tags = "资源总数")
    public Result<Integer> sumResource(){
        return new ResultUtil<Integer>().setData(iResourceService.sumResource());
    }

}
