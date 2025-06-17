<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>對戰系統 - 我的球隊</title>
    <style>
        body {
            background-color: #98b88a;
            font-family: Arial, sans-serif;
            color: white;
            margin: 0;
            padding: 20px;
            font-size: 160%;
            text-align: center;
        }

        .container {
		    display: grid;
		    grid-template-columns: repeat(2, 1fr); /* 每列兩個 */
		    gap: 40px;
		}

        .team-card {
            background-color: #fcdcde;
            color: black;
            border: 8.5px solid white;
            border-radius: 16px;
            padding: 25px;
            text-align: center;
            transition: transform 0.2s;
        }

        .team-card:hover {
            transform: scale(1.02);
            cursor: pointer;
            background-color: #fcc5c8;
        }

        .team-name {
            font-size: 35px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .team-info {
            font-size: 15px;
            margin: 5px 0;
        }

        form {
            margin: 0;
        }

        .team-button {
            all: unset;
            width: 100%;
        }
        
        .button-area {
            text-align: center;
            margin-top: 30px;
        }
        .button-area a {
            display: inline-block;
            padding: 12px 24px;
            margin: 10px;
            font-size: 16px;
            text-decoration: none;
            border-radius: 6px;
            border: 2px solid black;
            color: black;
            background-color: #8fd1eb;
        }
        .home-btn {
            background-color: #4b6ab8;
            color: white;
            border: none;
        }
        .logout-btn {
            background-color: #c97773;
            color: white;
            border: none;
        }
        
    </style>
</head>
<body>
    <h1> ${sessionScope.userCertDTO.username} 已經加入的球隊</h1>

    <div class="container">
        <c:forEach var="team" items="${teamDTOs}">
            <form method="get" action="${pageContext.request.contextPath}/match/teamlist/${team.id}">
                <button type="submit" class="team-button" name="teamId" value="${team.id}">
                    <div class="team-card">
                        <div class="team-name">${team.teamName}</div>
                        <div class="team-info">地區：${team.place}</div>
                        <div class="team-info">等級：${team.avgLevel}</div>
                        <div class="team-info">成員數：${team.teamNum}</div>
                    </div>
                </button>
            </form>
        </c:forEach>
    </div>
    <div class="button-area">
        <a href="${pageContext.request.contextPath}/" class="home-btn">回首頁</a>
    </div>
</body>
</html>
