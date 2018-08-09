package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.CityDTO;

public class CityDAO {
	/**
	 * 获取一个CityDTO 的静态方法
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static CityDTO toCityDTO(ResultSet rs) throws SQLException{
		CityDTO city=new CityDTO();
		city.setId(rs.getLong("Id"));
		city.setName(rs.getString("Name"));
		city.setDeleted(rs.getBoolean("IsDeleted"));
		return city;
	}
	/**
	 * 根据id获取城市DTO
	 * @param id
	 * @return
	 */
	public CityDTO getById(long id){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_Cities where id=? and IsDeleted=0",id);
			if(rs.next()){
			return 	toCityDTO(rs);
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
	 * 获取所有城市
	 * @return
	 */
	public CityDTO[] getAll(){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_cities");
			List<CityDTO> list=new ArrayList<CityDTO>();
			while(rs.next()){
				list.add(toCityDTO(rs));
			}
			return list.toArray(new CityDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
}
