package com.zgj.mps.bean;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserTypeBean {
    private String id;
    private String typeId;
    private String userId;
    private String isForever;
    private String isDelete;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp createTime;
    private String createBy;
    private String account;

    private String avatar;
    private String mobile;
    private String deviceName;
    private String deviceMac;
    private String deviceTypeId;
    private String deviceTypeName;
}
