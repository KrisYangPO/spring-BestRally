<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>羽球人 Battle Web - 首頁</title>
    <style>
        /* 全域樣式 */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            background: linear-gradient(135deg, #0b3d0b 0%, #1a5c1a 50%, #0b3d0b 100%);
            font-family: 'Arial', 'Microsoft JhengHei', sans-serif;
            color: white;
            min-height: 100vh;
            position: relative;
            overflow-x: hidden;
        }

        /* 羽球場背景裝飾 */
        .court-lines {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            opacity: 0.1;
            z-index: 0;
            background-image: 
                linear-gradient(90deg, transparent 49%, rgba(255,255,255,0.3) 50%, transparent 51%),
                linear-gradient(0deg, transparent 49%, rgba(255,255,255,0.3) 50%, transparent 51%);
            background-size: 100px 100px;
        }

        /* 主標題 */
        .main-title {
            text-align: center;
            font-size: clamp(3.6rem, 9.6vw, 7.2rem);
            font-weight: bold;
            color: white;
            text-shadow: 
                2px 2px 6px rgba(0,0,0,0.9),
                0 0 25px rgba(255,255,255,0.8),
                0 0 50px rgba(255,255,255,0.6),
                0 0 75px rgba(255,255,255,0.4);
            margin: 2rem 0;
            position: relative;
            z-index: 1;
            animation: titleGlow 2s ease-in-out infinite alternate;
        }

        @keyframes titleGlow {
            from { 
                text-shadow: 
                    2px 2px 6px rgba(0,0,0,0.9),
                    0 0 25px rgba(255,255,255,0.8),
                    0 0 50px rgba(255,255,255,0.6),
                    0 0 75px rgba(255,255,255,0.4);
            }
            to { 
                text-shadow: 
                    2px 2px 6px rgba(0,0,0,0.9),
                    0 0 35px rgba(255,255,255,1),
                    0 0 70px rgba(255,255,255,0.8),
                    0 0 100px rgba(255,255,255,0.6);
            }
        }

        /* 主容器 */
        .container {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 2rem;
            position: relative;
            z-index: 1;
            min-height: calc(100vh - 200px);
        }

        /* 功能區塊包裝器 */
        .box-wrapper {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 3rem;
            width: 100%;
            max-width: 1200px;
            padding: 0 1rem;
        }

        /* 功能區塊 */
        .box {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-radius: 15px;
            padding: 3.5rem 2.5rem;
            text-align: center;
            text-decoration: none;
            color: white;
            transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
            position: relative;
            overflow: hidden;
            min-height: 350px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .box::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.1), transparent);
            transition: left 0.5s;
        }

        .box:hover::before {
            left: 100%;
        }

        .box:hover {
            background: rgba(255, 255, 255, 0.2);
            border-color: rgba(255, 255, 255, 0.6);
            transform: translateY(-8px) scale(1.02);
            box-shadow: 
                0 15px 35px rgba(0, 0, 0, 0.3),
                0 0 20px rgba(255, 255, 255, 0.1);
        }

        .box h2 {
            font-size: 2.2rem;
            margin-bottom: 2rem;
            color: #ffffff;
            text-shadow: 
                2px 2px 4px rgba(0,0,0,0.8),
                0 0 10px rgba(255,255,255,0.5);
            font-weight: bold;
            letter-spacing: 1px;
        }

        .box p {
            font-size: 1.3rem;
            line-height: 1.8;
            color: rgba(255, 255, 255, 0.95);
            text-shadow: 
                1px 1px 2px rgba(0,0,0,0.7),
                0 0 5px rgba(255,255,255,0.3);
            font-weight: 500;
        }

        /* 特殊效果 - 用戶主頁 */
        .box.user-home {
            background: linear-gradient(135deg, rgba(255, 87, 34, 0.3), rgba(255, 152, 0, 0.3));
            border-color: rgba(255, 87, 34, 0.7);
        }

        .box.user-home:hover {
            background: linear-gradient(135deg, rgba(255, 87, 34, 0.5), rgba(255, 152, 0, 0.5));
            border-color: rgba(255, 87, 34, 1);
        }

        /* 特殊效果 - 登入註冊 */
        .box.auth {
            background: linear-gradient(135deg, rgba(156, 39, 176, 0.3), rgba(233, 30, 99, 0.3));
            border-color: rgba(156, 39, 176, 0.7);
        }

        .box.auth:hover {
            background: linear-gradient(135deg, rgba(156, 39, 176, 0.5), rgba(233, 30, 99, 0.5));
            border-color: rgba(156, 39, 176, 1);
        }

        /* 特殊效果 - 球隊 */
        .box.team {
            background: linear-gradient(135deg, rgba(0, 188, 212, 0.3), rgba(0, 150, 136, 0.3));
            border-color: rgba(0, 188, 212, 0.7);
        }

        .box.team:hover {
            background: linear-gradient(135deg, rgba(0, 188, 212, 0.5), rgba(0, 150, 136, 0.5));
            border-color: rgba(0, 188, 212, 1);
        }

        /* 特殊效果 - 對戰系統 */
        .box.battle {
            background: linear-gradient(135deg, rgba(255, 193, 7, 0.3), rgba(255, 235, 59, 0.3));
            border-color: rgba(255, 193, 7, 0.7);
        }

        .box.battle:hover {
            background: linear-gradient(135deg, rgba(255, 193, 7, 0.5), rgba(255, 235, 59, 0.5));
            border-color: rgba(255, 193, 7, 1);
        }

        /* 響應式設計 */
        @media (max-width: 768px) {
            .box-wrapper {
                grid-template-columns: 1fr;
                gap: 1.5rem;
            }
            
            .box {
                padding: 3rem 2rem;
                min-height: 280px;
            }
            
            .box h2 {
                font-size: 1.8rem;
                margin-bottom: 1.5rem;
            }
            
            .box p {
                font-size: 1.1rem;
            }
        }

        /* 載入動畫 */
        .box {
            animation: fadeInUp 0.8s ease-out forwards;
            opacity: 0;
            transform: translateY(30px);
        }

        .box:nth-child(1) { animation-delay: 0.2s; }
        .box:nth-child(2) { animation-delay: 0.4s; }
        .box:nth-child(3) { animation-delay: 0.6s; }

        @keyframes fadeInUp {
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* 背景粒子效果 */
        .particle {
            position: fixed;
            width: 10px;
            height: 10px;
            background: rgba(255, 255, 255, 0.3);
            border-radius: 50%;
            pointer-events: none;
            animation: float 6s infinite linear;
        }

        @keyframes float {
            0% {
                transform: translateY(100vh) rotate(0deg);
                opacity: 0;
            }
            10% {
                opacity: 1;
            }
            90% {
                opacity: 1;
            }
            100% {
                transform: translateY(-100vh) rotate(360deg);
                opacity: 0;
            }
        }
    </style>
</head>
<body>
    <!-- 羽球場背景線條 -->
    <div class="court-lines"></div>

    <!-- 主標題 -->
    <h1 class="main-title">羽球人 Battle Web</h1>

    <!-- 三功能區塊 -->
    <div class="container">
        <div class="box-wrapper">
            <!-- 登入狀態下替換區塊 -->
            <c:choose>
                <c:when test="${not empty sessionScope.userCertDTO}">
                    <a href="${pageContext.request.contextPath}/user/home" class="box user-home">
                        <h2>🏠 你的主頁</h2>
                        <p>進入主頁，管理使用者球員身份，球隊資訊。</p>
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/user/login" class="box auth">
                        <h2>🔐 註冊 / 登入</h2>
                        <p>加入我們的羽球社群，開始你的對戰旅程。</p>
                    </a>
                </c:otherwise>
            </c:choose>

            <!-- 球隊功能 -->
            <a href="${pageContext.request.contextPath}/team" class="box team">
                <h2>👥 加入球隊</h2>
                <p>尋找並加入心儀的球隊，或自己創立球隊。</p>
            </a>

            <!-- 對戰系統 -->
            <a href="${pageContext.request.contextPath}/match/teamlist" class="box battle">
                <h2>⚔️ 對戰系統</h2>
                <p>挑戰對手、安排比賽，展現你的實力！</p>
            </a>
        </div>
    </div>

    <!-- 動態背景粒子效果 -->
    <script>
        // 創建浮動粒子效果
        function createParticle() {
            const particle = document.createElement('div');
            particle.className = 'particle';
            particle.style.left = Math.random() * 100 + '%';
            particle.style.animationDuration = (Math.random() * 3 + 3) + 's';
            particle.style.animationDelay = Math.random() * 2 + 's';
            document.body.appendChild(particle);

            // 動畫結束後移除粒子
            setTimeout(() => {
                if (particle.parentNode) {
                    particle.parentNode.removeChild(particle);
                }
            }, 8000);
        }

        // 定期創建粒子
        setInterval(createParticle, 300);
    </script>
</body>
</html>