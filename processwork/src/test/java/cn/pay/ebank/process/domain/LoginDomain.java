package cn.pay.ebank.process.domain;

/**
 * 登录的领域对象。
 *                       
 * @filename LoginDomain.java
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
public class LoginDomain implements IDomain {
	
	/** Comment for <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -3988054026867056951L;
	
	private String				username;
	
	private String				password;
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void examSelf() throws Exception {
	}
	
}
