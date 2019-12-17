package com.zgj.mps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zgj.mps.tool.SnowFlakeUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by user on 2019/12/4.
 */
@Entity
@Table(name = "follow")
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
    private Long userId;
    private String resourceId;
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

    @Column(name = "user_id",nullable = false)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "resource_id",nullable = false)
    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Column(name = "create_time", length = 19)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
