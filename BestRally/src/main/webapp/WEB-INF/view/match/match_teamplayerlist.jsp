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
	        
	    </style>
	</head>
	<body>
		<h2 align="center">球隊成員列表</h2>
		    <table>
		        <tr>
		            <th>球員 ID</th>
		            <th>球員名稱</th>
		            <th>球員勝率</th>
		            <th>加入對戰</th>
		        </tr>
		
		        <c:forEach var="entry" items="${teamPlayerName}">
	            <tr id="${entry.key.playerId}">
	            
	                <td>${entry.key.playerId}</td>
	                <td>${entry.value}</td>
	                <td>${entry.key.winRate}</td>
	                <td>
		                <div class="button-links" align="center">
				        	<a href="${pageContext.request.contextPath}/match/teamPlayer/add?playerId=${entry.key.playerId}&teamId=${entry.key.teamId}">加入對戰</a>
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
		    				<a href="${pageContext.request.contextPath}/match/teamPlayer/remove?playerId=${player.playerId}&teamId=${player.teamId}"> 退出 </a>
		    			</div>
		    		</td>
		    	</tr>
		    	</c:forEach>
	    	</table>
	    	
	    	<div class="button-links" align="center">
   				<a href="${pageContext.request.contextPath}/match/teamPlayer/remove?playerId=reset&teamId=${teamId}"> 重新安排 </a>
   			</div>
   			
   			<form id="arrange" action="${pageContext.request.contextPath}/match/teamPlayer/battle?teamId=${teamId}" method="post" class="button-links" style="text-align: center;">
			    輸入球員程度差距容忍度：<input type="number" name="threshold" placeholder="請輸入 threshold 值" required />
			    <button type="submit">自動編排</button>
			</form>

   			
	    </c:if>
	    
	    <!-- 開始對戰 -->
	    <c:if test="${not empty sessionScope.battlePlayers}">
		    <h2 align="center">開始對戰！</h2>		
		    	   	
		   		<!-- 每個對戰（上場）的紀錄 -->
			    <c:forEach var="entry" items="${sessionScope.battlePlayers}">
			    
			        <form action="${pageContext.request.contextPath}/match/record/" method="post">
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
					            <a  class = "button-reset" href="${pageContext.request.contextPath}/match/teamPlayer/battle?groupNum=${entry.key}&teamId=${teamId}">重新編排</a>
							 </div>
							 </td>
					    </tr>				   			
				    </table> 
				    </form>
			 	<!-- 每個隊伍的回圈 -->
			    </c:forEach>
	    </c:if>
	    
	   	    <div class="button-home" align="center">
		       	<a href="${pageContext.request.contextPath}/match/close?teamId=${teamId}" class="home-btn">結束所有對戰</a>
		       	<a href="${pageContext.request.contextPath}/" class="home-btn">回首頁</a>
		    </div>
		    
	</body>
</html>
