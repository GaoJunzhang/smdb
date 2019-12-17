package com.zgj.mps.vo;

import com.aliyun.oss.model.Owner;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OssItem implements Serializable {
    private static final long serialVersionUID = -364728206042527335L;
    private String bucketName;
    private String key;
    private String eTag;
    private long size;
    private String lastModified;
    private String storageClass;
    private Owner owner;

    public OssItem() {
    }
}
