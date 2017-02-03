/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

/**
 * &lt;child-node&gt; 的 bean 信息。
 * <p>
 * 该信息持有了内部的&lt;child-node&gt; 的信息。
 * 
 * @author
 * 
 */
public class ChildNodeBeanInfo {
	
	private NodeNameHolder root;
	
	/**
	 * 得到头信息。
	 * @return 头信息。
	 */
	public NodeNameHolder getRoot() {
		return this.root;
	}
	
	/**
	 * 设置头信息。
	 * @param root 头信息。
	 */
	public void setRoot(NodeNameHolder root) {
		this.root = root;
	}
	
}
