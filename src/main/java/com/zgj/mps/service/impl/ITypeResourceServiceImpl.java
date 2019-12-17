package com.zgj.mps.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.bean.ResourceNoUrlBean;
import com.zgj.mps.bean.TypeResourceBean;
import com.zgj.mps.dao.mapper.TypeResourceMapper;
import com.zgj.mps.model.TypeResource;
import com.zgj.mps.service.ITypeResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 设备资源接口实现
 *
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class ITypeResourceServiceImpl extends ServiceImpl<TypeResourceMapper, TypeResource> implements ITypeResourceService {

    @Autowired
    private TypeResourceMapper typeResourceMapper;

    public int batchSave(List<TypeResource> list) {
        return typeResourceMapper.batchSave(list);
    }

    public List<TypeResourceBean> typeResourceByDid(String id) {
        return typeResourceMapper.typeResourceByDid(id);
    }

    public List<ResourceNoUrlBean> typeResourceByType(String id) {
        return typeResourceMapper.typeResourceById(id);
    }
}