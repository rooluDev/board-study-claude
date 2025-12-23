<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>404 - 페이지를 찾을 수 없습니다</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container error-page">
        <h1>404</h1>
        <p>요청하신 페이지를 찾을 수 없습니다.</p>
        <a href="${pageContext.request.contextPath}/boards" class="btn">게시판으로 이동</a>
    </div>
</body>
</html>