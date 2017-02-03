package cn.pay.ebank.process.domain;

import cn.pay.ebank.process.result.IResult;

/**
 * 登录结果。
 *                       
 * @filename LoginResult.java
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
public class LoginResult implements IResult{
	
	/** Comment for <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -2568531167256389896L;



	private String status;

	private String code;

	private String description;


	public String getStatus () {
		return status;
	}

	public void setStatus (String status) {
		this.status = status;
	}

	public String getCode () {
		return code;
	}

	public void setCode (String code) {
		this.code = code;
	}

	public String getDescription () {
		return description;
	}

	public void setDescription (String description) {
		this.description = description;
	}
}
