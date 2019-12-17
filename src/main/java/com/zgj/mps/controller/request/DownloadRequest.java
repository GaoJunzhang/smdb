package com.zgj.mps.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DownloadRequest {
    private String deviceMac;
    private String params;
}
