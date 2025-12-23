<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 작성</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>게시글 작성</h1>

        <c:if test="${not empty error}">
            <div class="error"><c:out value="${error}"/></div>
        </c:if>

        <form action="${pageContext.request.contextPath}/board/post" method="post" enctype="multipart/form-data" id="postForm">
            <div class="form-group">
                <label>카테고리 *</label>
                <select name="categoryId" required>
                    <option value="">선택</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.categoryId}"><c:out value="${cat.categoryName}"/></option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>작성자 * (4-10자)</label>
                <input type="text" name="writer" minlength="4" maxlength="10" required>
            </div>

            <div class="form-group">
                <label>비밀번호 * (8-12자, 영문+숫자)</label>
                <input type="password" name="password" minlength="8" maxlength="12" required>
            </div>

            <div class="form-group">
                <label>비밀번호 확인 *</label>
                <input type="password" name="passwordConfirm" required>
            </div>

            <div class="form-group">
                <label>제목 * (4-1000자)</label>
                <input type="text" name="title" minlength="4" maxlength="1000" required>
            </div>

            <div class="form-group">
                <label>내용 * (4-4000자)</label>
                <textarea name="content" minlength="4" maxlength="4000" rows="10" required></textarea>
            </div>

            <div class="form-group">
                <label>첨부파일 (최대 3개, 각 2MB 이하, jpg/png/pdf)</label>
                <input type="file" name="files" multiple accept=".jpg,.png,.pdf">
            </div>

            <div class="actions">
                <button type="submit" class="btn">등록</button>
                <a href="${pageContext.request.contextPath}/boards?page=${page}" class="btn">취소</a>
            </div>
        </form>
    </div>

    <script src="${pageContext.request.contextPath}/resources/js/validation.js"></script>
</body>
</html>