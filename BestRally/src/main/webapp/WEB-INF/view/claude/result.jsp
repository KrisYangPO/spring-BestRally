<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${resultTitle}</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #0b3d0b;
            font-family: Arial, sans-serif;
            color: white;
            position: relative;
            text-align: center;
        }

        h1 {
            margin-top: 120px;
            font-size: 36px;
            text-shadow: 2px 2px #000;
        }

        .button-links {
            margin-top: 40px;
        }

/*         .button-links a {
            display: inline-block;
            padding: 12px 30px;
            background-color: #b9c9f0;
            color: black;
            text-decoration: none;
            font-weight: bold;
            border-radius: 5px;
            font-size: 18px;
        } */

        .button-links a:hover {
            background-color: #e0e0e0;
            
        }
        
        .button-group {
            margin-top: 30px;
            display: flex;
            justify-content: center;
            gap: 40px;
        }

        .btn {
         	display: inline-block;
            font-size: 30px;
            padding: 10px 20px;
            background-color: #a8b8e0;
            color: black;
            border: 2px solid white;
            cursor: pointer;
            transition: background-color 0.2s;
            border-radius: 5px;
        }

        .btn:hover {
            background-color: #b9c9f0;
        }
        
        
    </style>
</head>
<body>

    <h1>${resultMessage}</h1>

    <div class="button-group">
    	<button type="button" class="btn" onclick="history.back()">回上一頁</button>
        <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/'">回首頁</button>
    </div>

</body>
</html>
