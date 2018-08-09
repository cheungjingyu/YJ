package com.zsz.service;
import com.zsz.dao.RoleDAO;
import com.zsz.dto.RoleDTO;
public class RoleService {
	private RoleDAO dao=new RoleDAO();
	public long addnew(String roleName){
		return dao.addnew(roleName);
	}
	public void update(long roleId,String roleName){
		dao.update(roleId, roleName);
	}
	public void markDeleted(long roleId){
		dao.markDeleted(roleId);
	}
	public RoleDTO getById(long id){
		return dao.getById(id);
	}
	public RoleDTO[] getAll(){
		return dao.getAll();
	}
	public RoleDTO[] getByAdminUserId(long adminUserId){
		return dao.getByAdminUserId(adminUserId);
	}
	public void addRoleIds(long adminUserId, long[] roleIds){
		dao.addRoleIds(adminUserId, roleIds);
	}
	public void updateRoleIds(long adminUserId, long[] roleIds){
		dao.updateRoleIds(adminUserId, roleIds);
	}
}
