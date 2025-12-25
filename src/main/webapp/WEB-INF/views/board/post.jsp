<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.board.dto.CategoryDTO" %>
<%
    List<CategoryDTO> categories = (List<CategoryDTO>) request.getAttribute("categories");
    String error = (String) request.getAttribute("error");
    String pageNum = request.getParameter("page");
    if (pageNum == null) pageNum = "1";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 작성</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>게시글 작성</h1>

        <% if (error != null && !error.isEmpty()) { %>
            <div class="error"><%= error %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/board/post" method="post" enctype="multipart/form-data" id="postForm">
            <div class="form-group">
                <label>카테고리 *</label>
                <select name="categoryId" required>
                    <option value="">선택</option>
                    <% if (categories != null) {
                        for (CategoryDTO cat : categories) { %>
                        <option value="<%= cat.getCategoryId() %>"><%= cat.getCategoryName() %></option>
                    <% }
                    } %>
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
                <a href="<%= request.getContextPath() %>/boards?page=<%= pageNum %>" class="btn">취소</a>
            </div>
        </form>
    </div>

    <script src="<%= request.getContextPath() %>/resources/js/validation.js"></script>
</body>
</html>
