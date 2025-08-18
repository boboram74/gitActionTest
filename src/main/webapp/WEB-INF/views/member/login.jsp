<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NEURAL_HUB - LOGIN</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
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
            overflow: hidden;
            height: 100vh;
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
                    radial-gradient(circle at 20% 30%, rgba(255, 0, 150, 0.15) 0%, transparent 50%),
                    radial-gradient(circle at 80% 70%, rgba(0, 255, 255, 0.15) 0%, transparent 50%),
                    radial-gradient(circle at 40% 90%, rgba(255, 255, 0, 0.1) 0%, transparent 50%);
            animation: bgFlow 15s ease-in-out infinite;
        }

        @keyframes bgFlow {
            0%, 100% { transform: translateX(0) translateY(0) scale(1) rotate(0deg); }
            25% { transform: translateX(-30px) translateY(-40px) scale(1.05) rotate(1deg); }
            50% { transform: translateX(25px) translateY(-25px) scale(0.95) rotate(-1deg); }
            75% { transform: translateX(-15px) translateY(30px) scale(1.02) rotate(0.5deg); }
        }

        .container {
            position: relative;
            z-index: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            padding: 20px;
        }

        .login-box {
            background: rgba(255, 255, 255, 0.03);
            backdrop-filter: blur(30px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 24px;
            padding: 50px 40px;
            width: 100%;
            max-width: 420px;
            position: relative;
            overflow: hidden;
            box-shadow:
                    0 25px 50px rgba(0, 0, 0, 0.5),
                    inset 0 1px 0 rgba(255, 255, 255, 0.1);
            transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
        }

        .login-box::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(135deg,
            rgba(255, 0, 128, 0.08) 0%,
            rgba(0, 255, 255, 0.08) 50%,
            rgba(255, 255, 0, 0.08) 100%);
            opacity: 0;
            transition: opacity 0.4s ease;
            pointer-events: none;
        }

        .login-box:hover::before {
            opacity: 1;
        }

        .login-box:hover {
            border-color: rgba(255, 255, 255, 0.2);
            box-shadow:
                    0 35px 70px rgba(0, 0, 0, 0.6),
                    0 0 40px rgba(255, 0, 128, 0.2),
                    inset 0 1px 0 rgba(255, 255, 255, 0.2);
        }

        .logo {
            text-align: center;
            margin-bottom: 40px;
        }

        .logo h1 {
            font-size: 2.5rem;
            font-weight: 700;
            background: linear-gradient(135deg, #ff0080, #00ffff, #ffff00);
            background-size: 300% 300%;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            animation: gradientShift 8s ease-in-out infinite;
            position: relative;
        }

        .logo .subtitle {
            font-size: 0.9rem;
            color: rgba(255, 255, 255, 0.6);
            margin-top: 8px;
            letter-spacing: 2px;
            text-transform: uppercase;
        }

        @keyframes gradientShift {
            0%, 100% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
        }

        .form-group {
            margin-bottom: 25px;
            position: relative;
        }

        .form-label {
            display: block;
            margin-bottom: 8px;
            color: rgba(255, 255, 255, 0.8);
            font-size: 0.9rem;
            font-weight: 500;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .form-input {
            width: 100%;
            padding: 15px 20px;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.15);
            border-radius: 12px;
            color: #ffffff;
            font-size: 1rem;
            font-family: 'Space Grotesk', sans-serif;
            transition: all 0.3s ease;
            backdrop-filter: blur(10px);
        }

        .form-input:focus {
            outline: none;
            border-color: rgba(255, 0, 128, 0.5);
            background: rgba(255, 255, 255, 0.08);
            box-shadow:
                    0 0 20px rgba(255, 0, 128, 0.2),
                    inset 0 0 20px rgba(255, 0, 128, 0.05);
            transform: translateY(-2px);
        }

        .form-input::placeholder {
            color: rgba(255, 255, 255, 0.4);
        }

        .login-btn {
            width: 100%;
            padding: 18px;
            background: linear-gradient(135deg, rgba(255, 0, 128, 0.8), rgba(0, 255, 255, 0.8));
            border: none;
            border-radius: 12px;
            color: #ffffff;
            font-size: 1.1rem;
            font-weight: 600;
            font-family: 'Space Grotesk', sans-serif;
            text-transform: uppercase;
            letter-spacing: 1px;
            cursor: pointer;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }

        .login-btn::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
            transition: left 0.5s;
        }

        .login-btn:hover::before {
            left: 100%;
        }

        .login-btn:hover {
            transform: translateY(-3px);
            box-shadow:
                    0 15px 35px rgba(255, 0, 128, 0.4),
                    0 0 30px rgba(0, 255, 255, 0.3);
        }

        .login-btn:active {
            transform: translateY(-1px);
        }

        .divider {
            display: flex;
            align-items: center;
            margin: 30px 0;
            color: rgba(255, 255, 255, 0.4);
            font-size: 0.9rem;
        }

        .divider::before,
        .divider::after {
            content: '';
            flex: 1;
            height: 1px;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
        }

        .divider span {
            margin: 0 20px;
        }

        .social-login {
            display: flex;
            gap: 15px;
        }

        .social-btn {
            flex: 1;
            padding: 12px;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.15);
            border-radius: 10px;
            color: rgba(255, 255, 255, 0.8);
            text-decoration: none;
            text-align: center;
            font-size: 0.9rem;
            font-weight: 500;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
        }

        .social-btn:hover {
            background: rgba(255, 255, 255, 0.1);
            border-color: rgba(255, 255, 255, 0.3);
            transform: translateY(-2px);
            color: #ffffff;
        }

        .forgot-password {
            text-align: center;
            margin-top: 25px;
        }

        .forgot-password a {
            color: rgba(255, 255, 255, 0.6);
            text-decoration: none;
            font-size: 0.9rem;
            transition: color 0.3s ease;
        }

        .forgot-password a:hover {
            color: rgba(0, 255, 255, 0.8);
        }

        /* 로딩 애니메이션 */
        .loading {
            display: none;
            width: 20px;
            height: 20px;
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-top: 2px solid #ffffff;
            border-radius: 50%;
            animation: spin 1s linear infinite;
            margin-left: 10px;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        /* 네온 펄스 효과 */
        .neon-pulse {
            animation: neonPulse 2s ease-in-out infinite alternate;
        }

        @keyframes neonPulse {
            from { box-shadow: 0 0 10px rgba(255, 0, 128, 0.3); }
            to { box-shadow: 0 0 30px rgba(255, 0, 128, 0.6); }
        }

        /* 에러 메시지 */
        .error-message {
            background: rgba(255, 0, 0, 0.1);
            border: 1px solid rgba(255, 0, 0, 0.3);
            color: #ff6b6b;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 0.9rem;
            display: none;
            animation: shake 0.5s ease-in-out;
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-5px); }
            75% { transform: translateX(5px); }
        }

        /* 반응형 */
        @media (max-width: 768px) {
            .login-box {
                margin: 20px;
                padding: 40px 30px;
            }

            .logo h1 {
                font-size: 2rem;
            }
        }
    </style>
