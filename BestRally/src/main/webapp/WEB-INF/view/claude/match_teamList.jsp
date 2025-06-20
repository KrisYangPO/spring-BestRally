<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>對戰系統 - 我的球隊</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: white;
            min-height: 100vh;
            padding: 20px;
            position: relative;
            overflow-x: hidden;
        }

        /* 背景動畫效果 */
        body::before {
            content: '';
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><circle cx="20" cy="20" r="2" fill="rgba(255,255,255,0.1)"/><circle cx="80" cy="80" r="2" fill="rgba(255,255,255,0.1)"/><circle cx="40" cy="60" r="1" fill="rgba(255,255,255,0.1)"/><circle cx="60" cy="40" r="1.5" fill="rgba(255,255,255,0.1)"/></svg>');
            animation: float 20s ease-in-out infinite;
            pointer-events: none;
            z-index: -1;
        }

        @keyframes float {
            0%, 100% { transform: translateY(0px) rotate(0deg); }
            50% { transform: translateY(-20px) rotate(180deg); }
        }

        .page-header {
            text-align: center;
            margin-bottom: 40px;
            animation: fadeInDown 0.8s ease-out;
        }

        .page-title {
            background: linear-gradient(45deg, #ff6b6b, #feca57, #48dbfb, #ff9ff3);
            background-size: 400% 400%;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            animation: gradientShift 3s ease infinite;
            font-size: 2.5rem;
            font-weight: 800;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }

        @keyframes gradientShift {
            0%, 100% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
        }

        .welcome-text {
            font-size: 1.2rem;
            opacity: 0.9;
            margin-bottom: 10px;
        }

        .user-badge {
            display: inline-block;
            background: rgba(255, 255, 255, 0.2);
            backdrop-filter: blur(10px);
            padding: 8px 20px;
            border-radius: 25px;
            border: 1px solid rgba(255, 255, 255, 0.3);
            font-weight: 600;
        }

        .container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
            gap: 30px;
            max-width: 1200px;
            margin: 0 auto;
            animation: fadeInUp 1s ease-out 0.3s both;
        }

        .team-card {
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.25) 0%, rgba(255, 255, 255, 0.1) 100%);
            backdrop-filter: blur(20px);
            border: 2px solid rgba(255, 255, 255, 0.4);
            border-radius: 20px;
            padding: 30px;
            text-align: center;
            transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            position: relative;
            overflow: hidden;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15), 
                        0 0 0 1px rgba(255, 255, 255, 0.1) inset;
        }

        .team-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.1), transparent);
            transition: left 0.5s;
        }

        .team-card:hover::before {
            left: 100%;
        }

        .team-card:hover {
            transform: translateY(-10px) scale(1.02);
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.35) 0%, rgba(255, 255, 255, 0.2) 100%);
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.25), 
                        0 0 0 2px rgba(255, 255, 255, 0.3) inset,
                        0 0 20px rgba(255, 255, 255, 0.1);
            border-color: rgba(255, 255, 255, 0.6);
        }

        .team-icon {
            font-size: 3rem;
            margin-bottom: 15px;
            background: linear-gradient(45deg, #ff6b6b, #4ecdc4);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .team-name {
            font-size: 1.8rem;
            font-weight: 700;
            margin-bottom: 20px;
            color: #fff;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.5), 
                         0 0 10px rgba(255,255,255,0.3);
        }

        .team-info {
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1rem;
            margin: 12px 0;
            opacity: 0.95;
            transition: opacity 0.3s;
            color: #f8f9fa;
            text-shadow: 1px 1px 2px rgba(0,0,0,0.4);
            font-weight: 500;
        }

        .team-info i {
            margin-right: 10px;
            font-size: 1.1rem;
            color: #ffd700;
            text-shadow: 0 0 5px rgba(255, 215, 0, 0.5);
        }

        .team-card:hover .team-info {
            opacity: 1;
        }

        form {
            margin: 0;
        }

        .team-button {
            all: unset;
            width: 100%;
            cursor: pointer;
        }

        .button-area {
            text-align: center;
            margin-top: 50px;
            animation: fadeInUp 1s ease-out 0.6s both;
        }

        .nav-button {
            display: inline-block;
            padding: 15px 30px;
            margin: 10px;
            font-size: 1.1rem;
            text-decoration: none;
            border-radius: 50px;
            border: 2px solid transparent;
            font-weight: 600;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
            backdrop-filter: blur(10px);
        }

        .nav-button::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
            transition: left 0.5s;
        }

        .nav-button:hover::before {
            left: 100%;
        }

        .home-btn {
            background: rgba(75, 106, 184, 0.3);
            color: white;
            border-color: #4b6ab8;
        }

        .home-btn:hover {
            background: #4b6ab8;
            transform: translateY(-3px);
            box-shadow: 0 10px 20px rgba(75, 106, 184, 0.3);
        }

        .logout-btn {
            background: rgba(201, 119, 115, 0.3);
            color: white;
            border-color: #c97773;
        }

        .logout-btn:hover {
            background: #c97773;
            transform: translateY(-3px);
            box-shadow: 0 10px 20px rgba(201, 119, 115, 0.3);
        }

        /* 動畫效果 */
        @keyframes fadeInDown {
            from {
                opacity: 0;
                transform: translateY(-30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* 響應式設計 */
        @media (max-width: 768px) {
            .container {
                grid-template-columns: 1fr;
                gap: 20px;
            }
            
            .page-title {
                font-size: 2rem;
            }
            
            .team-card {
                padding: 20px;
            }
            
            .nav-button {
                padding: 12px 24px;
                font-size: 1rem;
            }
        }

        /* 空狀態樣式 */
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            opacity: 0.8;
        }

        .empty-state i {
            font-size: 4rem;
            margin-bottom: 20px;
            color: #feca57;
        }

        .empty-state h3 {
            font-size: 1.5rem;
            margin-bottom: 10px;
        }

        .empty-state p {
            font-size: 1.1rem;
            opacity: 0.7;
        }
    </style>
</head>
<body>
    <div class="page-header">
        <h1 class="page-title">
            <i class="fas fa-trophy"></i> 對戰系統
        </h1>
        <p class="welcome-text">歡迎回來！</p>
        <div class="user-badge">
            <i class="fas fa-user"></i>
            ${sessionScope.userCertDTO.username}
        </div>
    </div>

    <c:choose>
        <c:when test="${not empty teamDTOs}">
            <div class="container">
                <c:forEach var="team" items="${teamDTOs}" varStatus="status">
                    <form method="get" action="${pageContext.request.contextPath}/match/teamlist/${team.id}">
                        <button type="submit" class="team-button" name="teamId" value="${team.id}">
                            <div class="team-card">
                                <div class="team-icon">
                                    <i class="fas fa-users"></i>
                                </div>
                                <div class="team-name">${team.teamName}</div>
                                <div class="team-info">
                                    <i class="fas fa-map-marker-alt"></i>
                                    地區：${team.place}
                                </div>
                                <div class="team-info">
                                    <i class="fas fa-star"></i>
                                    等級：${team.avgLevel}
                                </div>
                                <div class="team-info">
                                    <i class="fas fa-user-friends"></i>
                                    成員數：${team.teamNum}
                                </div>
                            </div>
                        </button>
                    </form>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <i class="fas fa-basketball-ball"></i>
                <h3>還沒有加入任何球隊</h3>
                <p>趕快去尋找並加入你的第一支球隊吧！</p>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="button-area">
        <a href="${pageContext.request.contextPath}/" class="nav-button home-btn">
            <i class="fas fa-home"></i> 回首頁
        </a>
        <a href="${pageContext.request.contextPath}/logout" class="nav-button logout-btn">
            <i class="fas fa-sign-out-alt"></i> 登出
        </a>
    </div>

    <script>
        // 添加一些互動效果
        document.addEventListener('DOMContentLoaded', function() {
            const teamCards = document.querySelectorAll('.team-card');
            
            teamCards.forEach((card, index) => {
                // 錯開動畫時間
                card.style.animationDelay = `${index * 0.1}s`;
                
                // 滑鼠移入效果
                card.addEventListener('mouseenter', function() {
                    this.style.transform = 'translateY(-10px) scale(1.02) rotate(1deg)';
                });
                
                card.addEventListener('mouseleave', function() {
                    this.style.transform = 'translateY(0) scale(1) rotate(0deg)';
                });
            });
            
            // 按鈕點擊效果
            const buttons = document.querySelectorAll('.nav-button');
            buttons.forEach(button => {
                button.addEventListener('click', function(e) {
                    this.style.transform = 'scale(0.95)';
                    setTimeout(() => {
                        this.style.transform = '';
                    }, 150);
                });
            });
        });
    </script>
</body>
</html>