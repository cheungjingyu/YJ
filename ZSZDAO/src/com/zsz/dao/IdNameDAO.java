package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.IdNameDTO;

public class IdNameDAO {
	/**
	 * 类别名  名字
	 * @param typeName
	 * @param name
	 * @return
	 */
public long addIdName(String typeName,String name){
try {
	long i=	JDBCUtils.executeInsert("insert into T_idnames(TypeName,Name) values(?,?)",typeName,name);
	return i;
} catch (SQLException e) {
	throw new RuntimeException(e);
}
}
/**
 * 根据id获取一条记录。
 * @param id
 * @return
 */
public IdNameDTO getById(long id){
	ResultSet rs=null;
	try {
		rs=JDBCUtils.executeQuery("select from T_idnames where id=?", id);
		
		if(rs.next()){
			IdNameDTO idName=new IdNameDTO();
			idName.setId(rs.getLong("Id"));
			idName.setName(rs.getString("Name"));
			idName.setTypeName(rs.getString("TypeName"));
			return idName;
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
 * 获取类别下的IdName（比如所有的民族）
 * @param typeName
 * @return
 */
public IdNameDTO[] getAll(String typeName){
	ResultSet rs=null;
	try {
	rs=	JDBCUtils.executeQuery("select * from T_idnames where TypeName=? and IsDeleted=0",typeName);
	List<IdNameDTO> list=new ArrayList<IdNameDTO>();
	while(rs.next()){
		IdNameDTO idNmae=new IdNameDTO();
		idNmae.setId(rs.getLong("Id"));
		idNmae.setName(rs.getString("Name"));
		idNmae.setTypeName(rs.getString("TypeName"));
		idNmae.setDeleted(rs.getBoolean("IsDeleted"));
		list.add(idNmae);
	}
	return list.toArray(new IdNameDTO[list.size()]);
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}finally{
		JDBCUtils.closeAll(rs);
	}
}
}
