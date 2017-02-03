package cn.pay.ebank.process.domain;

/**
 * 登录过程中发生的异常。
 *                       
 * @filename LoginException.java
 *
 * @Description 
 *
 * @version 1.0
 *
 *
 * @history
 *<li>Date: 2012-8-10</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public class LoginException extends RuntimeException {
	
	private static final long	serialVersionUID	= -593603829644257440L;
	
	public LoginException(String message) {
		super(message);
	}
	
	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
