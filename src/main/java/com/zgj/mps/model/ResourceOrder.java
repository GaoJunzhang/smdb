package com.zgj.mps.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zgj.mps.tool.SnowFlakeUtil;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "resource_order")
public class ResourceOrder implements java.io.Serializable {

    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
    private String deviceId;
    private String typeId;
    private String userId;
    private String resourceId;
    private String typeResourceId;
    private Timestamp startTime;
    private Timestamp endTime;
    private Short isForever;
    private Timestamp createTime;
    private Long createBy;
    @TableLogic
    private Short isDelete;
    private String deviceMac;
    private Short status;

    public ResourceOrder() {
    }

    public ResourceOrder(String id, String deviceId, String typeId, String userId, String resourceId) {
        this.id = id;
        this.deviceId = deviceId;
        this.typeId = typeId;
        this.userId = userId;
        this.resourceId = resourceId;
    }

    public ResourceOrder(String id, String deviceId, String typeId, String userId, String resourceId, Timestamp startTime, Timestamp endTime, Short isForever, Timestamp createTime, Long createBy, Short isDelete, String deviceMac, String typeResourceId,Short status) {
        this.id = id;
        this.deviceId = deviceId;
        this.typeId = typeId;
        this.userId = userId;
        this.resourceId = resourceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isForever = isForever;
        this.createTime = createTime;
        this.createBy = createBy;
        this.isDelete = isDelete;
        this.deviceMac = deviceMac;
        this.typeResourceId = typeResourceId;
        this.status = status;
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

    @Column(name = "device_id", length = 255)
    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Column(name = "type_id", nullable = false, length = 255)
    public String getTypeId() {
        return this.typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Column(name = "user_id", length = 255)
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "resource_id", nullable = false, length = 255)
    public String getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Column(name = "start_time", length = 19)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time", length = 19)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Column(name = "is_forever")
    public Short getIsForever() {
        return this.isForever;
    }

    public void setIsForever(Short isForever) {
        this.isForever = isForever;
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

    @Column(name = "is_delete")
    public Short getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }

    @Column(name = "device_mac", length = 32)
    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    @Column(name = "type_resource_id", length = 255)
    public String getTypeResourceId() {
        return typeResourceId;
    }

    public void setTypeResourceId(String typeResourceId) {
        this.typeResourceId = typeResourceId;
    }

    @Column(name = "status")
    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }
}
