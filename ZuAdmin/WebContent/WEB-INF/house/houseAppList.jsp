<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="zc" uri="http://www.zsz.com/tag/core"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/header.jsp" %>
<title>房源订单管理</title>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 房源中心 <span class="c-gray en">&gt;</span> 房源管理<a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">
	
	
	<div class="mt-20">
	<table class="table table-border table-bordered table-hover table-bg table-sort">
		<thead>
			<tr class="text-c">
				<th width="25"><input type="checkbox" name="" value=""></th>
				<th width="80">ID</th>
				<th width="100">姓名</th>
				<th width="100">手机号</th>
				<th width="100">看房时间</th>
				<th width="100">预约时间</th>
				<th width="100">跟进人</th>
				<th width="100">跟进时间</th>
				<th width="100">所属区域</th>
				<th width="100">小区名</th>
				<th width="100">抢单</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${houseApps}" var="houseApp">
			<tr class="text-c">
				<td><input type="checkbox" value="1" name=""></td>
				<td><c:out value="${houseApp.id }"/></td>
				<td><c:out value="${houseApp.name }"/></td>
				<td><c:out value="${houseApp.phoneNum}"/></td>
				<td><c:out value="${houseApp.visitDate }"/></td>
				<td><c:out value="${houseApp.createDateTime }"/></td>
				<td><c:out value="${houseApp.followAdminUserName }"/></td>
				<td><c:out value="${houseApp.followDateTime}"/></td>
				<td><c:out value="${houseApp.regionName }"/></td>
				<td><c:out value="${houseApp.communityName }"/></td>
				
				
				 
				
				 <td><a title="抢单" href="javascript:void(0);" onclick="house_follow(this,'${houseApp.id}')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	
	</div>
	<zc:pager urlFormat="${ctxPath}/House?action=houseAppList&pageIndex={pageNum}" pageSize="10" totalCount="${totalCount}" currentPageNum="${currentIndex}"/>
</div>

<script type="text/javascript">
$(function(){
	$('.table-sort').dataTable({
		"aaSorting": [[ 1, "desc" ]],//默认第几个排序
		"bStateSave": true,//状态保存
		"bFilter":false,
		"bLengthChange":false,
		"bPaginate":false
	});

});


/*抢单*/
function house_follow(link,id){
	layer.confirm('确认要抢单吗？',function(index){
		$.ajax({
			url:"<%=ctxPath%>/House",type:"post",
			data:{action:"followApp",id:id},
			success:function(obj){
				if(obj.status=="ok"){
					$(link).parents("tr").remove();
					layer.msg('已删除!',{icon:1,time:1000});
				}else{
					layer.msg(obj.msg,{icon:6,time:1000});
				}
				
			},
			error:function(){alert("ajax请求失败");//location.reload();
			}
			
		});
		
		
	});
		
}

</script> 
</body>
</html>