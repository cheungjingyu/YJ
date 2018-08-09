<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="zt" uri="http://www.zsz.com/tag/core" %>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/header.jsp" %>
<script type="text/javascript">
   $(function(){
	   $("#regionId").change(function(){
		   var regionId=$(this).val();
		   $.ajax({type:"post",url:"<%=ctxPath%>/House",
			   data:{action:"loadCommunities",regionId:regionId},
			   success:function(result){
				   if(result.status!="ok"){
					   layer.msg("加载小区失败"+result.msg,{icon:6,time:1000});
					   return;
				   }
				   $("#communityId").empty();//清空之前生成的元素
				   for(var i=0;i<result.data.length;i++){
					   var item=result.data[i];
					   $("<option value='"+item.id+"'>"+item.name+"</option>").appendTo($("#communityId"));
				   }
			   },
			   error:function(){
				   layer.msg("加载小区网络通讯错误",{icon:6,time:1000});
			   }
		   });
	   });
   })

</script>
<title>添加房源</title>
</head>
<body>
<div class="pd-20">
  <form action="" method="post" class="form form-horizontal" id="form-house-add">
  <input type="hidden" name="action" value="addSubmit"/>
  <input type="hidden" name="typeId" value="${typeId}">
    <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>区域：</label>
      <div class="formControls col-2">
       <zt:select items="${regions}" name="regionId" textName="name" valueName="id" id="regionId"/>
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>小区：</label>
      <div class="formControls col-2">
           <select id="communityId" name="communityId"></select>
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>房型：</label>
      <div class="formControls col-2">
        <zt:select items="${roomTypes}" name="roomTypeId" textName="name" valueName="id" id="roomTypeId"/>
      </div>
     </div>
   	<div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>地址：</label>
      <div class="formControls col-6">
        <input type="text" class="input-text" value="" placeholder="几号楼，几单元，门牌号。例：3号楼8—305" id="address" name="address" datatype="*" nullmsg="地址不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>月租金：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="" placeholder="" id="monthRent" name="monthRent" datatype="n" nullmsg="月租金不能为空">
      </div>
     </div>
     <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>状态：</label>
      <div class="formControls col-2">
        <zt:select items="${statuses}" name="statuseId" textName="name" valueName="id" id="statuseId"/>
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>面积：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="" placeholder="" id="area" name="area" datatype="n" nullmsg="面积不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>装修：</label>
      <div class="formControls col-2">
        <zt:select items="${decorationStatuses}" name="decorationStatuseId" textName="name" valueName="id" id="decorationStatuseId"/>
      </div>
     </div>
      <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>楼层：</label>
      <div class="formControls col-2">
      <input type="text" class="input-text" value="" placeholder="总层数" id="totalFloorCount" name="totalFloorCount" datatype="n" nullmsg="总层数不能为空">
        <input type="text" class="input-text" value="" placeholder="第几层" id="floorIndex" name="floorIndex" datatype="n" nullmsg="第几层不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>朝向：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="" placeholder="" id="direction" name="direction" datatype="*" nullmsg="朝向不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>可看房时间：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" onfocus="WdatePicker({minDate:'${now}'})" placeholder="" id="lookableDateTime" name="lookableDateTime"  nullmsg="可看房时间不能为空">
      </div>
     </div>
     <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>入住时间：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" onfocus="WdatePicker({minDate:'${now}'})" placeholder="" id="checkInDateTime" name="checkInDateTime"  nullmsg="入住时间不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>业主姓名：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="" placeholder="" id="ownerName" name="ownerName" datatype="*" nullmsg="业主姓名不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>业主电话：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="" placeholder="" id="ownerPhoneNum" name="ownerPhoneNum" datatype="m" nullmsg="请输入正确的手机号格式">
      </div>
     </div>
     <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>房源描述：</label>
      <div class="formControls col-10">
        <textarea class="input-text" id="description" name="description" style="height: 80px;" ></textarea>
      </div>
     </div>
     <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>配套设施：</label>
      <div class="formControls col-10">
      <c:forEach items="${attachments}" var="att">
       <zt:checkbox label="${att.name}" name="attachmentId" value="${att.id }" id="attId-${att.id}"/>
      </c:forEach>
       
      </div>
     </div>
    <div class="row cl">
      <div class="col-9 col-offset-3">
        <input class="btn btn-primary radius" id="btnSave" type="button" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
      </div>
    </div>
  </form>
</div>


<script type="text/javascript">
$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	var validForm=$("#form-house-add").Validform({tiptype:2});
	$("#btnSave").click(function(){
		if(!validForm.check(false)){
			return;
		}
		var data=$("#form-house-add").serializeArray();
		$.ajax({
			url:"<%=ctxPath%>/House",type:"post",
			data:data,
			success:function(result){
				if(result.status=="ok"){
				
					parent.location.reload();
				}else{
					alert("添加失败"+result.msg);
				}
			},
			error:function(){alert("ajax添加失败");}
		});
	})
	
});
</script>
</body>
</html>