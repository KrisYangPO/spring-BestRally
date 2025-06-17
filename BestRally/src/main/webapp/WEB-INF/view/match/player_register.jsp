<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html>
<head>
    <title>註冊球員身份</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #89ad89; /* 羽球場綠 */
            color: white;
            font-family: Arial, sans-serif;
        }

        .court-container {
            margin: 5% auto;
            width: 60%;
            padding: 2%;
            border: 5px solid rgba(255,255,255,0.6); /* 白線效果 */
            position: relative;
        }

        h2 {
            text-align: center;
            font-size: 200%;
            margin-bottom: 30px;
        }

        form {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        label, select, input[type="submit"] {
            font-size: 18px;
            margin: 10px 0;
        }

        select {
            padding: 5px 10px;
        }

        input[type="submit"] {
            padding: 10px 30px;
            background-color: white;
            color: #0b3d0b;
            border: none;
            cursor: pointer;
        }
       	.button-group {
            margin-top: 30px;
            display: flex;
            justify-content: center;
            gap: 10px;
        }

        .btn {
            font-size: 18px;
            padding: 10px 20px;
            background-color: #fac0c5;
            color: black;
            border: 2px solid white;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.2s;
        }

        .btn:hover {
            background-color: #f5939b;
        }
        
    </style>
</head>
<body>
    <div class="court-container">
        <h2>註冊球員身份</h2>

        <form method="post" action="${pageContext.request.contextPath}/player/register">
            <label for="level">選擇球員等級：</label>
            <select name="level" id="level" required>
                <option value="" disabled selected>請選擇等級</option>
                <c:forEach var="i" begin="1" end="12">
                    <option value="${i}">${i}</option>
                </c:forEach>
            </select>
            <div class="button-group">
				<button class="btn" name="change" value="userPhoto" type="submit">確認註冊/更新</button>
			</div>
            <!-- <input type="submit" value="確認註冊/更新"> -->
        </form>
    </div>
    <div class="button-group">
            <button type="button" class="btn" onclick="window.location.href='${pageContext.request.contextPath}/user/home'">回上一頁</button>
            <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/'">回首頁</button>
    </div>
</body>
</html>
