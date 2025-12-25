<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.board.dto.Board" %>
<%@ page import="com.board.dto.File" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>게시판 - 글수정</title>
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

    .info-section {
      background-color: #f8f9fa;
      padding: 15px;
      border-radius: 4px;
      margin-bottom: 20px;
      border: 1px solid #dee2e6;
    }

    .info-item {
      display: flex;
      margin-bottom: 10px;
      font-size: 14px;
    }

    .info-item:last-child {
      margin-bottom: 0;
    }

    .info-label {
      font-weight: bold;
      width: 100px;
      color: #495057;
    }

    .info-value {
      color: #212529;
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
    textarea {
      width: 100%;
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 14px;
      font-family: 'Malgun Gothic', sans-serif;
    }

    input[type="text"]:focus,
    input[type="password"]:focus,
    textarea:focus {
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

    .file-list {
      margin-top: 10px;
      padding: 10px;
      background-color: #f8f9fa;
      border-radius: 4px;
      border: 1px solid #dee2e6;
    }

    .file-item {
      display: flex;
      align-items: center;
      padding: 8px;
      margin-bottom: 5px;
      background-color: white;
      border-radius: 3px;
    }

    .file-item:last-child {
      margin-bottom: 0;
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
  <%
    // 게시글 정보
    Board board = (Board) request.getAttribute("board");
    List<File> files = (List<File>) request.getAttribute("files");
    String errorMessage = (String) request.getAttribute("errorMessage");

    // 검색 조건
    String page = (String) request.getAttribute("page");
    String category = (String) request.getAttribute("category");
    String from = (String) request.getAttribute("from");
    String to = (String) request.getAttribute("to");
    String keyword = (String) request.getAttribute("keyword");

    // 날짜 포맷
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  %>

  <div class="container">
    <h1>게시글 수정</h1>
    <p class="subtitle">게시글의 제목과 내용, 첨부파일을 수정할 수 있습니다. <span class="required">*</span> 표시는 필수 항목입니다.</p>

    <%
      // 오류 메시지 표시
      if (errorMessage != null && !errorMessage.trim().isEmpty()) {
    %>
      <div class="error-message">
        <%= errorMessage %>
      </div>
    <%
      }
    %>

    <!-- 수정 불가 정보 표시 -->
    <div class="info-section">
      <div class="info-item">
        <span class="info-label">카테고리:</span>
        <span class="info-value"><%= board.getCategoryName() %></span>
      </div>
      <div class="info-item">
        <span class="info-label">작성자:</span>
        <span class="info-value"><%= board.getUserName() %></span>
      </div>
      <div class="info-item">
        <span class="info-label">등록일시:</span>
        <span class="info-value"><%= sdf.format(board.getCreatedAt()) %></span>
      </div>
      <div class="info-item">
        <span class="info-label">조회수:</span>
        <span class="info-value"><%= board.getViews() %></span>
      </div>
    </div>

    <form id="editForm" action="<%= request.getContextPath() %>/board/edit" method="post" enctype="multipart/form-data">

      <!-- Hidden Fields -->
      <input type="hidden" name="boardId" value="<%= board.getBoardId() %>">
      <input type="hidden" name="page" value="<%= page != null ? page : "" %>">
      <input type="hidden" name="category" value="<%= category != null ? category : "" %>">
      <input type="hidden" name="from" value="<%= from != null ? from : "" %>">
      <input type="hidden" name="to" value="<%= to != null ? to : "" %>">
      <input type="hidden" name="keyword" value="<%= keyword != null ? keyword : "" %>">

      <!-- 비밀번호 -->
      <div class="form-group">
        <label for="password">
          비밀번호<span class="required">*</span>
          <span class="help-text">(수정 권한 확인)</span>
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
          value="<%= board.getTitle() %>"
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
        ><%= board.getContent() %></textarea>
        <div class="validation-error" id="contentError"></div>
      </div>

      <!-- 기존 첨부파일 -->
      <%
        if (files != null && !files.isEmpty()) {
      %>
      <div class="form-group">
        <label>기존 첨부파일</label>
        <div class="file-list" id="existingFiles">
          <%
            for (File file : files) {
          %>
            <div class="file-item">
              <input
                type="checkbox"
                name="deletedFileIds"
                value="<%= file.getFileId() %>"
                id="file_<%= file.getFileId() %>"
                onchange="updateFileCount()"
              >
              <label for="file_<%= file.getFileId() %>">
                <%= file.getOriginalName() %> (<%= String.format("%.1f KB", file.getSize() / 1024.0) %>)
              </label>
            </div>
          <%
            }
          %>
        </div>
        <div class="file-info">삭제할 파일에 체크하세요.</div>
      </div>
      <%
        }
      %>

      <!-- 새 첨부파일 -->
      <div class="form-group">
        <label for="files">
          새 파일 추가
          <span class="help-text">(선택, 최대 3개, 각 2MB 이하, jpg/pdf/png만 가능)</span>
        </label>
        <input
          type="file"
          id="files"
          name="files"
          multiple
          accept=".jpg,.jpeg,.pdf,.png"
          onchange="updateFileCount()"
        >
        <div class="file-info" id="fileInfo"></div>
        <div class="file-info" id="fileCountInfo"></div>
        <div class="validation-error" id="fileError"></div>
      </div>

      <!-- 버튼 -->
      <div class="button-group">
        <button type="submit" class="btn-submit">수정</button>
        <button type="button" class="btn-cancel" onclick="goBack()">취소</button>
      </div>
    </form>
  </div>

  <script>
    const MAX_FILE_COUNT = 3;
    const MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
    const ALLOWED_EXTENSIONS = ['jpg', 'jpeg', 'pdf', 'png'];

    // 페이지 로드 시 파일 개수 업데이트
    window.onload = function() {
      updateFileCount();
    };

    // 폼 유효성 검증
    document.getElementById('editForm').addEventListener('submit', function(e) {
      // 모든 오류 메시지 초기화
      clearErrors();

      let isValid = true;

      // 비밀번호 검증
      const password = document.getElementById('password').value;
      if (password.trim().length === 0) {
        showError('passwordError', '비밀번호를 입력해주세요.');
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
      if (!validateFiles()) {
        isValid = false;
      }

      // 검증 실패 시 제출 방지
      if (!isValid) {
        e.preventDefault();
        return false;
      }
    });

    // 파일 개수 및 유효성 검증
    function validateFiles() {
      const files = document.getElementById('files').files;

      if (files.length === 0) {
        return true; // 파일 선택 안 함
      }

      // 각 파일 검증
      for (let i = 0; i < files.length; i++) {
        const file = files[i];

        // 확장자 검증
        const fileName = file.name.toLowerCase();
        const extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (!ALLOWED_EXTENSIONS.includes(extension)) {
          showError('fileError', '허용된 파일 형식은 jpg, pdf, png입니다.');
          return false;
        }

        // 파일 크기 검증
        if (file.size > MAX_FILE_SIZE) {
          showError('fileError', '각 파일의 크기는 최대 2MB입니다. (파일: ' + file.name + ')');
          return false;
        }
      }

      // 총 파일 개수 검증
      const totalFiles = calculateTotalFiles();
      if (totalFiles > MAX_FILE_COUNT) {
        showError('fileError', '파일은 최대 3개까지 첨부할 수 있습니다.');
        return false;
      }

      return true;
    }

    // 파일 개수 업데이트 및 표시
    function updateFileCount() {
      const totalFiles = calculateTotalFiles();

      // 새 파일 정보 표시
      const files = document.getElementById('files').files;
      const fileInfo = document.getElementById('fileInfo');

      if (files.length > 0) {
        let infoText = '선택된 파일: ';
        for (let i = 0; i < files.length; i++) {
          if (i > 0) infoText += ', ';
          infoText += files[i].name + ' (' + formatFileSize(files[i].size) + ')';
        }
        fileInfo.textContent = infoText;
      } else {
        fileInfo.textContent = '';
      }

      // 총 파일 개수 표시
      const fileCountInfo = document.getElementById('fileCountInfo');
      fileCountInfo.textContent = '총 파일 개수: ' + totalFiles + '/' + MAX_FILE_COUNT;

      if (totalFiles > MAX_FILE_COUNT) {
        fileCountInfo.style.color = '#dc3545';
        fileCountInfo.style.fontWeight = 'bold';
      } else {
        fileCountInfo.style.color = '#666';
        fileCountInfo.style.fontWeight = 'normal';
      }
    }

    // 총 파일 개수 계산
    function calculateTotalFiles() {
      // 기존 파일 개수
      const existingFilesDiv = document.getElementById('existingFiles');
      let existingCount = 0;
      if (existingFilesDiv) {
        const allCheckboxes = existingFilesDiv.querySelectorAll('input[type="checkbox"]');
        existingCount = allCheckboxes.length;

        // 삭제 체크된 파일 개수
        const checkedCheckboxes = existingFilesDiv.querySelectorAll('input[type="checkbox"]:checked');
        existingCount -= checkedCheckboxes.length;
      }

      // 새 파일 개수
      const newFiles = document.getElementById('files').files;
      const newCount = newFiles.length;

      return existingCount + newCount;
    }

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

    // 취소 버튼 - 상세 페이지로 돌아가기
    function goBack() {
      if (confirm('수정을 취소하시겠습니까?')) {
        const boardId = <%= board.getBoardId() %>;
        const page = '<%= page != null ? page : "" %>';
        const category = '<%= category != null ? category : "" %>';
        const from = '<%= from != null ? from : "" %>';
        const to = '<%= to != null ? to : "" %>';
        const keyword = '<%= keyword != null ? keyword : "" %>';

        let url = '<%= request.getContextPath() %>/board/view?boardId=' + boardId;
        if (page) url += '&page=' + page;
        if (category) url += '&category=' + category;
        if (from) url += '&from=' + from;
        if (to) url += '&to=' + to;
        if (keyword) url += '&keyword=' + keyword;

        window.location.href = url;
      }
    }
  </script>
</body>
</html>
