package com.zsz.service;
import com.zsz.dao.AdminUserDAO;
import com.zsz.dto.AdminUserDTO;
public class AdminUserService {
	private AdminUserDAO dao=new AdminUserDAO();
	public long addAdminUser(String name,String phoneNum, String password, String email, Long cityId){
		if(dao.getByPhoneNum(phoneNum)!=null){
			throw new IllegalArgumentException("手机号为"+phoneNum+"已经被注册");
		}
		return dao.addAdminUser(name, phoneNum, password, email, cityId);
	}
	public void updateAdminUser(Long adminUserId,String  name,String  password,String  email,Long cityId){
		dao.updateAdminUser(adminUserId, name, password, email, cityId);
	}
	public AdminUserDTO[] getAll(long cityId){
		return dao.getAll(cityId);
	}
	public AdminUserDTO[] getAll(){
		return dao.getAll();
	}
	public AdminUserDTO getById(long id){
		return dao.getById(id);
	}
	public AdminUserDTO getByPhoneNum(String phoneNum){
	return dao.getByPhoneNum(phoneNum);
	}
	public void markDeleted(long adminUserId){
	 dao.markDeleted(adminUserId);
	}
	public boolean checkLogin(String phoneNum,String password){
     	return dao.checkLogin(phoneNum, password);
	}
	public boolean hasPermission(long adminUserId, String permissionName){
		return dao.hasPermission(adminUserId, permissionName);
	}
}
