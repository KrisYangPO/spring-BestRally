<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Spring Form 表單標籤 -->
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form" %>  

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>修改球隊資訊</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #0b3d0b;
            font-family: Arial, sans-serif;
            color: white;
            text-align: center;
            font-size: 150%;
        }

        h1 {
            font-size: 60px;
            margin-top: 50px;
            text-shadow: 1px 1px #000;
        }

        table {
            margin: 40px auto;
            width: 50%;
            border-collapse: collapse;
            border: 2px solid white;
        }

        td {
            border: 1px solid white;
            padding: 15px;
            text-align: center;
            font-size: 1.7rem;
        }

        input[type="text"], input[type="number"], select {
            width: 90%;
            padding: 7px;
            font-size: 1.5rem;
            text-align: center;
            border-radius: 4px;
            border: 1px solid black;
        }

        .button-group {
            margin-top: 30px;
            display: flex;
            justify-content: center;
            gap: 30px;
        }

        .btn {
            font-size: 1.2rem;
            padding: 10px 20px;
            background-color: white;
            color: black;
            border: 2px solid white;
            border-radius: 10px;
            cursor: pointer;
            transition: background-color 0.2s;
        }

        .btn:hover {
            background-color: #cccccc;
        }
    </style>
</head>
<body>

    <h1>修改球隊資訊</h1>

	<sp:form method="post" action="${pageContext.request.contextPath}/team/update/${teamDTO.id}" modelAttribute="teamDTO">
		<input type = "hidden" name="_method" value="PUT"/>
	    <table>
	        <tr>
	            <td>球隊 ID</td>
	            <td><sp:input path="id" readonly="true" type="number" /></td>
	        </tr>
	        <tr>
	            <td>球隊名稱</td>
	            <td><sp:input path="teamName" required="true" /></td>
	        </tr>
	        <tr>
	            <td>球隊場地</td>
	            <td>
	                <sp:select path="place" required="true">
	                    <sp:option value="幸安國小" label="幸安國小"/>
	                    <sp:option value="新生國小" label="新生國小"/>
	                    <sp:option value="延平國中" label="延平國中"/>
	                </sp:select>
	            </td>
	        </tr>
	        <tr>
	            <td>是否招募</td>
	            <td>
	                <sp:select path="recruit">
	                    <sp:option value="true" label="招募" />
	                    <sp:option value="false" label="停招" />
	                </sp:select>
	            </td>
	        </tr>
	       <tr>
			    <td>目前隊長 ID</td>
			    <td><input type="text" value="${teamDTO.player.id}" readonly /></td>
			</tr>
			<tr>
			    <td>新的隊長 ID</td>
			    <td><input type="number" value="${teamDTO.player.id}" name="playerId" required /></td>
			</tr>
	    </table>
	
	    <div class="button-group">
	        <button type="submit" class="btn">修改</button>
	        <button type="button" class="btn" onclick="history.back()">回上一頁</button>
	        <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/'">回首頁</button>
	    </div>
	</sp:form>


</body>
</html>
