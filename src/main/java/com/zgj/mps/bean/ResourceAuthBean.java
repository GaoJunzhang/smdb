package com.zgj.mps.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ResourceAuthBean {
    private String deviceName;
    private String deviceMac;
    private String deviceTypeName;
    private String isForever;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;
    private String resourceName;
}
