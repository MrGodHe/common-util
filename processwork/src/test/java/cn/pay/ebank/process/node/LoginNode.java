package cn.pay.ebank.process.node;


import cn.pay.ebank.process.domain.LoginDomain;
import cn.pay.ebank.process.domain.LoginResult;
import cn.pay.ebank.process.domain.SimpleDomainNode;

/**
 * 登录节点，通过继承领域节点简化操作。
 *
 * @author Agreal·Lee
 * @version 1.0
 * @filename LoginNode.java
 * @Description
 * @email lixiang@yiji.com
 * @history <li>Author: Agrael·Lee</li>
 * <li>Date: 2012-8-10</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
public class LoginNode extends SimpleDomainNode<LoginDomain, LoginResult> {
	
	public LoginNode (String name) {
		super (name);
	}

	@Override
	protected void doJobProcess (LoginDomain domain) {
		if ("lixiang".equals (domain.getUsername ()) && "111111".equals (domain.getPassword ())) {
			// 登录成功选取成功转换节点分支
			getJobContext ().setParameter ("loginResult", "successConvert");
		} else {
			// 登录失败选取失败转换节点分支
			getJobContext ().setParameter ("loginResult", "failConvert");
		}
		getJobContext ().next ();
	}
	
}
