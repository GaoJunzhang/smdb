package com.zgj.mps.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.model.ResourceType;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;

import java.util.List;

/**
 * 资源类型接口
 * @author GaoJunZhang
 */
public interface IResourceTypeService extends IService<ResourceType> {
    IPage<ResourceType> selectPage(ResourceType resourceType, PageVo pageVo, SearchVo searchVo);

    List<ResourceType> findByName(String name);

    List<ResourceType> findByIds(List<String> list);
}