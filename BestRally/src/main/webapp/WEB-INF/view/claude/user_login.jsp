<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>羽球人登入</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #0b3d0b;
            font-family: Arial, sans-serif;
            color: white;
            position: relative;
            overflow: hidden;
        }

        h1 {
            text-align: center;
            margin-top: 80px;
            font-size: 36px;
            text-shadow: 2px 2px #000;
        }

        .login-container {
            width: 400px;
            margin: 30px auto;
            padding: 30px;
            background-color: rgba(255, 255, 255, 0.1);
            border: 3px solid white;
            border-radius: 10px;
            text-align: center;
            z-index: 1;
            position: relative;
        }

        .login-container label {
            display: block;
            text-align: left;
            margin: 15px 0 5px;
            font-size: 16px;
        }

        .login-container input[type="text"],
        .login-container input[type="password"] {
            width: 100%;
            padding: 10px;
            border-radius: 5px;
            border: none;
            font-size: 16px;
        }

        .login-container input[type="submit"] {
            margin-top: 25px;
            padding: 12px 30px;
            font-size: 18px;
            background-color: white;
            color: #0b3d0b;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
        }

        .login-container input[type="submit"]:hover {
            background-color: #e0e0e0;
        }

        .button-links {
            margin-top: 20px;
            display: flex;
            justify-content: center;
            gap: 20px;
        }

        .button-links a {
            display: inline-block;
            padding: 10px 20px;
            background-color: #b9c9f0;
            color: black;
            text-decoration: none;
            font-weight: bold;
            border-radius: 5px;
        }

        .button-links a:hover {
            background-color: #a8b8e0;
        }

    </style>
</head>
<body>

    <div class="court-lines"></div>

    <h1>羽球人登入</h1>

    <div class="login-container">
        <form method="post" action="${pageContext.request.contextPath}/user/login">
            <label for="username">使用者帳號：</label>
            <input type="text" id="username" name="username" required />

            <label for="password">使用者密碼：</label>
            <input type="password" id="password" name="password" required />

            <input type="submit" value="登入" />
        </form>

        <div class="button-links">
            <a href="${pageContext.request.contextPath}/user/register">前往註冊</a>
            <a href="${pageContext.request.contextPath}/">回首頁</a>
        </div>
    </div>

</body>
</html>
