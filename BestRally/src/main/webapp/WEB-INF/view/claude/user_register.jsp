<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>使用者註冊</title>
    <style>
        body {
            margin: 0;
            font-size: 150%;
            font-family: Arial, sans-serif;
            background-color: #4e854e;
            color: white;
        }

        /* 加入頁面上方的 padding，避免頂部格線緊貼 */
        .page-wrapper {
            padding-top: 5vh;
        }

        h2 {
            margin: 0;
            padding: 20px;
            text-align: center;
            font-size: 157.5%; /* 預設是 150% 的基準字，加大 5% */
            border-top: 20px solid rgba(255,255,255,0.6); /* 上方格線 */
            border-bottom: 20px solid rgba(255,255,255,0.6); /* 下方格線 */
        }

        .container {
            display: flex;
            padding: 30px 5%;
            position: relative;
        }

        .form-section {
            width: 30%;
            padding: 20px;
        }

        .form-section label {
            display: block;
            margin-top: 15px;
        }

        .form-section input[type="text"],
        .form-section input[type="password"],
        .form-section input[type="file"] {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            border: none;
            border-radius: 4px;
        }

        .form-section input[type="submit"] {
            margin-top: 20px;
            padding: 10px 20px;
            background-color: white;
            color: #1b4721;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

		.preview-section {
		    width: 40%;
		    padding: 20px;
		    margin-left: 30%; /* 右移 70% */
		}
		
		#preview {
		    max-width: 100%;
		    max-height: 350px;
		    border: 2px solid white;
		    border-radius: 8px;
		    display: block;
		    margin-top: 10px;
		}

        /* 中間垂直白線：置中、加粗、透明 */
        .divider {
            width: 20px;
            background-color: rgba(255,255,255,0.6);
            height: 100vh;
            position: absolute;
            left: 50%;
            top: 0;
            transform: translateX(-50%);
        }
    </style>

    <script>
        function previewImage(event) {
            const reader = new FileReader();
            reader.onload = function(){
                const preview = document.getElementById('preview');
                preview.src = reader.result;
                preview.style.display = 'block';
            };
            reader.readAsDataURL(event.target.files[0]);
        }
    </script>
</head>
<body>
    <div class="page-wrapper">
        <h2>註冊帳號</h2>
        <div class="container">
            <div class="form-section">
                <form enctype="multipart/form-data" action="${pageContext.request.contextPath}/user/register" method="post">
                    <label for="username">使用者名稱：</label>
                    <input type="text" name="username" required>

                    <label for="password">密碼：</label>
                    <input type="password" name="password" required>

                    <label for="email">Email：</label>
                    <input type="text" name="email" required>

                    <label for="photo">大頭照上傳：</label>
                    <input type="file" name="photo" onchange="previewImage(event)">

                    <input type="submit" value="註冊">
                </form>
            </div>

            <!-- 中間白線 -->
            <div class="divider"></div>

            <div class="preview-section">
                <p>照片預覽：</p>
                <img id="preview" alt="照片預覽" style="display:none;"/>
            </div>
        </div>
    </div>
</body>
</html>
