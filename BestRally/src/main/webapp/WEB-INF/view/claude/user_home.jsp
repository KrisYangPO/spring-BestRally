<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>你的個人主頁 - 羽球人 Battle Web</title>
    <style>
        /* 全域樣式 */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            background: linear-gradient(135deg, #0b3d0b 0%, #1a5c1a 30%, #2e7d32 70%, #0b3d0b 100%);
            font-family: 'Segoe UI', 'Microsoft JhengHei', sans-serif;
            color: white;
            min-height: 100vh;
            position: relative;
            overflow-x: hidden;
        }

        /* 背景裝飾效果 */
        body::before {
            content: '';
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-image: 
                radial-gradient(circle at 20% 20%, rgba(255,255,255,0.1) 1px, transparent 1px),
                radial-gradient(circle at 80% 80%, rgba(255,255,255,0.1) 1px, transparent 1px);
            background-size: 100px 100px, 120px 120px;
            z-index: 0;
            pointer-events: none;
        }

        /* 主標題 */
        .main-title {
            text-align: center;
            font-size: clamp(2.5rem, 6vw, 4rem);
            font-weight: bold;
            color: white;
            text-shadow: 
                2px 2px 6px rgba(0,0,0,0.9),
                0 0 25px rgba(255,255,255,0.6),
                0 0 50px rgba(255,255,255,0.4);
            margin: 2rem 0;
            position: relative;
            z-index: 1;
            animation: titleGlow 3s ease-in-out infinite alternate;
        }

        @keyframes titleGlow {
            from { 
                text-shadow: 
                    2px 2px 6px rgba(0,0,0,0.9),
                    0 0 25px rgba(255,255,255,0.6),
                    0 0 50px rgba(255,255,255,0.4);
            }
            to { 
                text-shadow: 
                    2px 2px 6px rgba(0,0,0,0.9),
                    0 0 35px rgba(255,255,255,0.8),
                    0 0 70px rgba(255,255,255,0.6);
            }
        }

        /* 容器 */
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 2rem;
            position: relative;
            z-index: 1;
        }

        /* 按鈕區域 */
        .button-area {
            display: flex;
            justify-content: center;
            gap: 1.5rem;
            margin: 3rem 0;
            flex-wrap: wrap;
        }

        .btn {
            display: inline-block;
            padding: 1rem 2rem;
            font-size: 1.1rem;
            font-weight: 600;
            text-decoration: none;
            border-radius: 12px;
            border: 2px solid transparent;
            color: #4a5568;
            background: linear-gradient(135deg, rgba(226, 232, 240, 0.95), rgba(203, 213, 225, 0.95));
            backdrop-filter: blur(10px);
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            cursor: pointer;
            position: relative;
            overflow: hidden;
            text-shadow: none;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }

        .btn::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
            transition: left 0.5s;
        }

        .btn:hover::before {
            left: 100%;
        }

        .btn:hover {
            background: linear-gradient(135deg, rgba(203, 213, 225, 1), rgba(160, 174, 192, 1));
            border-color: rgba(107, 114, 128, 0.3);
            transform: translateY(-3px) scale(1.02);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }

        /* 區段標題 */
        .section-title {
            text-align: center;
            font-size: 2rem;
            font-weight: bold;
            color: white;
            margin: 3rem 0 2rem 0;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.7);
            position: relative;
        }

        .section-title::after {
            content: '';
            position: absolute;
            bottom: -10px;
            left: 50%;
            transform: translateX(-50%);
            width: 100px;
            height: 3px;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.8), transparent);
            border-radius: 2px;
        }

        /* 表格樣式 */
        .info-table {
            width: 75%;
            max-width: 800px;
            margin: 2rem auto;
            border-collapse: collapse;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(15px);
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 8px 32px rgba(0,0,0,0.3);
            border: 1px solid rgba(255,255,255,0.2);
        }

        .info-table th {
            background: linear-gradient(135deg, rgba(76, 175, 80, 0.8), rgba(56, 142, 60, 0.8));
            color: white;
            padding: 1.5rem 1rem;
            text-align: center;
            font-weight: bold;
            font-size: 1.4rem;
            text-shadow: 1px 1px 2px rgba(0,0,0,0.5);
            white-space: nowrap;
        }

        .info-table td {
            padding: 1.2rem 1rem;
            text-align: center;
            border-bottom: 1px solid rgba(255,255,255,0.1);
            background: rgba(255, 255, 255, 0.95);
            color: #2d3748;
            font-weight: 500;
            font-size: 1.2rem;
            text-shadow: none;
            white-space: nowrap;
        }

        .info-table tr:hover td {
            background: rgba(255,255,255,1);
        }

        /* 用戶頭像特殊樣式 */
        .user-avatar {
            border-radius: 50%;
            border: 4px solid rgba(76, 175, 80, 0.5);
            box-shadow: 0 4px 15px rgba(0,0,0,0.3);
            transition: transform 0.3s ease;
        }

        .user-avatar:hover {
            transform: scale(1.05);
        }

        /* 隊長標籤 */
        .captain-tag {
            background: linear-gradient(135deg, #fbbf24, #f59e0b);
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-weight: bold;
            text-shadow: 1px 1px 2px rgba(0,0,0,0.3);
            box-shadow: 0 2px 8px rgba(251, 191, 36, 0.3);
        }

        /* 管理按鈕區域 */
        .inline-group {
            display: flex;
            align-items: center;
            gap: 0.8rem;
            justify-content: center;
            flex-wrap: nowrap;
        }

        .manage-btn-container {
            position: relative;
            display: inline-block;
        }

        .item-count {
            position: absolute;
            top: -8px;
            right: -8px;
            background: linear-gradient(135deg, #ef4444, #dc2626);
            color: white;
            border-radius: 50%;
            width: 20px;
            height: 20px;
            line-height: 20px;
            text-align: center;
            font-size: 11px;
            font-weight: bold;
            box-shadow: 0 2px 8px rgba(239, 68, 68, 0.4);
            animation: pulse 2s infinite;
            z-index: 1;
        }

        @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.1); }
            100% { transform: scale(1); }
        }

        /* 管理按鈕 */
        .manage-btn {
            padding: 0.6rem 1.2rem;
            font-size: 1.1rem;
            background: linear-gradient(135deg, rgba(219, 234, 254, 0.95), rgba(191, 219, 254, 0.95));
            border: 1px solid rgba(147, 197, 253, 0.3);
            border-radius: 8px;
            color: #1e40af;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-shadow: none;
            box-shadow: 0 3px 10px rgba(59, 130, 246, 0.1);
            white-space: nowrap;
        }

        .manage-btn:hover {
            background: linear-gradient(135deg, rgba(191, 219, 254, 1), rgba(147, 197, 253, 1));
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(59, 130, 246, 0.2);
            border-color: rgba(59, 130, 246, 0.4);
        }

        /* 返回按鈕區域 */
        .button-return {
            display: flex;
            justify-content: center;
            gap: 2rem;
            margin: 4rem 0 2rem 0;
        }

        .home-btn {
            display: inline-block;
            padding: 1.2rem 2.5rem;
            font-size: 1.2rem;
            font-weight: bold;
            text-decoration: none;
            border-radius: 12px;
            color: #1f2937;
            background: linear-gradient(135deg, rgba(249, 250, 251, 0.95), rgba(229, 231, 235, 0.95));
            backdrop-filter: blur(10px);
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            text-shadow: none;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            border: 2px solid transparent;
            position: relative;
            overflow: hidden;
        }

        .home-btn::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
            transition: left 0.5s;
        }

        .home-btn:hover::before {
            left: 100%;
        }

        .home-btn:hover {
            background: linear-gradient(135deg, rgba(229, 231, 235, 1), rgba(209, 213, 219, 1));
            border-color: rgba(156, 163, 175, 0.3);
            transform: translateY(-3px) scale(1.02);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
        }

        /* 狀態標籤 */
        .status-recruiting {
            background: linear-gradient(135deg, #10b981, #059669);
            color: white;
            padding: 0.3rem 0.8rem;
            border-radius: 15px;
            font-size: 0.9rem;
            font-weight: bold;
            text-shadow: 1px 1px 1px rgba(0,0,0,0.3);
        }

        .status-closed {
            background: linear-gradient(135deg, #9e9e9e, #757575);
            color: white;
            padding: 0.3rem 0.8rem;
            border-radius: 15px;
            font-size: 0.9rem;
            font-weight: bold;
            text-shadow: 1px 1px 1px rgba(0,0,0,0.3);
        }

        /* 響應式設計 */
        @media (max-width: 768px) {
            .container {
                padding: 0 1rem;
            }

            .button-area {
                flex-direction: column;
                align-items: center;
                gap: 1rem;
            }

            .btn {
                width: 100%;
                max-width: 300px;
                text-align: center;
            }

            .info-table {
                font-size: 0.9rem;
            }

            .info-table th,
            .info-table td {
                padding: 1rem 0.5rem;
                white-space: normal;
            }

            .inline-group {
                flex-direction: row;
                gap: 0.5rem;
                flex-wrap: wrap;
            }

            .button-return {
                flex-direction: column;
                align-items: center;
                gap: 1rem;
            }

            .home-btn {
                width: 100%;
                max-width: 250px;
                text-align: center;
            }
        }

        /* 載入動畫 */
        .fade-in {
            animation: fadeIn 1s ease-out;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* 表格行動畫 */
        .info-table tr {
            animation: slideInLeft 0.6s ease-out forwards;
            opacity: 0;
        }

        .info-table tr:nth-child(1) { animation-delay: 0.1s; }
        .info-table tr:nth-child(2) { animation-delay: 0.2s; }
        .info-table tr:nth-child(3) { animation-delay: 0.3s; }
        .info-table tr:nth-child(4) { animation-delay: 0.4s; }
        .info-table tr:nth-child(5) { animation-delay: 0.5s; }

        @keyframes slideInLeft {
            from {
                opacity: 0;
                transform: translateX(-30px);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- 主標題 -->
        <h1 class="main-title">🏸 羽球人 個人主頁</h1>
        
        <!-- 功能按鈕區 -->
        <div class="button-area fade-in">
            <a class="btn" href="${pageContext.request.contextPath}/user/home/update">
                👤 更新會員身份
            </a>
            <a class="btn" href="${pageContext.request.contextPath}/player/register">
                🏆 建立/更新球員身份
            </a>
            <a class="btn" href="${pageContext.request.contextPath}/team/create">
                ⚡ 建立球隊
            </a>
        </div>

        <!-- 使用者資訊 -->
        <c:if test="${not empty userCertDTO}">
            <h2 class="section-title">📋 使用者資訊 ${userInfo}</h2>
            <table class="info-table fade-in">
                <tr>
                    <th>使用者頭貼</th>
                    <td>
                        <img class="user-avatar" width="120" height="120" 
                             src='data:image/png;base64,${userCertDTO.photo}' 
                             alt="使用者頭貼">
                    </td>
                </tr>
                <tr>
                    <th>使用者編號</th>
                    <td>${userCertDTO.id}</td>
                </tr>
                <tr>
                    <th>使用者名稱</th>
                    <td>${userCertDTO.username}</td>
                </tr>
                <tr>
                    <th>Email</th>
                    <td>${userCertDTO.email}</td>
                </tr>
            </table>
        </c:if>
        
        <!-- 球員身份資訊 -->
        <c:if test="${not empty playerDTO}">
            <h2 class="section-title">🏸 球員身份資訊</h2>
            <table class="info-table fade-in">
                <tr>
                    <th>球員編號</th>
                    <td>${playerDTO.id}</td>
                </tr>
                <tr>
                    <th>Level</th>
                    <td>
                        <span class="captain-tag">Lv.${playerDTO.level}</span>
                    </td>
                </tr>
            </table>
        </c:if>

        <!-- 球隊資訊 -->
        <c:if test="${not empty teamDTOs}">
            <h2 class="section-title">👥 你所加入的球隊</h2>
            <table class="info-table fade-in">
                <tr>
                    <th>球隊編號</th>
                    <th>球隊名稱</th>
                    <th>球隊人數</th>
                    <th>球隊級數</th>
                    <th>球隊地點</th>
                    <th>招募與否</th>
                    <th>是否為隊長</th>
                </tr>
                <c:forEach var="team" items="${teamDTOs}">
                    <tr>
                        <td>${team.id}</td>
                        <td><strong>${team.teamName}</strong></td>
                        <td>${team.teamNum} 人</td>
                        <td>
                            <span class="captain-tag">Lv.${team.avgLevel}</span>
                        </td>
                        <td>📍 ${team.place}</td>
                        <td>
                            <c:choose>
                                <c:when test="${team.recruit}">
                                    <span class="status-recruiting">✅ 招募中</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-closed">❌ 停招</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <%-- 判斷是否是隊長：建立 isCaptain 標記 --%>
                        <c:set var="isCaptain" value="false" />
                        <c:forEach var="capId" items="${captainTeamIds}">
                            <c:if test="${capId == team.id}">
                                <c:set var="isCaptain" value="true" />
                            </c:if>
                        </c:forEach>

                        <td>
                            <c:choose>
                                <c:when test="${isCaptain}">
                                    <div class="inline-group">
                                        <div class="manage-btn-container">
                                            <c:set var="applyCount" value="${teamApplys[team.id]}" />
                                            <c:if test="${applyCount > 0}">
                                                <span class="item-count">
                                                    <c:out value="${applyCount}" />
                                                </span>
                                            </c:if>
                                            <button type="button" class="manage-btn" 
                                                    onclick="location.href='${pageContext.request.contextPath}/teamplayer/list/${team.id}'">
                                                👔 管理球隊
                                            </button>
                                        </div>
                                        <button type="button" class="manage-btn" 
                                                onclick="location.href='${pageContext.request.contextPath}/team/update/${team.id}'">
                                            ⚙️ 更新資訊
                                        </button>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <span style="color: #6b7280;">否</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <!-- 返回按鈕區 -->
        <div class="button-return fade-in">
            <a href="${pageContext.request.contextPath}/" class="home-btn">
                🏠 回首頁
            </a>
            <a href="${pageContext.request.contextPath}/user/logout" class="home-btn">
                🚪 登出
            </a>
        </div>
    </div>

    <!-- 背景粒子效果 -->
    <script>
        // 創建浮動粒子效果
        function createParticle() {
            const particle = document.createElement('div');
            particle.style.position = 'fixed';
            particle.style.width = '3px';
            particle.style.height = '3px';
            particle.style.background = 'rgba(255, 255, 255, 0.4)';
            particle.style.borderRadius = '50%';
            particle.style.pointerEvents = 'none';
            particle.style.left = Math.random() * 100 + '%';
            particle.style.top = '100vh';
            particle.style.zIndex = '0';
            
            const duration = Math.random() * 4 + 4;
            particle.style.animation = `floatUp ${duration}s linear forwards`;
            
            document.body.appendChild(particle);

            setTimeout(() => {
                if (particle.parentNode) {
                    particle.parentNode.removeChild(particle);
                }
            }, duration * 1000);
        }

        // CSS 動畫定義
        const style = document.createElement('style');
        style.textContent = `
            @keyframes floatUp {
                0% {
                    transform: translateY(0) rotate(0deg);
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
        `;
        document.head.appendChild(style);

        // 定期創建粒子
        setInterval(createParticle, 500);

        // 頁面載入動畫
        window.addEventListener('load', function() {
            document.body.style.opacity = '0';
            document.body.style.transition = 'opacity 0.5s ease-in';
            setTimeout(() => {
                document.body.style.opacity = '1';
            }, 100);
        });
    </script>
</body>
</html>