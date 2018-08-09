package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.CommunityDTO;

public class CommunityDAO {
	/**
	 * 获取区域regionId下的所有小区
	 * @param regionId
	 * @return
	 */
	public CommunityDTO[] getByRegionId(long regionId){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_communities where RegionId=? and IsDeleted=0",regionId);
			List<CommunityDTO> list=new ArrayList<CommunityDTO>();
			while(rs.next()){
				CommunityDTO community=new CommunityDTO();
				community.setId(rs.getLong("Id"));
				community.setName(rs.getString("Name"));
				community.setRegionId(rs.getLong("RegionId"));
				community.setTraffic(rs.getString("Traffic"));
				community.setDeleted(rs.getBoolean("IsDeleted"));
				community.setBuiltYear((Integer) rs.getObject("BuiltYear"));
				community.setLocation(rs.getString("Location"));
				list.add(community);
			}
			return list.toArray(new CommunityDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
}
