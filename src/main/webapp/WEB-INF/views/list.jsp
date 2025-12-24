<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.board.dto.Board" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>게시판 - 목록</title>
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
      max-width: 1200px;
      margin: 0 auto;
      background-color: white;
      padding: 30px;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    h1 {
      font-size: 28px;
      margin-bottom: 10px;
      color: #333;
    }

    .info {
      margin-bottom: 20px;
      color: #666;
      font-size: 14px;
    }

    .write-button {
      float: right;
      margin-bottom: 15px;
      padding: 10px 20px;
      background-color: #007bff;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      text-decoration: none;
      display: inline-block;
    }

    .write-button:hover {
      background-color: #0056b3;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-bottom: 20px;
      clear: both;
    }

    thead {
      background-color: #f8f9fa;
    }

    th, td {
      padding: 12px;
      text-align: center;
      border-bottom: 1px solid #dee2e6;
    }

    th {
      font-weight: bold;
      color: #495057;
    }

    td {
      color: #212529;
    }

    .title-cell {
      text-align: left;
      max-width: 400px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .title-cell a {
      color: #007bff;
      text-decoration: none;
    }

    .title-cell a:hover {
      text-decoration: underline;
    }

    .file-icon {
      color: #6c757d;
      margin-right: 5px;
      font-size: 16px;
    }

    .pagination {
      text-align: center;
      margin-top: 20px;
    }

    .pagination a, .pagination span {
      display: inline-block;
      padding: 8px 12px;
      margin: 0 4px;
      border: 1px solid #dee2e6;
      border-radius: 4px;
      text-decoration: none;
      color: #007bff;
    }

    .pagination a:hover {
      background-color: #e9ecef;
    }

    .pagination .current {
      background-color: #007bff;
      color: white;
      border-color: #007bff;
    }

    .pagination .disabled {
      color: #6c757d;
      cursor: not-allowed;
      pointer-events: none;
    }

    .no-data {
      text-align: center;
      padding: 40px;
      color: #6c757d;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1>게시판</h1>
    <p class="info">총 <%= request.getAttribute("totalCount") %> 개의 게시글</p>

    <a href="${pageContext.request.contextPath}/board/post" class="write-button">글쓰기</a>

    <%
      @SuppressWarnings("unchecked")
      List<Board> boards = (List<Board>) request.getAttribute("boards");
      Integer currentPage = (Integer) request.getAttribute("currentPage");
      Integer totalPages = (Integer) request.getAttribute("totalPages");

      if (currentPage == null) currentPage = 1;
      if (totalPages == null) totalPages = 1;

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    %>

    <table>
      <thead>
        <tr>
          <th style="width: 80px;">번호</th>
          <th style="width: 120px;">카테고리</th>
          <th>제목</th>
          <th style="width: 120px;">작성자</th>
          <th style="width: 80px;">조회수</th>
          <th style="width: 150px;">등록일시</th>
        </tr>
      </thead>
      <tbody>
        <%
          if (boards == null || boards.isEmpty()) {
        %>
        <tr>
          <td colspan="6" class="no-data">등록된 게시글이 없습니다.</td>
        </tr>
        <%
          } else {
            for (Board board : boards) {
        %>
        <tr>
          <td><%= board.getBoardId() %></td>
          <td><%= board.getCategoryName() != null ? board.getCategoryName() : "" %></td>
          <td class="title-cell">
            <%
              if (board.getHasFile() != null && board.getHasFile()) {
            %>
            <span class="file-icon">📎</span>
            <%
              }
            %>
            <a href="${pageContext.request.contextPath}/board/view?boardId=<%= board.getBoardId() %>&page=<%= currentPage %>">
              <%= board.getTitle() %>
            </a>
          </td>
          <td><%= board.getUserName() %></td>
          <td><%= board.getViews() %></td>
          <td><%= dateFormat.format(board.getCreatedAt()) %></td>
        </tr>
        <%
            }
          }
        %>
      </tbody>
    </table>

    <!-- 페이지네이션 -->
    <div class="pagination">
      <%
        // 이전 버튼
        if (currentPage > 1) {
      %>
      <a href="?page=<%= currentPage - 1 %>">이전</a>
      <%
        } else {
      %>
      <span class="disabled">이전</span>
      <%
        }

        // 페이지 번호 (최대 10개 표시)
        int startPage = ((currentPage - 1) / 10) * 10 + 1;
        int endPage = Math.min(startPage + 9, totalPages);

        for (int i = startPage; i <= endPage; i++) {
          if (i == currentPage) {
      %>
      <span class="current"><%= i %></span>
      <%
          } else {
      %>
      <a href="?page=<%= i %>"><%= i %></a>
      <%
          }
        }

        // 다음 버튼
        if (currentPage < totalPages) {
      %>
      <a href="?page=<%= currentPage + 1 %>">다음</a>
      <%
        } else {
      %>
      <span class="disabled">다음</span>
      <%
        }
      %>
    </div>
  </div>
</body>
</html>
