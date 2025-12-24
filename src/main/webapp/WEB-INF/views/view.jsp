<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.board.dto.Board" %>
<%@ page import="com.board.dto.File" %>
<%@ page import="com.board.dto.Comment" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>게시판 - 상세보기</title>
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: 'Malgun Gothic', sans-serif;
      background-color: #f5f5f5;
      padding: 20px;
    }

    .container {
      max-width: 900px;
      margin: 0 auto;
      background-color: white;
      padding: 30px;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    h1 {
      font-size: 24px;
      margin-bottom: 20px;
      color: #333;
      border-bottom: 2px solid #007bff;
      padding-bottom: 10px;
    }

    .board-info {
      background-color: #f8f9fa;
      padding: 15px;
      border-radius: 4px;
      margin-bottom: 20px;
    }

    .board-info-row {
      display: flex;
      margin-bottom: 8px;
      font-size: 14px;
    }

    .board-info-row:last-child {
      margin-bottom: 0;
    }

    .board-info-label {
      font-weight: bold;
      width: 100px;
      color: #495057;
    }

    .board-info-value {
      color: #212529;
    }

    .board-content {
      padding: 20px 0;
      line-height: 1.8;
      min-height: 200px;
      color: #212529;
      border-bottom: 1px solid #dee2e6;
      margin-bottom: 20px;
      white-space: pre-wrap;
    }

    .attachments {
      margin-bottom: 30px;
    }

    .attachments h3 {
      font-size: 16px;
      margin-bottom: 10px;
      color: #495057;
    }

    .file-list {
      list-style: none;
    }

    .file-item {
      padding: 8px 12px;
      background-color: #f8f9fa;
      margin-bottom: 5px;
      border-radius: 4px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .file-item a {
      color: #007bff;
      text-decoration: none;
    }

    .file-item a:hover {
      text-decoration: underline;
    }

    .file-size {
      color: #6c757d;
      font-size: 12px;
    }

    .comments {
      margin-bottom: 30px;
    }

    .comments h3 {
      font-size: 16px;
      margin-bottom: 15px;
      color: #495057;
    }

    .comment-item {
      padding: 12px;
      background-color: #f8f9fa;
      margin-bottom: 10px;
      border-radius: 4px;
      border-left: 3px solid #007bff;
    }

    .comment-date {
      font-size: 12px;
      color: #6c757d;
      margin-bottom: 8px;
    }

    .comment-content {
      color: #212529;
      line-height: 1.6;
    }

    .comment-form {
      margin-bottom: 30px;
    }

    .comment-form textarea {
      width: 100%;
      padding: 10px;
      border: 1px solid #ced4da;
      border-radius: 4px;
      resize: vertical;
      font-family: inherit;
      font-size: 14px;
    }

    .comment-form button {
      margin-top: 10px;
      padding: 8px 16px;
      background-color: #28a745;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }

    .comment-form button:hover {
      background-color: #218838;
    }

    .buttons {
      text-align: center;
      padding-top: 20px;
      border-top: 1px solid #dee2e6;
    }

    .btn {
      display: inline-block;
      padding: 10px 20px;
      margin: 0 5px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      text-decoration: none;
      font-size: 14px;
    }

    .btn-primary {
      background-color: #007bff;
      color: white;
    }

    .btn-primary:hover {
      background-color: #0056b3;
    }

    .btn-warning {
      background-color: #ffc107;
      color: #212529;
    }

    .btn-warning:hover {
      background-color: #e0a800;
    }

    .btn-danger {
      background-color: #dc3545;
      color: white;
    }

    .btn-danger:hover {
      background-color: #c82333;
    }

    .no-data {
      text-align: center;
      padding: 20px;
      color: #6c757d;
      font-size: 14px;
    }
  </style>
</head>
<body>
  <%
    Board board = (Board) request.getAttribute("board");
    @SuppressWarnings("unchecked")
    List<File> files = (List<File>) request.getAttribute("files");
    @SuppressWarnings("unchecked")
    List<Comment> comments = (List<Comment>) request.getAttribute("comments");

    String page = (String) request.getAttribute("page");
    String category = (String) request.getAttribute("category");
    String from = (String) request.getAttribute("from");
    String to = (String) request.getAttribute("to");
    String keyword = (String) request.getAttribute("keyword");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 검색 조건 쿼리 문자열 생성
    StringBuilder queryString = new StringBuilder();
    if (page != null && !page.isEmpty()) {
      queryString.append("&page=").append(page);
    }
    if (category != null && !category.isEmpty()) {
      queryString.append("&category=").append(category);
    }
    if (from != null && !from.isEmpty()) {
      queryString.append("&from=").append(from);
    }
    if (to != null && !to.isEmpty()) {
      queryString.append("&to=").append(to);
    }
    if (keyword != null && !keyword.isEmpty()) {
      queryString.append("&keyword=").append(keyword);
    }
  %>

  <div class="container">
    <h1><%= board.getTitle() %></h1>

    <div class="board-info">
      <div class="board-info-row">
        <span class="board-info-label">카테고리:</span>
        <span class="board-info-value"><%= board.getCategoryName() %></span>
      </div>
      <div class="board-info-row">
        <span class="board-info-label">작성자:</span>
        <span class="board-info-value"><%= board.getUserName() %></span>
      </div>
      <div class="board-info-row">
        <span class="board-info-label">등록일시:</span>
        <span class="board-info-value"><%= dateFormat.format(board.getCreatedAt()) %></span>
      </div>
      <%
        if (board.getEditedAt() != null) {
      %>
      <div class="board-info-row">
        <span class="board-info-label">수정일시:</span>
        <span class="board-info-value"><%= dateFormat.format(board.getEditedAt()) %></span>
      </div>
      <%
        }
      %>
      <div class="board-info-row">
        <span class="board-info-label">조회수:</span>
        <span class="board-info-value"><%= board.getViews() %></span>
      </div>
    </div>

    <div class="board-content">
      <%= board.getContent() %>
    </div>

    <!-- 첨부파일 -->
    <div class="attachments">
      <h3>첨부파일 (<%= files.size() %>)</h3>
      <%
        if (files.isEmpty()) {
      %>
      <p class="no-data">첨부파일이 없습니다.</p>
      <%
        } else {
      %>
      <ul class="file-list">
        <%
          for (File file : files) {
        %>
        <li class="file-item">
          <a href="${pageContext.request.contextPath}/download?fileId=<%= file.getFileId() %>">
            📎 <%= file.getOriginalName() %>
          </a>
          <span class="file-size">(<%= formatFileSize(file.getSize()) %>)</span>
        </li>
        <%
          }
        %>
      </ul>
      <%
        }
      %>
    </div>

    <!-- 댓글 목록 -->
    <div class="comments">
      <h3>댓글 (<%= comments.size() %>)</h3>
      <%
        if (comments.isEmpty()) {
      %>
      <p class="no-data">댓글이 없습니다.</p>
      <%
        } else {
          for (Comment comment : comments) {
      %>
      <div class="comment-item">
        <div class="comment-date"><%= dateFormat.format(comment.getCreatedAt()) %></div>
        <div class="comment-content"><%= comment.getComment() %></div>
      </div>
      <%
          }
        }
      %>
    </div>

    <!-- 댓글 등록 폼 -->
    <div class="comment-form">
      <h3>댓글 작성</h3>
      <form action="${pageContext.request.contextPath}/comment" method="post">
        <input type="hidden" name="boardId" value="<%= board.getBoardId() %>">
        <textarea name="content" rows="4" placeholder="댓글을 입력하세요 (1~300자)" maxlength="300" required></textarea>
        <button type="submit">댓글 등록</button>
      </form>
    </div>

    <!-- 버튼 -->
    <div class="buttons">
      <a href="${pageContext.request.contextPath}/boards?dummy=1<%= queryString %>" class="btn btn-primary">목록</a>
      <a href="${pageContext.request.contextPath}/board/edit?boardId=<%= board.getBoardId() %><%= queryString %>" class="btn btn-warning">수정</a>
      <button type="button" class="btn btn-danger" onclick="deleteBoard()">삭제</button>
    </div>
  </div>

  <%!
    // 파일 크기 포맷팅 메서드
    private String formatFileSize(long size) {
      if (size < 1024) {
        return size + " B";
      } else if (size < 1024 * 1024) {
        return String.format("%.2f KB", size / 1024.0);
      } else {
        return String.format("%.2f MB", size / (1024.0 * 1024.0));
      }
    }
  %>

  <script>
    function deleteBoard() {
      if (confirm('정말 삭제하시겠습니까?')) {
        // TODO: 비밀번호 확인 모달 표시 후 삭제 처리
        alert('삭제 기능은 추후 구현 예정입니다.');
      }
    }
  </script>
</body>
</html>
