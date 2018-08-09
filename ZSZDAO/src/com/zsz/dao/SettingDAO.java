package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.SettingDTO;

public class SettingDAO {
	/**
	 * 设置配置项name的值为value
	 * @param name
	 * @param value
	 */
public void setValue(String name,String value){
	ResultSet rs=null;
	try {
		rs=JDBCUtils.executeQuery("select * from T_settings where Name=?", name);
		if(rs!=null){
			JDBCUtils.executeNonQuery("update T_settings set Value=? where Name=?",value,name);
		}else{
			JDBCUtils.executeNonQuery("insert into T_settings(Name,Value) values(?,?)",name,value);
		}
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}finally{
		JDBCUtils.closeAll(rs);
	}
}
/**
 * 获取配置项name的值
 * @param name
 * @return
 */
public String getValue(String name){
	ResultSet rs=null;
	try {
		rs=JDBCUtils.executeQuery("select Value from T_settings where Name=?", name);
		if(rs.next()){
			String value=rs.getString("Value");
			return value;
		}else{
			return null;
		}
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}
}
public SettingDTO[] getAll(){
	ResultSet rs=null;
	try {
	rs=	JDBCUtils.executeQuery("select * from T_settings");
	List<SettingDTO> list=new ArrayList<SettingDTO>();
	while(rs.next()){
		SettingDTO setting =new SettingDTO();
		setting.setId(rs.getLong("Id"));
		setting.setName(rs.getString("Name"));
		setting.setValue(rs.getString("Value"));
		list.add(setting);
	}
	return list.toArray(new SettingDTO[list.size()]);
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}finally{
		JDBCUtils.closeAll(rs);
	}
}
}
