package com.zsz.admin.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zsz.admin.utils.AdminUtils;
import com.zsz.service.AdminUserService;
import com.zsz.tools.AjaxResult;




public class BasicServlet extends HttpServlet {
	
	private static final Logger logger=LogManager.getLogger(BasicServlet.class);
@Override
public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String action=req.getParameter("action");
	if(StringUtils.isEmpty(action)){
		//resp.getWriter().println("action为空");
		AdminUtils.showError(req, resp, "action为空");
		logger.warn("action为空");
		return;
	}
		Class clz=this.getClass();//Class是子类的class，不是BasicServlet
		//约定方法的名字就是    action（req,resp）的名字
		
			try {
				Method methodAction=clz.getMethod(action, HttpServletRequest.class,HttpServletResponse.class);
				//拿到public void index(HttpServletRequest req, HttpServletResponse resp)
				
				//获得方法上标注的“是否允许匿名访问”
				AllowAnonymous allowAnonymous=methodAction.getAnnotation(AllowAnonymous.class);
				if(allowAnonymous==null){
					//统一检查用户是否有登陆，如果没用登录，则不执行methodAction.invoke
					Long adminUserId=AdminUtils.getAdminUserId(req);
					if(adminUserId==null){
						AdminUtils.showError(req, resp, "未登录");
						return;
					}
					//登录成功
					HasPermission hasPermission=methodAction.getAnnotation(HasPermission.class);
					if(hasPermission!=null){
						AdminUserService adminUserService=new AdminUserService();
						boolean isOk=adminUserService.hasPermission(adminUserId, hasPermission.value());
						if(!isOk){
							AdminUtils.showError(req, resp, "无权访问");
							return;
						}
					}
				}
				
					methodAction.invoke(this,req,resp);//调用方法
				
			} catch (NoSuchMethodException | SecurityException e) {
				//resp.getWriter().println("cannot invoke action method"+action);
				AdminUtils.showError(req, resp, "cannot invoke action method"+action);
				logger.warn("找不到"+action+"方法");
			 } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				 //resp.getWriter().println("invoke  method"+action+"error");
				 AdminUtils.showError(req, resp, "invoke  method "+action+" error");
				 logger.warn(action+"方法调用出错");
			}
	}
@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
 public void  writeJson(HttpServletResponse resp,AjaxResult ajaxResult) throws IOException{
	 resp.setContentType("application/json");
	 resp.getWriter().print(ajaxResult.toJson());
 }

}
