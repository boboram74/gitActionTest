<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>익명 커뮤니티 - 게시글 상세</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <style>
        body { font-family: 'Malgun Gothic', sans-serif; margin: 0; background-color: #f4f6f9; color: #333; }
        .container { max-width: 900px; margin: 20px auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        header h1 { margin: 0; font-size: 24px; cursor: pointer; }
        .post-view .post-header { border-bottom: 1px solid #ddd; padding-bottom: 10px; margin-bottom: 20px; }
        .post-view .post-header h2 { font-size: 28px; margin: 0 0 10px 0; }
        .post-view .post-meta { font-size: 14px; color: #666; }
        .post-view .post-meta span { margin-right: 15px; }
        .post-view .post-content { min-height: 200px; padding: 20px 0; font-size: 16px; line-height: 1.8; border-bottom: 1px solid #ddd; }
        .post-actions { text-align: center; margin: 20px 0; }
        .post-actions button { padding: 10px 20px; margin: 0 5px; border: 1px solid #ccc; background-color: #f0f0f0; cursor: pointer; border-radius: 5px; }
        .post-actions button.like { color: #007bff; }
        .post-actions button.dislike { color: #dc3545; }
        .list-button-container { text-align: right; margin-bottom: 20px; }
        .list-button-container a { text-decoration: none; background-color: #6c757d; color: white; padding: 10px 15px; border-radius: 5px; }
        
        /* 댓글 영역 */
        .comment-section { margin-top: 30px; }
        .comment-section h3 { font-size: 18px; border-bottom: 2px solid #eee; padding-bottom: 10px; margin-bottom: 20px; }
        .comment-form textarea { width: 100%; height: 80px; padding: 10px; border: 1px solid #ccc; border-radius: 5px; margin-bottom: 10px; box-sizing: border-box; }
        .comment-form .form-group { display: flex; justify-content: space-between; margin-bottom: 10px; }
        .comment-form .form-group input { width: 49%; padding: 8px; border: 1px solid #ccc; border-radius: 5px; box-sizing: border-box; }
        .comment-form .submit-button { float: right; padding: 10px 20px; background-color: #28a745; color: white; border: none; border-radius: 5px; cursor: pointer; }
        .comment-list .comment { border-bottom: 1px solid #eee; padding: 15px 0; }
        .comment-list .comment:last-child { border-bottom: none; }
        .comment-list .comment-meta { font-weight: bold; margin-bottom: 5px; }
        .comment-list .comment-meta span.date { font-size: 0.9em; color: #888; font-weight: normal; margin-left: 10px; }
        .comment-list .comment-content { margin-bottom: 10px; }
        .comment-list .comment-reply { margin-left: 30px; background-color: #f8f9fa; padding: 15px; border-left: 3px solid #ddd; }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1 onclick="location.href='/community/main'">익명 커뮤니티</h1>
        </header>

        <div class="list-button-container">
            <a href="/community/main">목록으로</a>
        </div>

        <main class="post-view">
            <div class="post-header">
                <h2>새로운 기술 트렌드에 대한 생각</h2>
                <div class="post-meta">
                    <span>작성자: 익명</span>
                    <span>작성일: 2025.08.22 10:30</span>
                    <span>조회수: 152</span>
                </div>
            </div>

            <div class="post-content">
                <p>여기에 게시글 내용이 표시됩니다.</p>
                <p>최근 AI, 블록체인, 메타버스 등 다양한 기술들이 주목받고 있는데, 여러분들은 어떤 기술이 미래를 주도할 것이라고 생각하시나요?</p>
                <p>자유롭게 의견을 나눠주세요.</p>
            </div>

            <div class="post-actions">
                <button class="like">👍 추천 <span id="like-count">28</span></button>
                <button class="dislike">👎 비추천 <span id="dislike-count">3</span></button>
            </div>
        </main>

        <section class="comment-section">
            <h3>댓글 <span id="comment-count">2</span></h3>
            <div class="comment-form">
                <div class="form-group">
                    <input type="text" id="comment-author" placeholder="닉네임">
                    <input type="password" id="comment-password" placeholder="비밀번호 (수정/삭제용)">
                </div>
                <textarea id="comment-text" placeholder="댓글을 입력하세요"></textarea>
                <button class="submit-button" id="submit-comment">등록</button>
                <div style="clear:both;"></div>
            </div>

            <div class="comment-list">
                <div class="comment">
                    <div class="comment-meta">
                        <span class="author">개발자A</span>
                        <span class="date">2025.08.22 11:05</span>
                    </div>
                    <p class="comment-content">저는 개인적으로 AI의 발전 가능성이 무궁무진하다고 봅니다.</p>
                </div>
                <div class="comment comment-reply">
                    <div class="comment-meta">
                        <span class="author">익명B</span>
                        <span class="date">2025.08.22 11:15</span>
                    </div>
                    <p class="comment-content">@개발자A 저도 동의합니다. 특히 생성형 AI 분야가 기대됩니다.</p>
                </div>
            </div>
        </section>
    </div>

    <script>
        $(document).ready(function() {
            // 댓글 등록 (AJAX)
            $('#submit-comment').click(function() {
                const author = $('#comment-author').val();
                const password = $('#comment-password').val();
                const content = $('#comment-text').val();

                if (!author || !password || !content) {
                    alert('닉네임, 비밀번호, 내용을 모두 입력해주세요.');
                    return;
                }

                // 실제로는 여기에서 AJAX 호출로 서버에 댓글 데이터를 전송합니다.
                console.log("댓글 등록 시도:", { author, password, content });
                alert('댓글이 등록되었습니다. (와이어프레임)');
                // 성공 시 댓글 목록 새로고침 또는 동적 추가
            });

            // 추천/비추천 기능
            $('.post-actions button').click(function() {
                // 서버로 추천/비추천 정보를 전송하고 카운트를 업데이트합니다.
                alert('요청을 보냈습니다. (와이어프레임)');
            });
        });
    </script>
</body>
</html>
