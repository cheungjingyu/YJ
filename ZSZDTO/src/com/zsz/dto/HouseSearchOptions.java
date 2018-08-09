package com.zsz.dto;

public class HouseSearchOptions {
private long cityId;
private Long typeId;
private Long regionId;
private  Integer startMonthRent;
private Integer endMonthRent;
private OrderByType orderByType=OrderByType.MonthRent;
private String keyWords;
private int pageSize;
private long currentIndex;
public long getCityId() {
	return cityId;
}

public void setCityId(long cityId) {
	this.cityId = cityId;
}

public Long getTypeId() {
	return typeId;
}

public void setTypeId(Long typeId) {
	this.typeId = typeId;
}

public Long getRegionId() {
	return regionId;
}

public void setRegionId(Long regionId) {
	this.regionId = regionId;
}

public Integer getStartMonthRent() {
	return startMonthRent;
}

public void setStartMonthRent(Integer startMonthRent) {
	this.startMonthRent = startMonthRent;
}

public Integer getEndMonthRent() {
	return endMonthRent;
}

public void setEndMonthRent(Integer endMonthRent) {
	this.endMonthRent = endMonthRent;
}

public OrderByType getOrderByType() {
	return orderByType;
}

public void setOrderByType(OrderByType orderByType) {
	this.orderByType = orderByType;
}

public String getKeyWords() {
	return keyWords;
}

public void setKeyWords(String keyWords) {
	this.keyWords = keyWords;
}

public int getPageSize() {
	return pageSize;
}

public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
}

public long getCurrentIndex() {
	return currentIndex;
}

public void setCurrentIndex(long currentIndex) {
	this.currentIndex = currentIndex;
}

public enum OrderByType{
	Area,MonthRent;
};

}
