<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시판 목록</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>게시판</h1>

        <!-- 검색 폼 -->
        <div class="search-box">
            <form action="${pageContext.request.contextPath}/boards" method="get" id="searchForm">
                <div class="search-row">
                    <label>카테고리:</label>
                    <select name="category">
                        <option value="">전체</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.categoryId}" ${condition.categoryId == cat.categoryId ? 'selected' : ''}>
                                <c:out value="${cat.categoryName}"/>
                            </option>
                        </c:forEach>
                    </select>

                    <label>등록일:</label>
                    <input type="date" name="from" value="${condition.from}">
                    ~
                    <input type="date" name="to" value="${condition.to}">

                    <label>검색어:</label>
                    <input type="text" name="keyword" value="${condition.keyword}" placeholder="제목/내용/작성자">

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
                <c:choose>
                    <c:when test="${empty boardList}">
                        <tr>
                            <td colspan="6" class="text-center">게시글이 없습니다.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="board" items="${boardList}">
                            <tr>
                                <td><c:out value="${board.categoryName}"/></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/board/view?boardId=${board.boardId}&page=${currentPage}&category=${condition.categoryId}&from=${condition.from}&to=${condition.to}&keyword=${condition.keyword}">
                                        <c:if test="${board.hasAttachment}">📁</c:if>
                                        <c:out value="${board.title}"/>
                                    </a>
                                </td>
                                <td><c:out value="${board.userName}"/></td>
                                <td>${board.views}</td>
                                <td><c:out value="${board.formattedCreatedAtShort}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${board.editedAt != null}">
                                            <c:out value="${board.formattedEditedAtShort}"/>
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>

        <!-- 페이징 -->
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="?page=${currentPage - 1}&category=${condition.categoryId}&from=${condition.from}&to=${condition.to}&keyword=${condition.keyword}">이전</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <strong>${i}</strong>
                    </c:when>
                    <c:otherwise>
                        <a href="?page=${i}&category=${condition.categoryId}&from=${condition.from}&to=${condition.to}&keyword=${condition.keyword}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="?page=${currentPage + 1}&category=${condition.categoryId}&from=${condition.from}&to=${condition.to}&keyword=${condition.keyword}">다음</a>
            </c:if>
        </div>

        <!-- 글쓰기 버튼 -->
        <div class="actions">
            <a href="${pageContext.request.contextPath}/board/post?page=${currentPage}&category=${condition.categoryId}&from=${condition.from}&to=${condition.to}&keyword=${condition.keyword}" class="btn">글쓰기</a>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/resources/js/board.js"></script>
</body>
</html>