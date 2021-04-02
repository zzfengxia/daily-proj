package com.zz.dailytest;

import com.bcloud.msg.http.HttpSender;
import org.apache.commons.httpclient.protocol.Protocol;

public class HttpSenderTest {

	public static void main(String[] args) {
		Protocol myHttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myHttps);

		String uri = "https://send.18sms.com/msg/HttpBatchSendSM";

		String account = "****";//账号 
		String passWord = "*******";//密码    
		String mobiles = "18673697511";//
		String msg = "您的验证码是:1111";//短信内容
		boolean needStatus = false;//是否需要状态报告，需要true，不需要false
		String product = "";//产品ID(不用填写)
		String extno = "";//扩展码(请登陆网站用户中心——>服务管理找到签名对应的extno并填写，线下用户请为空)
		try {
			String returnString = HttpSender.batchSend(uri, account, passWord, mobiles, msg, needStatus, product, extno);
			System.out.println(returnString);
			//TODO 处理返回值,参见HTTP协议文档
		} catch (Exception e) {	
			//TODO 处理异常
			e.printStackTrace();
		}



	}
}

	