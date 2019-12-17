package com.zgj.mps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.dao.mapper.RtypeDtypeMapper;
import com.zgj.mps.model.RtypeDtype;
import com.zgj.mps.service.IRtypeDtypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 资源类型接口实现
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IRtypeDtypeServiceImpl extends ServiceImpl<RtypeDtypeMapper, RtypeDtype> implements IRtypeDtypeService {

    @Autowired
    private RtypeDtypeMapper rtypeDtypeMapper;

    public List<RtypeDtype> findByDtypeName(String dtid){
        QueryWrapper<RtypeDtype> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",0);
        queryWrapper.eq("dt_id",dtid);
        return rtypeDtypeMapper.selectList(queryWrapper);
    }

    public int deleteBydtIdAndrtId(String rtId,String dtId){
        return rtypeDtypeMapper.deleteBydtIdAndrtId(rtId,dtId);
    }
}