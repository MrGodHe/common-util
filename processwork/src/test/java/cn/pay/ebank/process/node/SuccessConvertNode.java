package cn.pay.ebank.process.node;


import cn.pay.ebank.process.domain.DomainedProcessJobContext;
import cn.pay.ebank.process.domain.LoginDomain;
import cn.pay.ebank.process.domain.LoginResult;
import cn.pay.ebank.process.domain.SimpleDomainNode;

/**
 * 登录成功的转换节点，通过继承转换节点简化操作。
 *                       
 * @filename SuccessConvertNode.java
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
public class SuccessConvertNode extends SimpleDomainNode<LoginDomain, LoginResult> {
	
	public SuccessConvertNode(String name) {
		super(name);
	}

	@Override
	protected void doJobProcess (LoginDomain domain) {
		LoginResult loginResult = new LoginResult();
		loginResult.setStatus ("SUCCESS");
		loginResult.setDescription ("用户 '" + domain.getUsername () + "' 登录成功。");
		getJobContext ().next();
		setResult (loginResult);
	}


}
