package com.zgj.mps.controller;

import com.zgj.mps.controller.request.AuditTaskRequest;
import com.zgj.mps.generator.base.BaseController;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.model.Task;
import com.zgj.mps.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

/**
 * @author Wangj
 */
@Slf4j
@RestController
@Api(description = "任务管理接口")
@RequestMapping("/task")
@Transactional
public class TaskController extends BaseController<Task, Long> {

    @Autowired
    private TaskService taskService;

    @Override
    public TaskService getService() {
        return taskService;
    }

    @RequiresPermissions("taskList")
    @RequestMapping(value = "/taskListData", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取")
    public Object taskList(HttpSession session, Long mgroupId, String name, Short audit,
                                       @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
                                       @RequestParam(defaultValue = "descend") String sortOrder, @RequestParam(defaultValue = "createTime") String sortField) {
        long userId = (long) session.getAttribute("userId");
        return new ResultUtil().setData(taskService.taskList(userId, mgroupId, name, audit, pageNo, pageSize, sortOrder, sortField));
    }

    @RequiresPermissions("task:del")
    @RequestMapping(value = "/delTask/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除任务")
    public Object delRole(@PathVariable Long[] ids) {
        return taskService.delTask(ids);
    }

    @RequestMapping(value = "/auditTask", method = RequestMethod.POST)
    public Object auditTask(HttpSession session, @RequestBody AuditTaskRequest auditTaskRequest) {
        long userId = (long) session.getAttribute("userId");
        return taskService.auditTask(userId, auditTaskRequest);
    }

    @RequestMapping(value = "/auditTaskRecords/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "任务审核记录")
    public Object delRole(@PathVariable Long id) {
        return taskService.auditTaskRecords(id);
    }
}
