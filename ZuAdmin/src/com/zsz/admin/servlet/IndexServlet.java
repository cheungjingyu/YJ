package com.zsz.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.zsz.admin.utils.AdminUtils;
import com.zsz.dto.AdminUserDTO;
import com.zsz.service.AdminUserService;
import com.zsz.tools.AjaxResult;
import com.zsz.tools.CommonUtils;
import com.zsz.tools.VerifyCodeUtils;
@WebServlet("/Index")
public class IndexServlet extends BasicServlet {
	public void loginOut(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		req.getSession().invalidate();
		resp.sendRedirect(req.getContextPath()+"/Index?action=login");
	}
	public void index(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		Long adminUserId=AdminUtils.getAdminUserId(req);
		AdminUserDTO adminUser=new AdminUserService().getById(adminUserId);
		req.getSession().setAttribute("adminUser",adminUser);
		req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
	}
	@AllowAnonymous
	public void login(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req,resp);
		
	}
	@AllowAnonymous
	public void loginSubmit(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		String phoneNum=req.getParameter("phoneNum");
		String password=req.getParameter("password");
		String verifyCode=req.getParameter("verifyCode");
		if(StringUtils.isEmpty(verifyCode)){
			writeJson(resp, new AjaxResult("error","验证码必填"));
			return;
		}
		if(StringUtils.isEmpty(phoneNum)){
			writeJson(resp, new AjaxResult("error","手机号必填"));
			return;
		}
		if(StringUtils.isEmpty(password)){
			writeJson(resp, new AjaxResult("error","密码必填"));
			return;
		}
		String inSessCode=(String) req.getSession().getAttribute("code");
		if(!verifyCode.equalsIgnoreCase(inSessCode)){
			writeJson(resp, new AjaxResult("error","验证码不正确"));
			return;
		}
		AdminUserService adminUserService=new AdminUserService();
		if(adminUserService.checkLogin(phoneNum, password)){
			AdminUserDTO adminUser=adminUserService.getByPhoneNum(phoneNum);
			AdminUtils.setAdminUserId(req, adminUser.getId());
			AdminUtils.setAdminUserCityId(req, adminUser.getCityId());
			writeJson(resp, new AjaxResult("ok"));
		}else{
			writeJson(resp, new AjaxResult("error","用户名或者密码错误"));
		}
		
	}
	@AllowAnonymous
	public void verifyCode(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		String code = VerifyCodeUtils.generateVerifyCode(4);
		req.getSession().setAttribute("code", code);
		resp.setContentType("image/jpeg");
		VerifyCodeUtils.outputImage(100, 50,resp.getOutputStream(),code);
	}
}
