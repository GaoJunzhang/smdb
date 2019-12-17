package com.zgj.mps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.bean.DownResourceBean;
import com.zgj.mps.bean.DownloadTopBean;
import com.zgj.mps.bean.FollowBean;
import com.zgj.mps.bean.FollowResourceBean;
import com.zgj.mps.dao.mapper.FollowMapper;
import com.zgj.mps.model.Follow;
import com.zgj.mps.service.FollowService;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2019/12/4.
 */
@Slf4j
@Service
@Transactional
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Autowired
    private FollowMapper followMapper;

    public Page<FollowBean> pageFollow(FollowBean followBean, PageVo pageVo, SearchVo searchVo) {
        Page<FollowBean> pageFollow = new Page<FollowBean>(pageVo.getPageNo(), pageVo.getPageSize());
        return followMapper.pageFollow(pageFollow, followBean); //自定义方法，多表
    }

    public List<FollowResourceBean> userFollowList(Long  userId){
        return followMapper.userFollowList(userId);
    }

    public List<Follow> findByUserIdAndRid(Long userId,String rid){
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("resource_id", rid);
        return followMapper.selectList(queryWrapper);
    }

    public List<DownloadTopBean> downloadTop10(){
        return followMapper.downloadTop10();
    }

    public Map getMonthDownload(int year){
        return followMapper.getMonthDownload(year);
    }
}
