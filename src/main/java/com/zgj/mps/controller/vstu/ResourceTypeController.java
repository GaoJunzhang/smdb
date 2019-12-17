package com.zgj.mps.controller.vstu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zgj.mps.model.ResourceType;
import com.zgj.mps.model.RtypeDtype;
import com.zgj.mps.model.User;
import com.zgj.mps.service.IResourceTypeService;
import com.zgj.mps.service.IRtypeDtypeService;
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
@RequestMapping("/resourceType")
@Transactional
public class ResourceTypeController {

    @Autowired
    private IResourceTypeService iResourceTypeService;

    @Autowired
    private IRtypeDtypeService iRtypeDtypeService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ResourceType> get(@PathVariable String id) {

        ResourceType resourceType = iResourceTypeService.getById(id);
        return new ResultUtil<ResourceType>().setData(resourceType);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ResourceType>> getAll() {

        List<ResourceType> list = iResourceTypeService.list();
        return new ResultUtil<List<ResourceType>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo page,
                            @ModelAttribute SearchVo searchVo,
                            @ModelAttribute ResourceType resourceType) {

        IPage<ResourceType> data = iResourceTypeService.selectPage(resourceType, page, searchVo);
        Map<String, Object> map = new HashMap<>();
        map.put("data", data.getRecords());
        map.put("pageNo", data.getPages());
        map.put("totalCount", data.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ResourceType> saveOrUpdate(String id, String name, String remark) {
        User user = shiroSecurityUtil.getCurrentUser();
        ResourceType resourceType = null;
        if (id == null) {
            List<ResourceType> resourceTypes = iResourceTypeService.findByName(name);
            if (resourceTypes.size() > 0) {
                return new ResultUtil<ResourceType>().setErrorMsg("资源类型名称不可重复");
            }
            resourceType = new ResourceType();
            resourceType.setCreateTime(new Timestamp(System.currentTimeMillis()));
            resourceType.setIsDelete((short) 0);
        } else {
            resourceType = iResourceTypeService.getById(id);
        }
        resourceType.setCreateBy(user.getId());
        resourceType.setName(name);
        resourceType.setRemark(remark);
        if (iResourceTypeService.saveOrUpdate(resourceType)) {
            return new ResultUtil<ResourceType>().setData(resourceType);
        }
        return new ResultUtil<ResourceType>().setErrorMsg("操作失败");

    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iResourceTypeService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/findBydTypeId/{dtid}", method = RequestMethod.GET)
    @ApiOperation(value = "通过设备类型查找资源")
    public Result<List<ResourceType>> findBydTypeId(@PathVariable String dtid) {
        List<RtypeDtype> rlist = iRtypeDtypeService.findByDtypeName(dtid);
        if (rlist.size() <= 0) {
            return new ResultUtil<List<ResourceType>>().setData(new ArrayList<>());
        }
        List<String> ids = new ArrayList<>(rlist.size());
        for (RtypeDtype rtypeDtype : rlist) {
            ids.add(rtypeDtype.getRtId());
        }
        List<ResourceType> resources = iResourceTypeService.findByIds(ids);
        return new ResultUtil<List<ResourceType>>().setData(resources);
    }
}
