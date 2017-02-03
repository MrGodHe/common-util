/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import java.io.Serializable;

/**
 * bean 名称持有器。
 * 
 * @author
 * 
 */
public class BeanNameHolder implements Serializable {
	
	private static final long serialVersionUID = -8949520325008158258L;
	
	/** bean名称 */
	protected String beanName;
	
	/** 引用spring的bean的名称/id */
	protected String refName;
	
	/** 表示是使用类型（type）创建还是使用名称引用（refName），为 true 表示使用名称引用 */
	protected boolean isRef;
	
	/** 使用创建 bean 的类型信息 */
	protected String type;
	
	/**
	 * 构建一个 BeanNameHolder 。
	 * @param beanName bean 的名称。
	 */
	public BeanNameHolder(String beanName) {
		this.beanName = beanName;
	}
	
	/**
	 * 得到bean名称。
	 * @return bean名称。
	 */
	public String getBeanName() {
		return this.beanName;
	}
	
	/**
	 * 得到引用spring的bean的名称/id。
	 * @return 引用spring的bean的名称/id。
	 */
	public String getRefName() {
		return this.refName;
	}
	
	/**
	 * 设置引用spring的bean的名称/id。
	 * @param refName 引用spring的bean的名称/id。
	 */
	public void setRefName(String refName) {
		this.isRef = true;
		this.refName = refName;
	}
	
	/**
	 * 得到是使用类型（type）创建还是使用名称引用（refName）。
	 * @return 为 true 表示使用名称引用。
	 */
	public boolean isRef() {
		return this.isRef;
	}
	
	/**
	 * 设置是使用类型（type）创建还是使用名称引用（refName）。
	 * @param isRef 为 true 表示使用名称引用
	 */
	public void setRef(boolean isRef) {
		this.isRef = isRef;
	}
	
	/**
	 * 得到使用创建 bean 的类型信息。
	 * @return 使用创建 bean 的类型信息。
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * 设置使用创建 bean 的类型信息。
	 * @param type 使用创建 bean 的类型信息。
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.beanName == null) ? 0 : this.beanName.hashCode());
		result = prime * result + (this.isRef ? 1231 : 1237);
		result = prime * result + ((this.refName == null) ? 0 : this.refName.hashCode());
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
		if (!(obj instanceof BeanNameHolder)) {
			return false;
		}
		BeanNameHolder other = (BeanNameHolder) obj;
		if (this.beanName == null) {
			if (other.beanName != null) {
				return false;
			}
		} else if (!this.beanName.equals(other.beanName)) {
			return false;
		}
		if (this.isRef != other.isRef) {
			return false;
		}
		if (this.refName == null) {
			if (other.refName != null) {
				return false;
			}
		} else if (!this.refName.equals(other.refName)) {
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
		return "BeanNameHolder [beanName=" + this.beanName + ", refName=" + this.refName
				+ ", isRef=" + this.isRef + ", type=" + this.type + "]";
	}
	
}