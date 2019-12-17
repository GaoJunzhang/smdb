package com.zgj.mps.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zgj.mps.tool.SnowFlakeUtil;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "resource_lab")
public class ResourceLab implements java.io.Serializable {

    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
    private String name;
    @TableLogic
    private Short isDelete;
    private Long createBy;
    private Timestamp createTime;
    private String remark;

    public ResourceLab() {
    }

    public ResourceLab(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public ResourceLab(String id, String name, Short isDelete, Long createBy, Timestamp createTime, String remark) {
        this.id = id;
        this.name = name;
        this.isDelete = isDelete;
        this.createBy = createBy;
        this.createTime = createTime;
        this.remark = remark;
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

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "is_delete")
    public Short getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }

    @Column(name = "create_by", length = 255)
    public Long getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    @Column(name = "create_time", length = 19)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "remark", length = 100)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
