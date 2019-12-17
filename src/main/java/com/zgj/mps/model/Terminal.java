package com.zgj.mps.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "terminal")
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Terminal extends BaseEntity implements java.io.Serializable {
    private String name;
    private String mac;
    private String devState;
    private String dlFileSize;
    private String useableSpace;
    private String diskSpace;
    private String taskName;
    private String playContent;
    private Timestamp imdUpdateTime;
    private String serverIp;
    private String serverMac;
    private String nkVersion;
    private String appVersion;
    private Short connectStatus;
    private Timestamp connectTime;
    private Timestamp disconnectTime;
    private Short isDelete;
    private Timestamp deleteTime;
}