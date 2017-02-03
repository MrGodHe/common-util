/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;

import java.util.Map;

/**
 * 默认的 Job上下文 实现，中断方法为空。
 * 
 * @author
 * 
 */
public class DefaultJobContext extends SimpleJobContext {
	
	/**
	 * 构造一个DefaultJobContext。
	 * 
	 * @param root Job节点树的根节点。
	 * @param projectGlobalParams 项目全局参数，如果没有则为 null 。
	 */
	public DefaultJobContext(NodeWrapper root, Map<Object, Object> projectGlobalParams) {
		super(root, projectGlobalParams);
		setCurrentNode(root);
	}
	
	protected void doInterrupt(boolean interrupted) {
		
	}
	
}
