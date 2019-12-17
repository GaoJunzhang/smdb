package com.zgj.mps.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zgj.mps.tool.SnowFlakeUtil;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "resource")
public class Resource implements java.io.Serializable {

    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
    private String name;
    private String url;
    private String proImg;
    private String typeId;
    private String proVideo;
    private String title;
    private String content;
    @TableLogic
    private Short isDelete;
    private Timestamp createTime;
    private Long size;
    private String labId;
    private String packageName;
    private String filed2;
    private String filed3;

    public Resource() {
    }

    public Resource(String id) {
        this.id = id;
    }

    public Resource(String id, String name, String url, String proImg, String proVideo, String title,
                    String content, Short isDelete, Timestamp createTime,Long size,String labId,
                    String typeId, String packageName, String filed2, String filed3) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.proImg = proImg;
        this.proVideo = proVideo;
        this.title = title;
        this.content = content;
        this.isDelete = isDelete;
        this.createTime = createTime;
        this.size = size;
        this.labId = labId;
        this.typeId = typeId;
        this.packageName = packageName;
        this.filed2 = filed2;
        this.filed3 = filed3;
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

    @Column(name = "name", length = 45)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "url", length = 255)
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "pro_img", length = 255)
    public String getProImg() {
        return this.proImg;
    }

    public void setProImg(String proImg) {
        this.proImg = proImg;
    }

    @Column(name = "pro_video", length = 255)
    public String getProVideo() {
        return this.proVideo;
    }

    public void setProVideo(String proVideo) {
        this.proVideo = proVideo;
    }

    @Column(name = "title", length = 255)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "content", length = 255)
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Column(name = "size")
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Column(name = "lab_id")
    public String getLabId() {
        return labId;
    }

    public void setLabId(String labId) {
        this.labId = labId;
    }

    @Column(name = "type_id")
    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Column(name = "package_name")
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Column(name = "filed2")
    public String getFiled2() {
        return filed2;
    }

    public void setFiled2(String filed2) {
        this.filed2 = filed2;
    }

    @Column(name = "filed3")
    public String getFiled3() {
        return filed3;
    }

    public void setFiled3(String filed3) {
        this.filed3 = filed3;
    }
}
