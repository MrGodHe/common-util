package cn.pay.ebank.process.config;

import cn.pay.ebank.process.domain.DomainedProcessJob;
import cn.pay.ebank.process.domain.LoginDomain;
import cn.pay.ebank.process.domain.LoginResult;
import cn.pay.ebank.process.dynamic.DynamicJob;
import cn.pay.ebank.process.dynamic.config.DynamicJobFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 登录测试，用于演示job使用和测试。
 *
 * @version 1.0
 * @filename LoginTest.java
 * @Description <li>Date: 2012-8-10</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
public class LoginTest {
	
	private ApplicationContext context;
	
	@Before
	public void setUp () throws Exception {
		this.context = new ClassPathXmlApplicationContext ("/cn/pay/ebank/process/login-domain.xml");
	}
	
	@Test
	public void testLoad1 () {
		@SuppressWarnings("unchecked") DomainedProcessJob<LoginDomain, LoginResult> job = this.context.getBean ("loginJob", DomainedProcessJob.class);
		try {
			LoginDomain domain = new LoginDomain();
			job.getJobContext ().setDomain (domain);
			job.start ();
		} catch (Exception e) {
			e.printStackTrace ();
		}
		LoginResult result = job.getJobContext ().getResult ();
		System.out.println (result.getStatus ());
		System.out.println (result.getDescription ());
	}
	
	@Test
	public void testLoad2 () {
		@SuppressWarnings("unchecked") DomainedProcessJob<LoginDomain, LoginResult> job = this.context.getBean ("loginJob2", DomainedProcessJob.class);
		try {
			LoginDomain domain = new LoginDomain();
			job.getJobContext ().setDomain (domain);
			job.start ();
		} catch (Exception e) {
			e.printStackTrace ();
		}
		LoginResult result = job.getJobContext ().getResult ();
		System.out.println (result.getStatus ());
		System.out.println (result.getDescription ());
	}
	
	@Test
	public void testDynamic () {
		@SuppressWarnings("unchecked") DynamicJobFactory<DynamicJob> dynamicJobFactory = this.context.getBean ("&dynamicJob", DynamicJobFactory.class);
		try {
			LoginDomain domain = new LoginDomain();
			dynamicJobFactory.getJob ().start ();
			// 刷新，重新加载，包括 Class 和 ClassLoader 都会重新加载
			dynamicJobFactory.refresh ();
			dynamicJobFactory.getJob ().start ();
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
}
