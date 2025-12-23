// 모달 표시
function showEditModal() {
  document.getElementById('editModal').style.display = 'flex';
}

function showDeleteModal() {
  document.getElementById('deleteModal').style.display = 'flex';
}

function closeModal(modalId) {
  document.getElementById(modalId).style.display = 'none';
}

// 수정 확인
function confirmEdit() {
  const password = document.getElementById('editPassword').value;
  if (!password) {
    alert('비밀번호를 입력해주세요.');
    return;
  }

  fetch(contextPath + '/auth/confirm', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      boardId: boardId,
      password: password
    })
  })
  .then(response => response.json())
  .then(data => {
    if (data.success) {
      window.location.href = contextPath + '/board/edit?boardId=' + boardId;
    } else {
      alert(data.message);
    }
  })
  .catch(error => {
    console.error('Error:', error);
    alert('오류가 발생했습니다.');
  });
}

// 삭제 확인
function confirmDelete() {
  const password = document.getElementById('deletePassword').value;
  if (!password) {
    alert('비밀번호를 입력해주세요.');
    return;
  }

  if (!confirm('정말 삭제하시겠습니까?')) {
    return;
  }

  fetch(contextPath + '/board/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      boardId: boardId,
      password: password
    })
  })
  .then(response => response.json())
  .then(data => {
    if (data.success) {
      alert('게시글이 삭제되었습니다.');
      window.location.href = contextPath + '/boards';
    } else {
      alert(data.message);
    }
  })
  .catch(error => {
    console.error('Error:', error);
    alert('오류가 발생했습니다.');
  });
}

// 댓글 등록
if (document.getElementById('commentForm')) {
  document.getElementById('commentForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = new FormData(this);
    const content = formData.get('content');

    if (!content || content.trim().length === 0) {
      alert('댓글 내용을 입력해주세요.');
      return;
    }

    if (content.length > 300) {
      alert('댓글은 300자를 초과할 수 없습니다.');
      return;
    }

    fetch(contextPath + '/comment', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        boardId: boardId,
        content: content
      })
    })
    .then(response => response.json())
    .then(data => {
      if (data.success) {
        alert('댓글이 등록되었습니다.');
        location.reload();
      } else {
        alert(data.message);
      }
    })
    .catch(error => {
      console.error('Error:', error);
      alert('오류가 발생했습니다.');
    });
  });
}

// 파일 삭제 표시
let deletedFileIds = [];

function markForDeletion(fileId) {
  if (confirm('이 파일을 삭제하시겠습니까?')) {
    deletedFileIds.push(fileId);
    document.getElementById('deletedFileIdList').value = deletedFileIds.join(',');

    // 화면에서 제거
    event.target.closest('li').style.textDecoration = 'line-through';
    event.target.disabled = true;
  }
}