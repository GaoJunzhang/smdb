package com.zgj.mps.model;


import com.zgj.mps.tool.SnowFlakeUtil;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "device_resource")
public class DeviceResource  implements java.io.Serializable {

	private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
	private String resourceId;
	private String deviceId;

	public DeviceResource() {
	}

	public DeviceResource(String id, String resourceId, String deviceId) {
		this.id = id;
		this.resourceId = resourceId;
		this.resourceId = resourceId;
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

	@JoinColumn(name = "resource_id", nullable = false)
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	@JoinColumn(name = "device_id", nullable = false)
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
