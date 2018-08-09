package com.zsz.dto;

public class HouseSearchResult {
	private HouseDTO[] result;
	private Long totalCount;
	public HouseDTO[] getResult() {
		return result;
	}
	public void setResult(HouseDTO[] result) {
		this.result = result;
	}
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

}
