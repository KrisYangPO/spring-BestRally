<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>更新使用者資訊</title>
    <style>
        body {
            background-color: #0b3d0b;
            font-family: Arial, sans-serif;
            color: white;
            font-size: 120%;
        }
        table {
            border-collapse: collapse;
            margin: 20px auto;
            width: 60%;
        }
        td {
            padding: 20px;
            border: 1px solid white;
        }
        input[type="text"],
        input[type="password"],
        input[type="email"],
        input[type="file"] {
            padding: 10px;
            font-size: 100%;
            border-radius: 8px;
            border: 2px solid #ccc;
            flex: 1;
        }
        
        h2 {
            text-align: center;
            font-size: 150%;
        }
        .inline-group {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .preview-section {
            width: 40%;
            padding: 30px;
            margin: 0 auto;
            text-align: center;
        }
        #preview {
            max-width: 100%;
            max-height: 350px;
            border: 2px solid white;
            border-radius: 12px;
            display: none;
            margin-top: 10px;
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
    <script>
        function previewImage(event) {
            const input = event.target;
            const preview = document.getElementById('preview');
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    preview.src = e.target.result;
                    preview.style.display = 'block';
                };
                reader.readAsDataURL(input.files[0]);
            } else {
                preview.style.display = 'none';
            }
        }
    </script>
</head>
<body>
    <h2>更新使用者資訊</h2>
    <form enctype="multipart/form-data" method="post" action="${pageContext.request.contextPath}/user/home/update">
        <table>
            <tr>
                <td>使用者 ID：</td>
                <td><input type="text" name="userId" value="${userCertDTO.id}" readonly></td>
            </tr>
            <tr>
                <td>使用者名稱：</td>
                <td><input type="text" name="username" value="${userCertDTO.username}"></td>
            </tr>
            <tr>
                <td>密碼：</td>
                <td><input type="password" name="rowPassword" placeholder="輸入新密碼"></td>
            </tr>
            <tr>
                <td>Email：</td>
                <td><input type="email" name="email" value="${userCertDTO.email}"></td>
            </tr>
            <tr>
                <td>大頭照：</td>
                <td>
                    <div class="inline-group">
                        <input type="file" name="photofile" onchange="previewImage(event)">
                    </div>
                </td>
            </tr>
        </table>
        <div class="preview-section">
            <p>照片預覽：</p>
            <img id="preview" alt="照片預覽"/>
        </div>
        <div class="button-group">
            <button type="submit" class="btn">更新資訊</button>
            <button type="button" class="btn" onclick="window.location.href='${pageContext.request.contextPath}/user/home'">回上一頁</button>
            <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/'">回首頁</button>
        </div>
    </form>
</body>
</html>
