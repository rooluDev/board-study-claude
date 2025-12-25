<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.board.dto.BoardDTO" %>
<%@ page import="com.example.board.dto.CategoryDTO" %>
<%@ page import="com.example.board.dto.SearchCondition" %>
<%
    List<BoardDTO> boardList = (List<BoardDTO>) request.getAttribute("boardList");
    List<CategoryDTO> categories = (List<CategoryDTO>) request.getAttribute("categories");
    SearchCondition condition = (SearchCondition) request.getAttribute("condition");
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer totalPages = (Integer) request.getAttribute("totalPages");

    if (condition == null) {
        condition = new SearchCondition();
    }
    if (currentPage == null) currentPage = 1;
    if (totalPages == null) totalPages = 1;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시판 목록</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>게시판</h1>

        <!-- 검색 폼 -->
        <div class="search-box">
            <form action="<%= request.getContextPath() %>/boards" method="get" id="searchForm">
                <div class="search-row">
                    <label>카테고리:</label>
                    <select name="category">
                        <option value="">전체</option>
                        <% if (categories != null) {
                            for (CategoryDTO cat : categories) { %>
                            <option value="<%= cat.getCategoryId() %>" <%= (condition.getCategoryId() != null && condition.getCategoryId().equals(cat.getCategoryId())) ? "selected" : "" %>>
                                <%= cat.getCategoryName() %>
                            </option>
                        <% }
                        } %>
                    </select>

                    <label>등록일:</label>
                    <input type="date" name="from" value="<%= condition.getFrom() != null ? condition.getFrom() : "" %>">
                    ~
                    <input type="date" name="to" value="<%= condition.getTo() != null ? condition.getTo() : "" %>">

                    <label>검색어:</label>
                    <input type="text" name="keyword" value="<%= condition.getKeyword() != null ? condition.getKeyword() : "" %>" placeholder="제목/내용/작성자">

                    <button type="submit">검색</button>
                </div>
            </form>
        </div>

        <!-- 게시글 목록 -->
        <table class="board-table">
            <thead>
                <tr>
                    <th>카테고리</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>조회수</th>
                    <th>등록일</th>
                    <th>수정일</th>
                </tr>
            </thead>
            <tbody>
                <% if (boardList == null || boardList.isEmpty()) { %>
                    <tr>
                        <td colspan="6" class="text-center">게시글이 없습니다.</td>
                    </tr>
                <% } else {
                    for (BoardDTO board : boardList) { %>
                    <tr>
                        <td><%= board.getCategoryName() %></td>
                        <td>
                            <a href="<%= request.getContextPath() %>/board/view?boardId=<%= board.getBoardId() %>&page=<%= currentPage %>&category=<%= condition.getCategoryId() != null ? condition.getCategoryId() : "" %>&from=<%= condition.getFrom() != null ? condition.getFrom() : "" %>&to=<%= condition.getTo() != null ? condition.getTo() : "" %>&keyword=<%= condition.getKeyword() != null ? condition.getKeyword() : "" %>">
                                <% if (board.isHasAttachment()) { %>📁<% } %>
                                <%= board.getTitle() %>
                            </a>
                        </td>
                        <td><%= board.getUserName() %></td>
                        <td><%= board.getViews() %></td>
                        <td><%= board.getFormattedCreatedAtShort() %></td>
                        <td>
                            <% if (board.getEditedAt() != null) { %>
                                <%= board.getFormattedEditedAtShort() %>
                            <% } else { %>
                                -
                            <% } %>
                        </td>
                    </tr>
                <% }
                } %>
            </tbody>
        </table>

        <!-- 페이징 -->
        <div class="pagination">
            <% if (currentPage > 1) { %>
                <a href="?page=<%= currentPage - 1 %>&category=<%= condition.getCategoryId() != null ? condition.getCategoryId() : "" %>&from=<%= condition.getFrom() != null ? condition.getFrom() : "" %>&to=<%= condition.getTo() != null ? condition.getTo() : "" %>&keyword=<%= condition.getKeyword() != null ? condition.getKeyword() : "" %>">이전</a>
            <% } %>

            <% for (int i = 1; i <= totalPages; i++) {
                if (i == currentPage) { %>
                    <strong><%= i %></strong>
                <% } else { %>
                    <a href="?page=<%= i %>&category=<%= condition.getCategoryId() != null ? condition.getCategoryId() : "" %>&from=<%= condition.getFrom() != null ? condition.getFrom() : "" %>&to=<%= condition.getTo() != null ? condition.getTo() : "" %>&keyword=<%= condition.getKeyword() != null ? condition.getKeyword() : "" %>"><%= i %></a>
                <% }
            } %>

            <% if (currentPage < totalPages) { %>
                <a href="?page=<%= currentPage + 1 %>&category=<%= condition.getCategoryId() != null ? condition.getCategoryId() : "" %>&from=<%= condition.getFrom() != null ? condition.getFrom() : "" %>&to=<%= condition.getTo() != null ? condition.getTo() : "" %>&keyword=<%= condition.getKeyword() != null ? condition.getKeyword() : "" %>">다음</a>
            <% } %>
        </div>

        <!-- 글쓰기 버튼 -->
        <div class="actions">
            <a href="<%= request.getContextPath() %>/board/post?page=<%= currentPage %>&category=<%= condition.getCategoryId() != null ? condition.getCategoryId() : "" %>&from=<%= condition.getFrom() != null ? condition.getFrom() : "" %>&to=<%= condition.getTo() != null ? condition.getTo() : "" %>&keyword=<%= condition.getKeyword() != null ? condition.getKeyword() : "" %>" class="btn">글쓰기</a>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/resources/js/board.js"></script>
</body>
</html>
