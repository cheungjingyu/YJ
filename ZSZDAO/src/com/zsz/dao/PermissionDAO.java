package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.PermissionDTO;

public class PermissionDAO {
	public static PermissionDTO toPermissionDTO(ResultSet rs) throws SQLException{
		PermissionDTO permission=new PermissionDTO();
		permission.setDeleted(rs.getBoolean("IsDeleted"));
		permission.setDescription(rs.getString("Description"));
		permission.setId(rs.getLong("Id"));
		permission.setName(rs.getString("Name"));
		return permission;
	}
	/**
	 * 根据ID获得一个权限PermissionDTO对象
	 * @param id
	 * @return
	 */
	
	public PermissionDTO getById(long id){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_permissions where id=? and IsDeleted=0",id);
			if(rs.next()){
				return toPermissionDTO(rs);
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
	 * 软删除一个权限
	 * @param perminssionId
	 */
	public void markDeleted(long perminssionId){
		try {
			JDBCUtils.executeNonQuery("update T_permissions set IsDeleted=1 where Id=?", perminssionId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 更新权限
	 * @param description
	 * @param name
	 * @param permissionId
	 */
	public void updatePermission(String description,String name,Long permissionId){
		try {
			JDBCUtils.executeNonQuery("update T_permissions set Description=?,Name=? where Id=?", description,name,permissionId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 添加一个权限
	 * @param description
	 * @param name
	 * @return
	 */
	public long addPermission(String description,String name){
		try {
			return JDBCUtils.executeInsert("insert into T_permissions(Description,Name,IsDeleted) values(?,?,0)", description,name);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public PermissionDTO[] getAll(){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_permissions where IsDeleted=0");
			List<PermissionDTO> list=new ArrayList<PermissionDTO>();
			while(rs.next()){
				list.add(toPermissionDTO(rs));
			}
			return list.toArray(new PermissionDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
	/**
	 * 根据名字获得权限对象
	 * @param name
	 * @return
	 */
	
	public PermissionDTO getByName(String name){
		ResultSet rs=null;
		try {
		rs=	JDBCUtils.executeQuery("select * from T_permissions where Name=? and IsDeleted=0",name);
		if(rs.next()){
			return toPermissionDTO(rs);
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
	 * 获取角色的权限
	 * @param roleId
	 * @return
	 */
	public PermissionDTO[] getByRoleId(long roleId){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_permissions where IsDeleted=0 and Id in(select PermissionId from T_rolepermissions where RoleId=?)",roleId);
			List<PermissionDTO> list=new ArrayList<PermissionDTO>();
			while(rs.next()){
				list.add(toPermissionDTO(rs));
			}
			return list.toArray(new PermissionDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
	/**
	 * 给角色roleId增加权限项id permIds
	 * @param roleId
	 * @param permIds
	 */
	public void addPermIds(long roleId, long[] permIds){
			try {
				for(long permId : permIds){
				JDBCUtils.executeNonQuery("insert into T_rolepermissions(RoleId,PermissionId) values(?,?)",roleId,permId);
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
	}

	/**
	 * 更新角色role的权限项：先删除再添加
	 * @param roleId
	 * @param permIds
	 */
	public void updatePermIds(long roleId, long[] permIds){
		try{
			JDBCUtils.executeNonQuery("delete  from T_rolepermissions where RoleId=?",roleId);
			//addPermIds(roleId, permIds);
			for(long permId : permIds){
				JDBCUtils.executeNonQuery("insert into T_rolepermissions(PermissionId,RoleId) values(?,?)",permId,roleId);
				
			}
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}

}
