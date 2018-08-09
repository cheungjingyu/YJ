package com.zsz.service;
import com.zsz.dao.UserDAO;
import com.zsz.dto.UserDTO;
public class UserService {
	private UserDAO dao=new UserDAO();
	
	public UserDTO getByPhoneNum(String phoneNum){
		return dao.getByPhoneNum(phoneNum);
	}
	public UserDTO getById(long id){
		return dao.getById(id);
	}

	public void setUserCityId(long userId,long cityId){
		dao.setUserCityId(userId, cityId);
	}
	 public long addnew(String phoneNum, String password) {
		if(dao.getByPhoneNum(phoneNum)!=null){
			throw new IllegalArgumentException("手机号为"+phoneNum+"已被注册");
		}
		return dao.addnew(phoneNum, password);
	}
	 public boolean checkLogin(String phoneNum, String password){
		 return dao.checkLogin(phoneNum, password);
	 }
	 public void updatePwd(long userId, String newPassword){
		dao.updatePwd(userId, newPassword);
	 }
}
