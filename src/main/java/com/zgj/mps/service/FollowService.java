package com.zgj.mps.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.bean.DownloadTopBean;
import com.zgj.mps.bean.FollowBean;
import com.zgj.mps.bean.FollowResourceBean;
import com.zgj.mps.model.Follow;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2019/12/4.
 */
public interface FollowService extends IService<Follow> {
    Page<FollowBean> pageFollow(FollowBean followBean, PageVo pageVo, SearchVo searchVo);

    List<FollowResourceBean> userFollowList(Long  userId);

    List<Follow> findByUserIdAndRid(Long userId,String rid);

    List<DownloadTopBean> downloadTop10();

    Map getMonthDownload(int year);
}
