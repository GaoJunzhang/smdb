package com.zgj.mps.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zgj.mps.dao.MediaRepository;
import com.zgj.mps.model.Media;
import com.zgj.mps.service.MediaService;
import com.zgj.mps.service.UserService;
import com.zgj.mps.tool.OSSClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 媒体接口实现
 *
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserService userService;

    @Override
    public MediaRepository getRepository() {
        return mediaRepository;
    }


    @Autowired
    public OSSClientUtil ossClientUtil;

    @Override
    public Map<String, Object> pageMediaData(Long mgroupId, String name, Short audit, Short type, long userId, Pageable pageable) {
        List<String> userMgroupCode = userService.userMgroupCodes(userId);
        Specification specification = new Specification<Media>() {
            @Override
            public Predicate toPredicate(Root<Media> r, CriteriaQuery<?> q, CriteriaBuilder cb) {
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
                if (type != null) {
                    predicate.getExpressions().add(cb.equal(r.get("type"), type));
                }
                return predicate;
            }
        };
        Page<Media> mediaPage = mediaRepository.findAll(specification, pageable);
        List<Map<String, Object>> mediasMap = new ArrayList<>();
        for (Media mediaObj : mediaPage) {
            Map<String, Object> u = new HashMap<>();
            u.put("id", mediaObj.getId());
            u.put("name", mediaObj.getName());
            u.put("userName", mediaObj.getUser().getName());
            u.put("mGroupName", mediaObj.getMgroup().getName());
            u.put("mGroupId", mediaObj.getMgroup().getId());
            u.put("type", mediaObj.getType());
            u.put("size", mediaObj.getSize());
            u.put("width", mediaObj.getWidth());
            u.put("height", mediaObj.getHeight());
            u.put("path", mediaObj.getPath());
            u.put("audit", mediaObj.getAudit());
            u.put("remark", mediaObj.getRemark());
            u.put("duration", mediaObj.getDuration());
            u.put("createTime", DateUtil.format(mediaObj.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            mediasMap.add(u);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", pageable.getPageNumber());
        map.put("pageSize", pageable.getPageSize());
        map.put("totalPage", mediaPage.getTotalPages());
        map.put("totalCount", mediaPage.getTotalElements());
        map.put("data", mediasMap);
        return map;
    }

}