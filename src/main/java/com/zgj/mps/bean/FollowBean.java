package com.zgj.mps.bean;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by user on 2019/12/4.
 */
@Data
public class FollowBean {
    private String id;
    private Long userId;
    private String account;
    private String name;
    private String resourceId;
    private Timestamp createTime;
    private String userName;
    private String resourceName;
}
