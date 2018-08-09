<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/header.jsp" %>
<title>添加角色</title>
</head>
<body>
<div class="pd-20">
  <form action="" method="post" class="form form-horizontal" id="form-role-add">
  <input type="hidden" name="action" value="addSubmit"/>
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>角色名：</label>
      <div class="formControls col-5">
        <input type="text" class="input-text" value="" placeholder="" id="rolename" name="rolename" datatype="*2-16" nullmsg="角色名不能为空">
      </div>
       <div class="col-4"> </div>
    </div>
    <div class="row cl">
        
      <label class="form-label col-3"><span class="c-red">*</span>权限：</label>
      </div>
       <div class="row cl">
      <c:forEach items="${perms}" var="perm">
        
      <div class="col-2">
      
      <input type="checkbox" id="permId${perm.id}" name="permId" value="${perm.id}"/><label for="permId${perm.id}"><c:out value="${perm.description}"></c:out></label> </div>
      </c:forEach>
      
   
     
    
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
	var validForm=$("#form-role-add").Validform({tiptype:2});
	$("#btnSave").click(function(){
		if(!validForm.check(false)){
			return;
		}
		var data=$("#form-role-add").serializeArray();
		$.ajax({
			url:"<%=ctxPath%>/Role",type:"post",
			data:data,
			success:function(result){
				if(result.status=="ok"){
					//alert("添加成功");
					parent.location.reload();
				}else{
					alert("添加失败");
				}
			},
			error:function(){alert("ajax添加失败");}
		});
	})
	/* $("#form-member-add").Validform({
		tiptype:2,
		callback:function(form){
			form[0].submit();
			var index = parent.layer.getFrameIndex(window.name);
			parent.$('.btn-refresh').click();
			parent.layer.close(index);
		}
	}); */
});
</script>
</body>
</html>