package com.zgj.mps.controller.vstu;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DownloadTopBean;
import com.zgj.mps.bean.RateBean;
import com.zgj.mps.bean.RateStasticBean;
import com.zgj.mps.model.Rate;
import com.zgj.mps.model.User;
import com.zgj.mps.service.IRateService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "资源评论管理接口")
@RequestMapping("/rate")
@Transactional
public class RateController {

    @Autowired
    private IRateService iRateService;

    @Autowired
    private ShiroSecurityUtil shiroSecurityUtil;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Rate> get(@PathVariable String id) {

        Rate rate = iRateService.getById(id);
        return new ResultUtil<Rate>().setData(rate);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Rate>> getAll() {

        List<Rate> list = iRateService.list();
        return new ResultUtil<List<Rate>>().setData(list);
    }

    /*   @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
       @ApiOperation(value = "分页获取")
       public Result<IPage<Rate>> getByPage(@ModelAttribute PageVo pageVo,
                                            @ModelAttribute SearchVo searchVo,
                                            @ModelAttribute Rate rate){

           IPage<Rate> data = iRateService.page(PageUtil.initMpPage(pageVo));
           return new ResultUtil<IPage<Rate>>().setData(data);
       }*/
    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Object getByPage(@ModelAttribute PageVo pageVo,
                            @ModelAttribute SearchVo searchVo,
                            @ModelAttribute RateBean rateBean) {

        Page<RateBean> rateBeanPage = iRateService.pageRate(rateBean, pageVo, searchVo);
        Map<String, Object> map = new HashMap<>();
        map.put("data", rateBeanPage.getRecords());
        map.put("pageNo", rateBeanPage.getPages());
        map.put("totalCount", rateBeanPage.getTotal());
        return new ResultUtil<>().setData(map);
    }

    @RequestMapping(value = "/getPageRateStastic", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<Page<RateStasticBean>> getPageRateStastic(@ModelAttribute PageVo pageVo,
                                                            @ModelAttribute SearchVo searchVo,
                                                            @ModelAttribute RateBean rateBean) {

        Page<RateStasticBean> rateBeanPage = iRateService.pageRateStastic(rateBean, pageVo, searchVo);
        return new ResultUtil<Page<RateStasticBean>>().setData(rateBeanPage);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Rate> saveOrUpdate(@ModelAttribute Rate rate) {
        User user = shiroSecurityUtil.getCurrentUser();
        rate.setUserId(user.getId());
        rate.setCreateTime(new Timestamp(System.currentTimeMillis()));
        rate.setIsDelete((short) 0);
        if (iRateService.saveOrUpdate(rate)) {
            return new ResultUtil<Rate>().setSuccessMsg("评论成功");
        }
        return new ResultUtil<Rate>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iRateService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @GetMapping(value = "/getRatesByrid")
    @ApiOperation(value = "获取资源评论")
    public Result<List<Rate>> getRatesByrid(@RequestParam(name = "rid", required = true) String rid) {
        return new ResultUtil<List<Rate>>().setData(iRateService.rateByRid(rid));
    }

    @PostMapping(value = "/markRate")
    @ApiOperation(value = "点赞评论")
    public Object markRate(@RequestParam(name = "rateId",required = true) String rateId){
        Rate rate = iRateService.getById(rateId);
        if (rate == null){
            return new ResultUtil<>().setErrorMsg("评论无效");
        }
        rate.setMark(rate.getMark()+1);
        if (iRateService.saveOrUpdate(rate)){
            return new ResultUtil<>().setSuccessMsg("点赞成功");
        }
        return new ResultUtil<>().setErrorMsg("点赞失败，请稍后再试！");
    }

    @GetMapping(value = "/top10")
    @ApiOperation(value = "下载排行", tags = "下载排行")
    public Result<List<DownloadTopBean>> top10(){
        return new ResultUtil<List<DownloadTopBean>>().setData(iRateService.downloadTop10());
    }

    @GetMapping(value = "/getMonthDownload")
    @ApiOperation(value = "月下载量", tags = "月下载量")
    public Result<Map> getMonthDownload(){
        return new ResultUtil<Map>().setData(iRateService.getMonthDownload(DateUtil.year(new Date())));
    }
}
