/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;

/**
 * 直接调用 {@link JobContext#setDone(boolean)} 为 true 的节点。
 * 
 * @author
 * 
 */
public class EmptyNode extends AbstractNode {
	
	public EmptyNode(String name) {
		super(name);
	}
	
	public void invoke(JobContext context) throws Exception {
		context.setDone(true);
	}
	
}
