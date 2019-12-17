package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zgj.mps.model.RtypeDtype;
import org.apache.ibatis.annotations.Param;

/**
 * 资源类型数据处理层
 * @author GaoJunZhang
 */
public interface RtypeDtypeMapper extends BaseMapper<RtypeDtype> {

    int deleteBydtIdAndrtId(@Param("rtId") String rtId, @Param("dtId") String dtId);
}