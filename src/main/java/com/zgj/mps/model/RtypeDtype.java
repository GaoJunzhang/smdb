package com.zgj.mps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zgj.mps.tool.SnowFlakeUtil;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "rtype_dtype")
public class RtypeDtype implements java.io.Serializable {

    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
    ;
    private String rtId;
    private String dtId;
    private Short isDelete;
    private Timestamp createTime;
    private Long createBy;

    public RtypeDtype() {
    }

    public RtypeDtype(String id, String rtId, String dtId) {
        this.id = id;
        this.rtId = rtId;
        this.dtId = dtId;
    }

    public RtypeDtype(String id, String rtId, String dtId, Short isDelete, Timestamp createTime, Long createBy) {
        this.id = id;
        this.rtId = rtId;
        this.dtId = dtId;
        this.isDelete = isDelete;
        this.createTime = createTime;
        this.createBy = createBy;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "id", unique = true, nullable = false, length = 255)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "rt_id", nullable = false, length = 255)
    public String getRtId() {
        return this.rtId;
    }

    public void setRtId(String rtId) {
        this.rtId = rtId;
    }

    @Column(name = "dt_id", nullable = false, length = 255)
    public String getDtId() {
        return this.dtId;
    }

    public void setDtId(String dtId) {
        this.dtId = dtId;
    }

    @Column(name = "is_delete")
    public Short getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }

    @Column(name = "create_time", length = 19)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "create_by", length = 20)
    public Long getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

}
