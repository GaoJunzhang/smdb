package com.zgj.mps.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DevicePageBean {
    private String id;
    private Short status;
    private Short progress;
    private String resourceName;
    private String proImg;
    private String deviceMac;
    private String name;
    private String createTime;
    private String avatar;
    private String account;
}
