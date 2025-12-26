<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.board.dto.Board" %>
<%@ page import="com.board.dto.BoardFile" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>게시글 수정 - 게시판</title>
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
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    h1 {
      font-size: 28px;
      margin-bottom: 30px;
      color: #333;
      padding-bottom: 15px;
      border-bottom: 2px solid #007bff;
    }

    .error-message {
      background-color: #f8d7da;
      color: #721c24;
      padding: 15px;
      border-radius: 4px;
      margin-bottom: 20px;
      border: 1px solid #f5c6cb;
    }

    .info-section {
      background-color: #f8f9fa;
      padding: 15px;
      border-radius: 4px;
      margin-bottom: 20px;
    }

    .info-item {
      margin-bottom: 8px;
      color: #495057;
    }

    .info-label {
      font-weight: bold;
      margin-right: 10px;
    }

    .form-group {
      margin-bottom: 20px;
    }

    label {
      display: block;
      margin-bottom: 8px;
      font-weight: bold;
      color: #333;
    }

    .required {
      color: #dc3545;
      margin-left: 3px;
    }

    .field-info {
      font-size: 12px;
      color: #6c757d;
      margin-left: 5px;
      font-weight: normal;
    }

    input[type="text"],
    input[type="password"],
    textarea {
      width: 100%;
      padding: 10px;
      border: 1px solid #ced4da;
      border-radius: 4px;
      font-size: 14px;
      font-family: 'Malgun Gothic', sans-serif;
    }

    input:focus,
    textarea:focus {
      outline: none;
      border-color: #007bff;
    }

    textarea {
      resize: vertical;
      min-height: 200px;
    }

    .existing-files {
      margin-bottom: 20px;
    }

    .file-item {
      display: flex;
      align-items: center;
      padding: 10px;
      background-color: #f8f9fa;
      border-radius: 4px;
      margin-bottom: 5px;
    }

    .file-item input[type="checkbox"] {
      margin-right: 10px;
    }

    .file-item label {
      margin: 0;
      font-weight: normal;
      cursor: pointer;
      flex: 1;
    }

    .file-size {
      color: #6c757d;
      font-size: 12px;
      margin-left: 10px;
    }

    .new-files-section {
      margin-top: 10px;
    }

    .file-input-wrapper {
      margin-bottom: 10px;
    }

    input[type="file"] {
      padding: 8px;
    }

    .file-info {
      font-size: 12px;
      color: #6c757d;
      margin-top: 5px;
    }

    .button-group {
      display: flex;
      justify-content: space-between;
      margin-top: 30px;
    }

    .btn {
      padding: 12px 30px;
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

    .btn-secondary {
      background-color: #6c757d;
      color: white;
    }

    .btn-secondary:hover {
      background-color: #5a6268;
    }

    .validation-error {
      color: #dc3545;
      font-size: 12px;
      margin-top: 5px;
      display: none;
    }

    .invalid {
      border-color: #dc3545;
    }

    .no-files {
      text-align: center;
      padding: 15px;
      color: #999;
      font-size: 14px;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1>게시글 수정</h1>

    <%
      String error = (String) request.getAttribute("error");
      Board board = (Board) request.getAttribute("board");
      List<BoardFile> fileList = (List<BoardFile>) request.getAttribute("fileList");

      // 검색 조건 파라미터
      String currentPage = (String) request.getAttribute("page");
      String category = (String) request.getAttribute("category");
      String from = (String) request.getAttribute("from");
      String to = (String) request.getAttribute("to");
      String keyword = (String) request.getAttribute("keyword");

      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    %>

    <% if (error != null) { %>
      <div class="error-message">
        <%= error %>
      </div>
    <% } %>

    <!-- 수정 불가 정보 표시 -->
    <div class="info-section">
      <div class="info-item">
        <span class="info-label">카테고리:</span>
        <%= board.getCategoryName() %>
      </div>
      <div class="info-item">
        <span class="info-label">작성자:</span>
        <%= board.getUserName() %>
      </div>
      <div class="info-item">
        <span class="info-label">등록일시:</span>
        <%= board.getCreatedAt().format(dateFormatter) %>
      </div>
      <%
        if (board.getEditedAt() != null) {
      %>
      <div class="info-item">
        <span class="info-label">수정일시:</span>
        <%= board.getEditedAt().format(dateFormatter) %>
      </div>
      <%
        }
      %>
    </div>

    <form id="editForm" action="<%= request.getContextPath() %>/board/edit" method="post" enctype="multipart/form-data">
      <!-- Hidden Fields -->
      <input type="hidden" name="boardId" value="<%= board.getBoardId() %>">
      <input type="hidden" name="page" value="<%= currentPage != null ? currentPage : "" %>">
      <input type="hidden" name="category" value="<%= category != null ? category : "" %>">
      <input type="hidden" name="from" value="<%= from != null ? from : "" %>">
      <input type="hidden" name="to" value="<%= to != null ? to : "" %>">
      <input type="hidden" name="keyword" value="<%= keyword != null ? keyword : "" %>">

      <!-- 비밀번호 -->
      <div class="form-group">
        <label for="password">
          비밀번호<span class="required">*</span>
          <span class="field-info">(8-12자, 영문+숫자 조합)</span>
        </label>
        <input type="password" id="password" name="password"
               maxlength="12"
               placeholder="비밀번호를 입력하세요" required>
        <div class="validation-error" id="passwordError">비밀번호는 8-12자 영문+숫자 조합이어야 합니다.</div>
      </div>

      <!-- 제목 -->
      <div class="form-group">
        <label for="title">
          제목<span class="required">*</span>
          <span class="field-info">(4-1000자)</span>
        </label>
        <input type="text" id="title" name="title"
               maxlength="1000"
               value="<%= board.getTitle() %>"
               placeholder="제목을 입력하세요" required>
        <div class="validation-error" id="titleError">제목은 4-1000자로 입력해주세요.</div>
      </div>

      <!-- 내용 -->
      <div class="form-group">
        <label for="content">
          내용<span class="required">*</span>
          <span class="field-info">(4-4000자)</span>
        </label>
        <textarea id="content" name="content"
                  maxlength="4000"
                  placeholder="내용을 입력하세요" required><%= board.getContent() %></textarea>
        <div class="validation-error" id="contentError">내용은 4-4000자로 입력해주세요.</div>
      </div>

      <!-- 기존 첨부파일 -->
      <div class="form-group">
        <label>기존 첨부파일</label>
        <div class="existing-files">
          <%
            if (fileList != null && !fileList.isEmpty()) {
              for (BoardFile file : fileList) {
                long fileSizeKB = file.getSize() / 1024;
          %>
            <div class="file-item">
              <input type="checkbox" id="delete_<%= file.getFileId() %>"
                     name="deletedFileIds" value="<%= file.getFileId() %>"
                     onchange="updateFileCount()">
              <label for="delete_<%= file.getFileId() %>">
                📎 <%= file.getOriginalName() %>
                <span class="file-size">(<%= fileSizeKB %> KB)</span>
              </label>
            </div>
          <%
              }
            } else {
          %>
            <div class="no-files">첨부파일이 없습니다.</div>
          <%
            }
          %>
        </div>
      </div>

      <!-- 새 첨부파일 -->
      <div class="form-group">
        <label for="files">
          새 첨부파일 추가
          <span class="field-info">(최대 3개, jpg/pdf/png, 각 파일 최대 2MB)</span>
        </label>
        <div class="new-files-section">
          <div class="file-input-wrapper">
            <input type="file" id="file1" name="file1" accept=".jpg,.jpeg,.pdf,.png" onchange="updateFileCount()">
          </div>
          <div class="file-input-wrapper">
            <input type="file" id="file2" name="file2" accept=".jpg,.jpeg,.pdf,.png" onchange="updateFileCount()">
          </div>
          <div class="file-input-wrapper">
            <input type="file" id="file3" name="file3" accept=".jpg,.jpeg,.pdf,.png" onchange="updateFileCount()">
          </div>
        </div>
        <div class="validation-error" id="fileError">파일 업로드 조건을 확인해주세요.</div>
      </div>

      <!-- 버튼 그룹 -->
      <div class="button-group">
        <%
          // 검색 조건 쿼리 스트링 생성
          StringBuilder searchParams = new StringBuilder();
          if (currentPage != null && !currentPage.isEmpty()) {
            searchParams.append("&page=").append(currentPage);
          }
          if (category != null && !category.isEmpty()) {
            searchParams.append("&category=").append(category);
          }
          if (from != null && !from.isEmpty()) {
            searchParams.append("&from=").append(from);
          }
          if (to != null && !to.isEmpty()) {
            searchParams.append("&to=").append(to);
          }
          if (keyword != null && !keyword.isEmpty()) {
            searchParams.append("&keyword=").append(keyword);
          }
          String searchParamsStr = searchParams.toString();
        %>
        <a href="<%= request.getContextPath() %>/board/view?boardId=<%= board.getBoardId() %><%= searchParamsStr %>"
           class="btn btn-secondary">취소</a>
        <button type="submit" class="btn btn-primary">수정</button>
      </div>
    </form>
  </div>

  <script>
    // 기존 파일 개수
    const initialFileCount = <%= fileList != null ? fileList.size() : 0 %>;

    // 파일 개수 업데이트 및 검증
    function updateFileCount() {
      // 삭제할 파일 개수
      const deletedCount = document.querySelectorAll('input[name="deletedFileIds"]:checked').length;

      // 새로 추가할 파일 개수
      const file1 = document.getElementById('file1').files[0];
      const file2 = document.getElementById('file2').files[0];
      const file3 = document.getElementById('file3').files[0];
      const newFiles = [file1, file2, file3].filter(f => f);
      const newCount = newFiles.length;

      // 총 파일 개수: 기존 - 삭제 + 새 파일
      const totalCount = initialFileCount - deletedCount + newCount;

      // 파일 개수 검증 (최대 3개)
      if (totalCount > 3) {
        document.getElementById('fileError').textContent =
          '총 파일 개수는 최대 3개까지 가능합니다. (현재: ' + totalCount + '개)';
        document.getElementById('fileError').style.display = 'block';
        return false;
      } else {
        document.getElementById('fileError').style.display = 'none';
        return true;
      }
    }

    // 폼 제출 이벤트 핸들러
    document.getElementById('editForm').addEventListener('submit', function(e) {
      // 모든 에러 메시지 초기화
      hideAllErrors();

      let isValid = true;

      // 비밀번호 검증 (8-12자, 영문+숫자)
      const password = document.getElementById('password').value;
      const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,12}$/;
      if (!passwordPattern.test(password)) {
        showError('password', 'passwordError', '비밀번호는 8-12자 영문+숫자 조합이어야 합니다.');
        isValid = false;
      }

      // 제목 검증 (4-1000자)
      const title = document.getElementById('title').value.trim();
      if (title.length < 4 || title.length > 1000) {
        showError('title', 'titleError', '제목은 4-1000자로 입력해주세요.');
        isValid = false;
      }

      // 내용 검증 (4-4000자)
      const content = document.getElementById('content').value.trim();
      if (content.length < 4 || content.length > 4000) {
        showError('content', 'contentError', '내용은 4-4000자로 입력해주세요.');
        isValid = false;
      }

      // 파일 개수 검증
      if (!updateFileCount()) {
        isValid = false;
      }

      // 파일 검증
      const file1 = document.getElementById('file1').files[0];
      const file2 = document.getElementById('file2').files[0];
      const file3 = document.getElementById('file3').files[0];
      const files = [file1, file2, file3].filter(f => f);

      const allowedExtensions = ['jpg', 'jpeg', 'pdf', 'png'];
      const maxFileSize = 2 * 1024 * 1024; // 2MB

      for (let file of files) {
        // 확장자 검증
        const fileName = file.name.toLowerCase();
        const extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (!allowedExtensions.includes(extension)) {
          showError('file1', 'fileError', '허용된 파일 형식은 jpg, pdf, png입니다.');
          isValid = false;
          break;
        }

        // 파일 크기 검증
        if (file.size > maxFileSize) {
          showError('file1', 'fileError', '각 파일은 최대 2MB까지 업로드 가능합니다.');
          isValid = false;
          break;
        }
      }

      if (!isValid) {
        e.preventDefault();
        return false;
      }

      return true;
    });

    // 에러 표시 함수
    function showError(fieldId, errorId, message) {
      const field = document.getElementById(fieldId);
      const error = document.getElementById(errorId);

      field.classList.add('invalid');
      error.textContent = message;
      error.style.display = 'block';
    }

    // 모든 에러 숨김
    function hideAllErrors() {
      const errors = document.querySelectorAll('.validation-error');
      errors.forEach(error => error.style.display = 'none');

      const fields = document.querySelectorAll('.invalid');
      fields.forEach(field => field.classList.remove('invalid'));
    }

    // 입력 시 에러 메시지 제거
    const inputs = document.querySelectorAll('input, textarea');
    inputs.forEach(input => {
      input.addEventListener('input', function() {
        this.classList.remove('invalid');
        const errorId = this.id + 'Error';
        const error = document.getElementById(errorId);
        if (error) {
          error.style.display = 'none';
        }
      });
    });
  </script>
</body>
</html>
