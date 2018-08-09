package com.zsz.admin.servlet;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.zsz.admin.utils.AdminUtils;
import com.zsz.dto.AttachmentDTO;
import com.zsz.dto.CommunityDTO;
import com.zsz.dto.HouseAppointmentDTO;
import com.zsz.dto.HouseDTO;
import com.zsz.dto.HousePicDTO;
import com.zsz.dto.IdNameDTO;
import com.zsz.dto.RegionDTO;
import com.zsz.service.AttachmentService;
import com.zsz.service.CommunityService;
import com.zsz.service.HouseAppointmentService;
import com.zsz.service.HouseService;
import com.zsz.service.IdNameService;
import com.zsz.service.RegionService;
import com.zsz.service.SettingService;
import com.zsz.tools.AjaxResult;
import com.zsz.tools.CommonUtils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
@WebServlet("/House")
@MultipartConfig
public class HouseServlet extends BasicServlet {
	@HasPermission("House.Query")
	public void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long adminUserCityId=AdminUtils.getAdminUserCityId(req);
		if(adminUserCityId==null){
			AdminUtils.showError(req, resp, "总部的人不能管理房源");
			return;
		}
		Long typeId=Long.parseLong(req.getParameter("typeId"));
		Long pageIndex=Long.parseLong(req.getParameter("pageIndex"));
		req.setAttribute("typeId", typeId);
		req.setAttribute("pageIndex",pageIndex);
		
