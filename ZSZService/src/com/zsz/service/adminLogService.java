package com.zsz.service;

import java.sql.SQLException;

import com.zsz.dao.adminLogDAO;
import com.zsz.dao.utils.JDBCUtils;

public class adminLogService {
	private adminLogDAO dao=new adminLogDAO();
public void addnew(long adminUserId,String message){
	dao.addnew(adminUserId, message);
}
}