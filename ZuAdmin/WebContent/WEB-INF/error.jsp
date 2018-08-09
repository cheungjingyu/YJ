<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>错误界面</title>
</head>
<body>
<h1>出错啦!</h1>
<c:out value="${errorMsg}"></c:out><br/>
	<c:if test="${errorMsg eq '未登录'}"><a target="_top" href="${pageContext.request.contextPath}/Index?action=login">登录界面</a></c:if>
	

</body>
</html>