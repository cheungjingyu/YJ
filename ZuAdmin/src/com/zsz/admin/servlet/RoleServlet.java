package com.zsz.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zsz.dto.PermissionDTO;
import com.zsz.dto.RoleDTO;
import com.zsz.service.PermissionService;
import com.zsz.service.RoleService;
import com.zsz.tools.AjaxResult;
import com.zsz.tools.CommonUtils;
@WebServlet("/Role")
public class RoleServlet extends BasicServlet {
	@HasPermission("Role.Query")
 public void list(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
	 RoleService roleService=new RoleService();
	 RoleDTO[] roles=roleService.getAll();
	 req.setAttribute("roles",roles);
	 req.getRequestDispatcher("/WEB-INF/role/roleList.jsp").forward(req,resp);
 }
	@HasPermission("Role.Delete")
 public void delete(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
	 RoleService roleService=new RoleService();
	 Long roleId=Long.parseLong(req.getParameter("id"));
	 roleService.markDeleted(roleId);
	 writeJson(resp,new AjaxResult("ok"));
 }
	@HasPermission("Role.AddNew")
 public void add(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
	 PermissionService permission=new PermissionService();
	 PermissionDTO[] perms=permission.getAll();
	 req.setAttribute("perms",perms);
	 req.getRequestDispatcher("/WEB-INF/role/roleAdd.jsp").forward(req,resp);
 }
	@HasPermission("Role.AddNew")
 public void addSubmit(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
	RoleService roleService=new RoleService();
	String roleName=req.getParameter("rolename");
	String[] permIds=req.getParameterValues("permId");
	Long roleId=roleService.addnew(roleName);
	PermissionService permissionService=new PermissionService();
	permissionService.addPermIds(roleId, CommonUtils.toLongArray(permIds));
	writeJson(resp,new AjaxResult("ok"));
 }
	@HasPermission("Role.Edit")
 public void edit(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
	 long id=Long.parseLong(req.getParameter("id"));
	 RoleService roleService=new RoleService();
	RoleDTO role = roleService.getById(id);
	 req.setAttribute("role",role);
	 
	 PermissionService permissionService=new PermissionService();
	PermissionDTO[] perms = permissionService.getAll();
	req.setAttribute("perms",perms);
	PermissionDTO[] rolePerms=    permissionService.getByRoleId(id);
	long[] rolePermIds=new long[rolePerms.length];
	for(int i=0;i<rolePerms.length;i++){
		rolePermIds[i]=rolePerms[i].getId();
	}
	req.setAttribute("rolePermIds",rolePermIds);
	 req.getRequestDispatcher("/WEB-INF/role/roleEdit.jsp").forward(req, resp);
 }
	@HasPermission("Role.Edit")
 public void editSubmit(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
	 String roleName=req.getParameter("rolename");
	 long id=Long.parseLong(req.getParameter("id"));
	 String[] permIds=req.getParameterValues("permId");
	 RoleService roleService=new RoleService();
	roleService.update(id, roleName);
	
	PermissionService permissionService=new PermissionService();
	permissionService.updatePermIds(id,CommonUtils.toLongArray(permIds));
	writeJson(resp,new AjaxResult("ok"));
	 
 }
}
