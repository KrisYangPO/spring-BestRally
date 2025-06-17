<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>羽球人 Battle Web - 首頁</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #0b3d0b;
            font-family: Arial, sans-serif;
            color: white;
            position: relative;
            overflow-x: hidden;
        }
        
        h1 {
            text-align: center;
            font-size: 100px;
            text-shadow: 1px 1px #000;
        }

        .container {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 80px;
            position: relative;
            z-index: 1;
        }

        .box-wrapper {
            display: flex;
            justify-content: space-between;
            width: 70%;
        }

        .box {
            flex: 0 0 30%;
            height: 300px;
            background-color: rgba(255, 255, 255, 0.1);
            border: 4px solid white;
            border-radius: 10px;
            text-align: center;
            padding: 40px 20px;
            box-sizing: border-box;
            transition: background-color 0.3s, transform 0.3s;
            cursor: pointer;
            text-decoration: none;
            color: white;
        }

        .box:hover {
            background-color: rgba(255, 255, 255, 0.2);
            transform: translateY(-5px);
        }

        .box h2 {
            font-size: 24px;
            margin-bottom: 20px;
        }

        .box p {
            font-size: 16px;
        }
    </style>
</head>
<body>

    <!-- 羽球場背景線條 -->
    <div class="court-lines"></div>

    <!-- 標題 -->
    <h1>羽球人 Battle Web</h1>
    
    <!-- 三功能區塊 -->
    <div class="container">
	    <div class="box-wrapper">
	        
	        <!-- 登入狀態下替換區塊 -->
	        <c:choose>
	            <c:when test="${not empty sessionScope.userCertDTO}">
	                <a href="${pageContext.request.contextPath}/user/home" class="box">
	                    <h2>你的主頁</h2>
	                    <p>進入主頁，管理使用者球員身份，球隊資訊。</p>
	                </a>
	            </c:when>
	            <c:otherwise>
	                <a href="${pageContext.request.contextPath}/user/login" class="box">
	                    <h2>註冊 / 登入</h2>
	                    <p>加入我們的羽球社群，開始你的對戰旅程。</p>
	                </a>
	            </c:otherwise>
	        </c:choose>
	        
	        <!-- 另外兩個 box 不變 -->
	        <a href="${pageContext.request.contextPath}/team" class="box">
	            <h2>加入球隊</h2>
	            <p>尋找並加入心儀的球隊，或自己創立球隊。</p>
	        </a>
	        <a href="${pageContext.request.contextPath}/match/teamlist" class="box">
	            <h2>對戰系統</h2>
	            <p>挑戰對手、安排比賽，展現你的實力！</p>
	        </a>
	    </div>
	</div>


</body>
</html>
