/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config;

import cn.pay.ebank.process.*;
import cn.pay.ebank.process.SimpleJobContext.NodeWrapper;
import cn.pay.ebank.process.config.JobFactorySupport;
import cn.pay.ebank.process.config.NodeNameHolder;
import cn.pay.ebank.process.dynamic.DefaultDynamicJob;
import cn.pay.ebank.process.dynamic.DynamicJob;
import cn.pay.ebank.process.dynamic.DynamicJobApplicationContext;
import cn.pay.ebank.process.dynamic.ExceptionHandlerInformation;
import cn.pay.ebank.process.exception.ExceptionHandler;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 实现 DynamicJobFactory 的支持类，继承该类可减少实现工作。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public abstract class DynamicJobFactorySupport<J extends DynamicJob> extends JobFactorySupport<DynamicJob> implements DynamicJobFactory<DynamicJob> {
	
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock ();
	
	/**
	 * 得到 Job 时的锁
	 */
	protected final Lock readLock = this.readWriteLock.readLock ();
	/**
	 * 刷新时的锁
	 */
	protected final Lock writeLock = this.readWriteLock.writeLock ();
	
	/**
	 * 动态节点加载器集合
	 */
	protected List<DynamicNodeLoader> dynamicNodeLoaders = new ArrayList<DynamicNodeLoader> (0);
	
	/**
	 * key为 bean名称 value 为存放 bean名称 的 bean实例 的 Map
	 */
	protected Map<String, DynamicJobApplicationContext> beanNamesMap = new LinkedHashMap<String, DynamicJobApplicationContext> ();
	
	/**
	 * 存放加载动态 Node 的应用上下文 集合
	 */
	protected Set<DynamicJobApplicationContext> contexts = new LinkedHashSet<DynamicJobApplicationContext> ();
	
	/**
	 * 存放 异常处理器信息 的 Map ，key 为组名 ，value 为 处理器信息
	 */
	protected Map<String, Set<ExceptionHandlerInformation>> exceptionHandlerNamesMap = new LinkedHashMap<String, Set<ExceptionHandlerInformation>> ();
	
	/**
	 * 存放 Node信息 的 Map ，key 为 Node 的 id ，value 为 Node信息
	 */
	protected Map<String, NodeNameHolder> nodeNameHolderMap = new LinkedHashMap<String, NodeNameHolder> ();
	
	/**
	 * 动态 Job 的头节点名
	 */
	protected String root;
	
	/**
	 * Job使用异常处理器组的组名
	 */
	protected String exceptionHandlerGroup;
	
	/**
	 * 动态任务上下文构造器
	 */
	protected volatile DynamicJobContextBuilder dynamicJobContextBuilder;
	
	/**
	 * 设置动态 Job 的头节点名。
	 *
	 * @param root 动态 Job 的头节点名。
	 */
	public void setRoot (String root) {
		this.root = root;
	}
	
	/**
	 * 得到动态 Job 的头节点名。
	 *
	 * @return 动态 Job 的头节点名。
	 */
	public String getRoot () {
		return this.root;
	}
	
	/**
	 * 设置Job使用异常处理器组的组名。
	 *
	 * @param exceptionHandlerGroup Job使用异常处理器组的组名。
	 */
	public void setExceptionHandlerGroup (String exceptionHandlerGroup) {
		this.exceptionHandlerGroup = exceptionHandlerGroup;
	}
	
	/**
	 * 得到存放 异常处理器信息 的 Map。
	 *
	 * @return 存放 异常处理器信息 的 Map ，key 为组名 ，value 为 处理器信息。
	 */
	public Map<String, Set<ExceptionHandlerInformation>> getExceptionHandlerNamesMap () {
		return this.exceptionHandlerNamesMap;
	}
	
	/**
	 * 得到存放 Node信息 的 Map 。
	 *
	 * @return 存放 Node信息 的 Map ，key 为 Node 的 id ，value 为 Node信息 。
	 */
	public Map<String, NodeNameHolder> getNodeNameHolderMap () {
		return this.nodeNameHolderMap;
	}
	
	public void setDynamicNodeLoaders (List<DynamicNodeLoader> dynamicNodeLoaders) {
		this.dynamicNodeLoaders = dynamicNodeLoaders;
	}
	
	public List<DynamicNodeLoader> getDynamicNodeLoaders () {
		return Collections.unmodifiableList (this.dynamicNodeLoaders);
	}
	
	public boolean addDynamicNodeLoader (DynamicNodeLoader dynamicNodeLoader) {
		if (dynamicNodeLoader == null || this.dynamicNodeLoaders.contains (dynamicNodeLoader)) {
			return false;
		}
		return this.dynamicNodeLoaders.add (dynamicNodeLoader);
	}
	
	public boolean removeDynamicNodeLoader (DynamicNodeLoader dynamicNodeLoader) {
		return this.dynamicNodeLoaders.remove (dynamicNodeLoader);
	}
	
	public boolean hasDynamicNodeLoader (DynamicNodeLoader dynamicNodeLoader) {
		return this.dynamicNodeLoaders.contains (dynamicNodeLoader);
	}
	
	public void refreshLock () {
		this.writeLock.lock ();
	}
	
	public void refreshUnlock () {
		this.writeLock.unlock ();
	}
	
	@SuppressWarnings("unchecked")
	public DynamicJob getObject () throws Exception {
		this.readLock.lock ();
		try {
			DynamicJob job = createJob (DynamicJob.class);
			if (job instanceof DefaultDynamicJob) {
				// 构造上下文
				if (this.dynamicJobContextBuilder == null) {
					synchronized (this) {
						if (this.dynamicJobContextBuilder == null) {
							load ();
							DynamicJobContextBuilder dynamicJobContextBuilder = createJobContextBuilder (this);
							copy (dynamicJobContextBuilder);
							dynamicJobContextBuilder.setApplicationContext (this.applicationContext);
							dynamicJobContextBuilder.setClassLoader (this.classLoader);
							if (StringUtils.hasLength (this.projectGlobalParamsRefName)) {
								Map<Object, Object> projectGlobalParams = getBean (this.projectGlobalParamsRefName, Map.class);
								dynamicJobContextBuilder.setProjectGlobalParams (projectGlobalParams);
							}
							this.dynamicJobContextBuilder = dynamicJobContextBuilder;
						}
					}
				}
				((DefaultDynamicJob) job).setJobContextBuilder (this.dynamicJobContextBuilder);
			}
			// 异常处理器
			if (StringUtils.hasLength (this.exceptionHandlerGroup)) {
				Set<ExceptionHandlerInformation> groupInfos = getExceptionHandlerNamesMap ().get (this.exceptionHandlerGroup);
				Map<Class<Throwable>, ExceptionHandler> eh = new LinkedHashMap<Class<Throwable>, ExceptionHandler> ();
				if (!CollectionUtils.isEmpty (groupInfos)) {
					for (ExceptionHandlerInformation exceptionHandlerInformation : groupInfos) {
						Class<?> typeClass;
						try {
							typeClass = this.classLoader.loadClass (exceptionHandlerInformation.getType ());
						} catch (ClassNotFoundException e) {
							throw new RuntimeException ("[DynamicJob] '" + job.getName () + "'没有找到异常处理类型 '" + exceptionHandlerInformation.getType () + "' 对应的类。", e);
						}
						eh.put ((Class<Throwable>) typeClass, getBean (exceptionHandlerInformation.getId (), ExceptionHandler.class));
					}
					
				}
				job.setExceptionHandlers (eh);
			} else {
				job.setExceptionHandlers (new LinkedHashMap<Class<Throwable>, ExceptionHandler> ());
			}
			job.initOrReInt ();
			return job;
		} finally {
			this.readLock.unlock ();
		}
	}
	
	/**
	 * 创建动态任务上下文构造器。
	 *
	 * @param dynamicJobFactory 使用的动态任务工厂。
	 * @return 创建的动态任务上下文构造器。
	 */
	protected abstract <F extends DynamicJobFactorySupport<J>> DynamicJobContextBuilderSupport<F, J> createJobContextBuilder (DynamicJobFactorySupport<J> dynamicJobFactory);
	
	public Class<Job> getObjectType () {
		return Job.class;
	}
	
	public boolean isSingleton () {
		return false;
	}
	
	public final Job getJob () throws Exception {
		return getObject ();
	}
	
	public void refresh () throws Exception {
		this.writeLock.lock ();
		try {
			// 清理
			this.dynamicJobContextBuilder = null;
			this.beanNamesMap.clear ();
			this.nodeNameHolderMap.clear ();
			this.exceptionHandlerNamesMap.clear ();
			for (DynamicJobApplicationContext context : this.contexts) {
				context.close ();
			}
			this.contexts.clear ();
			// 重新加载
			load ();
		} finally {
			this.writeLock.unlock ();
		}
	}
	
	/**
	 * 完成加载。
	 *
	 * @throws Exception 发生异常时。
	 */
	protected void load () throws Exception {
		for (DynamicNodeLoader nodeLoader : getDynamicNodeLoaders ()) {
			DynamicJobApplicationContext context = nodeLoader.createApplicationContext (this.jobName, this.applicationContext);
			nodeLoader.load (context);
			this.contexts.add (context);
			// 合并bean信息，如果出现同名，则先加载的被后加载的覆盖
			for (String beanName : context.getBeanDefinitionNames ()) {
				this.beanNamesMap.put (beanName, context);
			}
			// 合并 node信息，如果出现同名，则先加载的被后加载的覆盖
			for (String nodeName : context.getNodeNameHolderNames ()) {
				this.nodeNameHolderMap.put (nodeName, context.getNodeNameHolder (nodeName));
			}
			// 合并异常处理器信息
			Set<String> exceptionHandlerGroupNames = context.getExceptionHandlerGroupNames ();
			for (String groupName : exceptionHandlerGroupNames) {
				Set<ExceptionHandlerInformation> groups = context.getExceptionHandlerInformations (groupName);
				Set<ExceptionHandlerInformation> ehns = this.exceptionHandlerNamesMap.get (groupName);
				if (ehns == null) {
					ehns = new LinkedHashSet<ExceptionHandlerInformation> ();
					this.exceptionHandlerNamesMap.put (groupName, ehns);
				}
				ehns.addAll (groups);
			}
		}
	}
	
	/**
	 * 根据 beanName 得到 实例。
	 *
	 * @param beanName 需要得到实例的 bean 的 name 。
	 * @return beanName 对应的实例。
	 */
	public Object getBean (String beanName) {
		if (beanName == null) {
			throwBeanNotFoundException (beanName);
		}
		DynamicJobApplicationContext context = this.beanNamesMap.get (beanName);
		return context == null ? this.applicationContext.getBean (beanName) : context.getBean (beanName);
	}
	
	/**
	 * 通过 beanName 得到 实例 ，并转换为 type 指定的对象。
	 *
	 * @param beanName 需要得到实例的 bean 的 name 。
	 * @param type     需要转换的对象。
	 * @return beanName 对应的实例。
	 */
	public <T> T getBean (String beanName, Class<T> type) {
		if (beanName == null) {
			throwBeanNotFoundException (beanName);
		}
		DynamicJobApplicationContext context = this.beanNamesMap.get (beanName);
		return context == null ? this.applicationContext.getBean (beanName, type) : context.getBean (beanName, type);
	}
	
	Object throwBeanNotFoundException (String beanName) {
		throw new BeanDefinitionStoreException ("Job '" + this.jobName + "'", beanName);
	}
	
	/**
	 * 动态任务上下文加载器支持类，继承该类可以简化实现 {@link DynamicJobContextBuilder} 。
	 *
	 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
	 */
	protected static abstract class DynamicJobContextBuilderSupport<F extends DynamicJobFactorySupport<J>, J extends DynamicJob> extends DynamicJobContextBuilder {
		
		/**
		 * 内部使用的相关联的动态任务工厂
		 */
		protected F dynamicJobFactory;
		
		/**
		 * 构建一个 DynamicJobFactorySupport 。
		 *
		 * @param dynamicJobFactory 内部使用的相关联的动态任务工厂。
		 */
		protected DynamicJobContextBuilderSupport (F dynamicJobFactory) {
			this.dynamicJobFactory = dynamicJobFactory;
		}
		
		public void refresh () throws Exception {
			this.dynamicJobFactory.refresh ();
		}
		
		public JobContext build (Job job) {
			// 如果头节点不存在则创建头节点
			if (this.root == null) {
				synchronized (this) {
					if (this.root == null) {
						this.root = buildRoot ();
					}
				}
			}
			// 创建上下文
			JobContext jobContext = createJobContext ();
			jobContext.setJob (job);
			return jobContext;
		}
		
		/**
		 * 构建头信息。
		 *
		 * @return 构建的头信息。
		 */
		protected NodeWrapper buildRoot () {
			Map<String, NodeNameHolder> nodeNameHolders = this.dynamicJobFactory.getNodeNameHolderMap ();
			// 组装节点父子关系
			for (NodeNameHolder holder : nodeNameHolders.values ()) {
				String parentName = holder.getParentName ();
				if (!StringUtils.hasText (parentName)) {
					// 为头节点
					continue;
				}
				NodeNameHolder parentHolder = nodeNameHolders.get (parentName);
				if (parentHolder == null) {
					throw new RuntimeException ("配置的用于生成[JobContext]的实例的[node]没有找到父节点 {" + parentName + "} 。");
				}
				List<NodeNameHolder> children = parentHolder.getChildren ();
				if (!children.contains (holder)) {
					children.add (holder);
				}
			}
			// 创建节点包装器
			String rootNodeName = this.dynamicJobFactory.getRoot ();
			if (StringUtils.hasLength (rootNodeName)) {
				NodeNameHolder rootHolder = nodeNameHolders.get (rootNodeName);
				if (rootHolder == null) {
					throw new NoSuchBeanDefinitionException (rootNodeName, "配置的用于生成[JobContext]的实例的[node]没有找到头节点 {" + rootNodeName + "} 的引用 。");
				}
				Node rootNode = this.dynamicJobFactory.getBean (rootNodeName, Node.class);
				Map<String, NodeWrapper> startNodeChiliren = new LinkedHashMap<String, NodeWrapper> ();
				NodeWrapper rootNodeWrapper = new NodeWrapper (rootNode, null, startNodeChiliren);
				// 循环创建孩子节点
				buildChildrenNode (rootNodeWrapper, rootHolder);
				return rootNodeWrapper;
			} else {
				return new NodeWrapper (new EmptyNode ("emptyNode"), null, Collections.<String, NodeWrapper>emptyMap ());
			}
		}
		
		private void buildChildrenNode (NodeWrapper nodeWrapper, NodeNameHolder nodeNameHolder) {
			for (NodeNameHolder childHolder : nodeNameHolder.getChildren ()) {
				Node node = this.dynamicJobFactory.getBean (childHolder.getRefName (), Node.class);
				Map<String, NodeWrapper> nodeChildren = new LinkedHashMap<String, NodeWrapper> ();
				NodeWrapper childrenNodeWrapper = NodeWrapperUtils.addChildWrapper (node.getName (), node, nodeWrapper, nodeChildren);
				buildChildrenNode (childrenNodeWrapper, childHolder);
			}
		}
		
		public DynamicJobFactory<?> getDynamicJobFactory () {
			return this.dynamicJobFactory;
		}
	}
	
}
