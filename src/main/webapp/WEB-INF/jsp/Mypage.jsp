<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>GachiWatch - 마이페이지</title>
  <style>
    body {
      margin: 0;
      font-family: 'Arial', sans-serif;
      background-color: #f7f7f7;
    }
    .container {
      max-width: 1000px;
      margin: auto;
      padding: 2rem;
      background: white;
      border-radius: 12px;
    }
    header {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    .logo {
      font-size: 2rem;
      font-weight: bold;
      color: purple;
    }
    .profile-section {
      display: flex;
      align-items: center;
      gap: 1rem;
      margin: 2rem 0;
    }
    .profile-img {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      background-color: #ddd;
    }
    .profile-info {
      display: flex;
      flex-direction: column;
    }
    .tabs {
      display: flex;
      margin-top: 1rem;
      border-bottom: 2px solid #ddd;
    }
    .tab {
      flex: 1;
      text-align: center;
      padding: 0.5rem;
      cursor: pointer;
      font-weight: bold;
      color: #555;
    }
    .tab.active {
      color: purple;
      border-bottom: 3px solid purple;
    }
    .card {
      background-color: #f2f0f9;
      margin: 1rem 0;
      padding: 1rem;
      border-radius: 8px;
    }
    .card-title {
      font-weight: bold;
      margin-bottom: 0.5rem;
    }
    .modal {
      position: fixed;
      bottom: 2rem;
      right: 2rem;
      background: white;
      border: 1px solid #ccc;
      border-radius: 8px;
      padding: 1rem;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }
    .modal button {
      padding: 0.5rem 1rem;
      margin: 0 0.5rem;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    .modal .confirm {
      background-color: purple;
      color: white;
    }
    .modal .cancel {
      background-color: #eee;
    }
  </style>
</head>
<body>
  <div class="container">
    <header>
      <div class="logo">Gachi Watch</div>
      <button>Logout</button>
    </header>

    <div class="profile-section">
      <div class="profile-img"></div>
      <div class="profile-info">
        <div>${nickname} &gt;</div>
        <div>${myClub} &gt;</div>
      </div>
    </div>

    <div class="tabs">
      <div class="tab active" onclick="switchTab('liked')">찜했어요</div>
      <div class="tab" onclick="switchTab('watched')">봤어요</div>
    </div>

    <div id="liked" class="tab-content">
      <div class="card">
        <div class="card-title">작성한 리뷰</div>
        <div>${reviewCount}</div>
      </div>
      <div class="card">
        <div class="card-title">구독중인 서비스</div>
        <div>
          <c:forEach var="service" items="${subscribedServices}">
            <img src="${service.logoUrl}" width="30" alt="${service.name}" />
          </c:forEach>
        </div>
      </div>
      <div class="card">
        <div class="card-title">선호하는 장르</div>
        <ul>
          <c:forEach var="genre" items="${preferredGenres}">
            <li>${genre}</li>
          </c:forEach>
        </ul>
      </div>
      <div class="card">
        <div class="card-title">신고</div>
        <button onclick="alert('신고가 접수되었습니다.')">신고하기</button>
      </div>
    </div>

    <div id="watched" class="tab-content" style="display: none;">
      <p>본 콘텐츠 목록이 여기에 표시됩니다.</p>
    </div>
  </div>

  <div class="modal">
    <p>정말 탈퇴하시겠습니까?</p>
    <button class="cancel" onclick="closeModal()">아니요</button>
    <button class="confirm" onclick="confirmLeave()">네</button>
  </div>

  <script>
    function switchTab(tab) {
      document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
      document.querySelectorAll('.tab-content').forEach(c => c.style.display = 'none');
      document.querySelector(`.tab.${tab === 'liked' ? ':first-child' : ':last-child'}`).classList.add('active');
      document.getElementById(tab).style.display = 'block';
    }

    function closeModal() {
      document.querySelector('.modal').style.display = 'none';
    }

    function confirmLeave() {
      alert('회원 탈퇴 처리되었습니다. 메인화면으로 이동합니다.');
      window.location.href = '/logout';
    }
  </script>
</body>
</html>