</head>
<body>
<div class="bg-animation"></div>

<div class="container">
    <div class="login-box neon-pulse">
        <div class="logo">
            <h1>NEURAL_HUB</h1>
            <div class="subtitle">Access Terminal</div>
        </div>

        <div class="error-message" id="errorMessage">
            Invalid credentials. Access denied.
        </div>

        <form id="loginForm" action="/member/login" method="post">
            <div class="form-group">
                <label class="form-label" for="username">Username</label>
                <input type="text" id="username" name="id" class="form-input" placeholder="Enter your username" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">Password</label>
                <input type="password" id="password" name="password" class="form-input" placeholder="Enter your password" required>
            </div>

            <button type="submit" class="login-btn" id="loginBtn">
                <span id="btnText">로그인</span>
                <div class="loading" id="loading"></div>
            </button>
        </form>

        <div class="divider">
            <span>OR CONNECT VIA</span>
        </div>

        <div class="social-login">
            <a href="#" class="social-btn" id="githubBtn">
                <span>⚡</span> GitHub
            </a>
            <a href="#" class="social-btn" id="googleBtn">
                <span>🌐</span> Google
            </a>
        </div>

        <div class="forgot-password">
            <a href="#" id="forgotPasswordLink">Forgot access codes?</a>
        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        $('#loginForm').on('submit', function(e) {
            e.preventDefault();

            const username = $('#username').val();
            const password = $('#password').val();

            if (!username || !password) {
                showError('Please fill in all fields');
                return;
            }
            startLoading();
            e.target.submit();

            // setTimeout(function() {
            //     if (username === 'admin' && password === 'password') {
            //         $('#btnText').text('Access Granted');
            //         $('.login-box').css({
            //             'border-color': 'rgba(0, 255, 0, 0.5)',
            //             'box-shadow': '0 0 40px rgba(0, 255, 0, 0.3)'
            //         });
            //         e.target.submit();
            //     } else {
            //         showError('Invalid credentials. Access denied.');
            //         shakeLoginBox();
            //     }
            //     stopLoading();
            // }, 2000);

        });

        $('#githubBtn').on('click', function(e) {
            e.preventDefault();
            $(this).css('background', 'rgba(255, 255, 255, 0.15)');
            setTimeout(() => {
                alert('GitHub OAuth integration coming soon...');
                $(this).css('background', 'rgba(255, 255, 255, 0.05)');
            }, 200);
        });

        $('#googleBtn').on('click', function(e) {
            e.preventDefault();
            $(this).css('background', 'rgba(255, 255, 255, 0.15)');
            setTimeout(() => {
                alert('Google OAuth integration coming soon...');
                $(this).css('background', 'rgba(255, 255, 255, 0.05)');
            }, 200);
        });

        // 비밀번호 찾기
        $('#forgotPasswordLink').on('click', function(e) {
            e.preventDefault();
            alert('Password recovery system initializing...');
        });

        // 입력 필드 포커스 효과
        $('.form-input').on('focus', function() {
            $(this).closest('.form-group').find('.form-label').css({
                'color': 'rgba(255, 0, 128, 0.8)',
                'text-shadow': '0 0 10px rgba(255, 0, 128, 0.5)'
            });
        });

        $('.form-input').on('blur', function() {
            $(this).closest('.form-group').find('.form-label').css({
                'color': 'rgba(255, 255, 255, 0.8)',
                'text-shadow': 'none'
            });
        });

        // 로딩 함수들
        function startLoading() {
            $('#loading').show();
            $('#btnText').text('Connecting...');
            $('#loginBtn').prop('disabled', true).css('opacity', '0.7');
        }

        function stopLoading() {
            $('#loading').hide();
            $('#btnText').text('Initialize Connection');
            $('#loginBtn').prop('disabled', false).css('opacity', '1');
        }

        // 에러 표시
        function showError(message) {
            $('#errorMessage').text(message).show();
            setTimeout(function() {
                $('#errorMessage').hide();
            }, 4000);
        }

        // 로그인 박스 흔들기
        function shakeLoginBox() {
            $('.login-box').css('animation', 'shake 0.5s ease-in-out');
            setTimeout(function() {
                $('.login-box').css('animation', 'neonPulse 2s ease-in-out infinite alternate');
            }, 500);
        }

        // 키보드 단축키 (Enter)
        $(document).on('keypress', function(e) {
            if (e.which === 13 && !$('#loginBtn').prop('disabled')) {
                $('#loginForm').submit();
            }
        });

        // 초기 애니메이션
        setTimeout(function() {
            $('.login-box').css({
                'opacity': '1',
                'transform': 'translateY(0)'
            });
        }, 300);
    });
</script>
</body>
</html>