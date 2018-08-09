package com.zsz.admin.utils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class AdminUtils {
   public static void showError(HttpServletRequest req,HttpServletResponse resp,String errorMsg) throws ServletException, IOException{
	  resp.setStatus(500);//设置响应的状态吗
	   req.setAttribute("errorMsg", errorMsg);
	   req.getRequestDispatcher("/WEB-INF/error.jsp").forward(req, resp);
   }
   public static void setAdminUserId(HttpServletRequest req,long adminUserId){
	   req.getSession().setAttribute("adminUserId",adminUserId);
   }
   public static Long getAdminUserId(HttpServletRequest req){
	   Long id=(Long) req.getSession().getAttribute("adminUserId");
	   return id;
   }
   public static void setAdminUserCityId(HttpServletRequest req,Long adminUserCityId){
	   req.getSession().setAttribute("adminUserCityId",adminUserCityId);
   }
   public static Long getAdminUserCityId(HttpServletRequest req){
	   Long id=(Long) req.getSession().getAttribute("adminUserCityId");
	   return id;
   }
}
