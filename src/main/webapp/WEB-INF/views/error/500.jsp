<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>500 - 서버 오류</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container error-page">
        <h1>500</h1>
        <p>서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.</p>
        <a href="${pageContext.request.contextPath}/boards" class="btn">게시판으로 이동</a>
    </div>
</body>
</html>