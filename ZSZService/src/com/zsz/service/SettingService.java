package com.zsz.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dao.SettingDAO;
import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.SettingDTO;

public class SettingService {
	private SettingDAO dao=new SettingDAO();
public void setValue(String name,String value){
	dao.setValue(name, value);
}
public String getValue(String name){
	return dao.getValue(name);
}
public SettingDTO[] getAll(){
	return dao.getAll();
}
}
