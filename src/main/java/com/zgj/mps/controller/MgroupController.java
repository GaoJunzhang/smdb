package com.zgj.mps.controller;

import com.zgj.mps.controller.request.SaveMgroupRequest;
import com.zgj.mps.generator.base.BaseController;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.model.Mgroup;
import com.zgj.mps.service.MgroupService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

/**
 * @author Wangj
 */
@Slf4j
@RestController
@Api(description = "媒体组管理接口")
@RequestMapping("/mgroup")
@Transactional
public class MgroupController extends BaseController<Mgroup, Long> {

    @Autowired
    private MgroupService mgroupService;

    @Override
    public MgroupService getService() {
        return mgroupService;
    }

    @RequestMapping(value = "/mgroupListData", method = RequestMethod.GET)
    public Object mgroupListData(HttpSession session, Long id) {
        long userId = (long) session.getAttribute("userId");
        return new ResultUtil<>().setData(mgroupService.mgroupListData(userId, id));
    }

    @RequestMapping(value = "/mgroupByName", method = RequestMethod.GET)
    public Object mgroupByName(HttpSession session, String name, Integer page) {
        long userId = (long) session.getAttribute("userId");
        return new ResultUtil<>().setData(mgroupService.mgroupByName(userId, name, page));
    }

    @RequestMapping(value = "/saveMgroup", method = RequestMethod.POST)
    public Object saveMgroup(HttpSession session, @RequestBody SaveMgroupRequest saveMgroupRequest) {
        long userId = (long) session.getAttribute("userId");
        return mgroupService.saveMgroup(userId, saveMgroupRequest);
    }

    @RequestMapping(value = "/delMgroup/{mgroupId}", method = RequestMethod.DELETE)
    public Object saveMgroup(HttpSession session, @PathVariable Long mgroupId) {
        long userId = (long) session.getAttribute("userId");
        return mgroupService.delMgroup(userId, mgroupId);
    }

    @RequestMapping(value = "/getAuhtSelectData", method = RequestMethod.GET)
    public Object getAuhtSelectData(HttpSession session){
        long userId = (long) session.getAttribute("userId");
        return new ResultUtil<>().setData(mgroupService.findMgroupTree(userId));
    }
}
