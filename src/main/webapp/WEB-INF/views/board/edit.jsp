<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.board.dto.BoardDTO" %>
<%@ page import="com.example.board.dto.FileDTO" %>
<%
    BoardDTO board = (BoardDTO) request.getAttribute("board");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 수정</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>게시글 수정</h1>

        <% if (error != null && !error.isEmpty()) { %>
            <div class="error"><%= error %></div>
        <% } %>

        <% if (board != null) { %>
        <form action="<%= request.getContextPath() %>/board/edit" method="post" enctype="multipart/form-data">
            <input type="hidden" name="boardId" value="<%= board.getBoardId() %>">

            <div class="form-group">
                <label>카테고리</label>
                <input type="text" value="<%= board.getCategoryName() %>" readonly disabled>
            </div>

            <div class="form-group">
                <label>작성자</label>
                <input type="text" value="<%= board.getUserName() %>" readonly disabled>
            </div>

            <div class="form-group">
                <label>비밀번호 *</label>
                <input type="password" name="password" required>
            </div>

            <div class="form-group">
                <label>제목 * (4-1000자)</label>
                <input type="text" name="title" value="<%= board.getTitle() %>" minlength="4" maxlength="1000" required>
            </div>

            <div class="form-group">
                <label>내용 * (4-4000자)</label>
                <textarea name="content" minlength="4" maxlength="4000" rows="10" required><%= board.getContent() %></textarea>
            </div>

            <div class="form-group">
                <label>기존 첨부파일</label>
                <ul id="existingFiles">
                    <% if (board.getFiles() != null) {
                        for (FileDTO file : board.getFiles()) { %>
                        <li>
                            <%= file.getOriginalName() %>
                            <button type="button" onclick="markForDeletion(<%= file.getFileId() %>)">삭제</button>
                        </li>
                    <% }
                    } %>
                </ul>
                <input type="hidden" name="deletedFileIdList" id="deletedFileIdList">
            </div>

            <div class="form-group">
                <label>새 첨부파일</label>
                <input type="file" name="files" multiple accept=".jpg,.png,.pdf">
            </div>

            <div class="actions">
                <button type="submit" class="btn">수정</button>
                <a href="<%= request.getContextPath() %>/board/view?boardId=<%= board.getBoardId() %>" class="btn">취소</a>
            </div>
        </form>
        <% } else { %>
            <p>게시글을 찾을 수 없습니다.</p>
        <% } %>
    </div>

    <script src="<%= request.getContextPath() %>/resources/js/board.js"></script>
</body>
</html>
