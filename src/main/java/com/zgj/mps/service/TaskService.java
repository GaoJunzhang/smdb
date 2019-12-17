package com.zgj.mps.service;

import com.zgj.mps.controller.request.AuditTaskRequest;
import com.zgj.mps.generator.base.BaseService;
import com.zgj.mps.model.Task;
import com.zgj.mps.vo.Result;

import java.util.Map;

/**
 * 任务接口
 *
 * @author Wangj
 */
public interface TaskService extends BaseService<Task, Long> {

    /**
     * 多条件分页获取
     */
    Map<String, Object> taskList(Long userId, Long mgroupId, String name, Short audit, Integer page, Integer size, String sortOrder, String sortValue);

    Result delTask(Long[] ids);

    Result auditTask(Long userId, AuditTaskRequest auditTaskRequest);

    Result auditTaskRecords(Long id);
}