<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:out value="${board.title}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1><c:out value="${board.title}"/></h1>

        <div class="board-info">
            <span>작성자: <c:out value="${board.userName}"/></span>
            <span>조회수: ${board.views}</span>
            <span>등록일: <fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
            <c:if test="${board.editedAt != null}">
                <span>수정일: <fmt:formatDate value="${board.editedAt}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
            </c:if>
        </div>

        <div class="board-content">
            <c:out value="${board.content}" escapeXml="false"/>
        </div>

        <!-- 첨부파일 -->
        <c:if test="${not empty board.files}">
            <div class="attachments">
                <h3>첨부파일</h3>
                <ul>
                    <c:forEach var="file" items="${board.files}">
                        <li>
                            <a href="${pageContext.request.contextPath}/download?fileId=${file.fileId}">
                                <c:out value="${file.originalName}"/> (${file.size} bytes)
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <!-- 댓글 -->
        <div class="comments">
            <h3>댓글 (${board.comments.size()})</h3>
            <ul>
                <c:forEach var="comment" items="${board.comments}">
                    <li>
                        <div class="comment-date">
                            <fmt:formatDate value="${comment.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                        </div>
                        <div class="comment-content">
                            <c:out value="${comment.comment}"/>
                        </div>
                    </li>
                </c:forEach>
            </ul>

            <!-- 댓글 등록 폼 -->
            <form id="commentForm">
                <input type="hidden" name="boardId" value="${board.boardId}">
                <textarea name="content" placeholder="댓글을 입력하세요 (1-300자)" maxlength="300" required></textarea>
                <button type="submit">댓글 등록</button>
            </form>
        </div>

        <!-- 버튼 -->
        <div class="actions">
            <a href="${pageContext.request.contextPath}/boards?page=${page}&category=${category}&from=${from}&to=${to}&keyword=${keyword}" class="btn">목록</a>
            <button onclick="showEditModal()" class="btn">수정</button>
            <button onclick="showDeleteModal()" class="btn btn-danger">삭제</button>
        </div>
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
        const contextPath = '${pageContext.request.contextPath}';
        const boardId = ${board.boardId};
    </script>
    <script src="${pageContext.request.contextPath}/resources/js/board.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/validation.js"></script>
</body>
</html>