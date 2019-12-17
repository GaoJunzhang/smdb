package com.zgj.mps.dao;

import com.zgj.mps.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.zgj.mps.generator.base.BaseRepository;

/**
 * 任务数据处理层
 *
 * @author Wangj
 */
public interface TaskRepository extends BaseRepository<Task, Long> {
    Page<Task> findAll(Specification<Task> spec, Pageable pageable);
}