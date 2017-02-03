/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config;

import cn.pay.ebank.process.dynamic.ClassedDynamicJobApplicationContext;
import cn.pay.ebank.process.dynamic.DynamicJobApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * 基于 class 文件的动态节点加载器实现。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public abstract class AbstractClassedDynamicNodeLoader implements ClassedDynamicNodeLoader {
	
	/**
	 * classpath参数表达式前缀，用于指定从classpath下扫描路径
	 */
	public static final String CLASS_PATH_START = "classpath://";
	
	/**
	 * classpath参数表达式前缀，用于指定从classpath下扫描路径，和 {@link #CLASS_PATH_START}
	 * 的区别是这个参数指示扫描所有可能的路径
	 */
	public static final String CLASS_PATH_ALL_START = "classpath*://";
	
	/**
	 * 加载器名称
	 */
	protected String name;
	
	/**
	 * 加载的 classpath ，从该路径列表中的路径加载类
	 */
	protected Set<String> classpaths = new LinkedHashSet<String> ();
	
	/**
	 * 扫描注解的基本包路径
	 */
	protected Set<String> basePackages = new LinkedHashSet<String> ();
	
	/**
	 * 需要动态加载类的路径表达式集合
	 */
	protected Set<String> includepathPatterns;
	
	/**
	 * 需要排除动态加载类的路径表达式集合
	 */
	protected Set<String> excludePathPatterns;
	
	/**
	 * 构建一个 AbstractClassedDynamicNodeLoader 。
	 */
	public AbstractClassedDynamicNodeLoader () {
		
	}
	
	/**
	 * 设置名称。
	 *
	 * @param name 名称。
	 */
	public void setName (String name) {
		this.name = name;
	}
	
	public String getName () {
		return this.name;
	}
	
	/**
	 * 设置需要动态加载类的路径表达式集合。
	 *
	 * @param includepathPatterns 需要动态加载类的路径表达式集合。
	 */
	public void setIncludepathPatterns (Set<String> includepathPatterns) {
		this.includepathPatterns = includepathPatterns;
	}
	
	/**
	 * 设置需要排除动态加载类的路径表达式集合。
	 *
	 * @param excludePathPatterns 需要排除动态加载类的路径表达式集合。
	 */
	public void setExcludePathPatterns (Set<String> excludePathPatterns) {
		this.excludePathPatterns = excludePathPatterns;
	}
	
	/**
	 * 设置加载的 classpath。
	 *
	 * @param classpaths 加载的 classpath。
	 */
	public void setClasspaths (Set<String> classpaths) {
		this.classpaths = classpaths;
	}
	
	public DynamicJobApplicationContext createApplicationContext (String jobId, ApplicationContext parentApplicationContext) {
		Set<String> basePackages = getBasePackages ();
		String[] basePackagesArray = basePackages.toArray (new String[basePackages.size ()]);
		ClassedDynamicJobApplicationContext context = new ClassedDynamicJobApplicationContext ();
		if (basePackagesArray.length != 0) {
			context.scan (basePackagesArray);
		}
		Set<String> classpaths = getClasspaths ();
		List<URL> classpathUrl = new ArrayList<URL> ();
		// 得到父加载器
		ClassLoader parentClassLoader = null;
		for (ApplicationContext ac = parentApplicationContext; ac != null; ) {
			if (ac instanceof ConfigurableApplicationContext) {
				parentClassLoader = ((ConfigurableApplicationContext) ac).getBeanFactory ().getBeanClassLoader ();
				break;
			} else {
				ac = ac.getParent ();
			}
		}
		if (parentClassLoader == null) {
			parentClassLoader = Thread.currentThread ().getContextClassLoader ();
		}
		for (String classpath : classpaths) {
			if (StringUtils.hasLength (classpath) && classpath.startsWith (CLASS_PATH_START)) {
				String path = classpath.trim ().substring (CLASS_PATH_START.length ());
				URL url = parentClassLoader.getResource (path);
				if (url == null) {
					url = Thread.currentThread ().getContextClassLoader ().getResource (path);
				}
				if (url == null) {
					url = getClass ().getClassLoader ().getResource (path);
				}
				if (url == null) {
					url = ClassLoader.getSystemResource (path);
				}
				if (url == null) {
					throwClasspathError (jobId, classpath);
				}
				classpathUrl.add (url);
			} else if (StringUtils.hasLength (classpath) && classpath.startsWith (CLASS_PATH_ALL_START)) {
				String path = classpath.trim ().substring (CLASS_PATH_ALL_START.length ());
				Set<URL> urls = new LinkedHashSet<URL> ();
				appendUrl (urls, parentClassLoader.getResource (path));
				appendUrl (urls, Thread.currentThread ().getContextClassLoader ().getResource (path));
				appendUrl (urls, getClass ().getClassLoader ().getResource (path));
				appendUrl (urls, ClassLoader.getSystemResource (path));
				if (urls.size () == 0) {
					throwClasspathError (jobId, classpath);
				}
				classpathUrl.addAll (urls);
			} else {
				try {
					classpathUrl.add (new URL (classpath));
				} catch (MalformedURLException e) {
					throw new ApplicationContextException ("DynamicJob '" + jobId + "' 的动态节点加载器 '" + getName () + "' 中定义的类加载路径 '" + classpath + "' 不是有效的 [URL] 。");
				}
			}
		}
		createClassLoader (context, classpathUrl, this.includepathPatterns, this.excludePathPatterns, parentClassLoader);
		return context;
	}
	
	/**
	 * 创建用于加载类的类加载器，并设置到应用上下文中。
	 *
	 * @param context             应用上下文。
	 * @param classpathUrl        加载的 classpath 的 URL 列表。
	 * @param includePathPatterns 需要动态加载类的路径表达式集合。
	 * @param excludePathPatterns 需要排除动态加载类的路径表达式集合。
	 * @param parentClassLoader   创建的类加载器所使用的父类加载器。
	 */
	protected void createClassLoader (ClassedDynamicJobApplicationContext context, List<URL> classpathUrl, Set<String> includePathPatterns, Set<String> excludePathPatterns, ClassLoader parentClassLoader) {
		final List<URL> cpu = classpathUrl;
		final ClassLoader pcl = parentClassLoader;
		URLDynamicFileClassLoader beanClassLoader = AccessController.doPrivileged (new PrivilegedAction<URLDynamicFileClassLoader> () {

			public URLDynamicFileClassLoader run () {
				return new URLDynamicFileClassLoader (cpu, pcl);
			}

		});
		beanClassLoader.setIncludePathPatterns (includePathPatterns);
		beanClassLoader.setExcludePathPatterns (excludePathPatterns);
		context.setClassLoader (beanClassLoader);
		context.getBeanFactory ().setBeanClassLoader (beanClassLoader);
	}
	
	private void throwClasspathError (String jobId, String classpath) {
		throw new ApplicationContextException ("DynamicJob '" + jobId + "' 的动态节点加载器 '" + getName () + "' 中定义的类加载路径 '" + classpath + "' 无法从当前上下文中进行加载 。");
	}
	
	private void appendUrl (Set<URL> urls, URL url) {
		if (url != null) {
			urls.add (url);
		}
	}
	
	public Set<String> getBasePackages () {
		return Collections.unmodifiableSet (this.basePackages);
	}
	
	public void setBasePackages (Set<String> basePackages) {
		if (basePackages == null) {
			return;
		}
		this.basePackages = basePackages;
	}
	
	public boolean addBasePackage (String basePackage) {
		if (basePackage == null) {
			return false;
		}
		return this.basePackages.add (basePackage);
	}
	
	public boolean removeBasePackage (String basePackage) {
		if (basePackage == null) {
			return false;
		}
		return this.basePackages.remove (basePackage);
	}
	
	public boolean hasBasePackage (String basePackage) {
		if (basePackage == null) {
			return false;
		}
		return this.basePackages.contains (basePackage);
	}
	
	public Set<String> getClasspaths () {
		return Collections.unmodifiableSet (this.classpaths);
	}
	
	public void setCpasspaths (Set<String> classpaths) {
		if (classpaths == null) {
			return;
		}
		this.classpaths = classpaths;
	}
	
	public boolean addClasspath (String classpath) {
		if (classpath == null) {
			return false;
		}
		return this.classpaths.add (classpath);
	}
	
	public boolean removeClasspath (String classpath) {
		if (classpath == null) {
			return false;
		}
		return this.classpaths.remove (classpath);
	}
	
	public boolean hasClasspath (String classpath) {
		if (classpath == null) {
			return false;
		}
		return this.classpaths.contains (classpath);
	}
	
	/**
	 * 使用指定的URL加载类的类加载器。
	 * <p/>
	 * 该类加载器只会加载指定类名表达式指定的类，如果没有指定，则加载指定URL下的所有类。
	 *
	 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
	 */
	protected static class URLDynamicFileClassLoader extends URLClassLoader {
		
		/**
		 * 需要动态加载类的路径表达式集合
		 */
		protected Set<String> includePathPatterns;
		
		/**
		 * 需要排除动态加载类的路径表达式集合
		 */
		protected Set<String> excludePathPatterns;
		
		/**
		 * 路径匹配器
		 */
		protected PathMatcher pathMatcher = new AntPathMatcher ();
		
		/**
		 * 构建一个 URLDynamicFileClassLoader 。
		 *
		 * @param urls    从其位置加载类和资源的 URL 列表。
		 * @param parent  用于委托的父类加载器 。
		 * @param factory 创建 URL 时使用的 URLStreamHandlerFactory 。
		 */
		public URLDynamicFileClassLoader (List<URL> urls, ClassLoader parent, URLStreamHandlerFactory factory) {
			super (urls.toArray (new URL[urls.size ()]), parent, factory);
		}
		
		/**
		 * 构建一个 URLDynamicFileClassLoader 。
		 *
		 * @param urls   从其位置加载类和资源的 URL 列表。
		 * @param parent 用于委托的父类加载器 。
		 */
		public URLDynamicFileClassLoader (List<URL> urls, ClassLoader parent) {
			super (urls.toArray (new URL[urls.size ()]), parent);
		}
		
		/**
		 * 构建一个 URLDynamicFileClassLoader 。
		 *
		 * @param urls 从其位置加载类和资源的 URL 列表。
		 */
		public URLDynamicFileClassLoader (List<URL> urls) {
			super (urls.toArray (new URL[urls.size ()]));
		}
		
		/**
		 * 设置需要动态加载类的路径表达式集合。
		 *
		 * @param includePathPatterns 需要动态加载类的路径表达式集合。
		 */
		public void setIncludePathPatterns (Set<String> includePathPatterns) {
			this.includePathPatterns = includePathPatterns;
		}
		
		/**
		 * 设置需要排除动态加载类的路径表达式集合。
		 *
		 * @param excludePathPatterns 需要排除动态加载类的路径表达式集合。
		 */
		public void setExcludePathPatterns (Set<String> excludePathPatterns) {
			this.excludePathPatterns = excludePathPatterns;
		}
		
		@Override
		protected synchronized Class<?> loadClass (String name, boolean resolve) throws ClassNotFoundException {
			Class<?> clazz = findLoadedClass (name);
			if (clazz == null) {
				if (CollectionUtils.isEmpty (this.excludePathPatterns)) {
					this.excludePathPatterns = Collections.emptySet ();
				}
				if (CollectionUtils.isEmpty (this.includePathPatterns)) {
					clazz = load (name);
				} else {
					for (String pathPattern : this.includePathPatterns) {
						if (this.pathMatcher.match (pathPattern, name)) {
							clazz = load (name);
							break;
						}
					}
				}
				if (clazz == null) {
					ClassLoader parent = getParent ();
					if (parent != null) {
						clazz = parent.loadClass (name);
					} else {
						throw new ClassNotFoundException (name);
					}
				}
			}
			if (resolve) {
				resolveClass (clazz);
			}
			return clazz;
		}
		
		private Class<?> load (String name) throws ClassNotFoundException {
			Class<?> clazz = null;
			boolean isLoad = true;
			for (String pathPattern : this.excludePathPatterns) {
				if (this.pathMatcher.match (pathPattern, name)) {
					isLoad = false;
					break;
				}
			}
			if (isLoad) {
				clazz = findClass (name);
			}
			return clazz;
		}
		
	}
	
}