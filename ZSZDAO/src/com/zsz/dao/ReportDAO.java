package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.zsz.dao.utils.JDBCUtils;

public class ReportDAO {
	//获得最近24小时内的新增的房源
	public Map<String,Long> getYesterdayCityInfo(){
		StringBuilder sb=new StringBuilder();
		sb.append("select c.Name cityName,count(*) count from T_Houses h\n");
		sb.append("left join T_Regions r on r.Id=h.regionId\n");
		sb.append("left join T_Cities c on c.Id=r.cityId\n");
		sb.append("where timestampdiff(hour,h.CreateDateTime,now())<=24\n");
		sb.append("group by c.Name\n");
		ResultSet rs=null;
		try{
		rs=JDBCUtils.executeQuery(sb.toString());
		Map<String,Long> map=new HashMap<String,Long>();
		while (rs.next()) {
			String cityName=rs.getString("cityName");
			Long count=rs.getLong("count");
			map.put(cityName, count);
		}
		return map;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally {
			JDBCUtils.closeAll(rs);
		}
	}
}
