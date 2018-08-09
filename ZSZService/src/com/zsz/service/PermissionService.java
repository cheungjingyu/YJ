package com.zsz.service;
import com.zsz.dao.PermissionDAO;
import com.zsz.dto.PermissionDTO;
public class PermissionService {
private PermissionDAO dao=new PermissionDAO();	
	public PermissionDTO getById(long id){
		return dao.getById(id);
	}
	public long addPermission(String description,String name){
		return dao.addPermission(description, name);
	}
	public void markDeleted(long perminssionId){
		dao.markDeleted(perminssionId);
	}
	public PermissionDTO[] getAll(){
		return dao.getAll();
	}
	public void updatePermission(String description,String name,Long permissionId){
		dao.updatePermission(description, name, permissionId);
	}
	public PermissionDTO getByName(String name){
		return dao.getByName(name);
	}
	public PermissionDTO[] getByRoleId(long roleId){
		return dao.getByRoleId(roleId);
	}
	public void addPermIds(long roleId, long[] permIds){
			dao.addPermIds(roleId, permIds);
	}
	public void updatePermIds(long roleId, long[] permIds){
		dao.updatePermIds(roleId, permIds);
	}
}
