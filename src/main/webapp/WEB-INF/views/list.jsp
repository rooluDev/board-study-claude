<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.board.dto.Board" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
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
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    h1 {
      font-size: 28px;
      margin-bottom: 20px;
      color: #333;
    }

    .board-info {
      margin-bottom: 20px;
      color: #666;
      font-size: 14px;
    }

    .board-actions {
      margin-bottom: 20px;
      text-align: right;
    }

    .btn {
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
      text-decoration: none;
      display: inline-block;
    }

    .btn-primary {
      background-color: #007bff;
      color: white;
    }

    .btn-primary:hover {
      background-color: #0056b3;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-bottom: 20px;
    }

    th, td {
      padding: 12px;
      text-align: left;
      border-bottom: 1px solid #ddd;
    }

    th {
      background-color: #f8f9fa;
      font-weight: bold;
      color: #495057;
    }

    tr:hover {
      background-color: #f8f9fa;
    }

    .board-title {
      color: #007bff;
      text-decoration: none;
      cursor: pointer;
    }

    .board-title:hover {
      text-decoration: underline;
    }

    .file-icon {
      display: inline-block;
      margin-left: 5px;
      color: #6c757d;
      font-size: 12px;
    }

    .text-center {
      text-align: center;
    }

    .pagination {
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 5px;
      margin-top: 30px;
    }

    .pagination a,
    .pagination span {
      padding: 8px 12px;
      border: 1px solid #ddd;
      border-radius: 4px;
      text-decoration: none;
      color: #333;
    }

    .pagination a:hover {
      background-color: #007bff;
      color: white;
      border-color: #007bff;
    }

    .pagination .current {
      background-color: #007bff;
      color: white;
      border-color: #007bff;
    }

    .pagination .disabled {
      color: #ccc;
      cursor: not-allowed;
    }

    .no-data {
      text-align: center;
      padding: 40px;
      color: #999;
      font-size: 16px;
    }

    /* 검색 폼 스타일 */
    .search-form {
      background-color: #f8f9fa;
      padding: 20px;
      border-radius: 8px;
      margin-bottom: 20px;
    }

    .search-row {
      display: flex;
      gap: 15px;
      align-items: flex-end;
      flex-wrap: wrap;
    }

    .search-field {
      display: flex;
      flex-direction: column;
      gap: 5px;
    }

    .search-field label {
      font-size: 13px;
      color: #495057;
      font-weight: bold;
    }

    .search-field select,
    .search-field input[type="text"],
    .search-field input[type="date"] {
      padding: 8px 12px;
      border: 1px solid #ced4da;
      border-radius: 4px;
      font-size: 14px;
    }

    .search-field select {
      min-width: 120px;
    }

    .search-field input[type="text"] {
      min-width: 200px;
    }

    .search-field input[type="date"] {
      width: 140px;
    }

    /* 검색 조건 표시 */
    .search-info {
      background-color: #e7f3ff;
      padding: 12px 15px;
      border-radius: 4px;
      margin-bottom: 15px;
      font-size: 14px;
      color: #004085;
    }

    .search-tag {
      display: inline-block;
      background-color: #007bff;
      color: white;
      padding: 4px 10px;
      border-radius: 3px;
      margin-right: 8px;
      font-size: 13px;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1>게시판</h1>

    <%
      List<Board> boardList = (List<Board>) request.getAttribute("boardList");
      Integer currentPage = (Integer) request.getAttribute("currentPage");
      Integer totalPages = (Integer) request.getAttribute("totalPages");
      Integer totalCount = (Integer) request.getAttribute("totalCount");

      // 검색 조건
      Integer searchCategory = (Integer) request.getAttribute("category");
      String searchFrom = (String) request.getAttribute("from");
      String searchTo = (String) request.getAttribute("to");
      String searchKeyword = (String) request.getAttribute("keyword");

      if (currentPage == null) currentPage = 1;
      if (totalPages == null) totalPages = 1;
      if (totalCount == null) totalCount = 0;

      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

      // 검색 조건 쿼리 스트링 생성 함수
      StringBuilder searchParams = new StringBuilder();
      if (searchCategory != null) {
        searchParams.append("&category=").append(searchCategory);
      }
      if (searchFrom != null && !searchFrom.isEmpty()) {
        searchParams.append("&from=").append(searchFrom);
      }
      if (searchTo != null && !searchTo.isEmpty()) {
        searchParams.append("&to=").append(searchTo);
      }
      if (searchKeyword != null && !searchKeyword.isEmpty()) {
        searchParams.append("&keyword=").append(java.net.URLEncoder.encode(searchKeyword, "UTF-8"));
      }
      String searchParamsStr = searchParams.toString();
    %>

    <!-- 검색 폼 -->
    <div class="search-form">
      <form method="get" action="<%= request.getContextPath() %>/boards" onsubmit="return validateSearch()">
        <div class="search-row">
          <div class="search-field">
            <label>카테고리</label>
            <select name="category">
              <option value="">전체</option>
              <option value="1" <%= searchCategory != null && searchCategory == 1 ? "selected" : "" %>>Java</option>
              <option value="2" <%= searchCategory != null && searchCategory == 2 ? "selected" : "" %>>Spring</option>
              <option value="3" <%= searchCategory != null && searchCategory == 3 ? "selected" : "" %>>Database</option>
            </select>
          </div>

          <div class="search-field">
            <label>등록일시</label>
            <input type="date" name="from" value="<%= searchFrom != null ? searchFrom : "" %>">
            ~
            <input type="date" name="to" value="<%= searchTo != null ? searchTo : "" %>">
          </div>

          <div class="search-field">
            <label>검색어</label>
            <input type="text" name="keyword" id="searchKeyword"
                   value="<%= searchKeyword != null ? searchKeyword : "" %>"
                   placeholder="제목, 내용, 작성자">
          </div>

          <button type="submit" class="btn btn-primary">검색</button>
        </div>
      </form>
    </div>

    <!-- 검색 조건 표시 -->
    <%
      if (searchKeyword != null || searchCategory != null || searchFrom != null || searchTo != null) {
    %>
    <div class="search-info">
      검색 조건:
      <% if (searchCategory != null) { %>
        <span class="search-tag">카테고리:
          <%= searchCategory == 1 ? "Java" : searchCategory == 2 ? "Spring" : "Database" %>
        </span>
      <% } %>
      <% if (searchFrom != null || searchTo != null) { %>
        <span class="search-tag">
          기간: <%= searchFrom != null ? searchFrom : "" %> ~ <%= searchTo != null ? searchTo : "" %>
        </span>
      <% } %>
      <% if (searchKeyword != null) { %>
        <span class="search-tag">검색어: <%= searchKeyword %></span>
      <% } %>
    </div>
    <% } %>

    <div class="board-info">
      전체 게시글: <strong><%= totalCount %></strong>개
    </div>

    <div class="board-actions">
      <a href="<%= request.getContextPath() %>/board/post?page=<%= currentPage %><%= searchParamsStr %>"
         class="btn btn-primary">글쓰기</a>
    </div>

    <%
      if (boardList != null && !boardList.isEmpty()) {
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
            <th style="width: 150px;">수정일시</th>
          </tr>
        </thead>
        <tbody>
          <%
            for (Board board : boardList) {
              String editedAtStr = board.getEditedAt() != null
                  ? board.getEditedAt().format(dateFormatter)
                  : "-";
          %>
            <tr>
              <td class="text-center"><%= board.getBoardId() %></td>
              <td class="text-center"><%= board.getCategoryName() %></td>
              <td>
                <a href="<%= request.getContextPath() %>/board/view?boardId=<%= board.getBoardId() %>&page=<%= currentPage %><%= searchParamsStr %>"
                   class="board-title">
                  <%= board.getTitle() %>
                </a>
                <%
                  if (board.getFileCount() != null && board.getFileCount() > 0) {
                %>
                  <span class="file-icon" title="첨부파일 <%= board.getFileCount() %>개">📎</span>
                <%
                  }
                %>
              </td>
              <td class="text-center"><%= board.getUserName() %></td>
              <td class="text-center"><%= board.getViews() %></td>
              <td class="text-center"><%= board.getCreatedAt().format(dateFormatter) %></td>
              <td class="text-center"><%= editedAtStr %></td>
            </tr>
          <%
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
          <a href="<%= request.getContextPath() %>/boards?page=<%= currentPage - 1 %><%= searchParamsStr %>">이전</a>
        <%
          } else {
        %>
          <span class="disabled">이전</span>
        <%
          }

          // 페이지 번호 (현재 페이지 기준 앞뒤 2개씩)
          int startPage = Math.max(1, currentPage - 2);
          int endPage = Math.min(totalPages, currentPage + 2);

          for (int i = startPage; i <= endPage; i++) {
            if (i == currentPage) {
        %>
          <span class="current"><%= i %></span>
        <%
            } else {
        %>
          <a href="<%= request.getContextPath() %>/boards?page=<%= i %><%= searchParamsStr %>"><%= i %></a>
        <%
            }
          }

          // 다음 버튼
          if (currentPage < totalPages) {
        %>
          <a href="<%= request.getContextPath() %>/boards?page=<%= currentPage + 1 %><%= searchParamsStr %>">다음</a>
        <%
          } else {
        %>
          <span class="disabled">다음</span>
        <%
          }
        %>
      </div>
    <%
      } else {
    %>
      <div class="no-data">
        등록된 게시글이 없습니다.
      </div>
    <%
      }
    %>
  </div>

  <script>
    /**
     * 검색 폼 유효성 검사
     * 검색어가 없으면 alert 표시
     */
    function validateSearch() {
      const keyword = document.getElementById('searchKeyword').value.trim();

      if (!keyword) {
        alert('검색어를 입력하세요!');
        return false;
      }

      return true;
    }
  </script>
</body>
</html>
