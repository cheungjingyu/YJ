package com.zsz.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.HouseDTO;
import com.zsz.dto.HousePicDTO;
import com.zsz.dto.HouseSearchOptions;
import com.zsz.dto.HouseSearchOptions.OrderByType;
import com.zsz.dto.HouseSearchResult;

public class HouseDAO {
	private static final String selectSQLMain;
	static{
		StringBuilder sb=new StringBuilder();
		sb.append("select h.*,r.CityId CityId,c.Name CityName ,r.Name RegionName ,com.Name CommunityName,\n");
		sb.append("com.Location CommunityLocation ,	com.Traffic CommunityTraffic,\n");
		sb.append("com.BuiltYear CommunityBuiltYear,id1.Name RoomTypeName,\n");
		sb.append("id2.Name StatusName,id3.Name DecorateStatusName,id4.Name TypeName\n");
		sb.append("from T_houses h \n");
		sb.append("left join T_regions r on r.Id=h.RegionId\n");
		sb.append("left join T_cities c on r.CityId=c.Id\n");
		sb.append("left join T_Communities com on h.CommunityId=com.Id\n");
		sb.append("left join T_idnames id1 on id1.Id=h.RoomTypeId\n");
		sb.append("left join T_idnames id2 on id2.Id=h.StatusId\n");
		sb.append("left join T_idnames id3 on id3.Id=h.DecorateStatusId\n");
		sb.append("left join T_idnames id4 on id4.Id=h.TypeId\n");
		selectSQLMain=sb.toString();
	}
	/**
	 * 返回一个HouseDTO对象
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	public static HouseDTO toHouseDTO(ResultSet rs) throws SQLException{
		HouseDTO house=new HouseDTO();
		long id=rs.getLong("Id");
		house.setId(id);
		house.setAddress(rs.getString("Address"));
		house.setArea(rs.getDouble("Area"));
		house.setCheckInDateTime(rs.getDate("CheckInDateTime"));
		house.setCityId(rs.getLong("CityId"));
		house.setCityName(rs.getString("CityName"));
		house.setCommunityBuiltYear((Integer)rs.getObject("CommunityBuiltYear"));
		house.setCommunityId(rs.getLong("CommunityId"));
		house.setCommunityLocation(rs.getString("communityLocation"));
		house.setCommunityName(rs.getString("CommunityName"));
		house.setCommunityTraffic(rs.getString("CommunityTraffic"));
		house.setCreateDateTime(rs.getDate("CreateDateTime"));
		house.setDecorateStatusId(rs.getLong("DecorateStatusId"));
		house.setDecorateStatusName(rs.getString("DecorateStatusName"));
		house.setDeleted(rs.getBoolean("IsDeleted"));
		house.setDescription(rs.getString("Description"));
		house.setDirection(rs.getString("Direction"));
		house.setFloorIndex(rs.getInt("FloorIndex"));
		house.setLookableDateTime(rs.getDate("LookableDateTime"));
		house.setMonthRent(rs.getInt("MonthRent"));
		house.setOwnerName(rs.getString("OwnerName"));
		house.setOwnerPhoneNum(rs.getString("OwnerPhoneNum"));
		house.setRegionId(rs.getLong("RegionId"));
		house.setRegionName(rs.getString("RegionName"));
		house.setRoomTypeId(rs.getLong("RoomTypeId"));
		house.setRoomTypeName(rs.getString("RoomTypeName"));
		house.setStatusId(rs.getLong("StatusId"));
		house.setStatusName(rs.getString("StatusName"));
		house.setTotalFloorCount(rs.getInt("TotalFloorCount"));
		house.setTypeId(rs.getLong("TypeId"));
		house.setTypeName(rs.getString("TypeName"));
		ResultSet rsAtt=null;
		List<Long> listAttId=new ArrayList<Long>();
		try{
		rsAtt=JDBCUtils.executeQuery("select AttachmentId from T_houseattachments where HouseId=?",id);
		while(rsAtt.next()){
			listAttId.add(rsAtt.getLong("AttachmentId"));
		}
		}finally{
			JDBCUtils.closeAll(rsAtt);
		}
		Long[] attIds=listAttId.toArray(new Long[listAttId.size()]);
		house.setAttachmentIds(ArrayUtils.toPrimitive(attIds));
		HousePicDTO[] pics=getPics(id);
		if(pics.length>0){
			house.setFirstThumbUrl(pics[0].getThumbUrl());
		}
		return house;
	}
	public HouseDTO getById(long id){
		StringBuilder sb=new StringBuilder();
		sb.append(selectSQLMain);
		sb.append("where h.Id=?");
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery(sb.toString(),id);
			if(rs.next()){
				return toHouseDTO(rs);
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
	 *获取typeId这种房源类别下cityId这个城市中房源的总数量 
	 * @param cityId
	 * @param typeId
	 * @return
	 */
	public long getTotalCount(long cityId, long typeId){
		try {
			Number number=(Number)JDBCUtils.querySingle("select count(*) from T_houses h left join T_regions r on r.Id=h.RegionId where r.CityId=? and h.TypeId=?", cityId,typeId);
			return number.longValue();
		} catch (SQLException e) {
		throw new RuntimeException(e);
		}
	}
	/**
	 * 获得所有的房子
	 * @return
	 */
	public HouseDTO[] getAll(){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_houses");
			 List<HouseDTO> list=new ArrayList<HouseDTO>();
			 while(rs.next()){
				 HouseDTO house=new HouseDTO();
				 house.setId(rs.getLong("Id"));
				 list.add(house);
			 }
			 return list.toArray(new HouseDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally {
			JDBCUtils.closeAll(rs);
		}
	
	}
	
	/**
	 * 分页获取typeId这种房源类别下cityId这个城市中房源
	 * @param cityId
	 * @param typeId
	 * @param pageSize
	 * @param currentIndex
	 * @return
	 */
	public HouseDTO[] getPagedData(long cityId, long typeId, int pageSize, long currentIndex){
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		sb.append(selectSQLMain);
		sb.append("where c.Id=? and h.IsDeleted=0 and h.TypeId=?\n");
		sb.append("limit ?,?\n");
		try {
			rs=JDBCUtils.executeQuery(sb.toString(),cityId,typeId,(currentIndex-1)*pageSize,pageSize);
		  List<HouseDTO> list=new ArrayList<HouseDTO>();
			while(rs.next()){
			  list.add(toHouseDTO(rs));
		  }
			return list.toArray(new HouseDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
	
	//新增房源，返回房源id  （讲） 事务
	/**
	 * 新增房源，返回房源id  （讲） 事务
	 * @param house
	 * @return
	 */
	 public long addnew(HouseDTO house){
		 StringBuilder sb=new StringBuilder();
		 sb.append("insert into T_houses(RegionId,CommunityId,RoomTypeId,Address,MonthRent,\n");
		 sb.append("StatusId,Area,DecorateStatusId,TotalFloorCount,FloorIndex,TypeId,Direction,\n");
		 sb.append("LookableDateTime,CheckInDateTime,OwnerName,OwnerPhoneNum,\n");
		 sb.append("CreateDateTime,Description,IsDeleted)\n");
		 sb.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,0)\n");
		 Connection conn=null;
		 try {
			 conn=JDBCUtils.getConnection();
			 conn.setAutoCommit(false);
			long houId=JDBCUtils.executeInsert(conn, sb.toString(),house.getRegionId(),house.getCommunityId(),
					house.getRoomTypeId(),house.getAddress(),house.getMonthRent(),house.getStatusId(),house.getArea(),
					house.getDecorateStatusId(),house.getTotalFloorCount(),house.getFloorIndex(),house.getTypeId(),
					house.getDirection(),house.getLookableDateTime(),house.getCheckInDateTime(),house.getOwnerName(),
					house.getOwnerPhoneNum(),house.getDescription());
			for(long attId : house.getAttachmentIds()){
				JDBCUtils.executeNonQuery(conn, "insert into T_houseattachments(HouseId,AttachmentId) values(?,?)", houId,attId);
			}
			
			conn.commit();
			return houId;
		} catch (SQLException e) {
			JDBCUtils.rollBack(conn);
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeQuietly(conn);
		}
	 }
	 /**
	  * 更新房源，房源的附件先删除再新增 （讲）
	  * @param house
	  */
	  public void update(HouseDTO house){
		  StringBuilder sb=new StringBuilder();
		  sb.append("update  T_houses set RegionId=?,CommunityId=?,RoomTypeId=?,Address=?,MonthRent=?,\n");
		 sb.append("StatusId=?,Area=?,DecorateStatusId=?,TotalFloorCount=?,FloorIndex=?,TypeId=?,Direction=?,\n");
		 sb.append("LookableDateTime=?,CheckInDateTime=?,OwnerName=?,OwnerPhoneNum=?,\n");
		 sb.append("Description=?\n");
		  sb.append("where Id=?\n");
		 Connection conn=null;
		 try{
			 conn=JDBCUtils.getConnection();
			 conn.setAutoCommit(false);
			 JDBCUtils.executeNonQuery(conn,sb.toString(),house.getRegionId(),house.getCommunityId(),
				house.getRoomTypeId(),house.getAddress(),house.getMonthRent(),house.getStatusId(),house.getArea(),
				house.getDecorateStatusId(),house.getTotalFloorCount(),house.getFloorIndex(),house.getTypeId(),
				house.getDirection(),house.getLookableDateTime(),house.getCheckInDateTime(),house.getOwnerName(),
				house.getOwnerPhoneNum(),house.getDescription(),house.getId());
			 JDBCUtils.executeNonQuery(conn, "delete from T_houseattachments where HouseId=?",house.getId());
			 for(long attId : house.getAttachmentIds()){
					JDBCUtils.executeNonQuery(conn, "insert into T_houseattachments(HouseId,AttachmentId) values(?,?)", house.getId(),attId);
				}
			 conn.commit();
		 }catch(SQLException e){
			 JDBCUtils.rollBack(conn);
			 throw new RuntimeException(e);
		 }finally{
			 JDBCUtils.closeQuietly(conn);
		 }
	  }
	/**
	 * 软删除
	 * @param id
	 */
	public void markDeleted(long id){
		try {
			JDBCUtils.executeNonQuery("update T_houses set IsDeleted=1 where Id=?", id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 获得HousePicDTO 对象
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static  HousePicDTO toHousePicDTO(ResultSet rs) throws SQLException{
	HousePicDTO houpic=new HousePicDTO();
	houpic.setCreateDateTime(rs.getDate("CreateDateTime"));
	houpic.setDeleted(rs.getBoolean("IsDeleted"));
	houpic.setHeight(rs.getInt("Height"));
	houpic.setHouseId(rs.getLong("HouseId"));
	houpic.setId(rs.getLong("Id"));
	houpic.setThumbUrl(rs.getString("ThumbUrl"));
	houpic.setUrl(rs.getString("Url"));
	houpic.setWidth(rs.getInt("Width"));
	return houpic;
	}
	/**
	 * 得到房源的图片
	 * @param houseId
	 * @return
	 */
	public static HousePicDTO[] getPics(long houseId){
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery("select * from T_housepics where HouseId=? and IsDeleted=0", houseId);
			List<HousePicDTO> list=new ArrayList<HousePicDTO>();
			while(rs.next()){
				list.add(toHousePicDTO(rs));
			}
			return list.toArray(new HousePicDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
	/**
	 * 添加房源图片
	 * @param housePic
	 * @return
	 */
	public long addnewHousePic(HousePicDTO housePic){
		try {
			Number number=(Number)JDBCUtils.executeInsert("insert into T_housepics(HouseId,Url,ThumbUrl,Width,Height,CreateDateTime,IsDeleted) "
					+ "values(?,?,?,?,?,now(),0)",housePic.getHouseId(),housePic.getUrl(),housePic.getThumbUrl(),housePic.getWidth(),housePic.getHeight());
			return number.longValue();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 *软删除房源图片 
	 * @param housePicId
	 */
	public void deleteHousePic(long housePicId){
		try {
			JDBCUtils.executeNonQuery("update T_housepics set IsDeleted=1 where Id=?", housePicId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 搜索，返回值包含：总条数和HouseDTO[] 两个属性
	 * @param options
	 * @return
	 */
	 public HouseDTO[] Search(HouseSearchOptions options){
		 StringBuilder sb=new StringBuilder();
		 sb.append(selectSQLMain);
		 sb.append("where c.Id=?\n");
		 ArrayList<Object> serList=new ArrayList<>();
		 serList.add(options.getCityId());
		 if(options.getRegionId()!=null){
			 sb.append("and r.Id=?\n");
			 serList.add(options.getRegionId());
		 }
		 if(options.getTypeId()!=null){
			 sb.append("and h.TypeId=?\n");
			 serList.add(options.getTypeId());
		 }
		 if(options.getStartMonthRent()!=null){
			 sb.append("and MonthRent>?\n");
			 serList.add(options.getStartMonthRent());
		 }
		 if(options.getEndMonthRent()!=null){
			 sb.append("and MonthRent<?\n");
			 serList.add(options.getEndMonthRent());
		 }
		 if(!StringUtils.isEmpty(options.getKeyWords())){
			 sb.append("and h.RegionName like ?");
			 serList.add("%"+options.getKeyWords()+"%");
		 }
		 if(options.getOrderByType()==OrderByType.Area){
			 sb.append("order by h.Area ASC\n");
			 
		 }else if(options.getOrderByType()==OrderByType.MonthRent){
			 sb.append("order by h.MonthRent ASC\n"); 
		 }
		 sb.append("limit ?,?\n");
		 serList.add((options.getCurrentIndex()-1)*options.getPageSize());
		 serList.add(options.getPageSize());
		 ResultSet rs=null;
		 List<HouseDTO> list=new ArrayList<HouseDTO>();
		 try {
			rs=JDBCUtils.executeQuery(sb.toString(), serList.toArray());
			while(rs.next()){
				list.add(toHouseDTO(rs));
			}
			return list.toArray(new HouseDTO[list.size()]);
		} catch (SQLException e) {
		throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	 }
	 public HouseSearchResult Search2(HouseSearchOptions options){
		 HouseSearchResult searchResult=new HouseSearchResult();
		 StringBuilder sb=new StringBuilder();
		 sb.append(selectSQLMain);
		 sb.append("where c.Id=?\n");
		 ArrayList<Object> serList=new ArrayList<>();
		 serList.add(options.getCityId());
		 if(options.getRegionId()!=null){
			 sb.append("and r.Id=?\n");
			 serList.add(options.getRegionId());
		 }
		 if(options.getTypeId()!=null){
			 sb.append("and h.TypeId=?\n");
			 serList.add(options.getTypeId());
		 }
		 if(options.getStartMonthRent()!=null){
			 sb.append("and MonthRent>?\n");
			 serList.add(options.getStartMonthRent());
		 }
		 if(options.getEndMonthRent()!=null){
			 sb.append("and MonthRent<?\n");
			 serList.add(options.getEndMonthRent());
		 }
		 if(!StringUtils.isEmpty(options.getKeyWords())){
			 sb.append("and c.Name like ?\n");
			 serList.add("%"+options.getKeyWords()+"%");
		 }
		 if(options.getOrderByType()==OrderByType.Area){
			 sb.append("order by h.Area ASC\n");
			 
		 }else if(options.getOrderByType()==OrderByType.MonthRent){
			 sb.append("order by h.MonthRent ASC\n"); 
		 }
		 Number numer=null;
		try {
			 numer=(Number) JDBCUtils.querySingle("select count(*) from ("+sb+") v", serList.toArray());
		   searchResult.setTotalCount(numer.longValue());
		} catch (SQLException e1) {
			throw new RuntimeException(e1);
		}
		 
		 sb.append("limit ?,?\n");
		 serList.add((options.getCurrentIndex()-1)*options.getPageSize());
		 serList.add(options.getPageSize());
		 ResultSet rs=null;
		 List<HouseDTO> list=new ArrayList<HouseDTO>();
		 try {
			rs=JDBCUtils.executeQuery(sb.toString(), serList.toArray());
			while(rs.next()){
				list.add(toHouseDTO(rs));
			}
			searchResult.setResult(list.toArray(new HouseDTO[list.size()]));
		} catch (SQLException e) {
		throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
		 return searchResult;
	 }
    
}
