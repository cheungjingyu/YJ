package com.zsz.service;
import java.util.Date;
import com.zsz.dao.HouseAppointmentDAO;
import com.zsz.dto.HouseAppointmentDTO;
public class HouseAppointmentService {
	private HouseAppointmentDAO dao=new HouseAppointmentDAO();
	public long addnew(Long userId, String name, String phoneNum, long houseId, Date visitDate){
		return dao.addnew(userId, name, phoneNum, houseId, visitDate);
	}
	
	public HouseAppointmentDTO getById(long id){
		return dao.getById(id);
	}
	public long getTotalCount(){
		return dao.getTotalCount();
	}
	public long getTotalCount(long cityId, String status){
		return dao.getTotalCount(cityId, status);
	}
	 public HouseAppointmentDTO[] getPagedData(long cityId, String status, int pageSize, long currentIndex){
		return dao.getPagedData(cityId, status, pageSize, currentIndex);
	 }
	 public boolean follow(long adminUserId, long houseAppointmentId) {
		return dao.follow(adminUserId, houseAppointmentId);
	 }
}
