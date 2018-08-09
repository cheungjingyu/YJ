<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="zf" uri="http://www.zsz.com/functions" %>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/header.jsp" %>
<title>编辑权限</title>
</head>
<body>
<div class="pd-20">
  <form action="" method="post" class="form form-horizontal" id="form-permission-edit">
  <input type="hidden" name="action" value="editSubmit"/>
   <input type="hidden" name="id" value="${permission.id }"/>
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>权限名：</label>
      <div class="formControls col-5">
        <input type="text" class="input-text" value="${permission.description}" placeholder="" id="description" name="description" datatype="*2-16" nullmsg="权限名不能为空">
      </div>
       <div class="col-4"> </div>
    </div>
     <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>权限编号：</label>
      <div class="formControls col-5">
        <input type="text" class="input-text" value="${permission.name}" placeholder="" id="name" name="name" datatype="*2-16" nullmsg="权限编号不能为空">
      </div>
       <div class="col-4"> </div>
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
	var validForm=$("#form-permission-edit").Validform({tiptype:2});
	$("#btnSave").click(function(){
		if(!validForm.check(false)){
			return;
		}
		var data=$("#form-permission-edit").serializeArray();
		$.ajax({
			url:"<%=ctxPath%>/Permission",type:"post",
			data:data,
			success:function(result){
				if(result.status=="ok"){
					parent.location.reload();
				}else{
					alert("编辑失败"+result.msg);
				}
			},
			error:function(){alert("ajax编辑失败");}
		});
	})
	
});
</script>
</body>
</html>