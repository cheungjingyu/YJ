package com.zsz.admin.servlet;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.mail.HtmlEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zsz.service.ReportService;
import com.zsz.service.SettingService;

public class ReportJob implements Job {
	private static final Logger log=LogManager.getLogger(ReportJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.debug("开始执行定时报表任务");
		try{
			ReportService reportService=new ReportService();
			Map<String,Long> map=reportService.getYesterdayCityInfo();
			StringBuilder sb=new StringBuilder();
			for(Entry<String,Long> entry:map.entrySet()){
				sb.append(entry.getKey()+":"+entry.getValue()+"<br/>");
			}
			//发送邮件
			SettingService settingService=new SettingService();
			
			HtmlEmail email=new HtmlEmail();//发送html格式邮件
			email.setHostName(settingService.getValue("Email.Smtp"));
			email.setCharset("UTF-8");
			//登录邮件服务器的用户名和密码
			email.setAuthentication(settingService.getValue("Email.UserName"), settingService.getValue("Email.Password"));
			email.addTo(settingService.getValue("Email.Boss"));
			email.setFrom(settingService.getValue("Email.From"));
			email.setSubject("近24小时的新增房源报表");
			email.setMsg(sb.toString());
			email.send();
			log.debug("完成执行定时报表任务");
		}catch(Throwable ex){
			log.error("执行定时报表任务失败",ex);
		}
		
	}

}
