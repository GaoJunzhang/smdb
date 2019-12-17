package com.zgj.mps.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.model.ResourceLab;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;

/**
 * 资源标签接口
 * @author GaoJunZhang
 */
public interface IResourceLabService extends IService<ResourceLab> {

    IPage<ResourceLab> selectPage(ResourceLab resourceLab, PageVo pageVo, SearchVo searchVo);
}