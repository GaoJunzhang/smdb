package com.zgj.mps.service;

import com.zgj.mps.generator.base.BaseService;
import com.zgj.mps.model.Media;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * 媒体接口
 * @author GaoJunZhang
 */
public interface MediaService extends BaseService<Media,Long> {


    Map<String, Object> pageMediaData(Long mgroupId, String name, Short audit, Short type, long userId, Pageable pageable);

}