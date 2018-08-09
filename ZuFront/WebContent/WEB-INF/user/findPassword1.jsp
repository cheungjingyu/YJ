<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>修改密码</title>
    <%@include file="/WEB-INF/header.jsp"%>
    <script type="text/javascript">
    $(function(){
    	$("#huanyizhang,#imgVerityCode").click(function(){
    		$("#imgVerityCode").attr("src","<%=ctxPath%>/User?action=verifyCode&w="+Math.random());
    	});
    	$("#huanyizhang").click();
    	$("#next").click(function(){
    		var phoneNum=$("#phoneNum").val();
    		var verifyCode=$("#verifyCode").val();
    		$.ajax({type:"post",url:"<%=ctxPath%>/User",
    		  	data:{action:"findPassword1Submit",phoneNum:phoneNum,verifyCode:verifyCode},
    		  	success:function(result){
    		  		if(result.status=="ok"){
    		  			location.href="<%=ctxPath%>/User?action=findPassword2";
    		  		}else{
    		  			alert(result.msg);
    		  		}
    		  	},
    		  	error:function(){alert("出现网络错误");}
    		});
    	});
    });
    
    </script>
 
    
</head>
<%@include file="/WEB-INF/loading.jsp"%>
	<body>
		<div class="headertwo clearfloat" id="header">
			<a href="javascript:history.go(-1)" class="fl box-s"><i class="iconfont icon-arrow-l fl"></i></a>
			<p class="fl">修改密码</p>
		</div>
		
		<div class="modify clearfloat" id="main">
			<ul>
				<li class="clearfloat">
					<input type="text" name="" id="phoneNum" value="" placeholder="手机" class="sname" />
				</li>
				<li class="clearfloat">
					<input type="text" name="" id="verifyCode" value="" placeholder="请输入右图验证码" class="syzma fl" />
					<span class="fl"><img alt="" id="imgVerityCode" src="<%=ctxPath%>/User?action=verifyCode"></span>
					<a href="javascript:void(0)" id="huanyizhang" class="fr">换一张</a>
				</li>
			</ul>
			<a href="javascript:void(0)" id="next" class="pay-btn clearfloat">
				下一步
			</a>
		</div>		
	</body>
	<script type="text/javascript" src="js/jquery.SuperSlide.2.1.js" ></script>
	<script type="text/javascript" src="slick/slick.min.js" ></script>
	<script type="text/javascript" src="js/jquery.leanModal.min.js"></script>
	<script type="text/javascript" src="js/tchuang.js" ></script>
	<script type="text/javascript" src="js/hmt.js" ></script>
</html>
