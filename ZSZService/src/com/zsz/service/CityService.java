package com.zsz.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dao.CityDAO;
import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.CityDTO;

public class CityService {
	private CityDAO dao=new CityDAO();
	public CityDTO getById(long id){
		return dao.getById(id);
	}
	public CityDTO[] getAll(){
		return dao.getAll();
	}
}
