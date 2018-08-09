package com.zsz.service;

import java.util.Map;

import com.zsz.dao.ReportDAO;

public class ReportService {
  ReportDAO reportDAO=new ReportDAO();
//获得最近24小时内的新增的房源
	public Map<String,Long> getYesterdayCityInfo(){
		return reportDAO.getYesterdayCityInfo();
	}
}
