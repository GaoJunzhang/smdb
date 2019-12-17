package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.DownloadTopBean;
import com.zgj.mps.bean.RateBean;
import com.zgj.mps.bean.RateStasticBean;
import com.zgj.mps.model.Rate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 资源评论数据处理层
 *
 * @author GaoJunZhang
 */
public interface RateMapper extends BaseMapper<Rate> {
    Page<RateBean> pageRate(Page<RateBean> page, @Param("rateBean") RateBean rateBean);

    Page<RateStasticBean> pageRateStastic(Page<RateStasticBean> page, @Param("rateBean") RateBean rateBean);

    List<DownloadTopBean> downloadTop10();

    Map getMonthDownload(@Param("year") int year);
}