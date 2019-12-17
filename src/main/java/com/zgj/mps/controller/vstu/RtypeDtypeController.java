package com.zgj.mps.controller.vstu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zgj.mps.model.RtypeDtype;
import com.zgj.mps.model.User;
import com.zgj.mps.service.IRtypeDtypeService;
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
@Api(description = "资源类型管理接口")
@RequestMapping("/rtypeDtype")
@Transactional
public class RtypeDtypeController {

    @Autowired
    private IRtypeDtypeService iRtypeDtypeService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<RtypeDtype> get(@PathVariable String id) {

        RtypeDtype rtypeDtype = iRtypeDtypeService.getById(id);
        return new ResultUtil<RtypeDtype>().setData(rtypeDtype);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<RtypeDtype>> getAll() {

        List<RtypeDtype> list = iRtypeDtypeService.list();
        return new ResultUtil<List<RtypeDtype>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo page) {

        IPage<RtypeDtype> data = iRtypeDtypeService.page(PageUtil.initMpPage(page));
        Map<String, Object> map = new HashMap<>();
        map.put("data", data.getRecords());
        map.put("pageNo", data.getPages());
        map.put("totalCount", data.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<RtypeDtype> saveOrUpdate(@ModelAttribute RtypeDtype rtypeDtype) {

        if (iRtypeDtypeService.saveOrUpdate(rtypeDtype)) {
            return new ResultUtil<RtypeDtype>().setData(rtypeDtype);
        }
        return new ResultUtil<RtypeDtype>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iRtypeDtypeService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/batchSave", method = RequestMethod.POST)
    @ApiOperation(value = "批量插入数据")
    public Result<String> batchSave(@RequestParam(name = "dtIds") String[] dtIds, @RequestParam(name = "rtIds") String[] rtIds) {
        User user = shiroSecurityUtil.getCurrentUser();
        List<RtypeDtype> list = new ArrayList<>(dtIds.length * rtIds.length);
        for (int i = 0; i < dtIds.length; i++) {
            for (int j = 0; j < rtIds.length; j++) {
                RtypeDtype rtypeDtype = new RtypeDtype();
                rtypeDtype.setDtId(dtIds[i]);
                rtypeDtype.setRtId(rtIds[j]);
                rtypeDtype.setCreateBy(user.getId());
                rtypeDtype.setCreateTime(new Timestamp(System.currentTimeMillis()));
                rtypeDtype.setIsDelete((short) 0);
                list.add(rtypeDtype);
            }
        }
        if (iRtypeDtypeService.saveBatch(list)) {
            return new ResultUtil<String>().setSuccessMsg("操作成功");
        }
        return new ResultUtil<String>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/deleteBydtIdAndrtId", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除设备类型和资源类型")
    public Result<String> deleteBydtIdAndrtId(@RequestParam(name = "dtId", required = true) String dtId,
                                              @RequestParam(name = "rtId", required = true) String rtId) {
        if (iRtypeDtypeService.deleteBydtIdAndrtId(rtId, dtId) == 1) {

            return new ResultUtil<String>().setSuccessMsg("操作成功");
        }
        return new ResultUtil<String>().setErrorMsg("删除失败");
    }
}
