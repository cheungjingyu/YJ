<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="zf" uri="http://www.zsz.com/functions" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="zt" uri="http://www.zsz.com/tag/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    
    <title>整租</title>
    <%@include file="/WEB-INF/header.jsp" %>
    <script type="text/javascript" src="js/menu.js" ></script>
	
</head>
<%@include file="/WEB-INF/loading.jsp" %>
	<body>
		<div class="headertwo clearfloat" id="header">
			<a href="javascript:history.go(-1)" class="fl box-s"><i class="iconfont icon-arrow-l fl"></i></a>
			<p class="fl"><span>${searchDisplay}</span></p>
			<a href="javascript:history.go(-1)" class="fr"><i class="iconfont icon-sousuo fl"></i></a>
		</div>		
		<div class="clearfloat" id="main">
			<div class="menu-list clearfloat am-sharetwo">
				<ul class="yiji" id="oe_menu">
					<li>
						<a href="#" class="inactive">区域<i></i></a>
						<ul style="display: none">
						<c:forEach items="${regions}" var="region">
							<li><a href='<%=ctxPath%>/House?${zf:addQueryStringPart(queryString,"regionId",region.id)}'>${region.name}</a></li> 
							</c:forEach>
						</ul>
					</li>
					<li>
						<a href="#" class="inactive">租金<i></i></a>
						<ul style="display: none">
							<li><a href='<%=ctxPath%>/House?${zf:addQueryStringPart(queryString,"monthRent","*-500")}'>500元以下</a></li> 
							<li><a href='<%=ctxPath%>/House?${zf:addQueryStringPart(queryString,"monthRent","500-800")}'>500-800元</a></li> 
							<li><a href='<%=ctxPath%>/House?${zf:addQueryStringPart(queryString,"monthRent","800-1000")}'>800-1000元</a></li> 
							<li><a href='<%=ctxPath%>/House?${zf:addQueryStringPart(queryString,"monthRent","1000-1500")}'>1000-1500元</a></li>
							<li><a href='<%=ctxPath%>/House?${zf:addQueryStringPart(queryString,"monthRent","1500-2000")}'>1500-2000元</a></li>
							<li><a href='<%=ctxPath%>/House?${zf:addQueryStringPart(queryString,"monthRent","2000-*")}'>2000元以上</a></li> 
						</ul>
					</li>
					<li>
						<a href="#" class="inactive">排序<i></i></a>
						<ul style="display: none">
							<li><a href='<%=ctxPath%>/House?${zf:addQueryStringPart(queryString,"orderBy","monthRent")}'>价格</a></li> 
							<li><a href='<%=ctxPath%>/House?${zf:addQueryStringPart(queryString,"orderBy","area")}'>面积</a></li> 
						</ul>
					</li>
				</ul>
			</div>
			
			<!-- <div id="oe_overlay" class="oe_overlay"></div> -->
			
			<div class="recom clearfloat recomtwo">
		    	<div class="content clearfloat box-s">
		    	
		    	
		    	<c:forEach items="${houses}" var="house">
		    		<div class="list clearfloat fl box-s">
		    		<!-- <a href="<%=ctxPath%>/House?action=view&houseId=${house.id}"> -->
		    			<!-- 静态页面访问房源 -->
		    			<a href="<%=ctxPath%>/house/${house.id}.html">
			    			<div class="tu clearfloat">
			    				<span></span>
			    				<img src="upload/list-tu.jpg"/>
			    			</div>
			    			<div class="right clearfloat">
			    				<div class="tit clearfloat">
			    					<p class="fl">${house.communityName}</p>
			    					<span class="fr">${house.monthRent }<samp>元/月</samp></span>
			    				</div>
			    				<p class="recom-jianjie">${house.roomTypeName }   |  ${house.area }m²  |  ${house.decorateStatusName}</p>
			    				<div class="recom-bottom clearfloat">
			    					<span><i class="iconfont icon-duihao"></i>随时住</span>
			    					<span><i class="iconfont icon-duihao"></i>家电齐全</span>
			    				</div>
			    			</div>
		    			</a>
		    		</div>
		    		</c:forEach>
		    	</div>
		    </div>
	    </div>
	    <zt:pager urlFormat="${pageUrlFormat }" pageSize="2" totalCount="${totalCount }" currentPageNum="${pageIndex }"/>
	    
	</body>
	<script type="text/javascript" src="js/jquery-1.8.3.min.js" ></script>
	<script type="text/javascript" src="js/psong.js" ></script>
	<script type="text/javascript" src="js/hmt.js" ></script>
</html>
