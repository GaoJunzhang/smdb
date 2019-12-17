package com.zgj.mps.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.bean.ResourceNoUrlBean;
import com.zgj.mps.bean.TypeResourceBean;
import com.zgj.mps.model.TypeResource;

import java.util.List;

/**
 * 设备资源接口
 *
 * @author GaoJunZhang
 */
public interface ITypeResourceService extends IService<TypeResource> {
    int batchSave(List<TypeResource> list);

    List<TypeResourceBean> typeResourceByDid(String id);

    List<ResourceNoUrlBean> typeResourceByType(String id);
}