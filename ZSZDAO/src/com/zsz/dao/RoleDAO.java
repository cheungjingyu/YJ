package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.RoleDTO;

public class RoleDAO {
	/**
	 * 新增角色
	 * @param roleName
	 * @return
	 */
	public long addnew(String roleName){
		try {
			Number number=JDBCUtils.executeInsert("insert into T_Roles(Name,IsDeleted) values(?,0)",roleName);
			return number.longValue();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 编辑
	 * @param roleId
	 * @param roleName
	 */
	public void update(long roleId,String roleName){
		try {
			JDBCUtils.executeNonQuery("update T_Roles set Name=? where Id=?",roleName,roleId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 软删除角色
	 * @param roleId
	 */
	public void markDeleted(long roleId){
		try {
			JDBCUtils.executeNonQuery("update T_roles set IsDeleted=1 where Id=?",roleId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 返回一个RoleDTO 对象
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static RoleDTO toRoleDTO(ResultSet rs) throws SQLException{
		RoleDTO role=new RoleDTO();
		role.setId(rs.getLong("Id"));
		role.setDeleted(rs.getBoolean("IsDeleted"));
		role.setName(rs.getString("Name"));
		return role;
	}
	/**
	 * 根据ID 获得一个RoleDTO   对象
	 * @param id
	 * @return
	 */
	public RoleDTO getById(long id){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_roles where Id=?", id);
			if(rs.next()){
				return toRoleDTO(rs);
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
	 * 获得所有的RoleDTO 对象
	 * @return
	 */
	public RoleDTO[] getAll(){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_roles where IsDeleted=0");
			List<RoleDTO> list=new ArrayList<RoleDTO>();
			while(rs.next()){
				list.add(toRoleDTO(rs));
			}
			return list.toArray(new RoleDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
	/**
	 * 获取用户的角色
	 * @param adminUserId
	 * @return
	 */
	public RoleDTO[] getByAdminUserId(long adminUserId){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select r.* from T_roles r where Id in (select RoleId from T_adminuserroles where AdminUserId=?)",adminUserId);
			List<RoleDTO> list=new ArrayList<RoleDTO>();
			while(rs.next()){
				list.add(toRoleDTO(rs));
			}
			return list.toArray(new RoleDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
	/**
	 * 给用户adminuserId增加权限roleIds
	 * @param adminUserId
	 * @param roleIds
	 */
	public void addRoleIds(long adminUserId, long[] roleIds){
		for(long roleId : roleIds){
			try {
				JDBCUtils.executeNonQuery("insert into T_adminuserroles(AdminUserId,RoleId) values(?,?)", adminUserId,roleId);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	/**
	 * 更新权限，先删再加
	 * @param adminUserId
	 * @param roleIds
	 */
	public void updateRoleIds(long adminUserId, long[] roleIds){
		try {
			JDBCUtils.executeNonQuery("delete  from T_adminuserroles where AdminUserId=?",adminUserId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		for(long roleId : roleIds){
			try {
				JDBCUtils.executeNonQuery("insert into T_adminuserroles(AdminUserId,RoleId) values(?,?)", adminUserId,roleId);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
