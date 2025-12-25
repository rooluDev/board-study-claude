<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.board.dto.BoardDTO" %>
<%@ page import="com.example.board.dto.FileDTO" %>
<%@ page import="com.example.board.dto.CommentDTO" %>
<%
    BoardDTO board = (BoardDTO) request.getAttribute("board");
    String pageNum = request.getParameter("page");
    String category = request.getParameter("category");
    String from = request.getParameter("from");
    String to = request.getParameter("to");
    String keyword = request.getParameter("keyword");

    if (pageNum == null) pageNum = "1";
    if (category == null) category = "";
    if (from == null) from = "";
    if (to == null) to = "";
    if (keyword == null) keyword = "";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= board != null ? board.getTitle() : "게시글 보기" %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/resources/css/style.css">
</head>
<body>
    <div class="container">
        <% if (board != null) { %>
        <h1><%= board.getTitle() %></h1>

        <div class="board-info">
            <span>작성자: <%= board.getUserName() %></span>
            <span>조회수: <%= board.getViews() %></span>
            <span>등록일: <%= board.getFormattedCreatedAt() %></span>
            <% if (board.getEditedAt() != null) { %>
                <span>수정일: <%= board.getFormattedEditedAt() %></span>
            <% } %>
        </div>

        <div class="board-content">
            <%= board.getContent() %>
        </div>

        <!-- 첨부파일 -->
        <% if (board.getFiles() != null && !board.getFiles().isEmpty()) { %>
            <div class="attachments">
                <h3>첨부파일</h3>
                <ul>
                    <% for (FileDTO file : board.getFiles()) { %>
                        <li>
                            <a href="<%= request.getContextPath() %>/download?fileId=<%= file.getFileId() %>">
                                <%= file.getOriginalName() %> (<%= file.getSize() %> bytes)
                            </a>
                        </li>
                    <% } %>
                </ul>
            </div>
        <% } %>

        <!-- 댓글 -->
        <div class="comments">
            <h3>댓글 (<%= board.getComments() != null ? board.getComments().size() : 0 %>)</h3>
            <ul>
                <% if (board.getComments() != null) {
                    for (CommentDTO comment : board.getComments()) { %>
                    <li>
                        <div class="comment-date">
                            <%= comment.getFormattedCreatedAt() %>
                        </div>
                        <div class="comment-content">
                            <%= comment.getComment() %>
                        </div>
                    </li>
                <% }
                } %>
            </ul>

            <!-- 댓글 등록 폼 -->
            <form id="commentForm">
                <input type="hidden" name="boardId" value="<%= board.getBoardId() %>">
                <textarea name="content" placeholder="댓글을 입력하세요 (1-300자)" maxlength="300" required></textarea>
                <button type="submit">댓글 등록</button>
            </form>
        </div>

        <!-- 버튼 -->
        <div class="actions">
            <a href="<%= request.getContextPath() %>/boards?page=<%= pageNum %>&category=<%= category %>&from=<%= from %>&to=<%= to %>&keyword=<%= keyword %>" class="btn">목록</a>
            <button onclick="showEditModal()" class="btn">수정</button>
            <button onclick="showDeleteModal()" class="btn btn-danger">삭제</button>
        </div>
        <% } else { %>
            <p>게시글을 찾을 수 없습니다.</p>
        <% } %>
    </div>

    <!-- 비밀번호 모달 (수정) -->
    <div id="editModal" class="modal" style="display:none;">
        <div class="modal-content">
            <h3>비밀번호 확인</h3>
            <input type="password" id="editPassword" placeholder="비밀번호">
            <button onclick="confirmEdit()">확인</button>
            <button onclick="closeModal('editModal')">취소</button>
        </div>
    </div>

    <!-- 비밀번호 모달 (삭제) -->
    <div id="deleteModal" class="modal" style="display:none;">
        <div class="modal-content">
            <h3>비밀번호 확인</h3>
            <input type="password" id="deletePassword" placeholder="비밀번호">
            <button onclick="confirmDelete()">확인</button>
            <button onclick="closeModal('deleteModal')">취소</button>
        </div>
    </div>

    <script>
        const contextPath = '<%= request.getContextPath() %>';
        const boardId = <%= board != null ? board.getBoardId() : 0 %>;
    </script>
    <script src="<%= request.getContextPath() %>/resources/js/board.js"></script>
    <script src="<%= request.getContextPath() %>/resources/js/validation.js"></script>
</body>
</html>
