<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>建立球隊</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #0b3d0b;
            font-family: Arial, sans-serif;
            color: white;
            text-align: center;
            font-size: 250%;
        }

        h1 {
            font-size: 60px;
            margin-top: 50px;
            text-shadow: 1px 1px #000;
        }

        table {
            margin: 40px auto;
            width: 60%;
            border-collapse: collapse;
            border: 2px solid white;
        }

        td {
            border: 1px solid white;
            padding: 15px;
            text-align: center;
            font-size: 25px;
        }

        input[type="text"], select {
            width: 95%;
            padding: 6px;
            font-size: 16px;
            text-align: center;
            border-radius: 4px;
            border: none;
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

    <h1>建立球隊</h1>

    <form action="${pageContext.request.contextPath}/team/create" method="post">
        <table>
            <tr>
                <td>球隊名稱</td>
                <td><input type="text" name="teamName" required></td>
            </tr>
            <tr>
                <td>球隊場地</td>
                <td>
                    <select name="place" required>
                        <option value="">請選擇</option>
                        <option value="幸安國小">幸安國小</option>
                        <option value="新生國小">新生國小</option>
                        <option value="延平國中">延平國中</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>是否招募</td>
                <td>
                    <select name="recruit" required>
                        <option value="">請選擇</option>
                        <option value="true">招募</option>
                        <option value="false">停招</option>
                    </select>
                </td>
            </tr>
        </table>

        <div class="button-group">
            <input type="submit" class="btn" value="送出">
            <button type="button" class="btn" onclick="history.back()">回上一頁</button>
            <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/'">回首頁</button>
        </div>
    </form>

</body>
</html>
