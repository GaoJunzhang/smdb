package com.zgj.mps.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.zgj.mps.tool.SnowFlakeUtil;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "type_resource")
public class TypeResource  implements java.io.Serializable {

	private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
	private String typeId;
	private String resourceId;
    @TableLogic
	private Short isDelete;
	private Timestamp createTime;
	private Long createBy;

	public TypeResource() {
	}

	public TypeResource(String id, String typeId, String resourceId, Long createBy) {
		this.id = id;
		this.typeId = typeId;
		this.resourceId = resourceId;
		this.createBy = createBy;
	}

	public TypeResource(String id, String typeId, String resourceId, Short isDelete,Timestamp createTime, Long createBy) {
		this.id = id;
		this.typeId = typeId;
		this.resourceId = resourceId;
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

	@Column(name = "type_id", nullable = false, length = 255)
	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Column(name = "resource_id", nullable = false, length = 255)
	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	@Column(name = "is_delete")
	public Short getIsDelete() {
		return this.isDelete;
	}

	public void setIsDelete(Short isDelete) {
		this.isDelete = isDelete;
	}

	@Column(name = "create_time", length = 19)
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "create_by", nullable = false, length = 20)
	public Long getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

}
