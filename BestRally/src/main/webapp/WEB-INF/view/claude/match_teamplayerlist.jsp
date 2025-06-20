<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>球隊對戰系統</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #2563eb;
            --primary-dark: #1d4ed8;
            --secondary-color: #10b981;
            --secondary-dark: #059669;
            --danger-color: #ef4444;
            --danger-dark: #dc2626;
            --warning-color: #f59e0b;
            --warning-dark: #d97706;
            --success-color: #22c55e;
            --success-dark: #16a34a;
            --background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            --card-shadow: 0 10px 25px rgba(0,0,0,0.1);
            --card-shadow-hover: 0 15px 35px rgba(0,0,0,0.15);
            --border-radius: 16px;
            --transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            background: var(--background);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            min-height: 100vh;
            padding: 20px;
            font-size: 150%;
            line-height: 1.6;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .page-header {
            text-align: center;
            margin-bottom: 40px;
            animation: fadeInDown 0.8s ease-out;
        }

        .page-title {
            color: white;
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }

        .page-subtitle {
            color: rgba(255,255,255,0.9);
            font-size: 1.1rem;
            font-weight: 300;
        }

        .section {
            background: white;
            border-radius: var(--border-radius);
            box-shadow: var(--card-shadow);
            margin-bottom: 30px;
            overflow: hidden;
            transition: var(--transition);
            animation: fadeInUp 0.8s ease-out;
        }

        .section:hover {
            box-shadow: var(--card-shadow-hover);
            transform: translateY(-2px);
        }

        .section-header {
            background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
            color: white;
            padding: 20px 30px;
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .section-header h2 {
            font-size: 1.5rem;
            font-weight: 600;
        }

        .section-header .icon {
            font-size: 1.3rem;
        }

        .section-content {
            padding: 30px;
        }

        /* 表格樣式 */
        .table-container {
            overflow-x: auto;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
        }

        th {
            background: linear-gradient(135deg, #1f2937, #374151);
            color: white;
            padding: 18px 15px;
            text-align: center;
            font-weight: 600;
            font-size: 0.95rem;
            letter-spacing: 0.5px;
            border: none;
        }

        td {
            padding: 16px 15px;
            text-align: center;
            border-bottom: 1px solid #f1f5f9;
            transition: var(--transition);
        }

        tr:hover td {
            background-color: #f8fafc;
            transform: scale(1.01);
        }

        tr:last-child td {
            border-bottom: none;
        }

        /* 按鈕樣式 */
        .btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 12px 24px;
            border: none;
            border-radius: 10px;
            font-size: 1rem;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
            transition: var(--transition);
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            position: relative;
            overflow: hidden;
        }

        .btn::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
            transition: left 0.5s;
        }

        .btn:hover::before {
            left: 100%;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 15px rgba(0,0,0,0.15);
        }

        .btn:active {
            transform: translateY(0);
        }

        .btn-primary {
            background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
            color: white;
        }

        .btn-success {
            background: linear-gradient(135deg, var(--success-color), var(--success-dark));
            color: white;
        }

        .btn-danger {
            background: linear-gradient(135deg, var(--danger-color), var(--danger-dark));
            color: white;
        }

        .btn-warning {
            background: linear-gradient(135deg, var(--warning-color), var(--warning-dark));
            color: white;
        }

        .btn-secondary {
            background: linear-gradient(135deg, #6b7280, #4b5563);
            color: white;
        }

        /* 表單樣式 */
        .form-container {
            background: #f8fafc;
            border-radius: 12px;
            padding: 25px;
            margin-top: 20px;
            border: 2px solid #e2e8f0;
        }

        .form-group {
            display: flex;
            align-items: center;
            gap: 15px;
            flex-wrap: wrap;
            justify-content: center;
        }

        .form-group label {
            font-weight: 600;
            color: #374151;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .form-control {
            padding: 12px 16px;
            border: 2px solid #e2e8f0;
            border-radius: 8px;
            font-size: 1.5rem;
            transition: var(--transition);
            min-width: 200px;
        }

        .form-control:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
        }

        .manual-form {
            display: none;
            flex-direction: column;
            gap: 20px;
            align-items: center;
            animation: slideDown 0.3s ease-out;
        }

        .manual-form.show {
            display: flex;
        }

        .select-group {
        	grid-template-columns: repeat(2, 1fr);
            display: grid;
            gap: 30px;
            width: 70%;
            max-width: 800px;
        }

        .select-wrapper {
            position: relative;
        }

        .select-wrapper select {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e2e8f0;
            border-radius: 8px;
            font-size: 1rem;
            background: white;
            cursor: pointer;
            transition: var(--transition);
        }

        .select-wrapper select:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
        }

        /* 按鈕組 */
        .button-group {
            display: flex;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
            margin: 20px 0;
        }

        /* 統計卡片 */
        .stats-card {
            background: linear-gradient(135deg, #3b82f6, #1d4ed8);
            color: white;
            padding: 20px;
            border-radius: 12px;
            text-align: center;
            margin-bottom: 20px;
            box-shadow: var(--card-shadow);
        }

        .stats-number {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 5px;
        }

        .stats-label {
            font-size: 0.9rem;
            opacity: 0.9;
        }

        /* 單選按鈕美化 */
        .radio-group {
            display: flex;
            gap: 15px;
            justify-content: center;
        }

        .radio-option {
            position: relative;
        }

        .radio-option input[type="radio"] {
            display: none;
        }

        .radio-option label {
            display: flex;
            align-items: center;
            gap: 8px;
            padding: 8px 16px;
            border: 2px solid #e2e8f0;
            border-radius: 25px;
            cursor: pointer;
            transition: var(--transition);
            font-weight: 500;
        }

        .radio-option input[type="radio"]:checked + label {
            background: var(--success-color);
            color: white;
            border-color: var(--success-color);
        }

        .radio-option:last-child input[type="radio"]:checked + label {
            background: var(--danger-color);
            border-color: var(--danger-color);
        }

        /* 對戰組別標題 */
        .battle-group-header {
            background: linear-gradient(135deg, #f59e0b, #d97706);
            color: white;
            padding: 15px;
            text-align: center;
            font-weight: 600;
            font-size: 1.1rem;
            margin-bottom: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }

        /* 動畫 */
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

/*         @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        } */

        @keyframes slideDown {
            from {
                opacity: 0;
                max-height: 0;
            }
            to {
                opacity: 1;
                max-height: 500px;
            }
        }

        /* 響應式設計 */
        @media (max-width: 768px) {
            .page-title {
                font-size: 2rem;
            }
            
            .section-content {
                padding: 20px;
            }
            
            .btn {
                padding: 10px 18px;
                font-size: 0.9rem;
            }
            
            .button-group {
                flex-direction: column;
                align-items: center;
            }
            
            .form-group {
                flex-direction: column;
                align-items: stretch;
            }
            
            .select-group {
                grid-template-columns: 1fr;
            }
        }

        /* 成功/失敗圖標 */
        .result-icon {
            margin-right: 5px;
        }

        .win-icon {
            color: var(--success-color);
        }

        .lose-icon {
            color: var(--danger-color);
        }

        /* 球員等級徽章 */
        .level-badge {
            display: inline-block;
            padding: 4px 12px;
            background: linear-gradient(135deg, #8b5cf6, #7c3aed);
            color: white;
            border-radius: 20px;
            font-size: 1.3rem;
            font-weight: 600;
        }

        /* 禁用按鈕樣式 */
        .btn-disabled {
            background: linear-gradient(135deg, #9ca3af, #6b7280) !important;
            color: #d1d5db !important;
            cursor: not-allowed !important;
            pointer-events: none;
            opacity: 0.6;
        }

        .btn-disabled:hover {
            transform: none !important;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1) !important;
        }

        .btn-disabled::before {
            display: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="page-header">
            <h1 class="page-title">
                <i class="fas fa-trophy"></i>
                球隊對戰系統
            </h1>
            <p class="page-subtitle">專業的球隊管理與對戰平台</p>
        </div>

        <!-- 球隊成員列表 -->
        <div class="section">
            <div class="section-header">
                <i class="fas fa-users icon"></i>
                <h2>球隊成員列表</h2>
            </div>
            <div class="section-content">
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th><i class="fas fa-id-card"></i> 球員 ID</th>
                                <th><i class="fas fa-user"></i> 球員名稱</th>
                                <th><i class="fas fa-star"></i> 球員等級</th>
                                <th><i class="fas fa-plus-circle"></i> 操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="player" items="${playerOfTeamDTO.players}">
                            <tr id="${player.id}">
                                <td><strong>#${player.id}</strong></td>
                                <td>${player.user.username}</td>
                                <td><span class="level-badge">Lv.${player.level}</span></td>
                                <td>
                                    <c:set var="isInMatch" value="false" />
                                    <c:forEach var="matchPlayer" items="${sessionScope.matchPlayers}">
                                        <c:if test="${matchPlayer.playerId == player.id}">
                                            <c:set var="isInMatch" value="true" />
                                        </c:if>
                                    </c:forEach>
                                    
                                    <c:choose>
                                        <c:when test="${isInMatch}">
                                            <span class="btn btn-disabled">
                                                <i class="fas fa-check"></i>
                                                已加入對戰
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/match/teamplayers/add/${playerOfTeamDTO.teamDTO.id}?playerId=${player.id}" 
                                               class="btn btn-primary">
                                                <i class="fas fa-plus"></i>
                                                加入對戰
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- 對戰名單 -->
        <c:if test="${not empty sessionScope.matchPlayers}">
        <div class="section">
            <div class="section-header">
                <i class="fas fa-list-ul icon"></i>
                <h2>對戰名單</h2>
                <div class="stats-card" style="margin: 0; padding: 10px 20px; background: rgba(255,255,255,0.2);">
                    <span class="stats-number" style="font-size: 1.2rem;">${fn:length(sessionScope.matchPlayers)}</span>
                    <span class="stats-label" style="font-size: 0.8rem;">參戰人數</span>
                </div>
            </div>
            <div class="section-content">
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th><i class="fas fa-user"></i> 球員名稱</th>
                                <th><i class="fas fa-star"></i> 球員級數</th>
                                <th><i class="fas fa-trophy"></i> 球員勝場</th>
                                <th><i class="fas fa-chart-line"></i> 目前場數</th>
                                <th><i class="fas fa-times-circle"></i> 操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="player" items="${sessionScope.matchPlayers}">
                            <tr id="removePlayer">
                                <td><strong>${player.userName}</strong></td>
                                <td><span class="level-badge">Lv.${player.playerLevel}</span></td>
                                <td><i class="fas fa-trophy win-icon"></i>${player.winGame}</td>
                                <td>${player.totalMatch}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/match/teamplayers/remove/${teamId}?playerId=${player.playerId}" 
                                       class="btn btn-danger">
                                        <i class="fas fa-times"></i>
                                        退出
                                    </a>
                                </td>
                            </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- 操作按鈕 -->
                <div class="button-group">
                    <a href="${pageContext.request.contextPath}/match/teamplayers/remove/${teamId}?playerId=0000" 
                       class="btn btn-warning">
                        <i class="fas fa-redo"></i>
                        重新安排
                    </a>
                    <button class="btn btn-secondary" type="button" onclick="toggleManualForm()">
                        <i class="fas fa-cog"></i>
                        手動編排
                    </button>
                </div>

                <!-- 手動編排表單 -->
                <div class="form-container">
                    <form action="${pageContext.request.contextPath}/match/arrange/manual/${teamId}" 
                          method="post" id="manualForm" class="manual-form">
                        <h3 style="color: #374151; margin-bottom: 20px;">
                            <i class="fas fa-users-cog"></i>
                            手動選擇對戰球員
                        </h3>
                        <div class="select-group">
                            <div class="select-wrapper">
                                <label>第一位球員：</label>
                                <select name="players" class="form-control">
                                    <c:forEach var="player" items="${sessionScope.matchPlayers}">
                                        <option value="${player.userName}">${player.userName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="select-wrapper">
                                <label>第二位球員：</label>
                                <select name="players" class="form-control">
                                    <c:forEach var="player" items="${sessionScope.matchPlayers}">
                                        <option value="${player.userName}">${player.userName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="select-wrapper">
                                <label>第三位球員：</label>
                                <select name="players" class="form-control">
                                    <c:forEach var="player" items="${sessionScope.matchPlayers}">
                                        <option value="${player.userName}">${player.userName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="select-wrapper">
                                <label>第四位球員：</label>
                                <select name="players" class="form-control">
                                    <c:forEach var="player" items="${sessionScope.matchPlayers}">
                                        <option value="${player.userName}">${player.userName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-play"></i>
                            建立對戰
                        </button>
                    </form>
                </div>

                <!-- 自動編排表單 -->
                <div class="form-container" id="arrange">
                    <form action="${pageContext.request.contextPath}/match/arrange/${teamId}" method="post">
                        <div class="form-group">
                            <label>
                                <i class="fas fa-balance-scale"></i>
                                程度差距容忍度：
                            </label>
                            <input type="number" name="threshold" class="form-control" 
                                   placeholder="請輸入 threshold 值" required />
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-magic"></i>
                                自動編排
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        </c:if>

        <!-- 開始對戰 -->
        <c:if test="${not empty sessionScope.battlePlayers}">
        <div class="section">
            <div class="section-header">
                <i class="fas fa-fire icon"></i>
                <h2>開始對戰！</h2>
            </div>
            <div class="section-content">
                <c:forEach var="entry" items="${sessionScope.battlePlayers}">
                <div class="battle-group-header">
                    <i class="fas fa-sword"></i>
                    對戰組別 ${entry.key}
                </div>
                
                <form action="${pageContext.request.contextPath}/match/record/${teamId}" method="post">
                    <input type="hidden" name="groupNum" value="${entry.key}" />
                    <input type="hidden" name="teamId" value="${teamId}" />
                    
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th><i class="fas fa-users"></i> 組別</th>
                                    <th><i class="fas fa-user"></i> 球員名稱</th>
                                    <th><i class="fas fa-star"></i> 球員等級</th>
                                    <th><i class="fas fa-medal"></i> 勝負結果</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="player" items="${entry.value}">
                                <tr id="reset_${entry.key}">
                                    <td><strong>${entry.key}</strong></td>
                                    <td><strong>${player.userName}</strong></td>
                                    <td><span class="level-badge">Lv.${player.playerLevel}</span></td>
                                    <td>
                                        <div class="radio-group">
                                            <div class="radio-option">
                                                <input type="radio" name="result_${player.playerId}" 
                                                       value="1" id="win_${player.playerId}"/>
                                                <label for="win_${player.playerId}">
                                                    <i class="fas fa-trophy result-icon win-icon"></i>
                                                    勝
                                                </label>
                                            </div>
                                            <div class="radio-option">
                                                <input type="radio" name="result_${player.playerId}" 
                                                       value="0" id="lose_${player.playerId}"/>
                                                <label for="lose_${player.playerId}">
                                                    <i class="fas fa-times result-icon lose-icon"></i>
                                                    敗
                                                </label>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                </c:forEach>
                                <tr id="manual">
                                    <td colspan="4">
                                        <div class="button-group">
                                            <button class="btn btn-success" type="submit">
                                                <i class="fas fa-check"></i>
                                                送出結果
                                            </button>
                                            <a class="btn btn-warning" 
                                               href="${pageContext.request.contextPath}/match/arrange/${teamId}?resetGroupNum=${entry.key}">
                                                <i class="fas fa-redo"></i>
                                                再次重排
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
                </c:forEach>
            </div>
        </div>
        </c:if>

        <!-- 底部按鈕 -->
        <div class="section">
            <div class="section-content">
                <div class="button-group">
                    <a href="${pageContext.request.contextPath}/match/close/${teamId}" class="btn btn-danger">
                        <i class="fas fa-stop"></i>
                        結束今日對戰
                    </a>
                    <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                        <i class="fas fa-home"></i>
                        回首頁
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script>
        function toggleManualForm() {
            const form = document.getElementById("manualForm");
            form.classList.toggle("show");
        }

        // 添加表格行的點擊效果
        document.addEventListener('DOMContentLoaded', function() {
            const rows = document.querySelectorAll('tbody tr');
            rows.forEach(row => {
                row.addEventListener('click', function() {
                    this.style.background = '#f0f9ff';
                    setTimeout(() => {
                        this.style.background = '';
                    }, 200);
                });
            });
        });
    </script>
</body>
</html>