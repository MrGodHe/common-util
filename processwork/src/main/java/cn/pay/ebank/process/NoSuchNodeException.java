/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;

/**
 * 在使用 {@link JobContext#next()} （以及相关方法） 时，如果没有节点可以跳转则抛出该异常。
 * 
 * @author
 * 
 */
public class NoSuchNodeException extends JobException {
	
	private static final long serialVersionUID = -2710041958926960916L;
	
	private String nodeName;
	
	public NoSuchNodeException(String msg, String nodeName) {
		super(msg);
		this.nodeName = nodeName;
	}
	
	/**
	 * 得到没有找到的节点的节点名。
	 * @return 没有找到的节点的节点名。
	 */
	public String getNodeName() {
		return this.nodeName;
	}
	
}
