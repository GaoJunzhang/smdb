package com.zgj.mps.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class DownloadBean {
    private String id;
    private long status;
    private long size;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp finishTime;
    private String userId;
    private String resourceId;
    private String account;
    private String resourceName;
    private String avatar;
    private String name;
    private String resourceProImg;
}
