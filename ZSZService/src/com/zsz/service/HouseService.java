package com.zsz.service;
import com.zsz.dao.HouseDAO;
import com.zsz.dto.HouseDTO;
import com.zsz.dto.HousePicDTO;
import com.zsz.dto.HouseSearchOptions;
import com.zsz.dto.HouseSearchResult;
public class HouseService {
	private HouseDAO dao =new HouseDAO();
	public HouseDTO getById(long id){
		return dao.getById(id);
	}
	public long getTotalCount(long cityId, long typeId){
		return dao.getTotalCount(cityId, typeId);
	}
	public HouseDTO[] getPagedData(long cityId, long typeId, int pageSize, long currentIndex){
		return dao.getPagedData(cityId, typeId, pageSize, currentIndex);
	}
	public HouseDTO[] getAll(){
		return dao.getAll();
	}
	 public long addnew(HouseDTO house){
		 return dao.addnew(house);
	 }
	  public void update(HouseDTO house){
		  dao.update(house);
	  }
	public void markDeleted(long id){
		dao.markDeleted(id);
	}
	public  HousePicDTO[] getPics(long houseId){
		return HouseDAO.getPics(houseId);
	}
	public long addnewHousePic(HousePicDTO housePic){
		return dao.addnewHousePic(housePic);
	}
	public void deleteHousePic(long housePicId){
		dao.deleteHousePic(housePicId);
	}
	 public HouseDTO[] Search(HouseSearchOptions options){
		 return dao.Search(options);
	 }
	 public HouseSearchResult Search2(HouseSearchOptions options){
		 return dao.Search2(options);
	 }
}
