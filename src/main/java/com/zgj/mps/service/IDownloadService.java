package com.zgj.mps.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.bean.DownResourceBean;
import com.zgj.mps.bean.DownStatusBean;
import com.zgj.mps.bean.DownloadBean;
import com.zgj.mps.bean.DownloadTopBean;
import com.zgj.mps.model.Download;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;

import java.util.List;
import java.util.Map;

/**
 * 下载资源接口
 * @author GaoJunZhang
 */
public interface IDownloadService extends IService<Download> {
    Page<DownloadBean> pageDownload(DownloadBean downloadBean, PageVo pageVo, SearchVo searchVo);

    List<DownStatusBean> downProcessByDevictType(String type);

    List<DownResourceBean> userDownList(Long  userId, String deviceMac);

    List<DownloadTopBean> downloadTop10();

    Map getMonthDownload(int year);

    List<Download> downLoadByUserIdAndRid(Long userId, String rid);
}