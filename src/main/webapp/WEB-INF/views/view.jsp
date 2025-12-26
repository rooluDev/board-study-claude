<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.board.dto.Board" %>
<%@ page import="com.board.dto.BoardFile" %>
<%@ page import="com.board.dto.Comment" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>게시글 상세 - 게시판</title>
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
      margin-bottom: 10px;
      color: #333;
      padding-bottom: 15px;
      border-bottom: 2px solid #007bff;
    }

    .board-info {
      display: flex;
      justify-content: space-between;
      padding: 15px 0;
      border-bottom: 1px solid #dee2e6;
      margin-bottom: 20px;
      color: #666;
      font-size: 14px;
    }

    .board-info .left {
      display: flex;
      gap: 20px;
    }

    .board-content {
      min-height: 200px;
      padding: 20px 0;
      line-height: 1.6;
      color: #333;
      border-bottom: 1px solid #dee2e6;
      margin-bottom: 20px;
      white-space: pre-wrap;
    }

    .files-section {
      margin-bottom: 30px;
    }

    .files-section h3 {
      font-size: 16px;
      margin-bottom: 10px;
      color: #495057;
    }

    .file-item {
      display: flex;
      align-items: center;
      padding: 10px;
      background-color: #f8f9fa;
      border-radius: 4px;
      margin-bottom: 5px;
    }

    .file-item a {
      color: #007bff;
      text-decoration: none;
      flex: 1;
    }

    .file-item a:hover {
      text-decoration: underline;
    }

    .file-size {
      color: #6c757d;
      font-size: 12px;
      margin-left: 10px;
    }

    .comments-section {
      margin-top: 40px;
    }

    .comments-section h3 {
      font-size: 18px;
      margin-bottom: 15px;
      color: #333;
    }

    .comment-item {
      padding: 15px;
      background-color: #f8f9fa;
      border-radius: 4px;
      margin-bottom: 10px;
    }

    .comment-info {
      font-size: 12px;
      color: #6c757d;
      margin-bottom: 8px;
    }

    .comment-content {
      color: #333;
      line-height: 1.5;
    }

    .comment-form {
      margin-top: 20px;
      padding: 20px;
      background-color: #f8f9fa;
      border-radius: 4px;
    }

    .comment-form textarea {
      width: 100%;
      min-height: 80px;
      padding: 10px;
      border: 1px solid #ced4da;
      border-radius: 4px;
      font-family: 'Malgun Gothic', sans-serif;
      resize: vertical;
    }

    .button-group {
      display: flex;
      justify-content: space-between;
      margin-top: 30px;
    }

    .button-group .left,
    .button-group .right {
      display: flex;
      gap: 10px;
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

    .btn-secondary {
      background-color: #6c757d;
      color: white;
    }

    .btn-secondary:hover {
      background-color: #5a6268;
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

    .no-files,
    .no-comments {
      text-align: center;
      padding: 20px;
      color: #999;
      font-size: 14px;
    }
  </style>
</head>
<body>
  <div class="container">
    <%
      Board board = (Board) request.getAttribute("board");
      List<BoardFile> fileList = (List<BoardFile>) request.getAttribute("fileList");
      List<Comment> commentList = (List<Comment>) request.getAttribute("commentList");

      // 검색 조건 파라미터
      String currentPage = (String) request.getAttribute("page");
      String category = (String) request.getAttribute("category");
      String from = (String) request.getAttribute("from");
      String to = (String) request.getAttribute("to");
      String keyword = (String) request.getAttribute("keyword");

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

      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    %>

    <h1><%= board.getTitle() %></h1>

    <div class="board-info">
      <div class="left">
        <span>작성자: <strong><%= board.getUserName() %></strong></span>
        <span>카테고리: <strong><%= board.getCategoryName() %></strong></span>
        <span>조회수: <strong><%= board.getViews() %></strong></span>
      </div>
      <div class="right">
        <span>등록일시: <%= board.getCreatedAt().format(dateFormatter) %></span>
        <%
          if (board.getEditedAt() != null) {
        %>
        <span>수정일시: <%= board.getEditedAt().format(dateFormatter) %></span>
        <%
          }
        %>
      </div>
    </div>

    <div class="board-content">
<%= board.getContent() %>
    </div>

    <!-- 첨부파일 -->
    <div class="files-section">
      <h3>첨부파일 (<%= fileList != null ? fileList.size() : 0 %>)</h3>
      <%
        if (fileList != null && !fileList.isEmpty()) {
          for (BoardFile file : fileList) {
            long fileSizeKB = file.getSize() / 1024;
      %>
        <div class="file-item">
          <span>📎</span>
          <a href="<%= request.getContextPath() %>/download?fileId=<%= file.getFileId() %>">
            <%= file.getOriginalName() %>
          </a>
          <span class="file-size">(<%= fileSizeKB %> KB)</span>
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

    <!-- 댓글 -->
    <div class="comments-section">
      <h3>댓글 (<%= commentList != null ? commentList.size() : 0 %>)</h3>
      <%
        if (commentList != null && !commentList.isEmpty()) {
          for (Comment comment : commentList) {
      %>
        <div class="comment-item">
          <div class="comment-info">
            <%= comment.getCreatedAt().format(dateFormatter) %>
            <%
              if (comment.getEditedAt() != null) {
            %>
              (수정됨: <%= comment.getEditedAt().format(dateFormatter) %>)
            <%
              }
            %>
          </div>
          <div class="comment-content"><%= comment.getComment() %></div>
        </div>
      <%
          }
        } else {
      %>
        <div class="no-comments">댓글이 없습니다.</div>
      <%
        }
      %>

      <!-- 댓글 등록 폼 -->
      <div class="comment-form">
        <textarea id="commentContent" placeholder="댓글을 입력하세요 (1~300자)" maxlength="300"></textarea>
        <div style="margin-top: 10px; display: flex; justify-content: space-between; align-items: center;">
          <span id="commentCharCount" style="font-size: 12px; color: #6c757d;">0 / 300자</span>
          <button type="button" class="btn btn-primary" onclick="submitComment()">댓글 등록</button>
        </div>
        <div id="commentError" style="color: #dc3545; font-size: 12px; margin-top: 5px; display: none;"></div>
      </div>
    </div>

    <!-- 버튼 그룹 -->
    <div class="button-group">
      <div class="left">
        <a href="<%= request.getContextPath() %>/boards?<%= searchParamsStr.substring(1) %>"
           class="btn btn-secondary">목록</a>
      </div>
      <div class="right">
        <button type="button" class="btn btn-warning" onclick="showPasswordModal()">수정</button>
        <button type="button" class="btn btn-danger" onclick="deleteBoard()">삭제</button>
      </div>
    </div>
  </div>

  <!-- 수정용 비밀번호 확인 모달 -->
  <div id="passwordModal" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.5); z-index: 1000;">
    <div style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); min-width: 400px;">
      <h3 style="margin-bottom: 20px; color: #333;">비밀번호 확인</h3>
      <p style="margin-bottom: 15px; color: #666; font-size: 14px;">게시글을 수정하려면 비밀번호를 입력하세요.</p>
      <input type="password" id="modalPassword" placeholder="비밀번호를 입력하세요" style="width: 100%; padding: 10px; border: 1px solid #ced4da; border-radius: 4px; margin-bottom: 10px;" maxlength="12">
      <div id="modalError" style="color: #dc3545; font-size: 12px; margin-bottom: 15px; display: none;"></div>
      <div style="display: flex; justify-content: flex-end; gap: 10px;">
        <button type="button" class="btn btn-secondary" onclick="closePasswordModal()">취소</button>
        <button type="button" class="btn btn-primary" onclick="confirmPassword()">확인</button>
      </div>
    </div>
  </div>

  <!-- 삭제용 비밀번호 확인 모달 -->
  <div id="deletePasswordModal" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.5); z-index: 1000;">
    <div style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); min-width: 400px;">
      <h3 style="margin-bottom: 20px; color: #333;">게시글 삭제</h3>
      <p style="margin-bottom: 15px; color: #666; font-size: 14px;">정말 삭제하시겠습니까? 삭제된 게시글은 복구할 수 없습니다.</p>
      <input type="password" id="deleteModalPassword" placeholder="비밀번호를 입력하세요" style="width: 100%; padding: 10px; border: 1px solid #ced4da; border-radius: 4px; margin-bottom: 10px;" maxlength="12">
      <div id="deleteModalError" style="color: #dc3545; font-size: 12px; margin-bottom: 15px; display: none;"></div>
      <div style="display: flex; justify-content: flex-end; gap: 10px;">
        <button type="button" class="btn btn-secondary" onclick="closeDeletePasswordModal()">취소</button>
        <button type="button" class="btn btn-danger" onclick="confirmDelete()">삭제</button>
      </div>
    </div>
  </div>

  <script>
    const boardId = <%= board.getBoardId() %>;
    const searchParams = '<%= searchParamsStr %>';

    // 비밀번호 확인 모달 표시
    function showPasswordModal() {
      document.getElementById('passwordModal').style.display = 'block';
      document.getElementById('modalPassword').value = '';
      document.getElementById('modalError').style.display = 'none';
      document.getElementById('modalPassword').focus();
    }

    // 비밀번호 확인 모달 닫기
    function closePasswordModal() {
      document.getElementById('passwordModal').style.display = 'none';
    }

    // 비밀번호 확인
    function confirmPassword() {
      const password = document.getElementById('modalPassword').value;

      if (!password) {
        showModalError('비밀번호를 입력하세요.');
        return;
      }

      // AJAX로 비밀번호 확인
      fetch('<%= request.getContextPath() %>/auth/confirm', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          boardId: boardId,
          password: password
        })
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          // 비밀번호 확인 성공 - 수정 페이지로 이동
          window.location.href = '<%= request.getContextPath() %>/board/edit?boardId=' + boardId + searchParams;
        } else {
          // 비밀번호 확인 실패
          showModalError(data.message);
        }
      })
      .catch(error => {
        console.error('Error:', error);
        showModalError('시스템 오류가 발생했습니다.');
      });
    }

    // 모달 에러 메시지 표시
    function showModalError(message) {
      const errorDiv = document.getElementById('modalError');
      errorDiv.textContent = message;
      errorDiv.style.display = 'block';
    }

    // Enter 키로 비밀번호 확인
    document.addEventListener('DOMContentLoaded', function() {
      const modalPasswordInput = document.getElementById('modalPassword');
      if (modalPasswordInput) {
        modalPasswordInput.addEventListener('keypress', function(e) {
          if (e.key === 'Enter') {
            confirmPassword();
          }
        });
      }
    });

    // 모달 외부 클릭 시 닫기
    document.getElementById('passwordModal').addEventListener('click', function(e) {
      if (e.target === this) {
        closePasswordModal();
      }
    });

    // 삭제 버튼 클릭
    function deleteBoard() {
      showDeletePasswordModal();
    }

    // 삭제용 비밀번호 확인 모달 표시
    function showDeletePasswordModal() {
      document.getElementById('deletePasswordModal').style.display = 'block';
      document.getElementById('deleteModalPassword').value = '';
      document.getElementById('deleteModalError').style.display = 'none';
      document.getElementById('deleteModalPassword').focus();
    }

    // 삭제용 비밀번호 확인 모달 닫기
    function closeDeletePasswordModal() {
      document.getElementById('deletePasswordModal').style.display = 'none';
    }

    // 삭제 확인 및 실행
    function confirmDelete() {
      const password = document.getElementById('deleteModalPassword').value;

      if (!password) {
        showDeleteModalError('비밀번호를 입력하세요.');
        return;
      }

      // AJAX로 게시글 삭제
      fetch('<%= request.getContextPath() %>/board/delete', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          boardId: boardId,
          password: password
        })
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          // 삭제 성공 - 목록 페이지로 이동 (검색 조건 유지 X)
          alert(data.message);
          window.location.href = data.redirectUrl;
        } else {
          // 삭제 실패
          showDeleteModalError(data.message);
        }
      })
      .catch(error => {
        console.error('Error:', error);
        showDeleteModalError('시스템 오류가 발생했습니다.');
      });
    }

    // 삭제 모달 에러 메시지 표시
    function showDeleteModalError(message) {
      const errorDiv = document.getElementById('deleteModalError');
      errorDiv.textContent = message;
      errorDiv.style.display = 'block';
    }

    // Enter 키로 삭제 확인
    document.addEventListener('DOMContentLoaded', function() {
      const deleteModalPasswordInput = document.getElementById('deleteModalPassword');
      if (deleteModalPasswordInput) {
        deleteModalPasswordInput.addEventListener('keypress', function(e) {
          if (e.key === 'Enter') {
            confirmDelete();
          }
        });
      }
    });

    // 삭제 모달 외부 클릭 시 닫기
    document.getElementById('deletePasswordModal').addEventListener('click', function(e) {
      if (e.target === this) {
        closeDeletePasswordModal();
      }
    });

    // 댓글 입력 글자 수 카운트
    const commentContentTextarea = document.getElementById('commentContent');
    const commentCharCount = document.getElementById('commentCharCount');

    if (commentContentTextarea && commentCharCount) {
      commentContentTextarea.addEventListener('input', function() {
        const length = this.value.length;
        commentCharCount.textContent = length + ' / 300자';

        if (length > 300) {
          commentCharCount.style.color = '#dc3545';
        } else {
          commentCharCount.style.color = '#6c757d';
        }
      });
    }

    // 댓글 등록
    function submitComment() {
      const content = document.getElementById('commentContent').value.trim();
      const errorDiv = document.getElementById('commentError');

      // 에러 메시지 초기화
      errorDiv.style.display = 'none';

      // 입력값 검증
      if (!content) {
        showCommentError('댓글 내용을 입력해주세요.');
        return;
      }

      if (content.length < 1 || content.length > 300) {
        showCommentError('댓글은 1~300자로 입력해주세요.');
        return;
      }

      // AJAX로 댓글 등록
      fetch('<%= request.getContextPath() %>/comment', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          boardId: boardId,
          content: content
        })
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          // 댓글 등록 성공 - 페이지 새로고침
          alert(data.message);
          location.reload();
        } else {
          // 댓글 등록 실패
          showCommentError(data.message);
        }
      })
      .catch(error => {
        console.error('Error:', error);
        showCommentError('시스템 오류가 발생했습니다.');
      });
    }

    // 댓글 에러 메시지 표시
    function showCommentError(message) {
      const errorDiv = document.getElementById('commentError');
      errorDiv.textContent = message;
      errorDiv.style.display = 'block';
    }

    // Enter 키로 댓글 등록 (Shift+Enter는 줄바꿈)
    if (commentContentTextarea) {
      commentContentTextarea.addEventListener('keydown', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
          e.preventDefault();
          submitComment();
        }
      });
    }
  </script>
</body>
</html>
