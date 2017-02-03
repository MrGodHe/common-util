/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic;

/**
 * 动态异常处理器信息。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class ExceptionHandlerInformation {
	
	private final String id;
	
	private final String handler;
	
	private final String group;
	
	private final String type;
	
	/**
	 * 构建一个 ExceptionHandlerInformation 。
	 * @param id id。
	 * @param handler 持有器类。
	 * @param group 组名。
	 * @param type 处理的类型。
	 */
	public ExceptionHandlerInformation(String id, String handler, String group, String type) {
		this.id = id;
		this.handler = handler;
		this.group = group;
		this.type = type;
	}
	
	/**
	 * 得到id。
	 * @return id。
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 得到持有器类。
	 * @return 持有器类。
	 */
	public String getHandler() {
		return this.handler;
	}
	
	/**
	 * 得到组名。
	 * @return 组名。
	 */
	public String getGroup() {
		return this.group;
	}
	
	/**
	 * 得到 处理的类型。
	 * @return 处理的类型。
	 */
	public String getType() {
		return this.type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.group == null) ? 0 : this.group.hashCode());
		result = prime * result + ((this.handler == null) ? 0 : this.handler.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ExceptionHandlerInformation)) {
			return false;
		}
		ExceptionHandlerInformation other = (ExceptionHandlerInformation) obj;
		if (this.group == null) {
			if (other.group != null) {
				return false;
			}
		} else if (!this.group.equals(other.group)) {
			return false;
		}
		if (this.handler == null) {
			if (other.handler != null) {
				return false;
			}
		} else if (!this.handler.equals(other.handler)) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!this.type.equals(other.type)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "ExceptionHandlerInformation [id=" + this.id + ", handler=" + this.handler
				+ ", group=" + this.group + ", type=" + this.type + "]";
	}
	
}
