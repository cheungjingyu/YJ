package com.zsz.dto;

import java.util.Date;

public class HouseDTO {
private long id;
private long cityId;//城市id
private String cityName;//城市名字
private long regionId;//地区id
private String regionName;//地区名称
private long communityId;//小区id
private String communityName;//小区名字
private String communityLocation;//小区位置
private String communityTraffic;//小区交通状况
private Integer communityBuiltYear;//小区建造年代
private long roomTypeId;//房屋户型id：三室一厅等
private String roomTypeName;//房屋户型名字
private String address;//地址
private int monthRent;//月租金
private long statusId;//状态：出租中，已经出租等
private String statusName;//状态名字
private double area;//面积
private long decorateStatusId;//装修状态:精装，简装
private String decorateStatusName;//装修状态名字
private int totalFloorCount;//总楼层
private int floorIndex;//房屋所在楼层
private long typeId;//房屋类型：短租 写字楼 合租 整租
private String typeName;//房屋类型名字
private String direction;//朝向
private Date lookableDateTime;//可以看房的时间
private Date checkInDateTime;//可以入住的时间
private String ownerName;//房主姓名
private String ownerPhoneNum;//房主电话
private String description;//房源描述
private Date createDateTime;//记录时间
private boolean isDeleted;//是否软删除
private long[] attachmentIds;//房屋设施id
private String firstThumbUrl;//第一张缩略图

public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public long getCityId() {
	return cityId;
}
public void setCityId(long cityId) {
	this.cityId = cityId;
}
public String getCityName() {
	return cityName;
}
public void setCityName(String cityName) {
	this.cityName = cityName;
}
public long getRegionId() {
	return regionId;
}
public void setRegionId(long regionId) {
	this.regionId = regionId;
}
public String getRegionName() {
	return regionName;
}
public void setRegionName(String regionName) {
	this.regionName = regionName;
}
public long getCommunityId() {
	return communityId;
}
public void setCommunityId(long communityId) {
	this.communityId = communityId;
}
public String getCommunityName() {
	return communityName;
}
public void setCommunityName(String communityName) {
	this.communityName = communityName;
}
public String getCommunityLocation() {
	return communityLocation;
}
public void setCommunityLocation(String communityLocation) {
	this.communityLocation = communityLocation;
}
public String getCommunityTraffic() {
	return communityTraffic;
}
public void setCommunityTraffic(String communityTraffic) {
	this.communityTraffic = communityTraffic;
}
public Integer getCommunityBuiltYear() {
	return communityBuiltYear;
}
public void setCommunityBuiltYear(Integer communityBuiltYear) {
	this.communityBuiltYear = communityBuiltYear;
}
public long getRoomTypeId() {
	return roomTypeId;
}
public void setRoomTypeId(long roomTypeId) {
	this.roomTypeId = roomTypeId;
}
public String getRoomTypeName() {
	return roomTypeName;
}
public void setRoomTypeName(String roomTypeName) {
	this.roomTypeName = roomTypeName;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public int getMonthRent() {
	return monthRent;
}
public void setMonthRent(int monthRent) {
	this.monthRent = monthRent;
}
public long getStatusId() {
	return statusId;
}
public void setStatusId(long statusId) {
	this.statusId = statusId;
}
public String getStatusName() {
	return statusName;
}
public void setStatusName(String statusName) {
	this.statusName = statusName;
}
public double getArea() {
	return area;
}
public void setArea(double area) {
	this.area = area;
}
public long getDecorateStatusId() {
	return decorateStatusId;
}
public void setDecorateStatusId(long decorateStatusId) {
	this.decorateStatusId = decorateStatusId;
}
public String getDecorateStatusName() {
	return decorateStatusName;
}
public void setDecorateStatusName(String decorateStatusName) {
	this.decorateStatusName = decorateStatusName;
}
public int getTotalFloorCount() {
	return totalFloorCount;
}
public void setTotalFloorCount(int totalFloorCount) {
	this.totalFloorCount = totalFloorCount;
}
public int getFloorIndex() {
	return floorIndex;
}
public void setFloorIndex(int floorIndex) {
	this.floorIndex = floorIndex;
}
public long getTypeId() {
	return typeId;
}
public void setTypeId(long typeId) {
	this.typeId = typeId;
}
public String getTypeName() {
	return typeName;
}
public void setTypeName(String typeName) {
	this.typeName = typeName;
}
public String getDirection() {
	return direction;
}
public void setDirection(String direction) {
	this.direction = direction;
}
public Date getLookableDateTime() {
	return lookableDateTime;
}
public void setLookableDateTime(Date lookableDateTime) {
	this.lookableDateTime = lookableDateTime;
}
public Date getCheckInDateTime() {
	return checkInDateTime;
}
public void setCheckInDateTime(Date checkInDateTime) {
	this.checkInDateTime = checkInDateTime;
}
public String getOwnerName() {
	return ownerName;
}
public void setOwnerName(String ownerName) {
	this.ownerName = ownerName;
}
public String getOwnerPhoneNum() {
	return ownerPhoneNum;
}
public void setOwnerPhoneNum(String ownerPhoneNum) {
	this.ownerPhoneNum = ownerPhoneNum;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public Date getCreateDateTime() {
	return createDateTime;
}
public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime;
}
public boolean isDeleted() {
	return isDeleted;
}
public void setDeleted(boolean isDeleted) {
	this.isDeleted = isDeleted;
}
public long[] getAttachmentIds() {
	return attachmentIds;
}
public void setAttachmentIds(long[] attachmentIds) {
	this.attachmentIds = attachmentIds;
}
public String getFirstThumbUrl() {
	return firstThumbUrl;
}
public void setFirstThumbUrl(String firstThumbUrl) {
	this.firstThumbUrl = firstThumbUrl;
}

}
