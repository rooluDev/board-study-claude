<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 수정</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>게시글 수정</h1>

        <c:if test="${not empty error}">
            <div class="error"><c:out value="${error}"/></div>
        </c:if>

        <form action="${pageContext.request.contextPath}/board/edit" method="post" enctype="multipart/form-data">
            <input type="hidden" name="boardId" value="${board.boardId}">

            <div class="form-group">
                <label>카테고리</label>
                <input type="text" value="${board.categoryName}" readonly disabled>
            </div>

            <div class="form-group">
                <label>작성자</label>
                <input type="text" value="${board.userName}" readonly disabled>
            </div>

            <div class="form-group">
                <label>비밀번호 *</label>
                <input type="password" name="password" required>
            </div>

            <div class="form-group">
                <label>제목 * (4-1000자)</label>
                <input type="text" name="title" value="${board.title}" minlength="4" maxlength="1000" required>
            </div>

            <div class="form-group">
                <label>내용 * (4-4000자)</label>
                <textarea name="content" minlength="4" maxlength="4000" rows="10" required>${board.content}</textarea>
            </div>

            <div class="form-group">
                <label>기존 첨부파일</label>
                <ul id="existingFiles">
                    <c:forEach var="file" items="${board.files}">
                        <li>
                            <c:out value="${file.originalName}"/>
                            <button type="button" onclick="markForDeletion(${file.fileId})">삭제</button>
                        </li>
                    </c:forEach>
                </ul>
                <input type="hidden" name="deletedFileIdList" id="deletedFileIdList">
            </div>

            <div class="form-group">
                <label>새 첨부파일</label>
                <input type="file" name="files" multiple accept=".jpg,.png,.pdf">
            </div>

            <div class="actions">
                <button type="submit" class="btn">수정</button>
                <a href="${pageContext.request.contextPath}/board/view?boardId=${board.boardId}" class="btn">취소</a>
            </div>
        </form>
    </div>

    <script src="${pageContext.request.contextPath}/resources/js/board.js"></script>
</body>
</html>