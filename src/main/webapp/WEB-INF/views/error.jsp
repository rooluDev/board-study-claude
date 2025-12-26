<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>오류 - 게시판</title>
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: 'Malgun Gothic', sans-serif;
      background-color: #f5f5f5;
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
    }

    .error-container {
      max-width: 600px;
      background-color: white;
      padding: 40px;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      text-align: center;
    }

    .error-icon {
      font-size: 60px;
      color: #dc3545;
      margin-bottom: 20px;
    }

    h1 {
      font-size: 24px;
      color: #333;
      margin-bottom: 10px;
    }

    .error-message {
      font-size: 16px;
      color: #666;
      margin-bottom: 30px;
      line-height: 1.5;
    }

    .btn {
      padding: 12px 24px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
      text-decoration: none;
      display: inline-block;
      margin: 0 5px;
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
  </style>
</head>
<body>
  <div class="error-container">
    <div class="error-icon">⚠️</div>
    <h1>오류가 발생했습니다</h1>
    <div class="error-message">
      <%
        String errorMessage = (String) request.getAttribute("error");
        if (errorMessage == null || errorMessage.isEmpty()) {
          errorMessage = "알 수 없는 오류가 발생했습니다.";
        }
      %>
      <%= errorMessage %>
    </div>
    <div>
      <a href="javascript:history.back()" class="btn btn-secondary">이전 페이지</a>
      <a href="<%= request.getContextPath() %>/boards" class="btn btn-primary">목록으로</a>
    </div>
  </div>
</body>
</html>
