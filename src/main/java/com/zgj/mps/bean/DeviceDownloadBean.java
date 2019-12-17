package com.zgj.mps.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceDownloadBean {
    private String id;
    private Short status;
    private Short progress;
    private String resourceName;
    private String url;
    private String resourceId;
    private String deviceMac;
}
