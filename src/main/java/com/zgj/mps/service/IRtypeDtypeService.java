package com.zgj.mps.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.model.RtypeDtype;

import java.util.List;

/**
 * 资源类型接口
 * @author GaoJunZhang
 */
public interface IRtypeDtypeService extends IService<RtypeDtype> {
    List<RtypeDtype> findByDtypeName(String dtid);

    int deleteBydtIdAndrtId(String rtId, String dtId);
}