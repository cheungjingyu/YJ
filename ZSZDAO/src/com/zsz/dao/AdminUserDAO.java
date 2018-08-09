package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.AdminUserDTO;
import com.zsz.tools.CommonUtils;

public class AdminUserDAO {
	/**
	 *加入一个用户，name用户姓名，phoneNum手机号，password密码，email，cityId城市id（null表示总部） 
	 * @param name
	 * @param phoneNum
	 * @param password
	 * @param email
	 * @param cityId
	 * @return
	 */
	public long addAdminUser(String name,String phoneNum, String password, String email, Long cityId){
		
		
		
		String passwordHash=CommonUtils.calcMD5(password);
	
		try {
			return JDBCUtils.executeInsert("insert into T_adminUsers(Name,PhoneNum,PasswordHash,Email,CityId,LoginErrorTimes,LastLoginErrorDateTime,IsDeleted,CreateDateTime) values(?,?,?,?,?,0,null,0,now())",name,phoneNum,passwordHash,email,cityId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 更新一个用户，name用户姓名，phoneNum手机号，password密码，email，cityId城市id
	 * @param name
	 * @param phoneNum
	 * @param password
	 * @param email
	 * @param cityId
	 */
	public void updateAdminUser(long adminUserId,String name, String password, String email, Long cityId){
		if(StringUtils.isEmpty(password)){
			try {
				JDBCUtils.executeNonQuery("update T_adminUsers set Name=?,Email=?,CityId=? where Id=?", name,email,cityId,adminUserId);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			
		}else{
			String passwordHash=CommonUtils.calcMD5(password);
			try {
				JDBCUtils.executeNonQuery("update T_adminUsers set Name=?,password=?,Email=?,CityId=? where Id=?", name,passwordHash,email,cityId,adminUserId);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	/**
	 *获取cityId这个城市下的管理员 
	 * @param cityId
	 * @return
	 */
	public AdminUserDTO[] getAll(long cityId){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select u.*,c.Name cityName from T_adminusers u left join T_cities c on u.CityId=c.Id where u.CityId=? and u.IsDeleted=0",cityId);
			List<AdminUserDTO> list=new ArrayList<AdminUserDTO>();
			while(rs.next()){
				list.add(toAdminUserDTO(rs));
			}
			return list.toArray(new AdminUserDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
	public static AdminUserDTO toAdminUserDTO(ResultSet rs) throws SQLException{
		AdminUserDTO adminuser=new AdminUserDTO();
		adminuser.setCityId(rs.getLong("CityId"));
		adminuser.setCityName(rs.getString("cityName"));
		adminuser.setCreateDateTime(rs.getDate("CreateDateTime"));
		adminuser.setDeleted(rs.getBoolean("IsDeleted"));
		adminuser.setEmail(rs.getString("Email"));
		adminuser.setId(rs.getLong("Id"));
		adminuser.setLastLoginErrorDateTime(rs.getDate("LastLoginErrorDateTime"));
		adminuser.setName(rs.getString("Name"));
		adminuser.setPasswordHash(rs.getString("PasswordHash"));
		adminuser.setPhoneNum(rs.getString("PhoneNum"));
		return adminuser;
	}
	/**
	 * 获取所有管理员
	 * @return
	 */
	public AdminUserDTO[] getAll(){
		ResultSet rs=null;
		try {
		rs=	JDBCUtils.executeQuery("select u.*,c.Name cityName from T_adminusers u left join T_cities c on u.CityId=c.Id where u.IsDeleted=0");
		List<AdminUserDTO> list=new ArrayList<AdminUserDTO>();
		while(rs.next()){
			list.add(toAdminUserDTO(rs));
		}
		return list.toArray(new AdminUserDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
	/**
	 * 根据id获取DTO
	 * @param id
	 * @return
	 */
	public AdminUserDTO getById(long id){
		ResultSet rs=null;
		try{
		rs=JDBCUtils.executeQuery("select u.*,c.Name cityName from T_adminusers u left join T_cities c on u.CityId=c.Id where u.Id=?",id);
		if(rs.next()){
			return toAdminUserDTO(rs);
		}else{
			return null;
		}
	}catch(SQLException e){
		throw new RuntimeException(e);
	}finally{
		JDBCUtils.closeAll(rs);
	}
	}
	/**
	 * 根据手机号获取DTO
	 * @param phoneNum
	 * @return
	 */
	public AdminUserDTO getByPhoneNum(String phoneNum){
		ResultSet rs=null;
		try{
			rs=JDBCUtils.executeQuery("select u.*,c.Name cityName from T_adminusers u left join T_cities c on u.CityId=c.Id where u.IsDeleted=0 and u.phoneNum=?",phoneNum);
			if(rs.next()){
				return toAdminUserDTO(rs);
			}else{
				return null;
			}
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
	/**
	 * 软删除
	 * @param adminUserId
	 */
	public void markDeleted(long adminUserId){
		try {
			JDBCUtils.executeNonQuery("update T_adminusers set IsDeleted=1 where Id=?", adminUserId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 检查用户名密码是否正确
	 * @param phoneNum
	 * @param password
	 * @return
	 */
	public boolean checkLogin(String phoneNum,String password){
     	String passwordHash=CommonUtils.calcMD5(password);
     	AdminUserDTO dto= getByPhoneNum(phoneNum);
     	if(dto==null){
     		return false;
     	}
     	if(!dto.getPasswordHash().equals(passwordHash)){
     		return false;
     	}
		return true;
	}
	/**
	 *判断adminUserId这个用户是否有permissionName这个权限项（举个例子） 
	 * @param adminUserId
	 * @param permissionName
	 * @return
	 */
	public boolean hasPermission(long adminUserId, String permissionName){
		StringBuilder sb=new StringBuilder();
		sb.append("select count(*)  from T_permissions where  Id in\n");
		sb.append("(\n");
		sb.append("select PermissionId from T_rolepermissions where RoleId in\n");
		sb.append("(select RoleId from T_adminuserroles where AdminUserId=?)\n");
		sb.append(")\n");
		sb.append(" and  Name=?\n");
		try {
			Number number=(Number)JDBCUtils.querySingle(sb.toString(),adminUserId,permissionName);
			return number.longValue()>0;
		} catch (SQLException e) {
		throw new RuntimeException(e);
		}
	}

}
