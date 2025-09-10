<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>익명 커뮤니티 - 메인</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <style>
        body { font-family: 'Malgun Gothic', sans-serif; margin: 0; background-color: #f4f6f9; color: #333; }
        .container { max-width: 900px; margin: 20px auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        header { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #eee; padding-bottom: 15px; margin-bottom: 20px; }
        header h1 { margin: 0; font-size: 24px; cursor: pointer; }
        header .actions a { text-decoration: none; background-color: #007bff; color: white; padding: 10px 15px; border-radius: 5px; font-weight: bold; }
        .post-list table { width: 100%; border-collapse: collapse; }
        .post-list th, .post-list td { padding: 12px 15px; text-align: left; border-bottom: 1px solid #ddd; }
        .post-list th { background-color: #f8f9fa; }
        .post-list .post-title { text-decoration: none; color: #0056b3; font-weight: 500; }
        .post-list .post-title:hover { text-decoration: underline; }
        .post-list .comment-count { color: #888; font-size: 0.9em; }
        .pagination { text-align: center; margin-top: 30px; }
        .pagination a, .pagination strong { margin: 0 5px; padding: 8px 12px; text-decoration: none; color: #007bff; border: 1px solid #ddd; border-radius: 4px; }
        .pagination strong { background-color: #007bff; color: white; border-color: #007bff; }
        .pagination a:hover { background-color: #f0f0f0; }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1 onclick="location.href='/community/main'">익명 커뮤니티</h1>
            <div class="actions">
                <a href="/community/write">글쓰기</a>
            </div>
        </header>

        <main class="post-list">
            <table>
                <colgroup>
                    <col style="width: 10%;">
                    <col style="width: auto;">
                    <col style="width: 15%;">
                    <col style="width: 10%;">
                </colgroup>
                <thead>
                    <tr>
                        <th>번호</th>
                        <th>제목</th>
                        <th>작성일</th>
                        <th>조회</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- JSTL 또는 스크립틀릿을 사용하여 동적으로 게시글 목록을 채웁니다. --%>
                    <%-- 예시 데이터 --%>
                    <tr>
                        <td>103</td>
                        <td>
                            <a href="/community/post/103" class="post-title">새로운 기술 트렌드에 대한 생각</a>
                            <span class="comment-count">[12]</span>
                        </td>
                        <td>2025.08.22</td>
                        <td>152</td>
                    </tr>
                    <tr>
                        <td>102</td>
                        <td>
                            <a href="/community/post/102" class="post-title">오늘 점심 메뉴 추천 받습니다</a>
                            <span class="comment-count">[5]</span>
                        </td>
                        <td>2025.08.22</td>
                        <td>88</td>
                    </tr>
                    <tr>
                        <td>101</td>
                        <td>
                            <a href="/community/post/101" class="post-title">JSP와 jQuery로 웹사이트 만들기</a>
                            <span class="comment-count">[3]</span>
                        </td>
                        <td>2025.08.21</td>
                        <td>204</td>
                    </tr>
                </tbody>
            </table>
        </main>

        <nav class="pagination">
            <a href="#">&laquo;</a>
            <strong>1</strong>
            <a href="#">2</a>
            <a href="#">3</a>
            <a href="#">4</a>
            <a href="#">5</a>
            <a href="#">&raquo;</a>
        </nav>
    </div>

    <script>
        // jQuery를 사용한 동적 기능 (예: 검색, 필터링 등)을 여기에 구현할 수 있습니다.
        $(document).ready(function() {
            console.log("메인 페이지 와이어프레임 로드 완료");
        });
    </script>
</body>
</html>
