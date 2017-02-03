/*
 * www.yiji.com Inc.
 * Copyright (c) 2012 All Rights Reserved.
 */
package cn.pay.ebank.process.support;

import cn.pay.ebank.process.JobContext;

/**
 * 通过表达式从JobContext的property取值作为下一个节点名称的节点。
 * <p>
 * 表达式为 <code>SPEL</code> ，并以当前 {@link JobContext} 实例为表达式第一个参数取值，
 * <p>
 * 如果取值成功，则调用 {@link JobContext#next(String)} 。
 * <p>
 * 如果没有设置表达式，则该节点不执行任何操作。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class SelectNameByContextNode extends SelectNameNodeSupport {
	
	/**
	 * 构建一个 SelectNameByContextNode 。
	 * 
	 * @param name 节点的名称。
	 */
	public SelectNameByContextNode(String name) {
		super(name);
	}
	
}
