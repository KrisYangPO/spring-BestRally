<%@ page contentType="text/html; charset=UTF-8" %>
<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html>
	<head>
	    <meta charset="UTF-8">
	    <title>球隊成員清單</title>
	    <style>
	        body {
	            background-color: #cff5ff1;
	            font-family: sans-serif;
	            margin: 5px;
	            font-size: 150%;
	        }
	        h2 {
	            color: #0b3d0b;
	        }
	        
	        /* 設定表格 */
	        table {
	            border-collapse: collapse;
	            width: 60%;
	            margin: auto;
	            background-color: white;
	            border: 4px solid #0b3d0b;
	        }
	        /* 設定行列欄位 */
	        td {
	         	border: 2px solid #0b3d0b;
	         	text-align: center;
	        	padding: 4px;
	            background-color: white;
	            color: black;
	        }
	        
	        /* 設定表頭 */
	        th {
	        	border: 2px solid white;
	         	text-align: center;
	        	padding: 30px;
	            background-color: #0b3d0b;
	            color: white;
	        }
	        
	        /* 設定按紐 */
	        .button-links {
	        	display: inline-block;
	            margin-top: 3px;
	            margin-bottom: 3px;
	            display: flex;
	            justify-content: center;
	            gap: 40px;
	        }
   	        .button-links a {
	            display: inline-block;
	            padding: 10px 20px;
	            background-color: #a8b8e0;
	            color: black;
	            text-decoration: none;
	            /* font-weight: bold; */
	            border-radius: 5px;
	        }
	
	        .button-links a:hover {
	            background-color: #b9c9f0;
	        }
	        
	         /* 設定按紐 */
	        .button-home {
	        	display: inline-block;
	            margin-top: 25px;
	            margin-bottom: 25px;
	            display: flex;
	            justify-content: center;
	            gap: 30px;
	        }
	        
	        .button-home a {
	            display: inline-block;
	            padding: 10px 20px;
	            background-color: #f7bebe;
	            color: black;
	            text-decoration: none;
	            font-weight: bold;
	            border-radius: 10px;
	        }
	        
	        .button-home a:hover {
	            background-color: #fad4d4;
	        }
	        
	        .button-group {
	            margin-top: 30px;
	            display: flex;
	            justify-content: center;
	            gap: 40px;
        	}
        	
        	.Battle-button-group {
	            margin-top: 5px;
	            display: flex;
	            justify-content: center;
	            gap: 40px;
        	}	
        	
        	.button-send {
				padding: 10px 20px;
				background-color: #82bf7a;
				color: black;
				border: none;
				border-radius: 5px;
				cursor: pointer;
				font-size: 100%;
			}
			.button-send:hover {
				background-color: #a2ed98;
			}
			
			.button-send2 {
				padding: 10px 20px;
				background-color: #f7c3c3;
				color: black;
				border: none;
				border-radius: 5px;
				cursor: pointer;
				font-size: 100%;
			}
			
			.button-reset {
			    padding: 10px 20px;
			    background-color: #fcd28c;
			    color: black;
			    text-decoration: none;
			    border-radius: 5px;
			    border: none;
			    cursor: pointer;
			}
			
			.button-reset:hover {
			    background-color: #fce8b2;
			}
	        .arrange-form {
		        display: flex;
		        justify-content: center;
		        align-items: center;
		        gap: 20px;
		        margin-top: 20px;
		        padding: 20px;
		        background-color: #ffffff;
		        border: 2px solid #0b3d0b;
		        border-radius: 12px;
		        width: fit-content;
		        margin-left: auto;
		        margin-right: auto;
		        box-shadow: 2px 2px 8px rgba(0,0,0,0.1);
		    }
		
		    .arrange-form label {
		        font-weight: bold;
		        color: #0b3d0b;
		    }
		
		    .arrange-form input[type="number"] {
		        padding: 8px;
		        font-size: 100%;
		        border: 1px solid #aaa;
		        border-radius: 5px;
		        width: 200px;
		    }
		
		    .arrange-form button {
		        padding: 10px 20px;
		        background-color: #a8b8e0;
		        color: black;
		        border: none;
		        border-radius: 8px;
		        cursor: pointer;
		        font-size: 100%;
		        font-weight: bold;
		    }
		
		    .arrange-form button:hover {
		        background-color: #b9c9f0;
		    }
		    .manual-form {
			    display: none; /* 初始隱藏 */
			    flex-direction: column;
			    align-items: center;
			    margin-top: 20px;
			    gap: 10px;
			    padding: 20px;
			    background-color: #ffffff;
			    border: 2px solid #0b3d0b;
			    border-radius: 12px;
			    width: fit-content;
			    margin-left: auto;
			    margin-right: auto;
			    box-shadow: 2px 2px 8px rgba(0,0,0,0.1);
			}
			
			.manual-form select {
			    padding: 8px;
			    font-size: 100%;
			    border-radius: 6px;
			    width: 220px;
			}
			
			.manual-form button {
			    padding: 10px 20px;
			    background-color: #82bf7a;
			    color: black;
			    border: none;
			    border-radius: 8px;
			    cursor: pointer;
			    font-size: 100%;
			    font-weight: bold;
			}
			
			.manual-form button:hover {
			    background-color: #a2ed98;
			}
	    </style>
	</head>
	<body>
		<h2 align="center">球隊成員列表</h2>
		    <table>
		        <tr>
		            <th>球員 ID</th>
		            <th>球員名稱</th>
		            <th>球員等級</th>
		            <th>加入對戰</th>
		        </tr>
		
		        <c:forEach var="player" items="${playerOfTeamDTO.players}">
	            <tr id="${player.id}">
	                <td>${player.id}</td>
	                <td>${player.user.username}</td>
	                <td>${player.level}</td>
	                <td>
		                <div class="button-links" align="center">
				        	<a href="${pageContext.request.contextPath}/match/teamplayers/add/${playerOfTeamDTO.teamDTO.id}?playerId=${player.id}">加入對戰</a>
				        </div>
	                </td>
	            </tr>
		        </c:forEach>
		    </table>    
		
	    <!-- 加入對戰的人員，可以設計拖曳式視窗。 -->
	    <c:if test="${not empty sessionScope.matchPlayers}">
	    	<h2 align="center"> 對戰名單(人數：${fn:length(sessionScope.matchPlayers)} ) </h2>
	    	<table>
		    	<tr>
	                <th>球員名稱</th>
	                <th>球員級數</th>
	                <th>球員勝場</th>
	                <th>目前場數</th>
	                <th>移除人員</th>
	            </tr>
		    	
		   		<!-- 將每個 matchPlayer 從 Session 當中抓取出來。 -->
		    	<c:forEach var="player" items = "${sessionScope.matchPlayers}">
		    	<tr id="removePlayer">
		    		<td>${player.userName}</td>
		    		<td>${player.playerLevel}</td>
		    		<td>${player.winGame}</td>
		    		<td>${player.totalMatch}</td>
		    		<td>
		    			<div class="button-links" align="center">
		    				<a href="${pageContext.request.contextPath}/match/teamplayers/remove/${teamId}?playerId=${player.playerId}"> 退出 </a>
		    			</div>
		    		</td>
		    	</tr>
		    	</c:forEach>
		    	<tr id = "manual">
			    	<td colspan="5" align="center">
				        <div class="Battle-button-group">
				        	<div class="button-links" align="center">
				   				<a href="${pageContext.request.contextPath}/match/teamplayers/remove/${teamId}?playerId=0000"> 重新安排 </a>
				   			</div>
				   			<!-- 顯示按鈕：點擊會顯示 select 表單 -->
							<div class="button-links" align="center">
							    <button class="button-send2" type="button" onclick="toggleManualForm()">手動編排</button>
							</div>
				        </div>
			        </td>
	    	</table>
   			<!-- 手動選擇對戰球員 -->
			<form action="${pageContext.request.contextPath}/match/arrange/manual/${teamId}" method="post" id="manualForm" class="manual-form">
			    <label>選擇第一位球員：
			        <select name="players">
			            <c:forEach var="player" items="${sessionScope.matchPlayers}">
			                <option value="${player.userName}">${player.userName}</option>
			            </c:forEach>
			        </select>
			    </label>
			    <label>選擇第二位球員：
			        <select name="players">
			            <c:forEach var="player" items="${sessionScope.matchPlayers}">
			                <option value="${player.userName}">${player.userName}</option>
			            </c:forEach>
			        </select>
			    </label>
			    <label>選擇第三位球員：
			        <select name="players">
			            <c:forEach var="player" items="${sessionScope.matchPlayers}">
			                <option value="${player.userName}">${player.userName}</option>
			            </c:forEach>
			        </select>
			    </label>
			    <label>選擇第四位球員：
			        <select name="players">
			            <c:forEach var="player" items="${sessionScope.matchPlayers}">
			                <option value="${player.userName}">${player.userName}</option>
			            </c:forEach>
			        </select>
			    </label>
			
			    <button type="submit">建立對戰</button>
			</form>
   			
   			<form id="arrange" action="${pageContext.request.contextPath}/match/arrange/${teamId}" method="post" class="arrange-form">
			    輸入球員程度差距容忍度：<input type="number" name="threshold" placeholder="請輸入 threshold 值" required />
			    <button type="submit">自動編排</button>
			</form>
   			
	    </c:if>
	    
	    <!-- 開始對戰 -->
	    <c:if test="${not empty sessionScope.battlePlayers}">
		    <h2 align="center">開始對戰！</h2>		
		    	   	
		   		<!-- 每個對戰（上場）的紀錄 -->
			    <c:forEach var="entry" items="${sessionScope.battlePlayers}">
			    
			        <form action="${pageContext.request.contextPath}/match/record/${teamId}" method="post">
			        <!-- 加入 groupNum 作為隱藏欄位傳送 -->
			        <input type="hidden" name="groupNum" value="${entry.key}" />
				    <input type="hidden" name="teamId" value="${teamId}" />
				     <table>
					    <tr id="reset_${entry.key}">
					        <th>對戰組別</th>
					        <th>球員名稱</th>
					        <th>球員等級</th>
					        <th>勝負</th>
					    </tr>
				        
				        <c:forEach var="player" items="${entry.value}">
				            <tr>
				                <td>${entry.key}</td>
				                <td>${player.userName}</td>
				                <td>${player.playerLevel}</td>
				                <td>
				                    <label>
				                        <input type="radio" name="result_${player.playerId}" value="1"/> 勝
				                        <%-- <c:if test="${player.matchRecord[player.matchRecord.size() - 1] == 1}">checked</c:if> /> --%>
				                    </label>
				                    <label>
				                        <input type="radio" name="result_${player.playerId}" value="0"/> 敗
				                        <%--  <c:if test="${player.matchRecord[player.matchRecord.size() - 1] == 0}">checked</c:if> /> --%>
				                    </label>
				                </td>
				            </tr>
	          				<!-- 每個隊伍的球員的回圈 -->
				        </c:forEach>
				        
				        <!-- 額外插入一列，用於放按鈕 -->
					    <tr>
					        <td colspan="4" align="center">
					        <div class="Battle-button-group">
					            <button class="button-send" type="submit">送出這組對戰結果</button>
					            <a  class = "button-reset" href="${pageContext.request.contextPath}/match/arrange/${teamId}?resetGroupNum=${entry.key}">再次重排</a>
							 </div>
							 </td>
					    </tr>				   			
				    </table> 
				    </form>
			 	<!-- 每個隊伍的回圈 -->
			    </c:forEach>
	    </c:if>
	    
	   	    <div class="button-home" align="center">
		       	<a href="${pageContext.request.contextPath}/match/close/${teamId}" class="home-btn">結束所有對戰</a>
		       	<a href="${pageContext.request.contextPath}/" class="home-btn">回首頁</a>
		    </div>
		    
	</body>
	<script>
	    function toggleManualForm() {
	        const form = document.getElementById("manualForm");
	        form.style.display = (form.style.display === "none" || form.style.display === "") ? "flex" : "none";
	    }
	</script>
</html>
