/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import cn.pay.ebank.process.Job;
import cn.pay.ebank.process.JobContext;
import cn.pay.ebank.process.Node;
import cn.pay.ebank.process.NodeWrapperUtils;
import cn.pay.ebank.process.SimpleJobContext.NodeWrapper;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于Xml配置文件的JobContextBuilder。
 *
 * @author
 */
public class XmlConfigJobContextBuilder extends JobContextBuilderSupport {
	
	private NodeNameHolder nodeNameHolder;
	
	public void setNodeNameHolder (NodeNameHolder nodeNameHolder) {
		this.nodeNameHolder = nodeNameHolder;
	}
	
	public JobContext build (Job job) {
		// 构建 Node 树
		if (this.root == null) {
			synchronized (this) {
				if (this.root == null) {
					Node startNode = this.applicationContext.getBean (this.nodeNameHolder.getRefName (), Node.class);
					Map<String, NodeWrapper> startNodeChiliren = new LinkedHashMap<String, NodeWrapper> ();
					NodeWrapper root = new NodeWrapper (startNode, null, startNodeChiliren);
					buildChildrenNode (root, this.nodeNameHolder);
					this.root = root;
				}
			}
		}
		// 构建 JobContext
		JobContext jobContext;
		jobContext = createJobContext ();
		jobContext.setJob (job);
		return jobContext;
	}
	
	private void buildChildrenNode (NodeWrapper nodeWrapper, NodeNameHolder nodeNameHolder) {
		for (NodeNameHolder childHolder : nodeNameHolder.getChildren ()) {
			Object obj = this.applicationContext.getBean (childHolder.getRefName ());
			process (nodeWrapper, childHolder, obj, childHolder.getRefName (), childHolder.getBeanName ());
		}
	}
	
	private void process (NodeWrapper nodeWrapper, NodeNameHolder nameHolder, Object obj, String beanRefName, String realName) {
		if (obj instanceof Node) {
			// 为普通 node 节点
			Node node = (Node) obj;
			Map<String, NodeWrapper> nodeChildren = new LinkedHashMap<String, NodeWrapper> ();
			NodeWrapper childrenNodeWrapper = NodeWrapperUtils.addChildWrapper (realName == null ? node.getName () : realName, node, nodeWrapper, nodeChildren);
			buildChildrenNode (childrenNodeWrapper, nameHolder);
		} else if (obj instanceof ChildNodeBeanInfo) {
			// child节点引用
			ChildNodeBeanInfo info = (ChildNodeBeanInfo) obj;
			NodeNameHolder holder = info.getRoot ();
			process (nodeWrapper, holder, this.applicationContext.getBean (holder.getRefName ()), holder.getRefName (), nameHolder.getBeanName ());
		} else {
			throw new BeanNotOfRequiredTypeException (beanRefName, Node.class, obj.getClass ());
		}
	}
	
}
