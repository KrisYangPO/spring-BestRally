<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>羽球對戰排行榜</title>
    <style>
        body {
		    margin: 0;
		    background: linear-gradient(to bottom, #e9fef4, #ccf5e6);
		    font-family: "Segoe UI", sans-serif;
		    background-size: 80px;
		}
		
		h2 {
		    color: #0b3d0b;
		    text-align: center;
		    margin-top: 40px;
		    font-size: 2.4em;
		    letter-spacing: 1px;
		    position: relative;
		}

        table {
            width: 80%;
            margin: 30px auto;
            border-collapse: collapse;
            background-color: white;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            font-size: 160%;
            overflow: hidden;
        }

        th, td {
            border-bottom: 1px solid #ddd;
            padding: 12px;
            text-align: center;
        }

        th {
            background-color: #0b3d0b;
            color: white;
            font-size: 1.1em;
        }

        tr {
            opacity: 0;
            transform: translateY(20px);
            animation: slideUp 0.5s ease-out forwards;
        }

        tr:nth-child(n+2) {
            animation-delay: calc(0.1s * var(--i));
        }

        tr:hover {
            background-color: #f5fff9;
        }

        .rank-1 {
            background-color: #fff7e6 !important;
            font-weight: bold;
        }

        .rank-2 {
            background-color: #f0f8ff !important;
            font-weight: bold;
        }

        .rank-3 {
            background-color: #f5f5dc !important;
            font-weight: bold;
        }

        .trophy {
            font-size: 1.2em;
        }

        .progress-container {
            background-color: #eee;
            border-radius: 20px;
            height: 14px;
            width: 80%;
            margin: auto;
            overflow: hidden;
        }

        .progress-bar {
            background-color: #0b3d0b;
            height: 100%;
            width: 0;
            transition: width 1.5s ease;
        }

        @keyframes slideUp {
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
    </style>
</head>
<body>
    <h2>🏸 羽球對戰排行榜 🏆</h2>

    <table>
		<tr>
		    <th>🏆 排名</th>
		    <th>👤 球員名稱</th>
		    <th>📈 等級</th>
		    <th>✅ 勝場</th>
		    <th>📊 總場數</th>
		    <th>🎯 勝率 (%)</th>
		    <th>📶 勝率條</th>
		</tr>

        <c:forEach var="player" items="${matchPlayers}" varStatus="status">
            <c:set var="winRate" value="${player.totalMatch == 0 ? 0 : (player.winGame * 100.0 / player.totalMatch)}" />
            <tr style="--i:${status.index}" class="${status.index == 0 ? 'rank-1' : status.index == 1 ? 'rank-2' : status.index == 2 ? 'rank-3' : ''}">
                <td>
                    ${status.index + 1}
                </td>
                <td>${player.userName}</td>
                <td>${player.playerLevel}</td>
                <td>${player.winGame}</td>
                <td>${player.totalMatch}</td>
                <td>${fn:substring(winRate, 0, 5)}</td>
                <td>
                    <div class="progress-container">
                        <div class="progress-bar" style="width: ${winRate}%;"></div>
                    </div>
                </td>
            </tr>
        </c:forEach>
    </table>

    <script>
        // 動畫延遲處理在 tr 上已做 CSS，但也可強制刷新條形圖動畫（載入後）
        window.addEventListener('load', () => {
            const bars = document.querySelectorAll('.progress-bar');
            bars.forEach(bar => {
                const width = bar.style.width;
                bar.style.width = '0';
                setTimeout(() => {
                    bar.style.width = width;
                }, 100);
            });
        });
    </script>
</body>
</html>
