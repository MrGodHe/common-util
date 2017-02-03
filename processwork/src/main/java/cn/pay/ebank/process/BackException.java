/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;

/**
 * 在使用 {@link JobContext#back()} （以及相关方法） 时，如果没有节点可以返回则抛出该异常。
 * 
 * @author
 * 
 */
public class BackException extends JobException {
	
	private static final long serialVersionUID = -2710041958926960916L;
	
	private String nodeName;
	
	public BackException(String nodeName, String msg) {
		super(msg);
		this.nodeName = nodeName;
	}
	
	/**
	 * 得到无法返回的节点名。
	 * @return 无法返回的节点名。
	 */
	public String getNodeName() {
		return this.nodeName;
	}
	
}
