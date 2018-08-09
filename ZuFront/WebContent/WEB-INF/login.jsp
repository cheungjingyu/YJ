<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@include file="/WEB-INF/header.jsp"%>
    <title>登录</title>
    <script type="text/javascript">
     $(function(){
    	 $("#loginSubmit").click(function(){
    		 var phoneNum=$("#phoneNum").val();
    		 var password=$("#password").val();
    		 $.ajax({type:"post",url:"<%=ctxPath%>/User",
    			 data:{action:"loginSubmit",phoneNum:phoneNum,password:password},
    			 success:function(result){
    				 if(result.status=="ok"){
    					 location.href="<%=ctxPath%>/Index?action=index";
    				 }else{
    					 alert(result.msg);
    					 location.reload();
    				 }
    			 },
    			 error:function(){alert("登录出现网络错误");}
    			 
    		 });
    	 });
     });
    </script>
</head>
    <%@include file="/WEB-INF/loading.jsp" %>
	<body>
		<div class="headertwo clearfloat" id="header">
			<a href="javascript:history.go(-1)" class="fl box-s"><i class="iconfont icon-arrow-l fl"></i></a>
			<p class="fl">登录</p>
		</div>
		
		<div class="sign clearfloat" id="main">
			<ul>
				<li class="clearfloat">
					<i class="iconfont icon-phone fl"></i>
					<input type="text" id="phoneNum" value="" placeholder="请输入手机号" class="fl phone" />
				</li>
				<li class="clearfloat">
					<i class="iconfont icon-lock fl"></i>
					<input type="password" id="password" value="" placeholder="请输入密码" class="fl phone" />
				</li>
			</ul>
			<a href="javascript:void(0)" id="loginSubmit" class="pay-btn clearfloat">
				登录
			</a>
			<div class="bottom clearfloat">
				<p class="fl">
					还不是会员？
					<a href="<%=ctxPath%>/User?action=register">立即注册</a>
				</p>
				<a href="<%=ctxPath%>/User?action=findPassword1" class="fr">忘记密码？</a>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="js/jquery.SuperSlide.2.1.js" ></script>
	<script type="text/javascript" src="js/hmt.js" ></script>
</html>
