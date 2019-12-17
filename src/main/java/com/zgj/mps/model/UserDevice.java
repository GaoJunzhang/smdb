package com.zgj.mps.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.zgj.mps.tool.SnowFlakeUtil;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user_device")
public class UserDevice {
    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
    private Long userId;
    private String deviceId;
    private Short isForever;
    @TableLogic
    private Short isDelete;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp createTime;


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false, length = 255)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "device_id")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Column(name = "is_forever")
    public Short getIsForever() {
        return isForever;
    }

    public void setIsForever(Short isForever) {
        this.isForever = isForever;
    }

    @Column(name = "is_delete")
    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }

    @Column(name = "start_time", length = 19)
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time", length = 19)
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Column(name = "create_time", length = 19)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
