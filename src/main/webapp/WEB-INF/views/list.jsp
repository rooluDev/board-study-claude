<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.board.dto.Board" %>
<%@ page import="com.board.dto.Category" %>
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

    /* 검색 폼 스타일 */
    .search-form {
      background-color: #f8f9fa;
      padding: 20px;
      border-radius: 4px;
      margin-bottom: 20px;
      clear: both;
    }

    .search-row {
      display: flex;
      gap: 10px;
      align-items: center;
      flex-wrap: wrap;
      margin-bottom: 10px;
    }

    .search-row:last-child {
      margin-bottom: 0;
    }

    .search-label {
      font-weight: bold;
      color: #495057;
      min-width: 80px;
    }

    .search-form select,
    .search-form input[type="date"],
    .search-form input[type="text"] {
      padding: 8px 12px;
      border: 1px solid #ced4da;
      border-radius: 4px;
      font-size: 14px;
    }

    .search-form select {
      min-width: 150px;
    }

    .search-form input[type="date"] {
      min-width: 150px;
    }

    .search-form input[type="text"] {
      flex: 1;
      min-width: 200px;
    }

    .search-button {
      padding: 8px 24px;
      background-color: #28a745;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
      font-weight: bold;
    }

    .search-button:hover {
      background-color: #218838;
    }

    .date-separator {
      color: #6c757d;
      padding: 0 8px;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1>게시판</h1>
    <p class="info">총 <%= request.getAttribute("totalCount") %> 개의 게시글</p>

    <%
      @SuppressWarnings("unchecked")
      List<Board> boards = (List<Board>) request.getAttribute("boards");
      @SuppressWarnings("unchecked")
      List<Category> categories = (List<Category>) request.getAttribute("categories");
      Integer currentPage = (Integer) request.getAttribute("currentPage");
      Integer totalPages = (Integer) request.getAttribute("totalPages");

      // 검색 조건
      String selectedCategory = (String) request.getAttribute("category");
      String searchFrom = (String) request.getAttribute("from");
      String searchTo = (String) request.getAttribute("to");
      String searchKeyword = (String) request.getAttribute("keyword");

      if (currentPage == null) currentPage = 1;
      if (totalPages == null) totalPages = 1;
      if (selectedCategory == null) selectedCategory = "";
      if (searchFrom == null) searchFrom = "";
      if (searchTo == null) searchTo = "";
      if (searchKeyword == null) searchKeyword = "";

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    %>

    <a href="${pageContext.request.contextPath}/board/post<%= !searchKeyword.isEmpty() || !selectedCategory.isEmpty() || !searchFrom.isEmpty() || !searchTo.isEmpty() ? "?category=" + selectedCategory + "&from=" + searchFrom + "&to=" + searchTo + "&keyword=" + searchKeyword : "" %>" class="write-button">글쓰기</a>

    <!-- 검색 폼 -->
    <form class="search-form" onsubmit="return validateSearch()">
      <div class="search-row">
        <span class="search-label">카테고리</span>
        <select name="category">
          <option value="">전체</option>
          <%
            if (categories != null) {
              for (Category cat : categories) {
          %>
          <option value="<%= cat.getCategoryId() %>" <%= selectedCategory.equals(String.valueOf(cat.getCategoryId())) ? "selected" : "" %>>
            <%= cat.getCategoryName() %>
          </option>
          <%
              }
            }
          %>
        </select>
      </div>

      <div class="search-row">
        <span class="search-label">등록일</span>
        <input type="date" name="from" value="<%= searchFrom %>">
        <span class="date-separator">~</span>
        <input type="date" name="to" value="<%= searchTo %>">
      </div>

      <div class="search-row">
        <span class="search-label">검색어</span>
        <input type="text" name="keyword" value="<%= searchKeyword %>" placeholder="제목, 내용, 작성자로 검색">
        <button type="submit" class="search-button">검색</button>
      </div>
    </form>

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
              String viewUrl = String.format("/board/view?boardId=%d&page=%d&category=%s&from=%s&to=%s&keyword=%s",
                  board.getBoardId(), currentPage, selectedCategory, searchFrom, searchTo, searchKeyword);
            %>
            <a href="<%= viewUrl %>">
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
        // 검색 조건 쿼리 스트링 생성
        String searchQuery = String.format("&category=%s&from=%s&to=%s&keyword=%s",
            selectedCategory, searchFrom, searchTo, searchKeyword);

        // 이전 버튼
        if (currentPage > 1) {
      %>
      <a href="?page=<%= currentPage - 1 %><%= searchQuery %>">이전</a>
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
      <a href="?page=<%= i %><%= searchQuery %>"><%= i %></a>
      <%
          }
        }

        // 다음 버튼
        if (currentPage < totalPages) {
      %>
      <a href="?page=<%= currentPage + 1 %><%= searchQuery %>">다음</a>
      <%
        } else {
      %>
      <span class="disabled">다음</span>
      <%
        }
      %>
    </div>
  </div>

  <script>
    /**
     * 검색 폼 제출 시 검색어 필수 검증
     * PRD 요구사항: 검색어가 없으면 alert 표시
     */
    function validateSearch() {
      const keyword = document.querySelector('input[name="keyword"]').value.trim();
      const category = document.querySelector('select[name="category"]').value;
      const from = document.querySelector('input[name="from"]').value;
      const to = document.querySelector('input[name="to"]').value;

      // 검색 조건이 하나라도 있으면 검색 허용
      // 하지만 검색어만 입력했을 때는 반드시 검색어가 있어야 함
      if (!keyword && !category && !from && !to) {
        alert("검색어를 입력하세요!");
        return false;
      }

      return true;
    }
  </script>
</body>
</html>
