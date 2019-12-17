package com.zgj.mps.dao;

import com.zgj.mps.model.Media;
import com.zgj.mps.generator.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * 媒体数据处理层
 * @author GaoJunZhang
 */
public interface MediaRepository extends BaseRepository<Media,Long>  {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update media set path=:path , duration=:duration, md5=:md5,remark=:remark where id=:id",nativeQuery = true)
    int updateMediaPathAndDuration(@Param("id") Long id,@Param("path") String path,@Param("duration") Long duration,@Param("md5") String md5,@Param("remark") String remark);
}