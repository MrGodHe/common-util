/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于存放 Node 在 springframework 中的 引用名的持有器，该持有器还保存 Node 的树形结构。
 * 
 * @author
 * 
 */
public class NodeNameHolder extends BeanNameHolder {
	
	private static final long serialVersionUID = -6833343944859183680L;
	
	private String parentName;
	
	private NodeNameHolder parent;
	
	private List<NodeNameHolder> children = new ArrayList<NodeNameHolder>();
	
	public NodeNameHolder(String beanName) {
		super(beanName);
	}
	
	public String getParentName() {
		return this.parentName;
	}
	
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	public NodeNameHolder getParent() {
		return this.parent;
	}
	
	public void setParent(NodeNameHolder parent) {
		this.parent = parent;
	}
	
	public List<NodeNameHolder> getChildren() {
		return this.children;
	}
	
	public void setChildren(List<NodeNameHolder> children) {
		this.children = children;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.parentName == null) ? 0 : this.parentName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof NodeNameHolder)) {
			return false;
		}
		NodeNameHolder other = (NodeNameHolder) obj;
		if (this.parentName == null) {
			if (other.parentName != null) {
				return false;
			}
		} else if (!this.parentName.equals(other.parentName)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "NodeNameHolder [parentName=" + this.parentName + ", parent=" + this.parent
				+ ", children=" + this.children + ", beanName=" + this.beanName + ", refName="
				+ this.refName + ", isRef=" + this.isRef + ", type=" + this.type + "]";
	}
	
}
