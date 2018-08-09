package com.zsz.front.utils;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zsz.service.CityService;
import com.zsz.service.UserService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class FrontUtils extends HttpServlet {
   public static void showError(HttpServletRequest req,HttpServletResponse resp,String errorMsg) throws ServletException, IOException{
	   resp.setStatus(500);
	   req.setAttribute("errorMsg", errorMsg);
	   req.getRequestDispatcher("/WEB-INF/error.jsp").forward(req, resp);
   }
   //设置当前用户Id
   public static void setCurrentUserId(HttpServletRequest req,Long id){
	   req.getSession().setAttribute("currentUserId", id);
   }
   //获取当前用户Id
   public static Long getCurrentUserId(HttpServletRequest req){
	   return (Long)req.getSession().getAttribute("currentUserId");
   }
   //设置当前城市Id
   public static void setCurrentCityId(HttpServletRequest req,Long cityId){
	   Long userId=FrontUtils.getCurrentUserId(req);
	   if(userId!=null){
		   UserService userService=new UserService();
		   userService.setUserCityId(userId, cityId);
	   }else{
	   req.getSession().setAttribute("currentCityId", cityId);
	   }
   }
   //获取当前城市Id
   public static Long getCurrentCityId(HttpServletRequest req){
	   Long userId=FrontUtils.getCurrentUserId(req);
	   Long cityId=null;
	   if(userId!=null){
		   cityId = new UserService().getById(userId).getCityId();
		   if(cityId!=null){
			   return cityId;
		   }
	   }
	   cityId=(Long) req.getSession().getAttribute("currentCityId");
	   if(cityId!=null){
		   return cityId;
	   }
	   return new CityService().getAll()[0].getId();
   }
   
   private static final JedisPool jedisPool=new JedisPool("localhost");
   public static Jedis createJedis(){
	   return jedisPool.getResource();
   }
}
