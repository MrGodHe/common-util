/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;

import org.springframework.util.StringUtils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * 简单实现的Job上下文，该上下文实现了除了响应中断的所有方法。
 * 
 * @author zhyang (e-mail:zhyang@ebank.pay.cn)
 * 
 */
public abstract class SimpleJobContext extends JobContext {
	
	private NodeWrapper root;
	
	private Deque<NodeWrapper> savedNodes;
	
	private MobilityType mobilityType = MobilityType.NEXT;
	
	private boolean isSave = false;
	
	private boolean isIgnore = false;
	
	private boolean isSaveIgnore = false;
	
	private String nextName;
	
	private boolean isFirst = true;
	
	/**
	 * 构造一个SimpleJobContext。
	 * 
	 * @param root Job节点树的根节点。
	 * @param projectGlobalParams 项目全局参数，如果没有则为 null 。
	 */
	public SimpleJobContext(NodeWrapper root, Map<Object, Object> projectGlobalParams) {
		super(projectGlobalParams);
		this.root = root;
		this.savedNodes = new LinkedList<NodeWrapper>();
	}
	
	public void next() {
		next0(null, false);
	}
	
	public void next(boolean isSave) {
		next0(null, isSave);
	}
	
	public void next(String name) {
		next0(name, false);
	}
	
	public void next(String name, boolean isSave) {
		next0(name, isSave);
	}
	
	private void next0(String name, boolean isSave) {
		this.nextName = name;
		this.isSave = isSave;
		this.mobilityType = MobilityType.NEXT;
	}
	
	public void invoke() throws Exception {
		if (this.isFirst) {
			NodeWrapper currentNode = (NodeWrapper) getCurrentNode();
			if (currentNode == null) {
				currentNode = this.root;
			}
			if (currentNode == null) {
				return;
			}
			invokeNode(currentNode);
			return;
		}
		if (isDone()) {
			throw new IllegalStateException("任务'" + getJob().getName() + "' 已经完成。");
		}
		switch (this.mobilityType) {
			case NEXT:
				NodeWrapper currentNode = (NodeWrapper) getCurrentNode();
				if (currentNode == null) {
					currentNode = this.root;
					this.savedNodes.push(currentNode);
					invokeNode(currentNode);
				} else {
					if (this.isSave) {
						this.savedNodes.push(currentNode);
						this.isSave = false;
					}
					Map<String, NodeWrapper> children = currentNode.getChildren();
					doNext(children);
				}
				break;
			
			case BACK:
				try {
					if (this.isIgnore) {
						NodeWrapper backNode = this.isSaveIgnore ? this.savedNodes.getLast()
							: this.savedNodes.pop();
						doNext(backNode.getChildren());
						this.isIgnore = false;
					} else {
						invokeNode(this.savedNodes.pop());
					}
				} catch (NoSuchElementException e) {
					throw new BackException(getCurrentNode().getName(), "节点 '"
																		+ getCurrentNode()
																			.getName() + "' 无法返回。");
				}
				break;
		}
	}
	
	private void doNext(Map<String, NodeWrapper> children) throws Exception {
		if (children != null) {
			if (children.size() == 0) {
				setDone(true);
				return;
			}
			if (!StringUtils.hasLength(this.nextName)) {
				for (Node node : children.values()) {
					invokeNode(node);
					break;
				}
			} else {
				Node node = children.get(this.nextName);
				if (node == null) {
					throw new NoSuchNodeException("任务'" + getJob().getName() + "' 的结点 '"
													+ getCurrentNode().getName() + "' 后没有结点 '"
													+ this.nextName + "'", this.nextName);
				}
				this.nextName = null;
				invokeNode(node);
			}
		}
	}
	
	private void invokeNode(Node node) throws Exception {
		setCurrentNode(node);
		try {
			this.isFirst = false;
			node.invoke(this);
		} catch (Exception e) {
			node.resolveException(this, e);
		}
	}
	
	public void back() {
		this.mobilityType = MobilityType.BACK;
		this.isIgnore = false;
		this.isSave = false;
		this.nextName = null;
		this.isSaveIgnore = false;
	}
	
	public void back(String name) {
		back0(name, false);
	}
	
	public void back(String name, boolean isSaveIgnore) {
		back0(name, isSaveIgnore);
	}
	
	private void back0(String name, boolean isSaveIgnore) {
		this.isIgnore = true;
		this.nextName = name;
		this.mobilityType = MobilityType.BACK;
		this.isSave = false;
		this.isSaveIgnore = isSaveIgnore;
	}
	
	public boolean isBack() {
		return this.mobilityType == MobilityType.BACK;
	}
	
	public boolean isNext() {
		return this.mobilityType == MobilityType.NEXT;
	}
	
	/**
	 * 移动的类型。
	 * 
	 * @author zhyang (e-mail:zhyang@ebank.pay.cn)
	 * 
	 */
	protected enum MobilityType {
		
		/** 向下一级别移动 */
		NEXT,
		
		/** 向前移动 */
		BACK;
		
	}
	
	/**
	 * 节点的包装器。
	 * 
	 * @author zhyang (e-mail:zhyang@ebank.pay.cn)
	 * 
	 */
	public static class NodeWrapper implements Node {
		
		private static final Reference<Class<?>> REFERENCE = new WeakReference<Class<?>>(
			Collections.emptyMap().getClass());
		
		private final Node node;
		
		private final NodeWrapper parent;
		
		private Map<String, NodeWrapper> children;
		
		/**
		 * 创建一个节点包装器。
		 * 
		 * @param node 被包装的节点。
		 * @param parent 被包装节点的父节点的包装器。
		 * @param children 被包装节点的子节点的包装器。
		 */
		public NodeWrapper(Node node, NodeWrapper parent, Map<String, NodeWrapper> children) {
			this.node = node;
			this.parent = parent;
			if (children == null) {
				this.children = Collections.emptyMap();
			} else {
				this.children = children;
			}
		}
		
		public void invoke(JobContext context) throws Exception {
			this.node.invoke(context);
		}
		
		public String getName() {
			return this.node.getName();
		}
		
		public String getCaption() {
			return this.node.getCaption();
		}
		
		/**
		 * 得到 被包装节点的父节点的包装器。
		 * 
		 * @return 被包装节点的父节点的包装器。
		 */
		public NodeWrapper getParent() {
			return this.parent;
		}
		
		/**
		 * 得到 被包装节点的子节点的包装器。
		 * 
		 * @return 被包装节点的子节点的包装器。
		 */
		public Map<String, NodeWrapper> getChildren() {
			return this.children;
		}
		
		/**
		 * 得到 被包装的节点。
		 * 
		 * @return 被包装的节点。
		 */
		public Node getNode() {
			return this.node;
		}
		
		public void resolveException(JobContext context, Exception exception) throws Exception {
			this.node.resolveException(context, exception);
		}
		
		void addChild(String key, NodeWrapper child) {
			Class<?> mapClass = REFERENCE.get();
			if (mapClass != null && mapClass.isAssignableFrom(this.children.getClass())) {
				this.children = new LinkedHashMap<String, NodeWrapper>();
			}
			this.children.put(key, child);
		}
		
		@Override
		public boolean equals(Object obj) {
			return this.node.equals(obj);
		}
		
		@Override
		public int hashCode() {
			return this.node.hashCode();
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName() + " [name=" + this.node.getName() + ", caption="
					+ this.node.getCaption() + "]";
		}
		
	}
	
}
