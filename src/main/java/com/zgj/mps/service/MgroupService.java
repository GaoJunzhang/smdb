package com.zgj.mps.service;

import com.zgj.mps.controller.request.SaveMgroupRequest;
import com.zgj.mps.generator.base.BaseService;
import com.zgj.mps.model.Mgroup;
import com.zgj.mps.vo.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 媒体组接口
 *
 * @author Wangj
 */
public interface MgroupService extends BaseService<Mgroup, Long> {

    List<HashMap<String, Object>> mgroupListData(Long userId, Long mgroupId);

    HashMap<String, Object> mgroupByName(Long userId, String name, Integer page);

    Result saveMgroup(Long userId, SaveMgroupRequest saveMgroupRequest);

    Result delMgroup(Long userId, Long mgroupId);

    Map<String, Object> findMgroupTree(Long userId);
}