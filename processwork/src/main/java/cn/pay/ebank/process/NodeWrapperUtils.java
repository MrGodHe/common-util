/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;


import cn.pay.ebank.process.SimpleJobContext.NodeWrapper;

import java.util.Map;

/**
 * {@link NodeWrapper} 的工具。
 *
 * @author zhyang (e-mail:zhyang@ebank.pay.cn)
 */
public abstract class NodeWrapperUtils {
	
	/**
	 * 为 parent 添加一个子节点包装器并且返回该包装器，该包装器的父节点为 parent ，子节点为 null 。
	 *
	 * @param name   节点名。
	 * @param node   需要包装的节点。
	 * @param parent 当前节点的父节点。
	 * @return 参数对应的节点包装器。
	 */
	public static NodeWrapper addChildWrapper (String name, Node node, NodeWrapper parent) {
		return addChildWrapper (name, node, parent, null);
	}
	
	/**
	 * 为 parent 添加一个子节点包装器并且返回该包装器，该包装器的父节点为 parent ，子节点为 children 。
	 *
	 * @param name     节点名。
	 * @param node     需要包装的节点。
	 * @param parent   当前节点的父节点。
	 * @param children 当前节点的子节点。
	 * @return 参数对应的节点包装器。
	 */
	public static NodeWrapper addChildWrapper (String name, Node node, NodeWrapper parent, Map<String, NodeWrapper> children) {
		NodeWrapper value = new NodeWrapper (node, parent, children);
		parent.addChild (name, value);
		return value;
	}
	
}
