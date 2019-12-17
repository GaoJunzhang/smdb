package com.zgj.mps.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.bean.UserDResourceBean;
import com.zgj.mps.model.Resource;
import com.zgj.mps.vo.PageVo;
import com.zgj.mps.vo.SearchVo;

import java.util.List;
import java.util.Map;

/**
 * 资源接口
 * @author GaoJunZhang
 */
public interface IResourceService extends IService<Resource> {

    IPage<Resource> selectPage(Resource resource, PageVo pageVo, SearchVo searchVo);

    List<UserDResourceBean> reourceListByUid(String uid);

    List<Resource> findByIds(List<Resource> list);

    List<Resource> findByTypeIds(List<String> list);

    Map monthResource(int year);

    int sumResource();
}