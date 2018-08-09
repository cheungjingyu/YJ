package com.zsz.front.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.zsz.dto.AttachmentDTO;
import com.zsz.dto.HouseDTO;
import com.zsz.dto.HousePicDTO;
import com.zsz.dto.HouseSearchOptions;
import com.zsz.dto.HouseSearchOptions.OrderByType;
import com.zsz.dto.HouseSearchResult;
import com.zsz.front.utils.CacheManager;
import com.zsz.front.utils.FrontUtils;
import com.zsz.service.AttachmentService;
import com.zsz.service.CityService;
import com.zsz.service.HouseAppointmentService;
import com.zsz.service.HouseService;
import com.zsz.service.IdNameService;
import com.zsz.service.RegionService;
import com.zsz.tools.AjaxResult;
import com.zsz.tools.CommonUtils;
import com.zsz.tools.Functions;

import redis.clients.jedis.Jedis;
@WebServlet("/House")
public class HouseServlet extends BasicServlet {
	/**
	 * 改造后的，运用Redis数据库（缓存），先从缓存中查找，如果找不到在去找数据库，可以降低数据库压力
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void view(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long houseId=Long.parseLong(req.getParameter("houseId"));
		/*
		 * 运用缓存的思想（步骤）
		 * 先尝试从缓存中获取数据，如果没有找到，则从数据库中获取，并且把获取到的结果放到缓存中，如果从缓存中取到了，则直接用缓存中的数据
		 * 因此缓存可以提高系统的运行效率（数据库的访问速度比Redis访问速度慢），降低数据库压力
		 * 缓存就是用访问速度快的设备来缓存访问数据慢的设备
		 * */
		
//		
//		Jedis jedis=FrontUtils.createJedis();//创建Redis连接
//		String houseJson=jedis.get("House"+houseId); //从Redis中获取指定key（House+houseId）的值，看看缓存中有没有
//		//key一定不能和别人重复
//		HouseService houseService=new HouseService();
//		if(StringUtils.isEmpty(houseJson)){//如果没有取到值就说明没有缓存，只能到数据库中去查
//		
//			HouseDTO house=houseService.getById(houseId);
//			req.setAttribute("house", house);
//			houseJson=CommonUtils.createGson().toJson(house);
//			jedis.setex("House"+houseId,30, houseJson);//把对象json序列化成字符串（因为Redis只支持字符串）保存，缓存30秒
//		}else{//如果从Redis中取到了，则直接反序列化为HouseDTO，不用再查数据库了
//			HouseDTO house=CommonUtils.createGson().fromJson(houseJson, HouseDTO.class);
//			req.setAttribute("house", house);
//		}
		HouseService houseService=new HouseService();
		CacheManager cacheManager=CacheManager.getManager();
		HouseDTO house=(HouseDTO)cacheManager.getValue("House"+houseId, HouseDTO.class);//首先到缓存中查找
		if(house==null){//如果没找到，则去数据库中查找
			house=houseService.getById(houseId);
			cacheManager.setValue("House"+houseId, 30, house);//在放到缓存中
		}
			req.setAttribute("house", house);
		
		
		
