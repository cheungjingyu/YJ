package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.UserDTO;
import com.zsz.tools.CommonUtils;
import com.zsz.tools.VerifyCodeUtils;

public class UserDAO {
	public static UserDTO toUserDTO(ResultSet rs) throws SQLException{
		UserDTO user=new UserDTO();
		user.setCityId((Long)rs.getObject("CityId"));
		user.setCreateDateTime(rs.getDate("CreateDateTime"));
		user.setId(rs.getLong("Id"));
		user.setLastLoginErrorDateTime((Date)rs.getObject("LastLoginErrorDateTime"));
		user.setLoginErrorTimes((int) rs.getObject("LoginErrorTimes"));
		user.setPasswordHash(rs.getString("PasswordHash"));
		user.setPhoneNum(rs.getString("PhoneNum"));
		user.setPasswordSalt(rs.getString("PasswordSalt"));
		return user;
	}
	/**
	 * 根据用户手机号获取用户UserDTO对象
	 * @param phoneNum
	 * @return
	 */
	public UserDTO getByPhoneNum(String phoneNum){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_users where PhoneNum=?", phoneNum);
			if(rs.next()){
				return toUserDTO(rs);
			}else{
				return null;
			}
		} catch (SQLException e) {
		throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
	/**
	 * 根君ID获得对象
	 * @param id
	 * @return
	 */
	public UserDTO getById(long id){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_Users where Id=?",id);
			if(rs.next()){
				return toUserDTO(rs);
			}
			else{
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}

	/**
	 * 设置用户userId的城市id
	 * @param userId
	 * @param cityId
	 */
	public void setUserCityId(long userId,long cityId){
		try {
			JDBCUtils.executeNonQuery("update T_Users set CityId=? where Id=?", cityId,userId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 加盐
	 * @param phoneNum
	 * @param password
	 * @return
	 */
	 public long addnew(String phoneNum, String password) {
		String salt=VerifyCodeUtils.generateVerifyCode(6,"abcdefghijklmn123456#$%*789");
		String hash=CommonUtils.calcMD5(password+salt);
		try {
			return JDBCUtils.executeInsert("insert into T_Users(PhoneNum,PasswordHash,PasswordSalt,CreateDateTime,LoginErrorTimes) values(?,?,?,now(),0)",phoneNum,hash,salt);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	 /**
	  * 检查用户名密码是否正确（很好体现了分层的思想）
	  * @param phoneNum
	  * @param password
	  * @return
	  */
	 
	 public boolean checkLogin(String phoneNum, String password){
		 UserDTO user=getByPhoneNum(phoneNum);
		 if(user==null){
			 return false;
		 }
		 String salt=user.getPasswordSalt();
		 String hash=user.getPasswordHash();
		 String passHash=CommonUtils.calcMD5(password+salt);
		 return hash.equals(passHash);
	 }
	 /**
	  * 修改密码
	  * @param userId
	  * @param newPassword
	  */
	 public void updatePwd(long userId, String newPassword){
		 UserDTO user=getById(userId);
		 if(user==null){
			 throw new IllegalArgumentException("传入的用户id为"+userId+"非法");
		 }
		 String salt=user.getPasswordSalt();
		 String newHash=CommonUtils.calcMD5(newPassword+salt);
		 try {
			JDBCUtils.executeNonQuery("update T_Users set PasswordHash=? where Id=?",newHash,userId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	 }
}
