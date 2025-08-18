<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Neuro Dashboard</title>
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@300;400;500;600;700&display=swap');

    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: 'Space Grotesk', sans-serif;
      background: #0a0a0a;
      color: #ffffff;
      overflow-x: hidden;
      position: relative;
    }

    /* 배경 애니메이션 */
    .bg-animation {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      pointer-events: none;
      z-index: 0;
    }

    .bg-animation::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background:
              radial-gradient(circle at 20% 30%, rgba(255, 0, 150, 0.1) 0%, transparent 50%),
              radial-gradient(circle at 80% 70%, rgba(0, 255, 255, 0.1) 0%, transparent 50%),
              radial-gradient(circle at 40% 90%, rgba(255, 255, 0, 0.08) 0%, transparent 50%);
      animation: bgFlow 20s ease-in-out infinite;
    }

    @keyframes bgFlow {
      0%, 100% { transform: translateX(0) translateY(0) scale(1); }
      25% { transform: translateX(-20px) translateY(-30px) scale(1.02); }
      50% { transform: translateX(20px) translateY(-20px) scale(0.98); }
      75% { transform: translateX(-10px) translateY(20px) scale(1.01); }
    }

    .container {
      position: relative;
      z-index: 1;
      max-width: 1600px;
      margin: 0 auto;
      padding: 25px;
    }

    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 40px;
      padding: 20px 0;
    }

    .logo {
      font-size: 2.8rem;
      font-weight: 700;
      background: linear-gradient(135deg, #ff0080, #00ffff, #ffff00);
      background-size: 300% 300%;
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      animation: gradientShift 8s ease-in-out infinite;
    }

    @keyframes gradientShift {
      0%, 100% { background-position: 0% 50%; }
      50% { background-position: 100% 50%; }
    }

    .status-bar {
      display: flex;
      gap: 20px;
      align-items: center;
    }

    .status-item {
      background: rgba(255, 255, 255, 0.05);
      backdrop-filter: blur(20px);
      border: 1px solid rgba(255, 255, 255, 0.1);
      padding: 8px 16px;
      border-radius: 20px;
      font-size: 0.9rem;
      font-weight: 500;
    }

    .main-grid {
      display: grid;
      grid-template-columns: 1fr 0.8fr 1fr 0.8fr;
      grid-template-rows: auto auto auto;
      gap: 20px;
      height: calc(100vh - 150px);
    }

    .panel {
      background: rgba(255, 255, 255, 0.02);
      backdrop-filter: blur(30px);
      border: 1px solid rgba(255, 255, 255, 0.08);
      border-radius: 24px;
      padding: 25px;
      position: relative;
      overflow: hidden;
      transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    }

    .panel::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(135deg,
      rgba(255, 0, 128, 0.05) 0%,
      rgba(0, 255, 255, 0.05) 50%,
      rgba(255, 255, 0, 0.05) 100%);
      opacity: 0;
      transition: opacity 0.4s ease;
      pointer-events: none;
    }

    .panel:hover::before {
      opacity: 1;
    }

    .panel:hover {
      transform: translateY(-8px) scale(1.02);
      border-color: rgba(255, 255, 255, 0.15);
      box-shadow:
              0 25px 50px rgba(0, 0, 0, 0.4),
              inset 0 1px 0 rgba(255, 255, 255, 0.1);
    }

    /* 각 패널별 특별한 스타일 */
    .chat-panel {
      grid-row: span 2;
      background: rgba(255, 0, 128, 0.03);
      border-color: rgba(255, 0, 128, 0.2);
    }

    .board-panel {
      grid-row: span 3;
      background: rgba(0, 128, 255, 0.03);
      border-color: rgba(0, 128, 255, 0.2);
    }

    .websocket-panel {
      background: rgba(255, 128, 255, 0.03);
      border-color: rgba(255, 128, 255, 0.2);
    }

    .project-panel {
      grid-row: span 2;
      background: rgba(0, 255, 255, 0.03);
      border-color: rgba(0, 255, 255, 0.2);
    }

    .quick-panel {
      background: rgba(255, 255, 0, 0.03);
      border-color: rgba(255, 255, 0, 0.2);
    }

    .tools-panel {
      background: rgba(128, 255, 0, 0.03);
      border-color: rgba(128, 255, 0, 0.2);
    }

    .stats-panel {
      background: rgba(255, 128, 0, 0.03);
      border-color: rgba(255, 128, 0, 0.2);
    }

    .notes-panel {
      grid-column: span 4;
      background: rgba(128, 0, 255, 0.03);
      border-color: rgba(128, 0, 255, 0.2);
    }

    .panel-header {
      display: flex;
      align-items: center;
      margin-bottom: 20px;
      padding-bottom: 15px;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    }

    .panel-icon {
      width: 35px;
      height: 35px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;
      font-size: 16px;
      background: rgba(255, 255, 255, 0.1);
    }

    .panel-title {
      font-size: 1.2rem;
      font-weight: 600;
      color: rgba(255, 255, 255, 0.9);
    }

    .panel-content {
      height: calc(100% - 80px);
      display: flex;
      align-items: center;
      justify-content: center;
      background: rgba(255, 255, 255, 0.02);
      border-radius: 16px;
      border: 1px dashed rgba(255, 255, 255, 0.15);
      color: rgba(255, 255, 255, 0.5);
      font-size: 0.95rem;
      text-align: center;
      transition: all 0.3s ease;
      position: relative;
    }

    .panel-content:hover {
      background: rgba(255, 255, 255, 0.05);
      border-color: rgba(255, 255, 255, 0.25);
      color: rgba(255, 255, 255, 0.7);
    }

    /* 특별한 효과들 */
    .glitch-text {
      position: relative;
      font-weight: 700;
    }

    .glitch-text::before,
    .glitch-text::after {
      content: attr(data-text);
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
    }

    .glitch-text::before {
      animation: glitch-anim-1 0.8s infinite linear alternate-reverse;
      color: #ff0080;
      z-index: -1;
    }

    .glitch-text::after {
      animation: glitch-anim-2 0.8s infinite linear alternate-reverse;
      color: #00ffff;
      z-index: -2;
    }

    @keyframes glitch-anim-1 {
      0% { transform: translate(0); }
      20% { transform: translate(-1px, 1px); }
      40% { transform: translate(-1px, -1px); }
      60% { transform: translate(1px, 1px); }
      80% { transform: translate(1px, -1px); }
      100% { transform: translate(0); }
    }

    @keyframes glitch-anim-2 {
      0% { transform: translate(0); }
      20% { transform: translate(1px, -1px); }
      40% { transform: translate(1px, 1px); }
      60% { transform: translate(-1px, -1px); }
      80% { transform: translate(-1px, 1px); }
      100% { transform: translate(0); }
    }

    /* 네온 효과 */
    .neon-border {
      box-shadow:
              0 0 20px rgba(255, 0, 128, 0.3),
              inset 0 0 20px rgba(255, 0, 128, 0.1);
    }

    .pulse {
      animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
    }

    @keyframes pulse {
      0%, 100% { opacity: 1; }
      50% { opacity: 0.7; }
    }

    /* 반응형 */
    @media (max-width: 1200px) {
      .main-grid {
        grid-template-columns: 1fr 1fr;
        grid-template-rows: auto;
        height: auto;
      }

      .chat-panel { grid-row: span 1; }
      .board-panel { grid-row: span 1; }
      .project-panel { grid-row: span 1; }
      .notes-panel { grid-column: span 2; }
    }

    @media (max-width: 768px) {
      .main-grid {
        grid-template-columns: 1fr;
      }

      .notes-panel { grid-column: span 1; }
      .logo { font-size: 2rem; }
      .status-bar { flex-wrap: wrap; }
    }
  </style>