		HouseService houseService=new HouseService();
		Long totalCount=houseService.getTotalCount(adminUserCityId, typeId);
		req.setAttribute("totalCount", totalCount);
		HouseDTO[] houses=houseService.getPagedData(adminUserCityId, typeId, 10,pageIndex);
		req.setAttribute("houses", houses);
		req.setAttribute("ctxPath", req.getContextPath());
		req.getRequestDispatcher("/WEB-INF/house/houseList.jsp").forward(req, resp);
	}
	@HasPermission("House.Delete")
	public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HouseService houseService = new HouseService();
		Long houseId=Long.parseLong(req.getParameter("id"));
		houseService.markDeleted(houseId);
		//从solr服务器中删除这个房源
		deleteFromSolr(houseId);
		writeJson(resp, new AjaxResult("ok"));
	}
	public void loadCommunities(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long regionId=Long.parseLong(req.getParameter("regionId"));
		CommunityService communityService=new CommunityService();
		CommunityDTO[] communities=communityService.getByRegionId(regionId);
		writeJson(resp, new AjaxResult("ok","", communities));
	}
	@HasPermission("House.AddNew")
	public void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long cityId=AdminUtils.getAdminUserCityId(req);
		if(cityId==null){
			AdminUtils.showError(req, resp, "总部的人不能管理房源");
			return;
		}
		RegionService regionService=new RegionService();
		RegionDTO[] regions=regionService.getAll(cityId);
		req.setAttribute("regions", regions);
		Long typeId=Long.parseLong(req.getParameter("typeId"));
		req.setAttribute("typeId", typeId);
		IdNameService idNameService=new IdNameService();
		IdNameDTO[] roomTypes=idNameService.getAll("户型");
		IdNameDTO[] statuses=idNameService.getAll("房屋状态");
		IdNameDTO[] decorationStatuses=idNameService.getAll("装修状态");
		AttachmentService attachmentService=new AttachmentService();
		AttachmentDTO[] attachments=attachmentService.getAll();
		req.setAttribute("roomTypes", roomTypes);
		req.setAttribute("statuses", statuses);
		req.setAttribute("decorationStatuses", decorationStatuses);
		req.setAttribute("attachments",attachments);
		Date now = new Date();
		req.setAttribute("now",DateFormatUtils.format(now,"yyyy-MM-dd"));
		req.getRequestDispatcher("/WEB-INF/house/houseAdd.jsp").forward(req, resp);
		
	}
	@HasPermission("House.AddNew")
	public void addSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long typeId=Long.parseLong(req.getParameter("typeId"));
	   Long cityId=AdminUtils.getAdminUserCityId(req);
	   Long regionId=Long.parseLong(req.getParameter("regionId"));
	   Long communityId=Long.parseLong(req.getParameter("communityId"));
	   Long roomTypeId=Long.parseLong(req.getParameter("roomTypeId"));
	   String address=req.getParameter("address");
	   Integer monthRent=Integer.parseInt(req.getParameter("monthRent"));
	   Double area=Double.parseDouble(req.getParameter("area"));
	   Long statuseId=Long.parseLong(req.getParameter("statuseId"));
	   Long decorationStatuseId=Long.parseLong(req.getParameter("decorationStatuseId"));
	   Integer totalFloorCount=Integer.parseInt(req.getParameter("totalFloorCount"));
	   Integer floorIndex=Integer.parseInt(req.getParameter("floorIndex"));
	   String direction=req.getParameter("direction");
	   Date lookableDateTime=CommonUtils.parseDate(req.getParameter("lookableDateTime"));
	   Date checkInDateTime=CommonUtils.parseDate(req.getParameter("checkInDateTime"));
	   String ownerName=req.getParameter("ownerName");
	   String ownerPhoneNum=req.getParameter("ownerPhoneNum");
	   String description=req.getParameter("description");
	   String[] attachmentIds=req.getParameterValues("attachmentId");
	    HouseDTO house=new HouseDTO();
	    house.setAddress(address);
	    house.setArea(area);
	    house.setAttachmentIds(CommonUtils.toLongArray(attachmentIds));
	    house.setCheckInDateTime(checkInDateTime);
	    house.setCityId(cityId);
	    house.setCommunityId(communityId);
	    house.setDecorateStatusId(decorationStatuseId);
	    house.setDescription(description);
	    house.setFloorIndex(floorIndex);
	    house.setTypeId(typeId);
	    house.setTotalFloorCount(totalFloorCount);
	    house.setStatusId(statuseId);
	    house.setRoomTypeId(roomTypeId);
	    house.setRegionId(regionId);
	    house.setOwnerPhoneNum(ownerPhoneNum);
	    house.setOwnerName(ownerName);
	    house.setDirection(direction);
	    house.setMonthRent(monthRent);
	    house.setLookableDateTime(lookableDateTime);
	    
	   Long houseId=new HouseService().addnew(house);
	   //插入solr服务器
	   insertToSolr(new HouseService().getById(houseId));
	   createStaticPage(houseId);
	   writeJson(resp, new AjaxResult("ok"));
	}
	/**
	 * 生成所有的静态页面房源
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void setAllStatic(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HouseService houseService=new HouseService();
		HouseDTO[] houses=houseService.getAll();
		for(HouseDTO house:houses){
			createStaticPage(house.getId());
		}
		  writeJson(resp, new AjaxResult("ok"));
		
	}
	/**
	 * 生成一个房源的静态页面
	 * @param houseId
	 */
	 private void createStaticPage(Long houseId){
		 SettingService settingService=new SettingService();
		String FrontRootUrl= settingService.getValue("FrontRootUrl");
		String HouseStaticPagesDir= settingService.getValue("HouseStaticPagesDir");
		 URL url;
		 try {
			url=new URL(FrontRootUrl+houseId);
			String html=IOUtils.toString(url,"UTF-8");//向服务器发送get请求，获得Id为houseId的房源的查看页面的html
			FileUtils.write(new File(HouseStaticPagesDir+houseId+".html"), html,"UTF-8");
			
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		 
	 }
	
	
	/**
	 * 把新增的房源插入solr服务器的方法
	 * @param house
	 */
	public static void insertToSolr(HouseDTO house){
		HttpSolrClient.Builder builder=new HttpSolrClient.Builder("http://localhost:8983/solr/houses");
		SolrClient solrClient= builder.build();
		
		SolrInputDocument  doc=new SolrInputDocument();
		doc.setField("id",house.getId());
		doc.setField("cityId",house.getCityId());
		doc.setField("address",house.getAddress());
		doc.setField("area",house.getArea());
		doc.setField("checkInDateTime",house.getCheckInDateTime());
		doc.setField("communityId",house.getCommunityId());
		doc.setField("communityLocation",house.getCommunityLocation());
		doc.setField("communityName",house.getCommunityName());
		doc.setField("communityTraffic",house.getCommunityTraffic());
		doc.setField("decorateStatusId",house.getDecorateStatusId());
		doc.setField("decorateStatusName",house.getDecorateStatusName());
		doc.setField("description",house.getDescription());
		doc.setField("direction",house.getDirection());
		doc.setField("floorIndex",house.getFloorIndex());
		doc.setField("lookableDateTime",house.getLookableDateTime());
		doc.setField("monthRent",house.getMonthRent());
		doc.setField("regionId",house.getRegionId());
		doc.setField("regionName",house.getRegionName());
		doc.setField("roomTypeId",house.getRoomTypeId());
		doc.setField("roomTypeName",house.getRoomTypeName());
		doc.setField("statusId",house.getStatusId());
		doc.setField("statusName",house.getStatusName());
		doc.setField("totalFloorCount",house.getTotalFloorCount());
		doc.setField("typeId",house.getTypeId());
		doc.setField("typeName",house.getTypeName());
		doc.setField("communityBuiltYear",house.getCommunityBuiltYear());
		try {
			solrClient.add(doc);
			solrClient.commit();
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally{
			try {
				solrClient.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	@HasPermission("House.Edit")
	public void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long id=Long.parseLong(req.getParameter("id"));
		HouseService houseService=new HouseService();
		HouseDTO house=houseService.getById(id);
		if(house==null){
			AdminUtils.showError(req, resp, "房源不存在");
			return;
		}
		Long cityId=AdminUtils.getAdminUserCityId(req);
		if(cityId==null){
			AdminUtils.showError(req, resp, "总部的人不能编辑房源");
			return;
		}
		req.setAttribute("house", house);
		RegionService regionService=new RegionService();
		RegionDTO[] regions=regionService.getAll(cityId);
		req.setAttribute("regions", regions);
		
		IdNameService idNameService=new IdNameService();
		IdNameDTO[] roomTypes=idNameService.getAll("户型");
		IdNameDTO[] statuses=idNameService.getAll("房屋状态");
		IdNameDTO[] decorationStatuses=idNameService.getAll("装修状态");
		AttachmentService attachmentService=new AttachmentService();
		AttachmentDTO[] attachments=attachmentService.getAll();
		req.setAttribute("roomTypes", roomTypes);
		req.setAttribute("statuses", statuses);
		req.setAttribute("decorationStatuses", decorationStatuses);
		req.setAttribute("attachments",attachments);
		AttachmentDTO[] houseAttachments=attachmentService.getAttachments(id);
		long[] houseAttachmentIds=new long[houseAttachments.length];
		for(int i=0;i<houseAttachments.length;i++){
			houseAttachmentIds[i]=houseAttachments[i].getId();
		}
		req.setAttribute("houseAttachmentIds", houseAttachmentIds);
		Date now = new Date();
		req.setAttribute("now",DateFormatUtils.format(now,"yyyy-MM-dd"));
		req.getRequestDispatcher("/WEB-INF/house/houseEdit.jsp").forward(req, resp);
		
	}
	@HasPermission("House.Edit")
	public void editSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		   Long id=Long.parseLong(req.getParameter("id"));
		   Long typeId=Long.parseLong(req.getParameter("typeId"));
		   Long cityId=AdminUtils.getAdminUserCityId(req);
		   Long regionId=Long.parseLong(req.getParameter("regionId"));
		   Long communityId=Long.parseLong(req.getParameter("communityId"));
		   Long roomTypeId=Long.parseLong(req.getParameter("roomTypeId"));
		   String address=req.getParameter("address");
		   Integer monthRent=Integer.parseInt(req.getParameter("monthRent"));
		   Double area=Double.parseDouble(req.getParameter("area"));
		   Long statuseId=Long.parseLong(req.getParameter("statuseId"));
		   Long decorationStatuseId=Long.parseLong(req.getParameter("decorationStatuseId"));
		   Integer totalFloorCount=Integer.parseInt(req.getParameter("totalFloorCount"));
		   Integer floorIndex=Integer.parseInt(req.getParameter("floorIndex"));
		   String direction=req.getParameter("direction");
		   Date lookableDateTime=CommonUtils.parseDate(req.getParameter("lookableDateTime"));
		   Date checkInDateTime=CommonUtils.parseDate(req.getParameter("checkInDateTime"));
		   String ownerName=req.getParameter("ownerName");
		   String ownerPhoneNum=req.getParameter("ownerPhoneNum");
		   String description=req.getParameter("description");
		   String[] attachmentIds=req.getParameterValues("attachmentId");
		    HouseDTO house=new HouseDTO();
		    house.setId(id);
		    house.setAddress(address);
		    house.setArea(area);
		    house.setAttachmentIds(CommonUtils.toLongArray(attachmentIds));
		    house.setCheckInDateTime(checkInDateTime);
		    house.setCityId(cityId);
		    house.setCommunityId(communityId);
		    house.setDecorateStatusId(decorationStatuseId);
		    house.setDescription(description);
		    house.setFloorIndex(floorIndex);
		    house.setTypeId(typeId);
		    house.setTotalFloorCount(totalFloorCount);
		    house.setStatusId(statuseId);
		    house.setRoomTypeId(roomTypeId);
		    house.setRegionId(regionId);
		    house.setOwnerPhoneNum(ownerPhoneNum);
		    house.setOwnerName(ownerName);
		    house.setDirection(direction);
		    house.setMonthRent(monthRent);
		    house.setLookableDateTime(lookableDateTime);
		   new HouseService().update(house);
		   //先删除solr服务器中的这个房源，再添加这个房源
		   deleteFromSolr(id);
		   //添加这个房源
		   insertToSolr(new HouseService().getById(id));
		   createStaticPage(id);
		   writeJson(resp, new AjaxResult("ok"));
	}
	/**
	 * 从solr服务器中删除一个房源
	 * @param houseId
	 */
	private void deleteFromSolr(Long houseId){
		HttpSolrClient.Builder builder=new HttpSolrClient.Builder("http://localhost:8983/solr/houses");
		SolrClient solrClient= builder.build();
		try {
			solrClient.deleteById(String.valueOf(houseId));
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@HasPermission("House.Pics")
	public void picsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long houseId=Long.parseLong(req.getParameter("houseId"));
		req.setAttribute("houseId", houseId);
		HouseService houseService=new HouseService();
		 HousePicDTO[] pics= houseService.getPics(houseId);
		 req.setAttribute("pics",pics);
		 req.getRequestDispatcher("/WEB-INF/house/picsList.jsp").forward(req, resp);
	}
	@HasPermission("House.Pics")
	public void deletePics(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long houseId=Long.parseLong(req.getParameter("houseId"));
		String[] picIds=req.getParameterValues("picIds");
		HouseService houseService=new HouseService();
		for(String picId:picIds){
			houseService.deleteHousePic(Long.parseLong(picId));
		}
		writeJson(resp, new AjaxResult("ok"));
	}
	@HasPermission("House.Pics")
	public void uploadImage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long houseId = Long.parseLong(req.getParameter("houseId"));

		Part part = req.getPart("file");// 用户上传的文件。由于webupload是每个文件一次请求，而且每个上传文件的表单名字都是file
		String filename = part.getSubmittedFileName();// 用户提交的文件名。如果是在IE6、7、8上传文件名会带着路径
		String fileExt = FilenameUtils.getExtension(filename);// 不带.的后缀名
		if (!fileExt.equalsIgnoreCase("jpg") && !fileExt.equalsIgnoreCase("png") && !fileExt.equalsIgnoreCase("jpeg")) {
			return;
		}

		String rootDir = req.getServletContext().getRealPath("/");// 网站根目录的物理路径
																	// c:/1/upload
		rootDir = FilenameUtils.separatorsToUnix(rootDir);// 把路径中的\改成/

		Calendar calendar = Calendar.getInstance();
		String fileRelativePath;// 大图相对路径
		String thumbFileRelativePath;// 缩略图相对路径

		InputStream inStream1 = null;
		InputStream inStream2 = null;

		try {
			inStream1 = part.getInputStream();

			String fileMd5 = CommonUtils.calcMD5(inStream1);// 为了避免第二次算md5值时候指针已经指向结尾，所以只算一次，重复使用
			fileRelativePath = "upload/" + calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "/" + fileMd5 + "." + fileExt;

			// 缩略图的路径
			thumbFileRelativePath = "upload/" + calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "/" + fileMd5 + ".thumb." + fileExt;

			// String filePath = rootDir + fileRelativePath;// 文件的文件夹全路径

			inStream2 = new BufferedInputStream(part.getInputStream());
			inStream2.mark(Integer.MAX_VALUE);

			File fileThumb = new File(rootDir, thumbFileRelativePath);
			fileThumb.getParentFile().mkdirs();// 创建父文件夹
			// 生成缩略图
			Thumbnails.of(inStream2).size(150, 150).toFile(fileThumb);
			inStream2.reset();// 指针归位

			File fileWaterMark = new File(rootDir, fileRelativePath);
			fileWaterMark.getParentFile().mkdirs();
			// 生成水印图片保存
			BufferedImage imgWaterMark = ImageIO
					.read(new File(req.getServletContext().getRealPath("/images/watermark.png")));
			Thumbnails.of(inStream2).size(500, 500).watermark(Positions.BOTTOM_RIGHT, imgWaterMark, 0.5f)
					.toFile(fileWaterMark);// new
											// File(rootDir,fileRelativePath)这样可以避免考虑路径拼接是否有分割符的问题

			HousePicDTO housePic = new HousePicDTO();
			housePic.setHouseId(houseId);
			housePic.setThumbUrl("http://localhost:8080/ZuAdmin/" + thumbFileRelativePath);
			housePic.setUrl("http://localhost:8080/ZuAdmin/" + fileRelativePath);
			housePic.setHeight(500);
			housePic.setWidth(500);
			HouseService houseService = new HouseService();
			houseService.addnewHousePic(housePic);

			// FileUtils.copyInputStreamToFile(inStream2, new File(rootDir,
			// fileRelativePath));// 第一个：帮助我们创建不存在的文件夹、会自动把InputStream的流归位
		} finally {
			IOUtils.closeQuietly(inStream1);
			IOUtils.closeQuietly(inStream2);
		}
	}
	public void uploadImage1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long houseId=Long.parseLong(req.getParameter("houseId"));
		Part part=req.getPart("file");//用户上传的文件，由于webupload每一个文件是一个请求，而且每个上传文件的表单名字都是file
		String fileName=part.getSubmittedFileName();//获取上传的文件名，如果是IE6可能是得到路径名
		//FileUtils.copyInputStreamToFile(part.getInputStream(),new File("d:/"+fileName));
		String fileExt=FilenameUtils.getExtension(fileName);//不带.的后缀名
		if(!fileExt.equalsIgnoreCase("jpg")&&!fileExt.equalsIgnoreCase("png")&&!fileExt.equalsIgnoreCase("jpeg")){
			AdminUtils.showError(req, resp, "上传图片格式不正确");
			return;
		}
		/*
		 * 
		 * //InputStream inStream =
		 * part.getInputStream();//每次调用.getInputStream()都会返回一个新的对象
		 * BufferedInputStream inStream = new
		 * BufferedInputStream(part.getInputStream());//用支持mark、
		 * reset的BufferedInputStream包装这个流
		 * //inStream.mark(Integer.MAX_VALUE);//把当前流的读取指针的位置做一个标记。
		 * 不是所有InputStream都支持mark、reset //inStream.reset();//把指针移动回上一次mark的位置
		 * inStream.mark(Integer.MAX_VALUE);
		 * 
		 * String uploadDir =
		 * req.getServletContext().getRealPath("/upload");//获得上传文件夹的物理路径
		 * c:/1/upload uploadDir =
		 * FilenameUtils.separatorsToUnix(uploadDir);//把路径中的\改成/ Calendar
		 * calendar = Calendar.getInstance(); String fileDir =
		 * uploadDir+"/"+calendar.get(Calendar.YEAR)+"/"
		 * +calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DAY_OF_MONTH)
		 * ;//文件的文件夹全路径 String filePath =
		 * fileDir+"/"+CommonUtils.calcMD5(inStream)+"."+fileExt;
		 * inStream.reset();//流的读取指针重新指回原始位置
		 * 
		 * new File(fileDir).mkdirs();//如果fileDir这个文件夹或者父文件夹不存在，则一路创建出所有的文件夹
		 * FileOutputStream fos = new FileOutputStream(filePath);//如果文件夹不存在，则报错
		 * 
		 * byte[] bytes=new byte[1024*124]; int len;
		 * while((len=inStream.read(bytes))>0)//part.getInputStream().read() {
		 * fos.write(bytes,0,len); } fos.close();
		 */
		// FileUtils.copyInputStreamToFile(part.getInputStream(), new
		// File(filePath));//第一个：帮助我们创建不存在的文件夹、会自动把InputStream的流归位
		String rootDir = req.getServletContext().getRealPath("/");// 网站根目录的物理路径
		rootDir = FilenameUtils.separatorsToUnix(rootDir);// 把路径中的\改成/
		Calendar calendar = Calendar.getInstance();
		String fileRelativePath;
		InputStream inStream1 = null;
		InputStream inStream2 = null;
		try {
			inStream1 = part.getInputStream();
			fileRelativePath = "upload/" + calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "/" + CommonUtils.calcMD5(inStream1) + "." + fileExt;
			String filePath = rootDir + fileRelativePath;// 文件的文件夹全路径

			inStream2 = part.getInputStream();
			FileUtils.copyInputStreamToFile(inStream2, new File(filePath));// 第一个：帮助我们创建不存在的文件夹、会自动把InputStream的流归位
		} finally {
			IOUtils.closeQuietly(inStream1);
			IOUtils.closeQuietly(inStream2);
			// 1、文件的路径用“年/月/日/”，避免一个文件夹下文件过多
			// 2、文件的文件名用文件内容的md5值，避免文件重名的问题
			// 3、上传的文件是在网站部署目录下，不是在源代码的目录下，通过getRealPath拿到物理路径
			// 4、part.getInputStream()每次调用都会返回一个新的InputStream对象
			// 5、如果计算md5值和保存文件用的是同一个InputStream，就会有计算md5值把文件流的指针指到最后，那么写入文件就写入0字节
			// 因此需要使用mark、reset来让读取指针返回原始位置。不是所有的流都支持reset，因此最好用BufferedInputStream包装一下
			// 因为part.getInputStream()每次调用都会返回一个新的InputStream对象，这样最简单的方法就是计算md5值和拷贝文件的时候用两个part.getInputStream()
			// 6、最简单的方法就是用FileUtils.copyInputStreamToFile，他会帮我们处理文件夹不存在的问题
		}
		HousePicDTO housePic=new HousePicDTO();
		housePic.setHouseId(houseId);
		housePic.setUrl("http://localhost:8080/ZuAdmin/"+fileRelativePath);
		housePic.setThumbUrl("http://localhost:8080/ZuAdmin/"+fileRelativePath);
		HouseService houseService=new HouseService();
		houseService.addnewHousePic(housePic);
	}
	public void houseAppList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long cityId=AdminUtils.getAdminUserCityId(req);
		if(cityId==null){
			AdminUtils.showError(req, resp, "总部的人不能进行房源订单处理");
			return;
		}
		String status=req.getParameter("status");
		Long currentIndex=Long.parseLong(req.getParameter("currentPageNum"));
		Integer pageSize=Integer.parseInt(req.getParameter("pageSize"));
		
		HouseAppointmentService houseAppointmentService=new HouseAppointmentService();
		Long totalCount=houseAppointmentService.getTotalCount(cityId, status);
		HouseAppointmentDTO[] houseApps= houseAppointmentService.getPagedData(cityId, status, pageSize, currentIndex);
		req.setAttribute("houseApps", houseApps);
		req.setAttribute("ctxPath",req.getContextPath());
		req.setAttribute("currentIndex", currentIndex);
		req.setAttribute("totalCount", totalCount);
		req.getRequestDispatcher("/WEB-INF/house/houseAppList.jsp").forward(req, resp);
	}
	public void followApp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long appId=Long.parseLong(req.getParameter("id"));
		Long adminUserId=AdminUtils.getAdminUserId(req);
		HouseAppointmentService houseAppointmentService=new HouseAppointmentService();
		boolean isOk=houseAppointmentService.follow(adminUserId, appId);
		if(isOk){
			writeJson(resp, new AjaxResult("ok"));
		}else{
			if(houseAppointmentService.getById(appId).getFollowAdminUserId()==adminUserId){
				writeJson(resp,  new AjaxResult("error", "你已经抢单此单了，不要重复抢单"));
			}else{
			writeJson(resp, new AjaxResult("error", "下手慢了，此单已被别人抢走！"));
		}
		}
	}
}
