package com.zgj.mps.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.bean.AturhResourceBean;
import com.zgj.mps.bean.UserAuthBean;
import com.zgj.mps.bean.UserResourceBean;
import com.zgj.mps.bean.UserResourceDtypeBean;
import com.zgj.mps.model.UserResource;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;

import java.util.List;

/**
 * 设备资源接口
 * @author GaoJunZhang
 */
public interface IUserResourceService extends IService<UserResource> {
    int batchSave(List<UserResource> list);

    List<UserResourceBean> userResourceByUid(Long uid);

    Page<UserAuthBean> userAuthData(PageVo pageVo, SearchVo searchVo, String nickName, Short isForever);

    int updateUrIsforever(Short isForever, Long userId);

    int updateUrStatus(Short status, Long userId);

    List<UserResource> userResourceByUserId(Long userId);

    List<UserResourceDtypeBean> udtData(Long userId, String[] typeIds);

    List<UserResource> findByUserIdAndResourceId(Long userId, String resourceId);

    List<AturhResourceBean> selectUrByUidAndRid(Long userId, String rid);
}