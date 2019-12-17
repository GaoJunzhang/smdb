package com.zgj.mps.model;

import com.baomidou.mybatisplus.annotation.TableLogic;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user_type")
public class UserType  implements java.io.Serializable {

	private String id;
	private String typeId;
	private String userId;
	private Timestamp startTime;
	private Timestamp endTime;
	private Short isForever;
	@TableLogic
	private Short isDelete;
	private Timestamp createTime;
	private String createBy;

	public UserType() {
	}

	public UserType(String id, String typeId, String userId) {
		this.id = id;
		this.typeId = typeId;
		this.userId = userId;
	}

	public UserType(String id, String typeId, String userId, Timestamp startTime, Timestamp endTime, Short isForever, Short isDelete, Timestamp createTime, String createBy) {
		this.id = id;
		this.typeId = typeId;
		this.userId = userId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isForever = isForever;
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

	@Column(name = "user_id", nullable = false, length = 255)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "start_time", length = 19)
	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time", length = 19)
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

	@Column(name = "create_by", length = 255)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

}
