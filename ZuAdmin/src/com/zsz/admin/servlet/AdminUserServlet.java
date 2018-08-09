package com.zsz.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.zsz.dto.AdminUserDTO;
import com.zsz.dto.CityDTO;
import com.zsz.dto.RoleDTO;
import com.zsz.service.AdminUserService;
import com.zsz.service.CityService;
import com.zsz.service.RoleService;
import com.zsz.tools.AjaxResult;
import com.zsz.tools.CommonUtils;
@WebServlet("/AdminUser")
public class AdminUserServlet extends BasicServlet {
	@HasPermission("AdminUser.Query")
	public void list(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
	AdminUserService adminUserService=new AdminUserService();
	AdminUserDTO[] adminUsers=adminUserService.getAll();
	req.setAttribute("adminUsers",adminUsers);
	req.getRequestDispatcher("/WEB-INF/adminUser/adminUserList.jsp").forward(req, resp);

}
	@HasPermission("AdminUser.Delete")
		public void delete(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
		AdminUserService adminUserService=new AdminUserService();
		long id=Long.parseLong(req.getParameter("id"));
		adminUserService.markDeleted(id);
		writeJson(resp, new AjaxResult("ok"));
		
	}
	@HasPermission("AdminUser.AddNew")
		public void add(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
			RoleService roleService=new RoleService();
			RoleDTO[] roles=roleService.getAll();
			req.setAttribute("roles",roles);
			CityService cityService=new CityService();
			CityDTO[] cities=cityService.getAll();
			req.setAttribute("cities",cities);
			req.getRequestDispatcher("/WEB-INF/adminUser/adminUserAdd.jsp").forward(req, resp);

		}
	@HasPermission("AdminUser.AddNew")
		public void addSubmit(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
			AdminUserService adminUserService=new AdminUserService();
			String name=req.getParameter("name");
			String password=req.getParameter("password");
			String phoneNum=req.getParameter("phoneNum");
			String email=req.getParameter("email");
			String[] roleIds=req.getParameterValues("roleId");
			String strcityId=req.getParameter("cityId");
			Long cityId=null;
			if(!StringUtils.isEmpty(strcityId)){
				cityId=Long.parseLong(strcityId);
			}
			AdminUserDTO user=adminUserService.getByPhoneNum(phoneNum);
			if(user!=null){
				writeJson(resp, new AjaxResult("error","手机号已经存在"," "));
				return;
			}
		     Long adminUserId=adminUserService.addAdminUser(name,phoneNum,password,email,cityId);
		     RoleService roleService=new RoleService();
		     roleService.addRoleIds(adminUserId, CommonUtils.toLongArray(roleIds));
			writeJson(resp, new AjaxResult("ok"));

		}
	@HasPermission("AdminUser.Edit")
		public void edit(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
			long userId=Long.parseLong(req.getParameter("id"));
			AdminUserService adminUserService=new AdminUserService();
			AdminUserDTO adminUser=adminUserService.getById(userId);
			req.setAttribute("adminUser",adminUser);
			 CityService cityService=new CityService();
			 CityDTO[] cities=cityService.getAll();
			 req.setAttribute("cities",cities);
			RoleService roleService =new RoleService();
			 RoleDTO[] roles=roleService.getAll(); 
			 req.setAttribute("roles",roles);
			 RoleDTO[] userRoles=roleService.getByAdminUserId(userId); //用户拥有的角色
			 long[] roleIds=new long[userRoles.length];
			 for(int i=0;i<userRoles.length;i++){
				 roleIds[i]=userRoles[i].getId();
			 }
			 req.setAttribute("roleIds",roleIds);
			req.getRequestDispatcher("/WEB-INF/adminUser/adminUserEdit.jsp").forward(req, resp);
		}
	@HasPermission("AdminUser.Edit")
		public void editSubmit(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
			AdminUserService adminUserService=new AdminUserService();
			long adminUserId=Long.parseLong(req.getParameter("id"));
			String name=req.getParameter("name");
			String password=req.getParameter("password");
		
			String email=req.getParameter("email");
			String[] roleIds=req.getParameterValues("roleId");
			String strcityId=req.getParameter("cityId");
			Long cityId=null;
			if(!StringUtils.isEmpty(strcityId)){
				cityId=Long.parseLong(strcityId);
			}
		
			adminUserService.updateAdminUser(adminUserId, name, password, email, cityId);
		     RoleService roleService=new RoleService();
		     roleService.updateRoleIds(adminUserId, CommonUtils.toLongArray(roleIds));
			writeJson(resp, new AjaxResult("ok"));

		}
		
}
