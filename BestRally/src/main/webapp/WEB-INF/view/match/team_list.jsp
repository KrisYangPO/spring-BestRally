<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>羽球人 Battle Web - 球隊列表</title>
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
            margin: 0;
            padding: 20px;
            background-color: #b8e0b8;
            font-family: Arial, sans-serif;
            color: black;
            font-size: 150%;
        }

        h2 {
            text-align: center;
            font-size: 48px;
            margin-bottom: 30px;
        }

        .filter-form {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 15px;
            margin-bottom: 30px;
        }

        input[type="number"] {
            width: 60px;
            padding: 5px;
            font-size: 16px;
        }

        button {
            background-color: white;
            color: black;
            border: 2px solid black;
            padding: 8px 16px;
            font-size: 25px;
            cursor: pointer;
            border-radius: 5px;
            transition: background-color 0.2s, transform 0.2s;
        }

        button:hover {
            background-color: #e0e0e0;
            transform: scale(1.03);
        }

        table {
            width: 95%;
            margin: auto;
            border-collapse: collapse;
            background-color: white;
        }

        th {
            color: white;
            background-color: #247a18;
            padding: 12px;
            border: 2px solid white;
            text-align: center;
        }

        td {
            padding: 12px;
            border: 2px solid #b7e3b1;
            text-align: center;
        }

        .no-teams {
            text-align: center;
            font-style: italic;
            padding: 20px;
            background-color: rgba(255, 255, 255, 0.7);
        }
    </style>
</head>
<body>

    <h2>羽球人 Battle Web - 球隊列表</h2>

    <div style="text-align: center; margin-bottom: 20px;">
        <button onclick="location.href='${pageContext.request.contextPath}/'">回首頁</button>
    </div>

    <!-- 篩選 -->
    <form method="get" class="filter-form">
        <button type="submit" name="filterBy" value="recruit">只看招募中</button>

        <input type="hidden" name="sortMethod" value="${currentSortBy}" />
        <input type="hidden" name="sortOrder" value="${currentSortOrder}" />

        等級高於：
        <input type="number" name="levelup" min="1" max="13" />
        <button type="submit" name="filterBy" value="level-above">篩選</button>

        等級低於：
        <input type="number" name="leveldown" min="1" max="13" />
        <button type="submit" name="filterBy" value="level-below">篩選</button>

        <button type="button" onclick="location.href='${pageContext.request.contextPath}/team/list'">清除篩選</button>
    </form>

    <!-- ✅ 球隊表格 + DataTables -->
    <table id="teamTable">
        <thead>
            <tr>
                <th>名稱</th>
                <th>球隊人數</th>
                <th>平均級數</th>
                <th>地區</th>
                <th>招募中</th>
                <th>隊長Id</th>
                <th>申請加入</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="team" items="${teamDTOs}">
                <tr>
                    <td>${team.teamName}</td>
                    <td>${team.teamNum}</td>
                    <td>${team.avgLevel}</td>
                    <td>${team.place}</td>
                    <td>${team.recruit ? "是" : "否"}</td>
                    <td>${team.player.id}</td>
                    <td>
                        <button onclick="location.href='${pageContext.request.contextPath}/team/apply/${team.id}'">加入球隊！</button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty teamDTOs}">
                <tr>
                    <td colspan="7" class="no-teams">目前沒有符合條件的球隊</td>
                </tr>
            </c:if>
        </tbody>
    </table>

    <!-- ✅ DataTables 初始化 -->
    <script>
        $(document).ready(function () {
            $('#teamTable').DataTable({
                dom: 'Bfrtip',
                buttons: [
                    {
                        extend: 'excelHtml5',
                        text: '匯出 Excel',
                        exportOptions: {
                            columns: ':not(:last-child)' // 不匯出「申請加入」按鈕
                        }
                    },
                    {
                        extend: 'pdfHtml5',
                        text: '匯出 PDF',
                        orientation: 'landscape',
                        pageSize: 'A4',
                        exportOptions: {
                            columns: ':not(:last-child)'
                        }
                    },
                    {
                        extend: 'print',
                        text: '列印'
                    }
                ],
                language: {
                    url: '//cdn.datatables.net/plug-ins/1.13.6/i18n/zh-HANT.json'
                }
            });
        });
    </script>

</body>
</html>
