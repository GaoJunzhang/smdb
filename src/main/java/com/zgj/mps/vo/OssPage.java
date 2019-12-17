package com.zgj.mps.vo;

import lombok.Data;

import java.util.List;

@Data
public class OssPage {
    private List<OssItem> summaryList;
    private String nextMarker;
    private Integer maxKeys;
}
