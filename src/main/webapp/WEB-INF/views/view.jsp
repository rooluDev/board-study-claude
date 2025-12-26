<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.board.dto.Board" %>
<%@ page import="com.board.dto.File" %>
<%@ page import="com.board.dto.Comment" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>게시판 - 상세보기</title>
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
      font-size: 24px;
      margin-bottom: 20px;
      color: #333;
      border-bottom: 2px solid #007bff;
      padding-bottom: 10px;
    }

    .board-info {
      background-color: #f8f9fa;
      padding: 15px;
      border-radius: 4px;
      margin-bottom: 20px;
    }

    .board-info-row {
      display: flex;
      margin-bottom: 8px;
      font-size: 14px;
    }

    .board-info-row:last-child {
      margin-bottom: 0;
    }

    .board-info-label {
      font-weight: bold;
      width: 100px;
      color: #495057;
    }

    .board-info-value {
      color: #212529;
    }

    .board-content {
      padding: 20px 0;
      line-height: 1.8;
      min-height: 200px;
      color: #212529;
      border-bottom: 1px solid #dee2e6;
      margin-bottom: 20px;
      white-space: pre-wrap;
    }

    .attachments {
      margin-bottom: 30px;
    }

    .attachments h3 {
      font-size: 16px;
      margin-bottom: 10px;
      color: #495057;
    }

    .file-list {
      list-style: none;
    }

    .file-item {
      padding: 8px 12px;
      background-color: #f8f9fa;
      margin-bottom: 5px;
      border-radius: 4px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .file-item a {
      color: #007bff;
      text-decoration: none;
    }

    .file-item a:hover {
      text-decoration: underline;
    }

    .file-size {
      color: #6c757d;
      font-size: 12px;
    }

    .comments {
      margin-bottom: 30px;
    }

    .comments h3 {
      font-size: 16px;
      margin-bottom: 15px;
      color: #495057;
    }

    .comment-item {
      padding: 12px;
      background-color: #f8f9fa;
      margin-bottom: 10px;
      border-radius: 4px;
      border-left: 3px solid #007bff;
    }

    .comment-date {
      font-size: 12px;
      color: #6c757d;
      margin-bottom: 8px;
    }

    .comment-content {
      color: #212529;
      line-height: 1.6;
    }

    .comment-form {
      margin-bottom: 30px;
    }

    .comment-form textarea {
      width: 100%;
      padding: 10px;
      border: 1px solid #ced4da;
      border-radius: 4px;
      resize: vertical;
      font-family: inherit;
      font-size: 14px;
    }

    .comment-form button {
      margin-top: 10px;
      padding: 8px 16px;
      background-color: #28a745;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }

    .comment-form button:hover {
      background-color: #218838;
    }

    .buttons {
      text-align: center;
      padding-top: 20px;
      border-top: 1px solid #dee2e6;
    }

    .btn {
      display: inline-block;
      padding: 10px 20px;
      margin: 0 5px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      text-decoration: none;
      font-size: 14px;
    }

    .btn-primary {
      background-color: #007bff;
      color: white;
    }

    .btn-primary:hover {
      background-color: #0056b3;
    }

    .btn-warning {
      background-color: #ffc107;
      color: #212529;
    }

    .btn-warning:hover {
      background-color: #e0a800;
    }

    .btn-danger {
      background-color: #dc3545;
      color: white;
    }

    .btn-danger:hover {
      background-color: #c82333;
    }

    .no-data {
      text-align: center;
      padding: 20px;
      color: #6c757d;
      font-size: 14px;
    }

    /* 모달 스타일 */
    .modal {
      display: none;
      position: fixed;
      z-index: 1000;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      overflow: auto;
      background-color: rgba(0, 0, 0, 0.4);
    }

    .modal.show {
      display: block;
    }

    .modal-content {
      background-color: white;
      margin: 15% auto;
      padding: 30px;
      border: 1px solid #888;
      border-radius: 8px;
      width: 400px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    .modal-header {
      margin-bottom: 20px;
    }

    .modal-header h2 {
      font-size: 20px;
      color: #333;
      margin: 0;
    }

    .modal-body {
      margin-bottom: 20px;
    }

    .modal-body label {
      display: block;
      margin-bottom: 8px;
      font-weight: bold;
      color: #495057;
    }

    .modal-body input[type="password"] {
      width: 100%;
      padding: 10px;
      border: 1px solid #ced4da;
      border-radius: 4px;
      font-size: 14px;
    }

    .modal-body input[type="password"]:focus {
      outline: none;
      border-color: #007bff;
      box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
    }

    .modal-error {
      color: #dc3545;
      font-size: 13px;
      margin-top: 8px;
      display: none;
    }

    .modal-error.show {
      display: block;
    }

    .modal-footer {
      text-align: right;
    }

    .modal-footer button {
      padding: 10px 20px;
      margin-left: 10px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
    }

    .modal-btn-confirm {
      background-color: #007bff;
      color: white;
    }

    .modal-btn-confirm:hover {
      background-color: #0056b3;
    }

    .modal-btn-cancel {
      background-color: #6c757d;
      color: white;
    }

    .modal-btn-cancel:hover {
      background-color: #545b62;
    }
  </style>
</head>
<body>
  <%
    Board board = (Board) request.getAttribute("board");
    @SuppressWarnings("unchecked")
    List<File> files = (List<File>) request.getAttribute("files");
    @SuppressWarnings("unchecked")
    List<Comment> comments = (List<Comment>) request.getAttribute("comments");

    String currentPage = (String) request.getAttribute("page");
    String category = (String) request.getAttribute("category");
    String from = (String) request.getAttribute("from");
    String to = (String) request.getAttribute("to");
    String keyword = (String) request.getAttribute("keyword");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 검색 조건 쿼리 문자열 생성
    StringBuilder queryString = new StringBuilder();
    boolean firstParam = true;

    if (currentPage != null && !currentPage.isEmpty()) {
      queryString.append(firstParam ? "?" : "&").append("page=").append(currentPage);
      firstParam = false;
    }
    if (category != null && !category.isEmpty()) {
      queryString.append(firstParam ? "?" : "&").append("category=").append(category);
      firstParam = false;
    }
    if (from != null && !from.isEmpty()) {
      queryString.append(firstParam ? "?" : "&").append("from=").append(from);
      firstParam = false;
    }
    if (to != null && !to.isEmpty()) {
      queryString.append(firstParam ? "?" : "&").append("to=").append(to);
      firstParam = false;
    }
    if (keyword != null && !keyword.isEmpty()) {
      queryString.append(firstParam ? "?" : "&").append("keyword=").append(keyword);
      firstParam = false;
    }

    // 파라미터가 하나도 없으면 빈 문자열
    if (firstParam) {
      queryString.append("");
    }
  %>

  <div class="container">
    <h1><%= board.getTitle() %></h1>

    <div class="board-info">
      <div class="board-info-row">
        <span class="board-info-label">카테고리:</span>
        <span class="board-info-value"><%= board.getCategoryName() %></span>
      </div>
      <div class="board-info-row">
        <span class="board-info-label">작성자:</span>
        <span class="board-info-value"><%= board.getUserName() %></span>
      </div>
      <div class="board-info-row">
        <span class="board-info-label">등록일시:</span>
        <span class="board-info-value"><%= dateFormat.format(board.getCreatedAt()) %></span>
      </div>
      <%
        if (board.getEditedAt() != null) {
      %>
      <div class="board-info-row">
        <span class="board-info-label">수정일시:</span>
        <span class="board-info-value"><%= dateFormat.format(board.getEditedAt()) %></span>
      </div>
      <%
        }
      %>
      <div class="board-info-row">
        <span class="board-info-label">조회수:</span>
        <span class="board-info-value"><%= board.getViews() %></span>
      </div>
    </div>

    <div class="board-content">
      <%= board.getContent() %>
    </div>

    <!-- 첨부파일 -->
    <div class="attachments">
      <h3>첨부파일 (<%= files.size() %>)</h3>
      <%
        if (files.isEmpty()) {
      %>
      <p class="no-data">첨부파일이 없습니다.</p>
      <%
        } else {
      %>
      <ul class="file-list">
        <%
          for (File file : files) {
        %>
        <li class="file-item">
          <a href="${pageContext.request.contextPath}/download?fileId=<%= file.getFileId() %>">
            📎 <%= file.getOriginalName() %>
          </a>
          <span class="file-size">(<%= formatFileSize(file.getSize()) %>)</span>
        </li>
        <%
          }
        %>
      </ul>
      <%
        }
      %>
    </div>

    <!-- 댓글 목록 -->
    <div class="comments">
      <h3>댓글 (<%= comments.size() %>)</h3>
      <%
        if (comments.isEmpty()) {
      %>
      <p class="no-data">댓글이 없습니다.</p>
      <%
        } else {
          for (Comment comment : comments) {
      %>
      <div class="comment-item">
        <div class="comment-date"><%= dateFormat.format(comment.getCreatedAt()) %></div>
        <div class="comment-content"><%= comment.getComment() %></div>
      </div>
      <%
          }
        }
      %>
    </div>

    <!-- 댓글 등록 폼 -->
    <div class="comment-form">
      <h3>댓글 작성</h3>
      <textarea id="commentContent" rows="4" placeholder="댓글을 입력하세요 (1~300자)" maxlength="300"></textarea>
      <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 10px;">
        <span id="commentCharCount" style="font-size: 12px; color: #6c757d;">0 / 300자</span>
        <div>
          <button type="button" onclick="submitComment()" style="padding: 8px 16px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;">댓글 등록</button>
        </div>
      </div>
      <div id="commentError" style="color: #dc3545; font-size: 13px; margin-top: 8px; display: none;"></div>
    </div>

    <!-- 버튼 -->
    <div class="buttons">
      <a href="${pageContext.request.contextPath}/boards<%= queryString %>" class="btn btn-primary">목록</a>
      <button type="button" class="btn btn-warning" onclick="showPasswordModal()">수정</button>
      <button type="button" class="btn btn-danger" onclick="deleteBoard()">삭제</button>
    </div>
  </div>

  <!-- 수정 비밀번호 확인 모달 -->
  <div id="passwordModal" class="modal">
    <div class="modal-content">
      <div class="modal-header">
        <h2>비밀번호 확인</h2>
      </div>
      <div class="modal-body">
        <label for="modalPassword">게시글 수정을 위해 비밀번호를 입력하세요:</label>
        <input type="password" id="modalPassword" placeholder="비밀번호 입력" maxlength="12">
        <div class="modal-error" id="modalError"></div>
      </div>
      <div class="modal-footer">
        <button type="button" class="modal-btn-confirm" onclick="confirmPassword()">확인</button>
        <button type="button" class="modal-btn-cancel" onclick="closePasswordModal()">취소</button>
      </div>
    </div>
  </div>

  <!-- 삭제 비밀번호 확인 모달 -->
  <div id="deletePasswordModal" class="modal">
    <div class="modal-content">
      <div class="modal-header">
        <h2>게시글 삭제</h2>
      </div>
      <div class="modal-body">
        <label for="deleteModalPassword">게시글 삭제를 위해 비밀번호를 입력하세요:</label>
        <input type="password" id="deleteModalPassword" placeholder="비밀번호 입력" maxlength="12">
        <div class="modal-error" id="deleteModalError"></div>
      </div>
      <div class="modal-footer">
        <button type="button" class="modal-btn-confirm" onclick="confirmDeletePassword()">삭제</button>
        <button type="button" class="modal-btn-cancel" onclick="closeDeletePasswordModal()">취소</button>
      </div>
    </div>
  </div>

  <%!
    // 파일 크기 포맷팅 메서드
    private String formatFileSize(long size) {
      if (size < 1024) {
        return size + " B";
      } else if (size < 1024 * 1024) {
        return String.format("%.2f KB", size / 1024.0);
      } else {
        return String.format("%.2f MB", size / (1024.0 * 1024.0));
      }
    }
  %>

  <script>
    // 비밀번호 확인 모달 열기
    function showPasswordModal() {
      const modal = document.getElementById('passwordModal');
      const passwordInput = document.getElementById('modalPassword');
      const errorDiv = document.getElementById('modalError');

      // 초기화
      passwordInput.value = '';
      errorDiv.textContent = '';
      errorDiv.classList.remove('show');

      // 모달 표시
      modal.classList.add('show');

      // 비밀번호 입력 필드에 포커스
      setTimeout(() => passwordInput.focus(), 100);
    }

    // 비밀번호 확인 모달 닫기
    function closePasswordModal() {
      const modal = document.getElementById('passwordModal');
      modal.classList.remove('show');
    }

    // 비밀번호 확인
    function confirmPassword() {
      const passwordInput = document.getElementById('modalPassword');
      const errorDiv = document.getElementById('modalError');
      const password = passwordInput.value.trim();

      // 비밀번호 입력 여부 확인
      if (password.length === 0) {
        errorDiv.textContent = '비밀번호를 입력해주세요.';
        errorDiv.classList.add('show');
        passwordInput.focus();
        return;
      }

      // 오류 메시지 숨김
      errorDiv.classList.remove('show');

      // AJAX로 비밀번호 확인
      const boardId = <%= board.getBoardId() %>;
      const requestData = {
        boardId: boardId,
        password: password
      };

      fetch('<%= request.getContextPath() %>/auth/confirm', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          // 비밀번호 확인 성공 - 수정 페이지로 이동
          const queryString = '<%= queryString %>';
          window.location.href = '<%= request.getContextPath() %>/board/edit?boardId=' + boardId + queryString;
        } else {
          // 비밀번호 불일치
          errorDiv.textContent = data.message;
          errorDiv.classList.add('show');
          passwordInput.value = '';
          passwordInput.focus();
        }
      })
      .catch(error => {
        console.error('Error:', error);
        errorDiv.textContent = '비밀번호 확인 중 오류가 발생했습니다.';
        errorDiv.classList.add('show');
      });
    }

    // 삭제 비밀번호 확인 모달 열기
    function showDeletePasswordModal() {
      const modal = document.getElementById('deletePasswordModal');
      const passwordInput = document.getElementById('deleteModalPassword');
      const errorDiv = document.getElementById('deleteModalError');

      // 초기화
      passwordInput.value = '';
      errorDiv.textContent = '';
      errorDiv.classList.remove('show');

      // 모달 표시
      modal.classList.add('show');

      // 비밀번호 입력 필드에 포커스
      setTimeout(() => passwordInput.focus(), 100);
    }

    // 삭제 비밀번호 확인 모달 닫기
    function closeDeletePasswordModal() {
      const modal = document.getElementById('deletePasswordModal');
      modal.classList.remove('show');
    }

    // 삭제 비밀번호 확인 및 삭제 처리
    function confirmDeletePassword() {
      const passwordInput = document.getElementById('deleteModalPassword');
      const errorDiv = document.getElementById('deleteModalError');
      const password = passwordInput.value.trim();

      // 비밀번호 입력 여부 확인
      if (password.length === 0) {
        errorDiv.textContent = '비밀번호를 입력해주세요.';
        errorDiv.classList.add('show');
        passwordInput.focus();
        return;
      }

      // 오류 메시지 숨김
      errorDiv.classList.remove('show');

      // AJAX로 게시글 삭제 요청
      const boardId = <%= board.getBoardId() %>;
      const requestData = {
        boardId: boardId,
        password: password
      };

      fetch('<%= request.getContextPath() %>/board/delete', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          // 삭제 성공 - 목록 페이지로 이동 (검색 조건 없이)
          alert(data.message);
          window.location.href = '<%= request.getContextPath() %>' + data.redirectUrl;
        } else {
          // 삭제 실패 (비밀번호 불일치 또는 기타 오류)
          errorDiv.textContent = data.message;
          errorDiv.classList.add('show');
          passwordInput.value = '';
          passwordInput.focus();
        }
      })
      .catch(error => {
        console.error('Error:', error);
        errorDiv.textContent = '게시글 삭제 중 오류가 발생했습니다.';
        errorDiv.classList.add('show');
      });
    }

    // 게시글 삭제 버튼 클릭
    function deleteBoard() {
      // 삭제 확인 후 비밀번호 모달 표시
      if (confirm('정말 삭제하시겠습니까?')) {
        showDeletePasswordModal();
      }
    }

    // 엔터 키로 비밀번호 확인
    document.addEventListener('DOMContentLoaded', function() {
      // 수정 모달 엔터 키 처리
      const passwordInput = document.getElementById('modalPassword');
      if (passwordInput) {
        passwordInput.addEventListener('keypress', function(event) {
          if (event.key === 'Enter') {
            confirmPassword();
          }
        });
      }

      // 삭제 모달 엔터 키 처리
      const deletePasswordInput = document.getElementById('deleteModalPassword');
      if (deletePasswordInput) {
        deletePasswordInput.addEventListener('keypress', function(event) {
          if (event.key === 'Enter') {
            confirmDeletePassword();
          }
        });
      }

      // 수정 모달 외부 클릭 시 닫기
      const modal = document.getElementById('passwordModal');
      if (modal) {
        modal.addEventListener('click', function(event) {
          if (event.target === modal) {
            closePasswordModal();
          }
        });
      }

      // 삭제 모달 외부 클릭 시 닫기
      const deleteModal = document.getElementById('deletePasswordModal');
      if (deleteModal) {
        deleteModal.addEventListener('click', function(event) {
          if (event.target === deleteModal) {
            closeDeletePasswordModal();
          }
        });
      }

      // 댓글 입력 글자 수 카운터
      const commentContent = document.getElementById('commentContent');
      const commentCharCount = document.getElementById('commentCharCount');
      if (commentContent && commentCharCount) {
        commentContent.addEventListener('input', function() {
          const length = commentContent.value.length;
          commentCharCount.textContent = length + ' / 300자';
        });
      }
    });

    // 댓글 등록
    function submitComment() {
      const commentContent = document.getElementById('commentContent');
      const commentError = document.getElementById('commentError');
      const content = commentContent.value.trim();

      // 입력값 검증
      if (content.length === 0) {
        commentError.textContent = '댓글 내용을 입력해주세요.';
        commentError.style.display = 'block';
        commentContent.focus();
        return;
      }

      if (content.length < 1 || content.length > 300) {
        commentError.textContent = '댓글은 1자 이상 300자 이하로 입력해주세요.';
        commentError.style.display = 'block';
        commentContent.focus();
        return;
      }

      // 오류 메시지 숨김
      commentError.style.display = 'none';

      // AJAX로 댓글 등록 요청
      const boardId = <%= board.getBoardId() %>;
      const requestData = {
        boardId: boardId,
        content: content
      };

      fetch('<%= request.getContextPath() %>/comment', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          // 등록 성공 - 댓글 목록에 새 댓글 추가
          addCommentToList(data.comment);

          // 입력 필드 초기화
          commentContent.value = '';
          document.getElementById('commentCharCount').textContent = '0 / 300자';

          // 성공 메시지 표시 (옵션)
          alert(data.message);
        } else {
          // 등록 실패
          commentError.textContent = data.message;
          commentError.style.display = 'block';
        }
      })
      .catch(error => {
        console.error('Error:', error);
        commentError.textContent = '댓글 등록 중 오류가 발생했습니다.';
        commentError.style.display = 'block';
      });
    }

    // 댓글 목록에 새 댓글 추가
    function addCommentToList(comment) {
      const commentsDiv = document.querySelector('.comments');
      if (!commentsDiv) return;

      // "댓글이 없습니다" 메시지 제거
      const noDataMsg = commentsDiv.querySelector('.no-data');
      if (noDataMsg) {
        noDataMsg.remove();
      }

      // 새 댓글 요소 생성
      const commentItem = document.createElement('div');
      commentItem.className = 'comment-item';

      const commentDate = document.createElement('div');
      commentDate.className = 'comment-date';
      commentDate.textContent = comment.createdAt;

      const commentContentDiv = document.createElement('div');
      commentContentDiv.className = 'comment-content';
      commentContentDiv.textContent = comment.comment;

      commentItem.appendChild(commentDate);
      commentItem.appendChild(commentContentDiv);

      // 댓글 목록 h3 태그 찾기
      const h3 = commentsDiv.querySelector('h3');

      // h3 다음에 댓글 추가 (마지막에 추가)
      commentsDiv.appendChild(commentItem);

      // 댓글 개수 업데이트
      const currentCount = commentsDiv.querySelectorAll('.comment-item').length;
      if (h3) {
        h3.textContent = '댓글 (' + currentCount + ')';
      }
    }
  </script>
</body>
</html>
