package com.zsz.dto;

public class IdNameDTO {
private long id;
private String name;
private String typeName;
private boolean isDeleted;
public boolean isDeleted() {
	return isDeleted;
}
public void setDeleted(boolean isDeleted) {
	this.isDeleted = isDeleted;
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getTypeName() {
	return typeName;
}
public void setTypeName(String typeName) {
	this.typeName = typeName;
}
}
