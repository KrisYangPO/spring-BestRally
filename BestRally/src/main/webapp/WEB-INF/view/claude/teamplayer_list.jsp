<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>球隊成員管理</title>
    <meta charset="UTF-8">

    <!-- ✅ DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css"/>
    <link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.4.1/css/buttons.dataTables.min.css"/>

    <!-- ✅ jQuery & DataTables JS -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/2.4.1/js/dataTables.buttons.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.html5.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.print.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.68/pdfmake.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.68/vfs_fonts.js"></script>

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #e8f5e9;
            padding: 20px;
            font-size: 150%
        }

        h2 {
            text-align: center;
            font-size: 48px;
            margin-bottom: 30px;
        }

        table {
            width: 80%;
            margin: auto;
            border-collapse: collapse;
            background-color: white;
        }

        th {
            color: white;
            background-color: #388e3c;
            padding: 12px;
            border: 2px solid white;
            text-align: center;
        }

        td {
            padding: 12px;
            border: 2px solid #c8e6c9;
            text-align: center;
        }

        .btn {
            padding: 6px 12px;
            background-color: #ff7070;
            color: white;
            border: 2px solid black;
            border-radius: 8px;
            cursor: pointer;
            font-size: 105%
        }

        .btn:hover {
            background-color: #d32f2f;
        }

        .back-btn {
            text-align: center;
            margin-top: 30px;
            gap: 50px
        }

        .back-btn button {
            background-color: white;
            color: black;
            padding: 10px 20px;
            border: 2px solid black;
            border-radius: 8px;
            cursor: pointer;
            font-size: 120%
        }
        
        .applyblock {
       	 	display: inline-block;
            text-align: center;
            margin-top: 5px;
            gap: 10%;
        }
        
        .accept_button{
       		display: inline-block;
            padding: 6px 12px;
            background-color: #69c9fa;
            color: white;
            border: 2px solid black;
            border-radius: 8px;
            cursor: pointer;
            font-size: 105%
        }
       	.accept_button:hover{
       		background-color:#0f91d4;
       	}
       	
       	 .reject_button{
       	 	display: inline-block;
            padding: 6px 12px;
            background-color: #ff7070;
            color: white;
            border: 2px solid black;
            border-radius: 8px;
            cursor: pointer;
            font-size: 105%
        }
       	.reject_button:hover{
       		background-color:#d32f2f;
       	}

        .back-btn button:hover {
            background-color: #d9d9d9;
        }

        .no-members {
            text-align: center;
            font-style: italic;
            background-color: #f1f8e9;
        }
    </style>
</head>
<body>

    <h2>球隊成員管理</h2>

    <table id="teamPlayerTable">
        <thead>
            <tr>
                <th>隊員 ID</th>
                <th>使用者名稱</th>
                <th>勝率</th>
                <th>勝場數</th>
                <th>比賽總數</th>
                <th>加入時間</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
        	<c:if test="${not empty teamPlayerDTOs}">
	            <c:forEach var="tp" items="${teamPlayerDTOs}">
	                <tr>
	                    <td>${tp.id}</td>
	                    <td>${tp.player.user.username}</td>
	                    <td><c:out value="${tp.winRate}" default="0.0"/></td>
	                    <td>${tp.winGame}</td>
	                    <td>${tp.total}</td>
	                    <td>${tp.joinTime}</td>
	                    <td>
	                        <form method="post" action="${pageContext.request.contextPath}/teamplayer/list/delete/${tp.team.id}" style="display:inline;">
	                            <input type="hidden" name="teamPlayerId" value="${tp.id}" />
	                            <input type="hidden" name="_method" value="DELETE"/>
	                            <button type="submit" class="btn">刪除</button>
	                        </form>
	                    </td>
	                </tr>
	            </c:forEach>
        	</c:if>
<%-- 			<c:if test="${empty teamPlayerDTOs}">
                <tr><td colspan="7" class="no-members">此球隊目前尚無成員。</td></tr>
            </c:if> --%>
        </tbody>
    </table>
    
    <h2>球隊申請列表</h2>
    <table>
    	<thead>
            <tr>
                <th>申請編號</th>
                <th>申請人名稱</th>
                <th>申請人信箱</th>
                <th>球員等級</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="teamapply" items="${teamApplicationDTOs}">
	            <tr>
	            	<td>${teamapply.id}</td>
	            	<td>${teamapply.username}</td>
	            	<td>${teamapply.email}</td>
	            	<td>${teamapply.player.level}</td>
	            	<td>
	            		<div class="applyblock">
					        <button class ="accept_button" onclick="location.href='${pageContext.request.contextPath}/team/accept/${teamId}?playerId=${teamapply.player.id}'">加入球員</button>
					        <button class="reject_button" onclick="location.href='${pageContext.request.contextPath}/team/reject/${teamId}?applyId=${teamapply.id}'">拒絕球員</button>
					    </div>
	            	</td>
	            </tr>
            </c:forEach>
            <c:if test="${empty teamApplicationDTOs}">
                <tr><td colspan="5" class="no-members">目前尚無球員申請此球隊</td></tr>
            </c:if>
    </table>

    <div class="back-btn">
        <button onclick="location.href='${pageContext.request.contextPath}/user/home'">回上一頁</button>
    </div>

    <!-- ✅ DataTables 初始化 -->
    <script>
        $(document).ready(function () {
            $('#teamPlayerTable').DataTable({
                dom: 'Bfrtip',
                buttons: [
                    {
                        extend: 'excelHtml5',
                        text: '匯出 Excel',
                        exportOptions: { columns: ':not(:last-child)' }
                    },
                    {
                        extend: 'pdfHtml5',
                        text: '匯出 PDF',
                        orientation: 'landscape',
                        pageSize: 'A4',
                        exportOptions: { columns: ':not(:last-child)' }
                    },
                    {
                        extend: 'print',
                        text: '列印'
                    }
                ],
                language: {
                    url: '//cdn.datatables.net/plug-ins/1.13.6/i18n/zh-HANT.json',
                   	emptyTable: "此球隊目前尚無成員。"
                }
            });
        });
    </script>

</body>
</html>
