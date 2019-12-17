package com.zgj.mps.controller.vstu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zgj.mps.model.ResourceLab;
import com.zgj.mps.model.User;
import com.zgj.mps.service.IResourceLabService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "资源标签管理接口")
@RequestMapping("/resourceLab")
@Transactional
public class ResourceLabController {

    @Autowired
    private IResourceLabService iResourceLabService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ResourceLab> get(@PathVariable String id) {

        ResourceLab resourceLab = iResourceLabService.getById(id);
        return new ResultUtil<ResourceLab>().setData(resourceLab);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ResourceLab>> getAll() {

        List<ResourceLab> list = iResourceLabService.list();
        return new ResultUtil<List<ResourceLab>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo pageVo,
                                                @ModelAttribute ResourceLab resourceLab,
                                                @ModelAttribute SearchVo searchVo) {

        IPage<ResourceLab> page = iResourceLabService.selectPage(resourceLab, pageVo, searchVo);
        Map<String, Object> map = new HashMap<>();
        map.put("data", page.getRecords());
        map.put("pageNo", page.getPages());
        map.put("totalCount", page.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ResourceLab> saveOrUpdate(@ModelAttribute ResourceLab resourceLab) {
        User user = shiroSecurityUtil.getCurrentUser();
        if (resourceLab.getCreateTime() == null) {
            resourceLab.setCreateBy(user.getId());
            resourceLab.setCreateTime(new Timestamp(System.currentTimeMillis()));
            resourceLab.setIsDelete((short) 0);
        }
        if (iResourceLabService.saveOrUpdate(resourceLab)) {
            return new ResultUtil<ResourceLab>().setData(resourceLab);
        }
        return new ResultUtil<ResourceLab>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iResourceLabService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

}
