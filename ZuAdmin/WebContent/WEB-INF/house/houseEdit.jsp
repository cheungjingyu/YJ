<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="zt" uri="http://www.zsz.com/tag/core" %>
    <%@taglib prefix="zf" uri="http://www.zsz.com/functions" %>
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
					   var eqId=item.id==${house.communityId};
					   $("<option value='"+item.id+"'"+(eqId?" selected":"")+">"+item.name+"</option>").appendTo($("#communityId"));
				   }
			   },
			   error:function(){
				   layer.msg("加载小区网络通讯错误",{icon:6,time:1000});
			   }
		   });
	   });
	   $("#regionId").change();//在HTML中如果直接设定某个option选中，则页面加载的时候change不会被触发
                               //因此ajax就无法加载这个区域下的小区，所以这里主动加载	   
   })

</script>
<title>编辑房源</title>
</head>
<body>
<div class="pd-20">
  <form action="" method="post" class="form form-horizontal" id="form-house-edit">
  <input type="hidden" name="action" value="editSubmit"/>
  <input type="hidden" name="id" value="${house.id }"/>
  <input type="hidden" name="typeId" value="${house.typeId}">
    <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>区域：</label>
      <div class="formControls col-2">
       <zt:select items="${regions}" selectedValue="${house.regionId }" name="regionId" textName="name" valueName="id" id="regionId"/>
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>小区：</label>
      <div class="formControls col-2">
           <select id="communityId" name="communityId"></select>
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>房型：</label>
      <div class="formControls col-2">
        <zt:select items="${roomTypes}" name="roomTypeId" selectedValue="${house.roomTypeId}"  textName="name" valueName="id" id="roomTypeId"/>
      </div>
     </div>
   	<div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>地址：</label>
      <div class="formControls col-6">
        <input type="text" class="input-text" value="${house.address}" placeholder="几号楼，几单元，门牌号。例：3号楼8—305" id="address" name="address" datatype="*" nullmsg="地址不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>月租金：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.monthRent}" placeholder="" id="monthRent" name="monthRent" datatype="n" nullmsg="月租金不能为空">
      </div>
     </div>
     <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>状态：</label>
      <div class="formControls col-2">
        <zt:select items="${statuses}" name="statuseId" selectedValue="${house.statusId}"  textName="name" valueName="id" id="statuseId"/>
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>面积：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.area}" placeholder="" id="area" name="area" datatype="*" nullmsg="面积不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>装修：</label>
      <div class="formControls col-2">
        <zt:select items="${decorationStatuses}"  selectedValue="${house.decorateStatusId}" name="decorationStatuseId" textName="name" valueName="id" id="decorationStatuseId"/>
      </div>
     </div>
      <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>楼层：</label>
      <div class="formControls col-2">
      <input type="text" class="input-text" value="${house.totalFloorCount}" placeholder="总层数" id="totalFloorCount" name="totalFloorCount" datatype="n" nullmsg="总层数不能为空">
        <input type="text" class="input-text" value="${house.floorIndex}" placeholder="第几层" id="floorIndex" name="floorIndex" datatype="n" nullmsg="第几层不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>朝向：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.direction}" placeholder="" id="direction" name="direction" datatype="*" nullmsg="朝向不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>可看房时间：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.lookableDateTime}" onfocus="WdatePicker({minDate:'${now}'})" placeholder="" id="lookableDateTime" name="lookableDateTime"  nullmsg="可看房时间不能为空">
      </div>
     </div>
     <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>入住时间：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.checkInDateTime}" onfocus="WdatePicker({minDate:'${now}'})" placeholder="" id="checkInDateTime" name="checkInDateTime"  nullmsg="入住时间不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>业主姓名：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.ownerName}" placeholder="" id="ownerName" name="ownerName" datatype="*" nullmsg="业主姓名不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>业主电话：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.ownerPhoneNum}" placeholder="" id="ownerPhoneNum" name="ownerPhoneNum" datatype="m" nullmsg="请输入正确的手机号格式">
      </div>
     </div>
     <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>房源描述：</label>
      <div class="formControls col-10">
        <textarea class="input-text" id="description"  name="description" style="height: 80px;" ><c:out value="${house.description}"></c:out></textarea>
      </div>
     </div>
     <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>配套设施：</label>
      <div class="formControls col-10">
      <c:forEach items="${attachments}" var="att">
       <zt:checkbox label="${att.name}" checked="${zf:contains(houseAttachmentIds,att.id) }" name="attachmentId" value="${att.id}"  id="attId-${att.id}"/>
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
	var validForm=$("#form-house-edit").Validform({tiptype:2});
	$("#btnSave").click(function(){
		if(!validForm.check(false)){
			return;
		}
		var data=$("#form-house-edit").serializeArray();
		$.ajax({
			url:"<%=ctxPath%>/House",type:"post",
			data:data,
			success:function(result){
				if(result.status=="ok"){
				
					parent.location.reload();
				}else{
					alert("保存失败"+result.msg);
				}
			},
			error:function(){alert("ajax保存失败");}
		});
	})
	
});
</script>
</body>
</html>