package com.zsz.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zsz.dto.PermissionDTO;
import com.zsz.service.PermissionService;
import com.zsz.tools.AjaxResult;
@WebServlet("/Permission")
public class permissionServlet extends BasicServlet {
	public void list(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
		PermissionService permissionService=new PermissionService();
		PermissionDTO[] permissions=permissionService.getAll();
		req.setAttribute("permissions",permissions);
		req.getRequestDispatcher("/WEB-INF/permission/permissionList.jsp").forward(req, resp);
	}
	public void delete(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
		long id=Long.parseLong(req.getParameter("id"));
		PermissionService permissionService=new PermissionService();
		permissionService.markDeleted(id);
		writeJson(resp, new AjaxResult("ok"));
	}
	public void add(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
		req.getRequestDispatcher("/WEB-INF/permission/permissionAdd.jsp").forward(req, resp);
	}
	public void addSubmit(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
		String description=req.getParameter("description");
		String name=req.getParameter("name");
		
		PermissionService permissionService=new PermissionService();
		PermissionDTO permission=permissionService.getByName(name);
		if(permission!=null){
			writeJson(resp, new AjaxResult("error","该权限已存在",""));
			return;
		}
		permissionService.addPermission(description, name);
		writeJson(resp, new AjaxResult("ok"));
	}
	public void edit(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
		long id=Long.parseLong(req.getParameter("id"));
		PermissionService permissionService=new PermissionService();
		PermissionDTO permission= permissionService.getById(id);
		req.setAttribute("permission",permission);
		req.getRequestDispatcher("/WEB-INF/permission/permissionEdit.jsp").forward(req, resp);
	}
	public void editSubmit(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
		long id=Long.parseLong(req.getParameter("id"));
		String description=req.getParameter("description");
		String name=req.getParameter("name");
		PermissionService permissionService=new PermissionService();
		PermissionDTO permission=permissionService.getByName(name);
		if(permission!=null){
			writeJson(resp, new AjaxResult("error","该权限已存在",""));
			return;
		}
		permissionService.updatePermission(description, name, id);
		writeJson(resp, new AjaxResult("ok"));
	}
}
