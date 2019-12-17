package com.zgj.mps.bean;

import lombok.Data;

@Data
public class RateStasticBean {
    private String resourceId;
    private Long avgMark;
    private String resourceName;
    private String proImg;
    private Integer rateCount;
}
