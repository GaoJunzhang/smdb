package com.zgj.mps.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserResourceDtypeBean {
    private String id;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp startTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp endTime;
    private String userId;
    private String resourceId;
    private Short isForever;
    private Short status;

    private String resourceName;
    private String url;
    private String proImg;
    private String proVideo;
}
