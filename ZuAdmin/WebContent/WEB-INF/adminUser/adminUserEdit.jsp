<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="zf" uri="http://www.zsz.com/functions" %>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/header.jsp" %>
<title>编辑管理员</title>
</head>
<body>
<div class="pd-20">
  <form action="" method="post" class="form form-horizontal" id="form-adminUser-edit">
  <input type="hidden" name="action" value="editSubmit"/>
   <input type="hidden" name="id" value="${adminUser.id}"/>
   <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>手机号：</label>
      <div class="formControls col-5">
        <c:out value="${adminUser.phoneNum }"></c:out>
      </div>
       <div class="col-4"> </div>
          </div>
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>姓名：</label>
      <div class="formControls col-5">
        <input type="text" class="input-text" value="${adminUser.name }" placeholder="" id="name" name="name" datatype="*" nullmsg="姓名不能为空">
      </div>
       <div class="col-4"> </div>
          </div>
      
       
         
        <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>初始密码：</label>
      <div class="formControls col-5">
        <input type="password" class="input-text" value="" placeholder="" id="password" name="password" >
      </div>
       <div class="col-4"> </div>
          </div>
         
        <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>确认密码：</label>
      <div class="formControls col-5">
        <input type="password" class="input-text" value="" placeholder="" recheck="password" id="password2" name="password2" >
      </div>
       <div class="col-4"> </div>
          </div>
         
        <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>邮箱：</label>
      <div class="formControls col-5">
        <input type="text" class="input-text" value="${adminUser.email }" placeholder="" id="email" name="email" datatype="e" nullmsg="请输入正确的邮箱格式">
      </div>
       <div class="col-4"> </div>
          </div>
         
        <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>所在城市：</label>
      
      <div class="formControls col-5">
   		<select id="cityId" name="cityId" datatype="*">
   		 <c:if test="${empty adminUser.cityId }">
           	<option selected="selected">总部</option>
           </c:if>
			<c:if test="${ not empty adminUser.cityId }">
           	<option>总部</option>
           </c:if>

      <c:forEach items="${cities}"  var="city">
           <c:if test="${city.id eq adminUser.cityId }">
           	<option value="${city.id}" selected="selected">${city.name}</option>
           </c:if>
      	<c:if test="${city.id  ne adminUser.cityId }">
           	<option value="${city.id}" >${city.name}</option>
           </c:if>
     
      </c:forEach>
      </select>
       
      </div>
      
       
       
    </div>
    <div class="row cl">
        
      <label class="form-label col-3"><span class="c-red">*</span>角色：</label>
      </div>
       <div class="row cl">
      <c:forEach items="${roles}" var="role">
        
      <div class="col-3">
      <c:if test="${zf:contains(roleIds,role.id) }">
      <input type="checkbox"  checked="checked" id="roleId${role.id}" name="roleId" value="${role.id}"/>
      <label for="roleId${role.id}"><c:out value="${role.name}"></c:out></label> 
      </c:if>
      <c:if test="${not zf:contains(roleIds,role.id) }">
      <input type="checkbox"   id="roleId${role.id}" name="roleId" value="${role.id}"/>
      <label for="roleId${role.id}"><c:out value="${role.name}"></c:out></label> 
      </c:if>
      
      </div>
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
	var validForm=$("#form-adminUser-edit").Validform({tiptype:2});
	$("#btnSave").click(function(){
		if(!validForm.check(false)){
			return;
		}
		var data=$("#form-adminUser-edit").serializeArray();
		$.ajax({
			url:"<%=ctxPath%>/AdminUser",type:"post",
			data:data,
			success:function(result){
				if(result.status=="ok"){
					parent.location.reload();
				}else{
					alert("编辑失败");
				}
			},
			error:function(){alert("ajax编辑失败");}
		});
	})
	
});
</script>
</body>
</html>