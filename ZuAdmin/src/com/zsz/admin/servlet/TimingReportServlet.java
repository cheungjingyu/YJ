package com.zsz.admin.servlet;

import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
@WebServlet(value="/TimingReportServlet",loadOnStartup=1)
public class TimingReportServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log=LogManager.getLogger(TimingReportServlet.class);
  @Override
	public void init() throws ServletException {
		super.init();
		try{
			log.debug("开始执行发送报表的定时任务的安排");
			Scheduler scheduler=StdSchedulerFactory.getDefaultScheduler();
			JobDetail job=JobBuilder.newJob(ReportJob.class).build();
			CronScheduleBuilder cronScheduleBuilder=
					CronScheduleBuilder.dailyAtHourAndMinute(14, 10).inTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			CronTrigger cronTrigger=TriggerBuilder.newTrigger().withSchedule(cronScheduleBuilder).build();
			scheduler.scheduleJob(job, cronTrigger);
			scheduler.start();//不要忘了启动
			log.debug("完成执行发送报表的定时任务的安排");
			
		}catch(Throwable ex){
			log.error("启动定时任务出错",ex);
		}
		
	}
}
