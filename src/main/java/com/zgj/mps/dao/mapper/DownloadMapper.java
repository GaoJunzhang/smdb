package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DownResourceBean;
import com.zgj.mps.bean.DownStatusBean;
import com.zgj.mps.bean.DownloadBean;
import com.zgj.mps.bean.DownloadTopBean;
import com.zgj.mps.model.Download;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 下载资源数据处理层
 * @author GaoJunZhang
 */
public interface DownloadMapper extends BaseMapper<Download> {
    Page<DownloadBean> pageDownload(Page<DownloadBean> page, @Param("downloadBean") DownloadBean downloadBean);

    List<DownStatusBean> downProcessByDevictType(@Param("type") String type);

    List<DownResourceBean> userDownList(@Param("userId") String userId,@Param("deviceMac") String deviceMac);

    List<DownloadTopBean> downloadTop10();

    Map getMonthDownload(@Param("year") int year);
}