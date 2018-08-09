package com.zsz.front.servlet;

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

import com.zsz.front.utils.FrontUtils;
import com.zsz.tools.AjaxResult;



public class BasicServlet extends HttpServlet {
	private static final Logger logger=LogManager.getLogger(BasicServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action=req.getParameter("action");
		if(StringUtils.isEmpty(action)){
			FrontUtils.showError(req, resp, "action不能为空");
		}
		try {
			Method actionMethod=this.getClass().getMethod(action, HttpServletRequest.class,HttpServletResponse.class);
			if(actionMethod==null){
				FrontUtils.showError(req, resp, "找不到名字为"+action+"的方法");
				return;
			}
			actionMethod.invoke(this, req,resp);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error("action调用方法出错"+action,e);
			FrontUtils.showError(req, resp, "action调用出错"+action);
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
	public void  writeJson(HttpServletResponse resp,AjaxResult ajaxResult) throws IOException{
		 resp.setContentType("application/json");
		 resp.getWriter().print(ajaxResult.toJson());
	 }

}
