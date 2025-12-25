<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.board.dto.Category" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>게시판 - 글쓰기</title>
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
      font-size: 28px;
      margin-bottom: 10px;
      color: #333;
    }

    .subtitle {
      margin-bottom: 30px;
      color: #666;
      font-size: 14px;
    }

    .error-message {
      background-color: #f8d7da;
      color: #721c24;
      padding: 12px;
      border-radius: 4px;
      margin-bottom: 20px;
      border: 1px solid #f5c6cb;
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

    label .required {
      color: #dc3545;
      margin-left: 3px;
    }

    .help-text {
      font-size: 12px;
      color: #666;
      margin-left: 5px;
      font-weight: normal;
    }

    input[type="text"],
    input[type="password"],
    textarea,
    select {
      width: 100%;
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 14px;
      font-family: 'Malgun Gothic', sans-serif;
    }

    input[type="text"]:focus,
    input[type="password"]:focus,
    textarea:focus,
    select:focus {
      outline: none;
      border-color: #007bff;
      box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
    }

    textarea {
      resize: vertical;
      min-height: 200px;
    }

    input[type="file"] {
      padding: 8px;
      font-size: 14px;
    }

    .file-info {
      font-size: 12px;
      color: #666;
      margin-top: 5px;
    }

    .button-group {
      margin-top: 30px;
      text-align: center;
    }

    button {
      padding: 12px 30px;
      margin: 0 5px;
      border: none;
      border-radius: 4px;
      font-size: 14px;
      cursor: pointer;
      transition: background-color 0.2s;
    }

    .btn-submit {
      background-color: #007bff;
      color: white;
    }

    .btn-submit:hover {
      background-color: #0056b3;
    }

    .btn-cancel {
      background-color: #6c757d;
      color: white;
    }

    .btn-cancel:hover {
      background-color: #545b62;
    }

    .validation-error {
      color: #dc3545;
      font-size: 12px;
      margin-top: 5px;
      display: none;
    }

    .validation-error.show {
      display: block;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1>게시글 작성</h1>
    <p class="subtitle">새로운 게시글을 작성합니다. <span class="required">*</span> 표시는 필수 항목입니다.</p>

    <%
      // 오류 메시지 표시
      String errorMessage = (String) request.getAttribute("errorMessage");
      if (errorMessage != null && !errorMessage.trim().isEmpty()) {
    %>
      <div class="error-message">
        <%= errorMessage %>
      </div>
    <%
      }
    %>

    <form id="postForm" action="<%= request.getContextPath() %>/board/post" method="post" enctype="multipart/form-data">

      <!-- 카테고리 선택 -->
      <div class="form-group">
        <label for="categoryId">
          카테고리<span class="required">*</span>
        </label>
        <select id="categoryId" name="categoryId" required>
          <option value="">카테고리를 선택하세요</option>
          <%
            List<Category> categories = (List<Category>) request.getAttribute("categories");
            String selectedCategoryId = (String) request.getAttribute("categoryId");

            if (categories != null) {
              for (Category category : categories) {
                boolean isSelected = selectedCategoryId != null &&
                                   selectedCategoryId.equals(String.valueOf(category.getCategoryId()));
          %>
            <option value="<%= category.getCategoryId() %>" <%= isSelected ? "selected" : "" %>>
              <%= category.getCategoryName() %>
            </option>
          <%
              }
            }
          %>
        </select>
        <div class="validation-error" id="categoryError"></div>
      </div>

      <!-- 작성자 -->
      <div class="form-group">
        <label for="userName">
          작성자<span class="required">*</span>
          <span class="help-text">(2~10자)</span>
        </label>
        <input
          type="text"
          id="userName"
          name="userName"
          maxlength="10"
          value="<%= request.getAttribute("userName") != null ? request.getAttribute("userName") : "" %>"
          placeholder="작성자명을 입력하세요"
          required
        >
        <div class="validation-error" id="userNameError"></div>
      </div>

      <!-- 비밀번호 -->
      <div class="form-group">
        <label for="password">
          비밀번호<span class="required">*</span>
          <span class="help-text">(8~12자, 영문+숫자 조합)</span>
        </label>
        <input
          type="password"
          id="password"
          name="password"
          maxlength="12"
          placeholder="비밀번호를 입력하세요"
          required
        >
        <div class="validation-error" id="passwordError"></div>
      </div>

      <!-- 비밀번호 확인 -->
      <div class="form-group">
        <label for="passwordConfirm">
          비밀번호 확인<span class="required">*</span>
        </label>
        <input
          type="password"
          id="passwordConfirm"
          name="passwordConfirm"
          maxlength="12"
          placeholder="비밀번호를 다시 입력하세요"
          required
        >
        <div class="validation-error" id="passwordConfirmError"></div>
      </div>

      <!-- 제목 -->
      <div class="form-group">
        <label for="title">
          제목<span class="required">*</span>
          <span class="help-text">(4~1000자)</span>
        </label>
        <input
          type="text"
          id="title"
          name="title"
          maxlength="1000"
          value="<%= request.getAttribute("title") != null ? request.getAttribute("title") : "" %>"
          placeholder="제목을 입력하세요"
          required
        >
        <div class="validation-error" id="titleError"></div>
      </div>

      <!-- 내용 -->
      <div class="form-group">
        <label for="content">
          내용<span class="required">*</span>
          <span class="help-text">(4~4000자)</span>
        </label>
        <textarea
          id="content"
          name="content"
          maxlength="4000"
          placeholder="내용을 입력하세요"
          required
        ><%= request.getAttribute("content") != null ? request.getAttribute("content") : "" %></textarea>
        <div class="validation-error" id="contentError"></div>
      </div>

      <!-- 첨부파일 -->
      <div class="form-group">
        <label for="files">
          첨부파일
          <span class="help-text">(선택, 최대 3개, 각 2MB 이하, jpg/pdf/png만 가능)</span>
        </label>
        <input
          type="file"
          id="files"
          name="files"
          multiple
          accept=".jpg,.jpeg,.pdf,.png"
        >
        <div class="file-info" id="fileInfo"></div>
        <div class="validation-error" id="fileError"></div>
      </div>

      <!-- 버튼 -->
      <div class="button-group">
        <button type="submit" class="btn-submit">등록</button>
        <button type="button" class="btn-cancel" onclick="goToList()">취소</button>
      </div>
    </form>
  </div>

  <script>
    // 폼 유효성 검증
    document.getElementById('postForm').addEventListener('submit', function(e) {
      // 모든 오류 메시지 초기화
      clearErrors();

      let isValid = true;

      // 카테고리 검증
      const categoryId = document.getElementById('categoryId').value;
      if (!categoryId) {
        showError('categoryError', '카테고리를 선택해주세요.');
        isValid = false;
      }

      // 작성자 검증 (2~10자)
      const userName = document.getElementById('userName').value.trim();
      if (userName.length < 2 || userName.length > 10) {
        showError('userNameError', '작성자명은 2~10자로 입력해주세요.');
        isValid = false;
      }

      // 비밀번호 검증 (8~12자, 영문+숫자)
      const password = document.getElementById('password').value;
      const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,12}$/;
      if (!passwordRegex.test(password)) {
        showError('passwordError', '비밀번호는 8~12자의 영문+숫자 조합이어야 합니다.');
        isValid = false;
      }

      // 비밀번호 확인 검증
      const passwordConfirm = document.getElementById('passwordConfirm').value;
      if (password !== passwordConfirm) {
        showError('passwordConfirmError', '비밀번호가 일치하지 않습니다.');
        isValid = false;
      }

      // 제목 검증 (4~1000자)
      const title = document.getElementById('title').value.trim();
      if (title.length < 4 || title.length > 1000) {
        showError('titleError', '제목은 4~1000자로 입력해주세요.');
        isValid = false;
      }

      // 내용 검증 (4~4000자)
      const content = document.getElementById('content').value.trim();
      if (content.length < 4 || content.length > 4000) {
        showError('contentError', '내용은 4~4000자로 입력해주세요.');
        isValid = false;
      }

      // 파일 검증
      const files = document.getElementById('files').files;
      if (files.length > 0) {
        // 파일 개수 검증 (최대 3개)
        if (files.length > 3) {
          showError('fileError', '파일은 최대 3개까지 업로드할 수 있습니다.');
          isValid = false;
        }

        // 각 파일 검증
        const allowedExtensions = ['jpg', 'jpeg', 'pdf', 'png'];
        const maxFileSize = 2 * 1024 * 1024; // 2MB

        for (let i = 0; i < files.length; i++) {
          const file = files[i];

          // 확장자 검증
          const fileName = file.name.toLowerCase();
          const extension = fileName.substring(fileName.lastIndexOf('.') + 1);
          if (!allowedExtensions.includes(extension)) {
            showError('fileError', '허용된 파일 형식은 jpg, pdf, png입니다.');
            isValid = false;
            break;
          }

          // 파일 크기 검증
          if (file.size > maxFileSize) {
            showError('fileError', '각 파일의 크기는 최대 2MB입니다. (파일: ' + file.name + ')');
            isValid = false;
            break;
          }
        }
      }

      // 검증 실패 시 제출 방지
      if (!isValid) {
        e.preventDefault();
        return false;
      }
    });

    // 파일 선택 시 파일 정보 표시
    document.getElementById('files').addEventListener('change', function(e) {
      const fileInfo = document.getElementById('fileInfo');
      const files = e.target.files;

      if (files.length === 0) {
        fileInfo.textContent = '';
        return;
      }

      let infoText = '선택된 파일: ';
      for (let i = 0; i < files.length; i++) {
        if (i > 0) infoText += ', ';
        infoText += files[i].name + ' (' + formatFileSize(files[i].size) + ')';
      }

      fileInfo.textContent = infoText;
    });

    // 파일 크기 포맷팅
    function formatFileSize(bytes) {
      if (bytes < 1024) return bytes + ' B';
      if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
      return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
    }

    // 오류 메시지 표시
    function showError(elementId, message) {
      const errorElement = document.getElementById(elementId);
      errorElement.textContent = message;
      errorElement.classList.add('show');
    }

    // 모든 오류 메시지 초기화
    function clearErrors() {
      const errors = document.querySelectorAll('.validation-error');
      errors.forEach(error => {
        error.textContent = '';
        error.classList.remove('show');
      });
    }

    // 목록으로 돌아가기
    function goToList() {
      if (confirm('작성을 취소하시겠습니까?')) {
        window.location.href = '<%= request.getContextPath() %>/boards';
      }
    }
  </script>
</body>
</html>
