package com.zgj.mps.model;


import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zgj.mps.tool.SnowFlakeUtil;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "rate")
public class Rate {

  private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
  private String content;
  private long mark;
  private Timestamp createTime;
  private String publisherName;
  private long isNick;
  @TableLogic
  private long isDelete;
  private Long userId;
  private String resourceId;

  public Rate(){}
  
  public Rate(String id, String content, long mark, Timestamp createTime, String publisherName, long isNick, long isDelete, String resourceId){
    this.id = id;
    this.content = content;
    this.mark = mark;
    this.createTime = createTime;
    this.publisherName = publisherName;
    this.isNick = isNick;
    this.isDelete = isDelete;
    this.resourceId = resourceId;
  }

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", unique = true, nullable = false, length = 255)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  @Column(name = "content", length = 255)
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Column(name = "mark")
  public long getMark() {
    return mark;
  }

  public void setMark(long mark) {
    this.mark = mark;
  }


  @Column(name = "create_time", length = 19)
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  public Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
  }


  @Column(name = "publisher_name")
  public String getPublisherName() {
    return publisherName;
  }

  public void setPublisherName(String publisherName) {
    this.publisherName = publisherName;
  }


  @Column(name = "is_nick")
  public long getIsNick() {
    return isNick;
  }

  public void setIsNick(long isNick) {
    this.isNick = isNick;
  }


  @Column(name = "is_delete")
  public long getIsDelete() {
    return isDelete;
  }

  public void setIsDelete(long isDelete) {
    this.isDelete = isDelete;
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

}
