package com.zsz.front.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zsz.dto.CityDTO;
import com.zsz.front.utils.CacheManager;
import com.zsz.front.utils.FrontUtils;
import com.zsz.service.CityService;
import com.zsz.service.HouseAppointmentService;
import com.zsz.service.UserService;
import com.zsz.tools.AjaxResult;
@WebServlet("/Index")
public class IndexServlet extends BasicServlet {
	public void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CacheManager cacheManager=CacheManager.getManager();
		CityDTO[] cities=null;
		cities=	(CityDTO[])cacheManager.getValue("Cities", CityDTO[].class);
			
		if(cities==null){
			cities=new CityService().getAll();
			cacheManager.setValue("Cities", 60*60, cities);
		}
		req.setAttribute("cities", cities);
		
		HouseAppointmentService appointmentService=new HouseAppointmentService();
		Long totalCount=null;
		totalCount=(Long)cacheManager.getValue("totalCount", Long.class);
		if(totalCount==null){
			totalCount=appointmentService.getTotalCount();
			cacheManager.setValue("totalCount", 60, totalCount);
		}
		req.setAttribute("totalCount", totalCount);
		
		
		
	
		req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
		
		
	}
	public void getCurrentCity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cityName=new CityService().getById(FrontUtils.getCurrentCityId(req)).getName();
		writeJson(resp, new AjaxResult("ok","",cityName));
	}
	public void changeCity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long cityId=Long.parseLong(req.getParameter("cityId"));
        FrontUtils.setCurrentCityId(req, cityId);
        writeJson(resp, new AjaxResult("ok"));
	}
	
}
