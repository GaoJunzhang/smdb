package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zgj.mps.bean.ResourceNoUrlBean;
import com.zgj.mps.bean.TypeResourceBean;
import com.zgj.mps.model.TypeResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备资源数据处理层
 *
 * @author GaoJunZhang
 */
public interface TypeResourceMapper extends BaseMapper<TypeResource> {

    int batchSave(@Param("list") List<TypeResource> list);

    List<TypeResourceBean> typeResourceByDid(@Param("id") String id);

    List<ResourceNoUrlBean> typeResourceById(@Param("id") String id);
}