package com.zgj.mps.dao;

import com.zgj.mps.generator.base.BaseRepository;
import com.zgj.mps.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends BaseRepository<User,Long> {

    List<User> findAllByAccountAndIsDelete(String account, Short isDelete);

    List<User> findByMobile(String mobile);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update `user` u set u.login_ip = :ip , u.login_time = NOW() where u.id = :id", nativeQuery = true)
    int updateUserLogin(@Param("id") Long id,@Param("ip") String ip);

    User findByAccountAndIsDelete(String account, Short isDelete);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update user u set password=:password where id=:id", nativeQuery = true)
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Modifying(clearAutomatically = true)
    @Query(value = "update User set avatar=:avatar where id=:id")
    int updateAvatar(@Param("id") Long id, @Param("avatar") String avatar);

    int countByIsDelete(short isDelete);

    @Query(value = "SELECT SUM(case MONTH(create_time) WHEN '1' then 1 else 0 end) as '1',SUM(case MONTH(create_time) WHEN '2' then 1 else 0 end) as '2',SUM(case MONTH(create_time) WHEN '3' then 1 else 0 end) as '3',SUM(case MONTH(create_time) WHEN '4' then 1 else 0 end) as '4',SUM(case MONTH(create_time) WHEN '5' then 1 else 0 end) as '5',SUM(case MONTH(create_time) WHEN '6' then 1 else 0 end) as '6',SUM(case MONTH(create_time) WHEN '7' then 1 else 0 end) as '7',SUM(case MONTH(create_time) WHEN '8' then 1 else 0 end) as '8',SUM(case MONTH(create_time) WHEN '9' then 1 else 0 end) as '9',SUM(case MONTH(create_time) WHEN '10' then 1 else 0 end) as '10',SUM(case MONTH(create_time) WHEN '11' then 1 else 0 end) as '11',SUM(case MONTH(create_time) WHEN '12' then 1 else 0 end) as '12' FROM user where is_delete=0 AND year(create_time) =:year", nativeQuery = true)
    Object mounthUser(@Param("year") int year);
}