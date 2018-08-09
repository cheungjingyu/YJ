package com.zsz.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.HouseAppointmentDTO;

public class HouseAppointmentDAO {
	/**
	 *新增一个预约：userId用户id（可以为null）；name姓名、phoneNum手机号、houseId房间id、visiteDate预约看房时间 
	 * @param userId
	 * @param name
	 * @param phoneNum
	 * @param houseId
	 * @param visitDate
	 * @return
	 */
	public long addnew(Long userId, String name, String phoneNum, long houseId, Date visitDate){
		StringBuilder sb=new StringBuilder();
		sb.append("insert into T_houseappointments(UserId,Name,PhoneNum,HouseId,VisitDate,\n");
		sb.append("CreateDateTime,Status,FollowAdminUserId,FollowDateTime)\n");
		sb.append("values(?,?,?,?,?,now(),'新增',null,null)\n");
		try {
			return JDBCUtils.executeInsert(sb.toString(),userId,name,phoneNum,houseId,visitDate);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public static HouseAppointmentDTO toHouseAppointmentDTO(ResultSet rs) throws SQLException{
		HouseAppointmentDTO houseAppointment =new HouseAppointmentDTO();
		houseAppointment.setId(rs.getLong("Id"));
		houseAppointment.setCommunityName(rs.getString("CommunityName"));
		houseAppointment.setCreateDateTime(rs.getDate("CreateDateTime"));
		houseAppointment.setFollowAdminUserId(rs.getLong("FollowAdminUserId"));
		houseAppointment.setFollowAdminUserName(rs.getString("FollowAdminUserName"));
		houseAppointment.setFollowDateTime((Date)rs.getObject("FollowDateTime"));
		houseAppointment.setHouseId(rs.getLong("HouseId"));
		houseAppointment.setName(rs.getString("Name"));
		houseAppointment.setPhoneNum(rs.getString("PhoneNum"));
		houseAppointment.setRegionName(rs.getString("RegionName"));
		houseAppointment.setStatus(rs.getString("Status"));
		houseAppointment.setUserId(rs.getLong("UserId"));
		houseAppointment.setVisitDate(rs.getDate("VisitDate"));
		return houseAppointment;
	}
	/**
	 *根据id获取预约 
	 * @param id
	 * @return
	 */
	public HouseAppointmentDTO getById(long id){
		StringBuilder sb=new StringBuilder();
		sb.append("select ha.* ,au.Name FollowAdminUserName ,r.Name RegionName,c.Name CommunityName from T_houseappointments ha \n");
		sb.append("left join T_adminusers au on ha.FollowAdminUserId=au.Id\n");
		sb.append("left join T_houses h on ha.HouseId=h.Id \n");
		sb.append("left join T_communities c on c.Id=h.CommunityId \n");
		sb.append("left join T_Regions r on c.RegionId=r.Id\n");
		sb.append("where ha.Id=?\n");
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery(sb.toString(),id);
			if(rs.next()){
				return toHouseAppointmentDTO(rs);
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
	 * 得到所有城市的所有订单数量
	 * @return
	 */
	public long getTotalCount(){
		StringBuilder sb=new StringBuilder();
		sb.append("select count(*) from T_houseappointments ");
		
		try {
			return (long)JDBCUtils.querySingle(sb.toString());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 *得到cityId这个城市中状态为sstatus的预约订单数 
	 * @param cityId
	 * @param status
	 * @return
	 */
	public long getTotalCount(long cityId, String status){
		StringBuilder sb=new StringBuilder();
		sb.append("select count(*) from T_houseappointments ha where ha.HouseId in\n");
		sb.append("(\n");
		sb.append("select h.Id from T_houses h where h.RegionId in\n");
		sb.append("(\n");
		sb.append("select r.Id from T_Regions r where r.CityId=? and r.IsDeleted=0\n");
		sb.append(")\n");
		sb.append(") and ha.Status=?\n");
		try {
			return (long)JDBCUtils.querySingle(sb.toString(),cityId,status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 分页获取数据
	 * @param cityId
	 * @param status
	 * @param pageSize
	 * @param currentIndex
	 * @return
	 */
	 public HouseAppointmentDTO[] getPagedData(long cityId, String status, int pageSize, long currentIndex){
		 StringBuilder sb=new StringBuilder();
		 sb.append("select ha.* ,au.Name FollowAdminUserName ,r.Name RegionName,c.Name CommunityName from T_houseappointments ha \n");
		 sb.append("left join T_adminusers au on ha.FollowAdminUserId=au.Id\n");
		 sb.append("left join T_houses h on ha.HouseId=h.Id\n");
		 sb.append("left join T_communities c on c.Id=h.CommunityId\n");
		 sb.append("left join T_Regions r on c.RegionId=r.Id\n");
		 sb.append("where r.CityId=? and ha.Status=?\n");
		 sb.append("limit ?,?\n");
		ResultSet rs=null;
		List<HouseAppointmentDTO> list=new ArrayList<HouseAppointmentDTO>();
		 try {
			rs=JDBCUtils.executeQuery(sb.toString(),cityId,status,(currentIndex-1)*pageSize,pageSize);
			while(rs.next()){
				list.add(toHouseAppointmentDTO(rs));
			}
			return list.toArray(new HouseAppointmentDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	 }
	/**
	 *  抢单
	 * @param adminUserId
	 * @param houseAppointmentId
	 * @return
	 */
	 public boolean follow(long adminUserId, long houseAppointmentId) {
		 Connection conn=null;
		 try {
			 conn=JDBCUtils.getConnection();
			conn.setAutoCommit(false);
			Number number=(Number) JDBCUtils.querySingle(conn,"select count(*) from T_houseappointments where Id=? and Status='新增' for update", houseAppointmentId);
			if(number.intValue()<=0){
				conn.rollback();
				return false;
			}
			JDBCUtils.executeNonQuery(conn, "update T_houseappointments set FollowAdminUserId=? ,Status='跟进中',FollowDateTime=now() ", adminUserId);
			conn.commit();
			return true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeQuietly(conn);
		}
		 
	 }
}
