<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>你的個人主頁</title>
    <style>
        body {
            background-color: #f0f9ff;
            font-family: "Segoe UI", sans-serif;
            margin: 20px;
            font-size: 120%;
        }
        h1, h2 {
            text-align: center;
            color: #1a3d6d;
        }
        table {
            width: 65%;
            margin: 20px auto;
            border-collapse: collapse;
            background-color: white;
            box-shadow: 0 0 10px #ccc;
        }
        th, td {
            border: 1px solid #999;
            padding: 12px;
            text-align: center;
        }
        th {
            background-color: #4b6ab8;
            color: white;
        }
        .captain-tag {
            color: white;
            background-color: #4b6ab8;
            padding: 5px 10px;
            border-radius: 5px;
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
                
        .button-return  {
            text-align: center;
            margin-top: 30px;
        }
        
        .button-return a {
            display: inline-block;
            padding: 12px 24px;
            margin: 10px;
            font-size: 20px;
            text-decoration: none;
            border-radius: 6px;
            border: 2px solid black;
            color: black;
            background-color: #f2a483;
        }
        
        .home-btn {
            background-color: #f2a483;
            color: black;
            border: none;
        }
        
        .home-btn:hover {
            background-color: #f5c4b0;
        }

        .inline-group {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .btn {
            font-size: 18px;
            padding: 10px 20px;
            background-color: #87ceeb;
            color: black;
            border: 2px solid black;
            border-radius: 8px;
            cursor: pointer;
            transition: background-color 0.2s;
        }

        .btn:hover {
            background-color: #add8e6;
        }
        
    </style>
</head>
<body>

    <h1>羽球人 個人主頁</h1>
    <div class="button-area">
    	<a class = "btn" href="${pageContext.request.contextPath}/user/home/update">更新會員身份</a>
        <a class = "btn" href="${pageContext.request.contextPath}/player/register">建立/更新球員身份</a>
        <a class = "btn" href="${pageContext.request.contextPath}/team/create">建立球隊</a>
    </div>

    <c:if test="${not empty userCertDTO}">
        <h2>${userInfo}</h2>
        <table>
        	<tr><th>使用者頭貼</th><td><img width="300" src='data:image/png;base64,${userCertDTO.photo}'></td></tr>
            <tr><th>使用者編號</th><td>${userCertDTO.id}</td></tr>
            <tr><th>使用者名稱</th><td>${userCertDTO.username}</td></tr>
            <tr><th>Email</th><td>${userCertDTO.email}</td></tr>
        </table>
    </c:if>
    
	
    <c:if test="${not empty playerDTO}">
    <h2>球員身份資訊</h2>
        
        <table>
            <tr><th>球員編號</th><td>${playerDTO.id}</td></tr>
            <tr><th>Level</th><td>${playerDTO.level}</td></tr>
        </table>
    </c:if>

    

    <c:if test="${not empty teamDTOs}">
    <h2>你所加入的球隊</h2>
        <table>
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
                    <td>${team.teamName}</td>
                    <td>${team.teamNum}</td>
                    <td>${team.avgLevel}</td>
                    <td>${team.place}</td>
                    <td>${team.recruit ? "招募中" : "停招"}</td>

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
                            		<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/teamplayer/list/${team.id}'">管理球隊</button>
                            		<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/team/update/${team.id}'">更新球隊資訊</button>
                            	</div>
                            </c:when>
                            <c:otherwise>否</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <div class="button-return">
        <a href="${pageContext.request.contextPath}/" class="home-btn" >回首頁</a>
        <a href="${pageContext.request.contextPath}/user/logout" class="home-btn" >登出</a>
    </div>

</body>
</html>
