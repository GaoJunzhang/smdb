package com.zgj.mps.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class RateBean {
    private String id;
    private String content;
    private long mark;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    private String publisherName;
    private Long isNick;
    private long isDelete;
    private String userId;
    private String resourceId;
    private String account;
    private String resourceName;
    private String avatar;
    private String name;
    private String proImg;
}
