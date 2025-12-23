// 폼 검증
document.addEventListener('DOMContentLoaded', function() {

  // 게시글 작성 폼 검증
  const postForm = document.getElementById('postForm');
  if (postForm) {
    postForm.addEventListener('submit', function(e) {
      // 작성자 검증
      const writer = this.querySelector('[name="writer"]').value;
      if (writer.length < 4 || writer.length > 10) {
        alert('작성자는 4~10자 사이여야 합니다.');
        e.preventDefault();
        return false;
      }

      // 비밀번호 검증
      const password = this.querySelector('[name="password"]').value;
      const passwordConfirm = this.querySelector('[name="passwordConfirm"]').value;

      if (password.length < 8 || password.length > 12) {
        alert('비밀번호는 8~12자 사이여야 합니다.');
        e.preventDefault();
        return false;
      }

      const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]+$/;
      if (!passwordPattern.test(password)) {
        alert('비밀번호는 영문과 숫자를 포함해야 합니다.');
        e.preventDefault();
        return false;
      }

      if (password !== passwordConfirm) {
        alert('비밀번호가 일치하지 않습니다.');
        e.preventDefault();
        return false;
      }

      // 제목 검증
      const title = this.querySelector('[name="title"]').value;
      if (title.length < 4 || title.length > 1000) {
        alert('제목은 4~1000자 사이여야 합니다.');
        e.preventDefault();
        return false;
      }

      // 내용 검증
      const content = this.querySelector('[name="content"]').value;
      if (content.length < 4 || content.length > 4000) {
        alert('내용은 4~4000자 사이여야 합니다.');
        e.preventDefault();
        return false;
      }

      // 파일 검증
      const files = this.querySelector('[name="files"]').files;
      if (files.length > 3) {
        alert('파일은 최대 3개까지 업로드 가능합니다.');
        e.preventDefault();
        return false;
      }

      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const maxSize = 2 * 1024 * 1024; // 2MB

        if (file.size > maxSize) {
          alert('파일 크기는 2MB를 초과할 수 없습니다.');
          e.preventDefault();
          return false;
        }

        const allowedExtensions = ['jpg', 'png', 'pdf'];
        const fileName = file.name;
        const extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        if (!allowedExtensions.includes(extension)) {
          alert('허용되는 파일 형식: jpg, png, pdf');
          e.preventDefault();
          return false;
        }
      }
    });
  }

  // 검색 폼 검증
  const searchForm = document.getElementById('searchForm');
  if (searchForm) {
    searchForm.addEventListener('submit', function(e) {
      const keyword = this.querySelector('[name="keyword"]').value;
      const from = this.querySelector('[name="from"]').value;
      const to = this.querySelector('[name="to"]').value;
      const category = this.querySelector('[name="category"]').value;

      // 검색어 없이 다른 조건도 없으면 경고
      if (!keyword && !from && !to && !category) {
        // 전체 목록 조회는 허용
        return true;
      }

      // 검색어가 있지만 비어있으면 경고
      if (keyword !== undefined && keyword !== null && keyword.trim() === ''
          && (from || to || category)) {
        // 다른 조건이 있으면 검색어 없이도 검색 허용
        return true;
      }
    });
  }
});

// 실시간 글자 수 카운터 (선택사항)
function setupCharCounter(textareaId, counterId, maxLength) {
  const textarea = document.getElementById(textareaId);
  const counter = document.getElementById(counterId);

  if (textarea && counter) {
    textarea.addEventListener('input', function() {
      const currentLength = this.value.length;
      counter.textContent = currentLength + ' / ' + maxLength;

      if (currentLength > maxLength) {
        counter.style.color = 'red';
      } else {
        counter.style.color = '#666';
      }
    });
  }
}