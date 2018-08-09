package com.zsz.front.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.zsz.service.SettingService;
import com.zsz.tools.CommonUtils;

public class RupengSMSAPI {
  public static RupengSMSResult send(String code,String phoneNum){
	  //从系统配置表中读取出用户名、appKey等
	  SettingService settingService=new SettingService();
	  String username=settingService.getValue("RuPengSMS.UserName");
	  String appKey=settingService.getValue("RuPengSMS.AppKey");
	  String templateId=settingService.getValue("RuPengSMS.RegisterTemplateId");
	  //构造请求的get字符串
	  String sendUrl="http://sms.rupeng.cn/SendSms.ashx?userName="+CommonUtils.urlEncodeUTF8(username)+
			  "&appKey="+CommonUtils.urlEncodeUTF8(appKey)+"&templateId="+templateId+"&code="+
			  CommonUtils.urlEncodeUTF8(code)+"&phoneNum="+phoneNum;
         try {
			String resp=IOUtils.toString(new URI(sendUrl),"UTF-8");//发出http请求，获得响应报文
			Gson gson=CommonUtils.createGson();
			return gson.fromJson(resp, RupengSMSResult.class);//解析成Java对象
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
  }
  public static RupengSMSResult sendFindPassword(String code,String phoneNum){
	  //从系统配置表中读取出用户名、appKey等
	  SettingService settingService=new SettingService();
	  String username=settingService.getValue("RuPengSMS.UserName");
	  String appKey=settingService.getValue("RuPengSMS.AppKey");
	  String templateId=settingService.getValue("RuPengSMS.findPasswordTemplateId");
	  //构造请求的get字符串
	  String sendUrl="http://sms.rupeng.cn/SendSms.ashx?userName="+CommonUtils.urlEncodeUTF8(username)+
			  "&appKey="+CommonUtils.urlEncodeUTF8(appKey)+"&templateId="+templateId+"&code="+
			  CommonUtils.urlEncodeUTF8(code)+"&phoneNum="+phoneNum;
         try {
			String resp=IOUtils.toString(new URI(sendUrl),"UTF-8");//发出http请求，获得响应报文
			Gson gson=CommonUtils.createGson();
			return gson.fromJson(resp, RupengSMSResult.class);//解析成Java对象
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
  }
}
