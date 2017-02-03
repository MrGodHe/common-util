package cn.pay.ebank.process.domain;


import cn.pay.ebank.process.JobContext;
import cn.pay.ebank.process.exception.ExceptionHandler;

/**
 * 登录异常的异常处理器。
 *                       
 * @filename LoginExceptionHandler.java
 *
 * @Description 
 *
 * @version 1.0
 *
 * @history
 *<li>Date: 2012-8-10</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public class LoginExceptionHandler implements ExceptionHandler {
	
	public void resolveException(JobContext context, Throwable throwable) {
		System.out.println("异常处理器处理异常。");
		System.out.println("处理的异常是：");
		throwable.printStackTrace(System.out);
	}
	
}
