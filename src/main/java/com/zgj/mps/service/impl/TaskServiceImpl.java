package com.zgj.mps.service.impl;

import com.zgj.mps.controller.request.AuditTaskRequest;
import com.zgj.mps.dao.TaskAuditRepository;
import com.zgj.mps.dao.TaskRepository;
import com.zgj.mps.model.Task;
import com.zgj.mps.model.TaskAudit;
import com.zgj.mps.model.TaskAuditEnum;
import com.zgj.mps.service.TaskService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zgj.mps.service.UserService;
import com.zgj.mps.tool.ResultUtil;
import com.zgj.mps.vo.Result;
import com.zgj.mps.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * 任务接口实现
 *
 * @author Wangj
 */
@Slf4j
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAuditRepository taskAuditRepository;

    @Autowired
    private UserService userService;

    @Override
    public TaskRepository getRepository() {
        return taskRepository;
    }

    @Override
    public Map<String, Object> taskList(Long userId, Long mgroupId, String name, Short audit, Integer page, Integer size, String sortOrder, String sortValue) {
        List<String> userMgroupCode = userService.userMgroupCodes(userId);

        Specification specification = new Specification<Task>() {
            @Override
            public Predicate toPredicate(Root<Task> r, CriteriaQuery<?> q, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                predicate.getExpressions().add(cb.equal(r.get("isDelete"), 0));

                if (mgroupId == null) {
                    Predicate[] userMgroupPredicates = new Predicate[userMgroupCode.size()];
                    for (int i = 0; i < userMgroupCode.size(); i++) {
                        userMgroupPredicates[i] = cb.like(r.get("mgroup").get("code"), userMgroupCode.get(i) + "%");
                    }
                    predicate.getExpressions().add(cb.or(userMgroupPredicates));
                } else {
                    predicate.getExpressions().add(cb.equal(r.get("mgroup").get("id"), mgroupId));
                }


                if (!StrUtil.isEmpty(name)) {
                    predicate.getExpressions().add(cb.like(r.get("name"), "%" + name + "%"));
                }
                if (audit != null) {
                    predicate.getExpressions().add(cb.equal(r.get("audit"), audit));
                }
                return predicate;
            }
        };
        Page<Task> tasks = taskRepository.findAll(specification, PageRequest.of(page > 0 ? page - 1 : page, size, sortOrder.equals("descend") ? Sort.Direction.DESC : Sort.Direction.ASC, sortValue));
        List<Map<String, Object>> tasksMap = new ArrayList<>();
        for (Task task : tasks) {
            Map<String, Object> u = new HashMap<>();
            u.put("id", task.getId());
            u.put("name", task.getName());
            u.put("audit", task.getAudit().ordinal());
            u.put("start", task.getStart() == null ? "" : DateUtil.format(task.getStart(), "yyyy-MM-dd HH:mm:ss"));
            u.put("end", task.getEnd() == null ? "" : DateUtil.format(task.getEnd(), "yyyy-MM-dd HH:mm:ss"));
            u.put("userName", task.getUser().getName());
            u.put("createTime", DateUtil.format(task.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            tasksMap.add(u);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", page);
        map.put("totalCount", tasks.getTotalElements());
        map.put("data", tasksMap);
        return map;
    }

    @Override
    public Result delTask(Long[] taskIds) {
        if (taskIds == null) {
            return new ResultUtil().setErrorMsg("请选择任务");
        }
        List<Task> tasks = new ArrayList<>();
        for (Long id : taskIds) {
            Task task = taskRepository.getOne(id);
            if (task == null) {
                return new ResultUtil().setErrorMsg("请选择正确媒体组");
            }

            task.setIsDelete((short) 1);
        }
        taskRepository.saveAll(tasks);
        return new ResultUtil<>().setData("");
    }

    @Override
    @Transactional
    public Result auditTask(Long userId, AuditTaskRequest auditTaskRequest) {
        if (auditTaskRequest == null || auditTaskRequest.getIds() == null) {
            return new ResultUtil().setErrorMsg("无效的参数");
        }
        Long[] ids = auditTaskRequest.getIds();
        User user = new User();
        user.setId(userId);
        for (Long id : ids) {
            Task task = taskRepository.getOne(id);
            if (id == null)
                continue;

            TaskAuditEnum audit = auditTaskRequest.getAudit().intValue() == 1 ? TaskAuditEnum.AUDIT_OK : TaskAuditEnum.AUDIT_FAILED;
            if (task.getAudit() == audit)
                continue;

            TaskAudit taskAudit = new TaskAudit();
            taskAudit.setUser(user);
            taskAudit.setTask(task);
            taskAudit.setRemark(auditTaskRequest.getRemark());
            taskAudit.setAudit(audit);
            taskAuditRepository.save(taskAudit);

            task.setAudit(audit);
            taskRepository.save(task);
        }

        return new ResultUtil<>().setData("");
    }

    @Override
    public Result auditTaskRecords(Long id) {
        if (id == null) {
            return new ResultUtil().setErrorMsg("无效的参数");
        }
        List<TaskAudit> taskAudits = taskAuditRepository.findAllByTaskIdOrderByCreateTimeDesc(id);
        List<HashMap<String, Object>> records = new ArrayList<>();
        for (TaskAudit taskAudit : taskAudits) {
            HashMap<String, Object> record = new HashMap<>();
            record.put("audit", taskAudit.getAudit().ordinal());
            record.put("remark", StrUtil.nullToEmpty(taskAudit.getRemark()));
            record.put("userName", taskAudit.getUser().getName());
            record.put("createTime", DateUtil.format(taskAudit.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            records.add(record);
        }
        return new ResultUtil<>().setData(records);
    }
}