</head>
<body>
<div class="bg-animation"></div>

<div class="container">
  <div class="header">
    <div class="logo glitch-text" data-text="NEURAL_HUB">Hello World</div>
    <div class="status-bar">
      <div class="status-item pulse">🟢 ONLINE</div>
      <div class="status-item">🎯 AI와 함께 성장하는 개발자</div>
      <div class="status-item" id="clock"></div>
    </div>
  </div>

  <div class="main-grid">
    <!-- AI 채팅 패널 -->
    <div class="panel chat-panel neon-border">
      <div class="panel-header">
        <div class="panel-icon">🤖</div>
        <div class="panel-title">AI_CHAT_INTERFACE</div>
      </div>
      <div class="panel-content">
        <div>
          AI 채팅 인터페이스가 들어갈 공간<br>
          <small style="opacity: 0.6;">GPT, Claude 등 멀티 AI 채팅</small>
        </div>
      </div>
    </div>

    <!-- 게시판 -->
    <div class="panel board-panel">
      <div class="panel-header">
        <div class="panel-icon">📋</div>
        <div class="panel-title">BOARD_SYSTEM</div>
      </div>
      <div class="panel-content">
        <div>
          게시판 시스템<br>
          <small style="opacity: 0.6;">공지사항, 자유게시판, 개발 로그</small>
        </div>
      </div>
    </div>

    <!-- 프로젝트 관리 -->
    <div class="panel project-panel">
      <div class="panel-header">
        <div class="panel-icon">⚡</div>
        <div class="panel-title">PROJECT_MATRIX</div>
      </div>
      <div class="panel-content">
        <div>
          프로젝트 관리 시스템<br>
          <small style="opacity: 0.6;">LLM 관리, 노션 연동, 깃허브 통합</small>
        </div>
      </div>
    </div>

    <!-- 웹소켓 채팅 -->
    <div class="panel websocket-panel">
      <div class="panel-header">
        <div class="panel-icon">💬</div>
        <div class="panel-title">WEBSOCKET_CHAT</div>
      </div>
      <div class="panel-content">
        <div>
          실시간 채팅<br>
          <small style="opacity: 0.6;">웹소켓 기반 실시간 커뮤니케이션</small>
        </div>
      </div>
    </div>

    <!-- 빠른 도구들 -->
    <div class="panel quick-panel">
      <div class="panel-header">
        <div class="panel-icon">🎯</div>
        <div class="panel-title">QUICK_TOOLS</div>
      </div>
      <div class="panel-content">
        <div>
          빠른 실행 도구모음<br>
          <small style="opacity: 0.6;">계산기, 타이머, 변환기</small>
        </div>
      </div>
    </div>

    <!-- 시스템 모니터링 -->
    <div class="panel tools-panel">
      <div class="panel-header">
        <div class="panel-icon">📊</div>
        <div class="panel-title">SYS_MONITOR</div>
      </div>
      <div class="panel-content">
        <div>
          시스템 상태 모니터링<br>
          <small style="opacity: 0.6;">API 사용량, 성능 지표</small>
        </div>
      </div>
    </div>

    <!-- 개인 통계 -->
    <div class="panel stats-panel">
      <div class="panel-header">
        <div class="panel-icon">🧠</div>
        <div class="panel-title">NEURAL_STATS</div>
      </div>
      <div class="panel-content">
        <div>
          개인 활동 분석<br>
          <small style="opacity: 0.6;">생산성, 습관 트래킹</small>
        </div>
      </div>
    </div>

    <!-- 메모 & 노트 -->
    <div class="panel notes-panel">
      <div class="panel-header">
        <div class="panel-icon">💭</div>
        <div class="panel-title">MEMORY_BANK</div>
      </div>
      <div class="panel-content">
        <div>
          스마트 메모 시스템<br>
          <small style="opacity: 0.6;">AI 기반 검색, 자동 태깅, 링크 연결</small>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
  function updateClock() {
    const now = new Date();
    const time = now.toLocaleTimeString('ko-KR', {
      hour12: false,
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });
    document.getElementById('clock').textContent = `⏰ ${time}`;
  }

  updateClock();
  setInterval(updateClock, 1000);

  // 패널 클릭 효과
  document.querySelectorAll('.panel').forEach(panel => {
    panel.addEventListener('click', function() {
      this.style.transform = 'scale(0.98)';
      setTimeout(() => {
        this.style.transform = '';
      }, 150);
    });
  });
</script>
</body>
</html>