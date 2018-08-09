package com.zsz.front.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zsz.front.utils.FrontUtils;
import com.zsz.front.utils.RupengSMSAPI;
import com.zsz.service.UserService;
import com.zsz.tools.AjaxResult;
import com.zsz.tools.VerifyCodeUtils;
@WebServlet("/User")
public class UserServlet extends BasicServlet {
	public void register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(req, resp);
	}
	public void registerSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String smsCode=req.getParameter("smsCode");
		String phoneNum=req.getParameter("phoneNum");
		String password=req.getParameter("password");
		String serverSmsCode=(String) req.getSession().getAttribute("smsCode");
		String serverPhoneNum=(String)req.getSession().getAttribute("phoneNum");
		if(!serverPhoneNum.equals(phoneNum)){
			writeJson(resp, new AjaxResult("error", "两次手机号不一致"));
			return;
		}
		if(!serverSmsCode.equalsIgnoreCase(smsCode)){
			writeJson(resp, new AjaxResult("error", "短信验证码不正确"));
			return;
		}
		UserService userService=new UserService();
		userService.addnew(phoneNum, password);
		writeJson(resp, new AjaxResult("ok"));
	}
	public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
	}
	public void loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String phoneNum=req.getParameter("phoneNum");
		String password=req.getParameter("password");
		UserService userService=new UserService();
		if(userService.checkLogin(phoneNum, password)){
			
			Long userId=userService.getByPhoneNum(phoneNum).getId();
			FrontUtils.setCurrentUserId(req, userId);
			writeJson(resp, new AjaxResult("ok"));
		}else{
			writeJson(resp, new AjaxResult("error","手机号或密码错误，请重新登录！"));
			return;
		}
	}
	
	public void sendSms(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String userCode=req.getParameter("userCode");
	String phoneNum=req.getParameter("phoneNum");
	UserService userService=new UserService();
	if(userService.getByPhoneNum(phoneNum)!=null){
		writeJson(resp, new AjaxResult("error", "手机号已经被注册了"));
		return;
	}
	String serverCode=(String) req.getSession().getAttribute("code");
	if(!serverCode.equalsIgnoreCase(userCode)){
		writeJson(resp, new AjaxResult("error", "图片验证码不正确"));
		return;
	}
	String smsCode=VerifyCodeUtils.generateVerifyCode(4);
	req.getSession().setAttribute("smsCode",smsCode);
	req.getSession().setAttribute("phoneNum", phoneNum);
	RupengSMSAPI.send(smsCode, phoneNum);
	writeJson(resp, new AjaxResult("ok"));
	}
	//图片验证码
	public void verifyCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/jpeg");
		String code=VerifyCodeUtils.generateVerifyCode(4);
		req.getSession().setAttribute("code", code);
		VerifyCodeUtils.outputImage(100,50,resp.getOutputStream(), code);
	}
	public void findPassword1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/user/findPassword1.jsp").forward(req, resp);
	}
	public void findPassword1Submit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String phoneNum=req.getParameter("phoneNum");
		String verifyCode=req.getParameter("verifyCode");
		String serverCode=(String)req.getSession().getAttribute("code");
		if(!serverCode.equalsIgnoreCase(verifyCode)){
			writeJson(resp, new AjaxResult("error","图片验证码错误"));
			return;
		}
		String smsCode=VerifyCodeUtils.generateVerifyCode(4);
		req.getSession().setAttribute("smsCode",smsCode);
		req.getSession().setAttribute("phoneNum",phoneNum);
		RupengSMSAPI.sendFindPassword(smsCode, phoneNum);
		req.getSession().removeAttribute("findPassword2OK");
		writeJson(resp, new AjaxResult("ok"));
	}
	public void findPassword2(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/user/findPassword2.jsp").forward(req, resp);
	}
	public void findPassword2Submit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String smsCode=req.getParameter("smsCode");
		String serverSmsCode=(String)req.getSession().getAttribute("smsCode");
		if(!serverSmsCode.equalsIgnoreCase(smsCode)){
			writeJson(resp, new AjaxResult("error","短信验证码错误"));
			return;
		}
		
	    req.getSession().setAttribute("findPassword2OK", "ok");
		writeJson(resp, new AjaxResult("ok"));
	}
	public void findPassword3(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/user/findPassword3.jsp").forward(req, resp);
	}
public void findPassword3Submit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String password=req.getParameter("password");
		String serverPhoneNum=(String) req.getSession().getAttribute("phoneNum");
		String findPassword2OK=(String)req.getSession().getAttribute("findPassword2OK");
		if(!"ok".equals(findPassword2OK)){
			writeJson(resp, new AjaxResult("error","不要跳过中间验证"));
			return;
		}
		UserService userService=new UserService();
		Long userId=userService.getByPhoneNum(serverPhoneNum).getId();
		userService.updatePwd(userId, password);
		writeJson(resp, new AjaxResult("ok"));
	}
public void findPasswordComplete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	req.getRequestDispatcher("/WEB-INF/user/findPasswordComplete.jsp").forward(req, resp);
	}
public void center(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	req.getRequestDispatcher("/WEB-INF/user/center.jsp").forward(req, resp);
	}
}
