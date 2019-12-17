package com.zgj.mps.dao;

import com.zgj.mps.generator.base.BaseRepository;
import com.zgj.mps.model.TaskAudit;

import java.util.List;

/**
 * 任务审核数据处理层
 *
 * @author Wangj
 */
public interface TaskAuditRepository extends BaseRepository<TaskAudit, Long> {
    List<TaskAudit> findAllByTaskIdOrderByCreateTimeDesc(Long taskId);
}