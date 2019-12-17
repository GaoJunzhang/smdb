package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.AturhResourceBean;
import com.zgj.mps.bean.UserAuthBean;
import com.zgj.mps.bean.UserResourceBean;
import com.zgj.mps.bean.UserResourceDtypeBean;
import com.zgj.mps.model.UserResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备资源数据处理层
 *
 * @author GaoJunZhang
 */
public interface UserResourceMapper extends BaseMapper<UserResource> {

    int batchSave(@Param("list") List<UserResource> list);

    List<UserResourceBean> userResourceByUid(@Param("uid") Long uid);

    Page<UserAuthBean> userAuthData(Page<UserAuthBean> page, @Param("name") String name, @Param("isForever") Short isForever);

    int updateUrIsforever(@Param("isForever") Short isForever, @Param("userId") Long userId);

    int updateUrStatus(@Param("status") Short status, @Param("userId") Long userId);

    List<UserResourceDtypeBean> userResourceByDtype(@Param("userId") Long userId, @Param("typeIds") String[] typeIds);

    List<AturhResourceBean> selectUrByUidAndRid(@Param("userId") Long userId, @Param("rid") String rid);
}