		HousePicDTO[] pics=houseService.getPics(houseId);
		req.setAttribute("pics", pics);
		AttachmentService attachmentService=new AttachmentService();
		AttachmentDTO[] attachments=attachmentService.getAttachments(houseId);
		req.setAttribute("attachments",attachments);
		req.getRequestDispatcher("/WEB-INF/house/view.jsp").forward(req, resp);
	}

	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void view1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long houseId=Long.parseLong(req.getParameter("houseId"));
		HouseService houseService=new HouseService();
		HouseDTO house=houseService.getById(houseId);
		req.setAttribute("house", house);
		HousePicDTO[] pics=houseService.getPics(houseId);
		req.setAttribute("pics", pics);
		AttachmentService attachmentService=new AttachmentService();
		AttachmentDTO[] attachments=attachmentService.getAttachments(houseId);
		req.setAttribute("attachments",attachments);
		req.getRequestDispatcher("/WEB-INF/house/view.jsp").forward(req, resp);
	}
	
	/**
	 * 数据库实现搜索功能
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void search1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long cityId=FrontUtils.getCurrentCityId(req);
		
		String strRegionId=req.getParameter("regionId");
		String strMonthRent=req.getParameter("monthRent");
		String strOrderBy=req.getParameter("orderBy");
		String strTypeId=req.getParameter("typeId");
		String keyWords=req.getParameter("keyWords");
		//Integer pageSize=Integer.parseInt("pageSize");
		Integer startMonthRent=null;
		Integer endMonthRent=null;
		if(!StringUtils.isEmpty(strMonthRent)){
			String[] monthRents=strMonthRent.split("-");//100-200,*-200,200-*
			if(!monthRents[0].equals("*")){//如果为*就是不设限
				startMonthRent=Integer.parseInt(monthRents[0]);
			}
			if(!monthRents[1].equals("*")){
				endMonthRent=Integer.parseInt(monthRents[1]);
			}
			
		}
		Long regionId=null;
		if(!StringUtils.isEmpty(strRegionId)){
			regionId=Long.parseLong(strRegionId);
		}
		Long typeId=null;
		if(!StringUtils.isEmpty(strTypeId)){
			typeId=Long.parseLong(strTypeId);
		}
		
		StringBuilder sbSerachDisplay=new StringBuilder();
		sbSerachDisplay.append(new CityService().getById(cityId).getName()).append(",");
		if(regionId!=null){
			sbSerachDisplay.append(new RegionService().getById(regionId).getName()).append(",");
		}
		if(startMonthRent!=null){
			sbSerachDisplay.append("房租高于").append(startMonthRent).append(",");
		}
		if(endMonthRent!=null){
			sbSerachDisplay.append("房租低于").append(endMonthRent).append(",");
		}
		if(typeId!=null){
			sbSerachDisplay.append(new IdNameService().getById(typeId).getName()).append(",");
		}
		if(!StringUtils.isEmpty(keyWords)){
			sbSerachDisplay.append(keyWords);
		}
	req.setAttribute("searchDisplay", sbSerachDisplay.toString());
		
	Long pageIndex=1L;
	String strPageIndex=req.getParameter("pageIndex");
	if(!StringUtils.isEmpty(strPageIndex)){
		pageIndex=Long.parseLong(strPageIndex);
	}
	
	
		HouseSearchOptions searchOptions=new HouseSearchOptions();
		searchOptions.setCityId(cityId);
		searchOptions.setCurrentIndex(pageIndex);
		searchOptions.setEndMonthRent(endMonthRent);
		searchOptions.setKeyWords(keyWords);
		searchOptions.setOrderByType("monthRent".equals(strOrderBy)?OrderByType.MonthRent:OrderByType.Area);
		searchOptions.setPageSize(3);
		searchOptions.setRegionId(regionId);
		searchOptions.setStartMonthRent(startMonthRent);
		searchOptions.setTypeId(typeId);
		HouseService houseService=new HouseService();
		HouseSearchResult searchResult=houseService.Search2(searchOptions);
	    req.setAttribute("houses", searchResult.getResult());
		//req.setAttribute("cityName", new CityService().getById(cityId).getName());
		
		RegionService regionService=new RegionService();
		req.setAttribute("regions",regionService.getAll(cityId));
		req.setAttribute("queryString",req.getQueryString());
		
		
		req.setAttribute("pageIndex", pageIndex);
		req.setAttribute("totalCount", searchResult.getTotalCount());
		String pageUrlFormat=Functions.addQueryStringPart(req.getQueryString(), "pageIndex", "{pageNum}");
		req.setAttribute("pageUrlFormat", req.getContextPath()+"/House?"+pageUrlFormat);
		req.getRequestDispatcher("/WEB-INF/house/search.jsp").forward(req, resp);
		
	}
	/**
	 * 运用solr服务器实现搜索功能
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void search(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long cityId=FrontUtils.getCurrentCityId(req);
		
		String strRegionId=req.getParameter("regionId");
		String strMonthRent=req.getParameter("monthRent");
		String strOrderBy=req.getParameter("orderBy");
		String strTypeId=req.getParameter("typeId");
		String keyWords=req.getParameter("keyWords");
		
		Integer startMonthRent=null;
		Integer endMonthRent=null;
		//拼接solr服务器的查询语句
		StringBuilder queryString=new StringBuilder();
		queryString.append("cityId:").append(cityId);
		
		if(!StringUtils.isEmpty(strMonthRent)){
			queryString.append(" AND montRent:[").append(strMonthRent.replace("-", "TO")).append("]");
			String[] monthRents=strMonthRent.split("-");//100-200,*-200,200-*
			if(!monthRents[0].equals("*")){//如果为*就是不设限
				startMonthRent=Integer.parseInt(monthRents[0]);
			}
			if(!monthRents[1].equals("*")){
				endMonthRent=Integer.parseInt(monthRents[1]);
			}
			
		}
		Long regionId=null;
		if(!StringUtils.isEmpty(strRegionId)){
			regionId=Long.parseLong(strRegionId);
			
			queryString.append(" AND regionId:").append(regionId);
		}
		Long typeId=null;
		if(!StringUtils.isEmpty(strTypeId)){
			typeId=Long.parseLong(strTypeId);
			queryString.append(" AND typeId:").append(typeId);
		}
		
		StringBuilder sbSerachDisplay=new StringBuilder();
		sbSerachDisplay.append(new CityService().getById(cityId).getName()).append(",");
		if(regionId!=null){
			sbSerachDisplay.append(new RegionService().getById(regionId).getName()).append(",");
		}
		if(startMonthRent!=null){
			sbSerachDisplay.append("房租高于").append(startMonthRent).append(",");
		}
		if(endMonthRent!=null){
			sbSerachDisplay.append("房租低于").append(endMonthRent).append(",");
		}
		if(typeId!=null){
			sbSerachDisplay.append(new IdNameService().getById(typeId).getName()).append(",");
		}
		if(!StringUtils.isEmpty(keyWords)){
			sbSerachDisplay.append(keyWords);
			queryString.append(" AND (regionName:").append(keyWords).append(" OR communityName:").append(keyWords)
			.append(" OR communityLocation:").append(keyWords).append(" OR communityTraffic:").append(keyWords).append(" OR roomTypeName:")
			.append(keyWords).append(" OR address:").append(keyWords).append(" OR statusName:").append(keyWords)
			.append(" OR decorateStatusName:").append(keyWords).append(" OR typeName:").append(keyWords).append(" OR description:").append(keyWords)
			.append(")");
		}
	req.setAttribute("searchDisplay", sbSerachDisplay.toString());
		
	Long pageIndex=1L;
	String strPageIndex=req.getParameter("pageIndex");
	if(!StringUtils.isEmpty(strPageIndex)){
		pageIndex=Long.parseLong(strPageIndex);
	}
	//创建客户端的连接
			HttpSolrClient.Builder builder=new HttpSolrClient.Builder("http://localhost:8983/solr/houses");
			//创建客户端连接的对象
			HttpSolrClient solrClient=builder.build();
			//新建查询语句
			SolrQuery solrQuery=new SolrQuery(queryString.toString());//AND OR  NOT 
			if(!StringUtils.isEmpty(strOrderBy)){
				solrQuery.setSort(strOrderBy, ORDER.asc);
			}
			
			solrQuery.setStart((int)((pageIndex-1)*2));
			solrQuery.setRows(2);
			QueryResponse respQuery;
			List<HouseDTO> houses=new ArrayList<HouseDTO>();
			try {
				respQuery = solrClient.query(solrQuery);
			} catch (SolrServerException e) {
				throw new RuntimeException(e);
			}
			SolrDocumentList documentList= respQuery.getResults();
			
			HouseService houseService=new HouseService();
			for(SolrDocument doc : documentList){
			HouseDTO house=	houseService.getById(Long.parseLong((String)doc.get("id")));
			houses.add(house);
			}
			
		
				solrClient.close();
			
			
	    req.setAttribute("houses", houses.toArray());
		//req.setAttribute("cityName", new CityService().getById(cityId).getName());
		RegionService regionService=new RegionService();
		req.setAttribute("regions",regionService.getAll(cityId));
		req.setAttribute("queryString",req.getQueryString());
		req.setAttribute("pageIndex", pageIndex);
		req.setAttribute("totalCount", documentList.getNumFound());
		String pageUrlFormat=Functions.addQueryStringPart(req.getQueryString(), "pageIndex", "{pageNum}");
		req.setAttribute("pageUrlFormat", req.getContextPath()+"/House?"+pageUrlFormat);
		req.getRequestDispatcher("/WEB-INF/house/search.jsp").forward(req, resp);
		
	}
	public void makeAppointment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name=req.getParameter("name");
		String phoneNum=req.getParameter("phoneNum");
		String visitDate=req.getParameter("visitDate");
		Long houseId=Long.parseLong(req.getParameter("houseId"));
		Long userId=FrontUtils.getCurrentUserId(req);
		HouseAppointmentService appointmentService=new HouseAppointmentService();
		appointmentService.addnew(userId, name, phoneNum, houseId,CommonUtils.parseDate(visitDate));
		writeJson(resp, new AjaxResult("ok"));
	}

}
