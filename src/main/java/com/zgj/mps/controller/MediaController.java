package com.zgj.mps.controller;

import cn.hutool.core.util.StrUtil;
import com.zgj.mps.generator.base.BaseController;
import com.zgj.mps.integration.redis.RedisObjectManager;
import com.zgj.mps.model.Media;
import com.zgj.mps.model.Mgroup;
import com.zgj.mps.service.MediaService;
import com.zgj.mps.tool.OSSClientUtil;
import com.zgj.mps.tool.PageUtil;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "媒体管理接口")
@RequestMapping("/media")
@Transactional
public class MediaController extends BaseController<Media, Long> {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private OSSClientUtil ossClientUtil;

    @Autowired
    private RedisObjectManager redisObjectManager;

    @Override
    public MediaService getService() {
        return mediaService;
    }

    @Value("${video.dir}")
    private String videoDir;

    @RequestMapping(value = "/getMediaData", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取")
    public Object getMediaData(HttpSession session, Long mgroupId, String name, Short audit, Short type, @ModelAttribute PageVo pageVo) {
        long userId = (long) session.getAttribute("userId");
        if (StrUtil.isEmpty(pageVo.getSortField())) {
            pageVo.setSortField("createTime");
            pageVo.setSortOrder("desc");
        }
        return new ResultUtil().setData(mediaService.pageMediaData(mgroupId, name, audit, type, userId, PageUtil.initPage(pageVo)));
    }

    //    @RequiresPermissions("media:del")
    @RequestMapping(value = "/delMedia/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除媒体")
    public Result<String> delMedia(@PathVariable String[] ids) {
        for (String id : ids) {
            Media media = mediaService.get(Long.parseLong(id));
            mediaService.delete(Long.parseLong(id));
            ossClientUtil.deleteFile(media.getPath());
            String jpgFilename = media.getPath().substring(0, media.getPath().lastIndexOf(".")) + ".jpg";
            ossClientUtil.deleteFile(jpgFilename);
        }
        return new ResultUtil<String>().setSuccessMsg("删除成功");
    }

    @RequestMapping(value = "/getTransStatus", method = RequestMethod.GET)
    @ApiOperation(value = "获取视频转化上传信息")
    public Object getTransStatus() {
        List<Media> medias = (List<Media>) redisObjectManager.getObject("transFiles");
        if (medias == null) {
            return new ResultUtil<List<Media>>().setData(new ArrayList<>());
        }
        return new ResultUtil<List<Media>>().setData(medias);
    }

    @RequestMapping(value = "/saveMedia", method = RequestMethod.POST)
    @ApiOperation(value = "保存媒体")
    public Result<String> saveMedia(@RequestParam(name = "name", required = true) String name,
                                    @RequestParam(name = "id", required = true) Long id, String remark,
                                    @RequestParam(name = "mGroupId", required = true) long mGroupId) {
        Media media = mediaService.get(id);
        if (media == null) {
            return new ResultUtil<String>().setErrorMsg("媒体数据异常");
        }
        media.setName(name);
        media.setRemark(remark);
        Mgroup mgroup = new Mgroup();
        mgroup.setId(mGroupId);
        media.setMgroup(mgroup);
        mediaService.save(media);
        return new ResultUtil<String>().setSuccessMsg("保存成功");
    }

    @RequestMapping(value = "/saveSubtitle", method = RequestMethod.POST)
    @ApiOperation(value = "保存字幕")
    public Result<String> saveSubtitle(HttpSession session, Long id, String path, @RequestParam(name = "subtitle", required = true) String subtitle) {


        if (id == null){
            Random random = new Random();
            String tmpFileName = random.nextInt(10000) + System.currentTimeMillis() + ".txt";
            File file = new File(videoDir+"//"+tmpFileName);
            try {
                file.createNewFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(file));
                out.write(subtitle);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResultUtil<String>().setSuccessMsg("保存成功");
    }
}
