package com.zgj.mps.service;

import com.zgj.mps.generator.base.BaseService;
import com.zgj.mps.model.Role;
import com.zgj.mps.vo.SearchVo;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 角色接口
 * @author GaoJunZhang
 */
public interface RoleService extends BaseService<Role,Long> {

    /**
    * 多条件分页获取
    * @param role
    * @param searchVo
    * @param pageable
    * @return
    */
    Map<String, Object> roleData(Role role, SearchVo searchVo, Pageable pageable);

    List<Role> findAllByRole(String role);

    void updateIsDelete(Long id);

    List<Role> findAllByIsDelete(Short isDelete);
}