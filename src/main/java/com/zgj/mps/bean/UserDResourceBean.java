package com.zgj.mps.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDResourceBean {
    private String id;
    private String name;
    private String url;
    private String proImg;
    private String proVideo;
    private String title;
    private String content;
    private Short isDelete;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp startTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp endTime;
    private Short isForever;
    private String userId;
    private String deviceId;
}
