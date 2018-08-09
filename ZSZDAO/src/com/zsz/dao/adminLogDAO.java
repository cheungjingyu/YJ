package com.zsz.dao;

import java.sql.SQLException;

import com.zsz.dao.utils.JDBCUtils;

public class adminLogDAO {
	/**
	 * 插入一条日志，adminUserId为操作用户id，message为消息
	 * @param adminUserId
	 * @param message
	 */
public void addnew(long adminUserId,String message){
	try {
		JDBCUtils.executeNonQuery("insert into T_AdminLogs(AdminUserId,CreateDateTime,message) value(?,now(),?)", adminUserId,message);
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}
}
}
