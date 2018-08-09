package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.RegionDTO;

public class RegionDAO {
	/**
	 * 返回RegionDTO的静态方法
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static RegionDTO toRegionDTO(ResultSet rs) throws SQLException{
		RegionDTO region=new RegionDTO();
		region.setId(rs.getLong("Id"));
		region.setCityId(rs.getLong("CityId"));
		region.setName(rs.getString("Name"));
		region.setDeleted(rs.getBoolean("IsDeleted"));
		return region;
	}
	/**
	 * 根据id获得一个RegionDTO  对象
	 * @param id
	 * @return
	 */
	public RegionDTO getById(long id){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_regions where id=? and IsDeleted=0",id);
			if(rs.next()){
				return toRegionDTO(rs);
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
	 * 获取城市下的区域
	 * @param cityId
	 * @return
	 */
	public RegionDTO[] getAll(long cityId){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_regions where CityId=? and IsDeleted=0",cityId);
			List<RegionDTO> list=new ArrayList<RegionDTO>();
			while(rs.next()){
				list.add(toRegionDTO(rs));
			}
			return list.toArray(new RegionDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
	
	
}
