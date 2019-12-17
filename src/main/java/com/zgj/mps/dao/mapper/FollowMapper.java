package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DownloadTopBean;
import com.zgj.mps.bean.FollowBean;
import com.zgj.mps.bean.FollowResourceBean;
import com.zgj.mps.model.Follow;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2019/12/4.
 */
public interface FollowMapper extends BaseMapper<Follow> {
    Page<FollowBean> pageFollow(Page<FollowBean> page, @Param("followBean") FollowBean followBean);

    List<FollowResourceBean> userFollowList(@Param("userId") Long userId);

    List<DownloadTopBean> downloadTop10();

    Map getMonthDownload(@Param("year") int year);
}
