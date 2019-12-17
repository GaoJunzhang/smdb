package com.zgj.mps.bean;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDeviceBean {
    private String id;
    private String deviceId;
    private String userId;
    private String isForever;
    private String isDelete;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp createTime;
    private String account;

    private String avatar;
    private String mobile;
    private String deviceName;
    private String deviceMac;
    private String deviceTypeId;
    private String deviceTypeName;
}
