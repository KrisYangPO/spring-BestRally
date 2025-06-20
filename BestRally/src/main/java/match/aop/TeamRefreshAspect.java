package match.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import match.annotation.DoTeamRefresh;
import match.exception.PlayerException;
import match.exception.TeamPlayerException;
import match.exception.TeamRefreshException;
import match.model.dto.TeamDTO;
import match.model.dto.TeamsOfPlayerDTO;
import match.service.PlayerService;
import match.service.TeamPlayerService;
import match.service.TeamRefreshDataService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Component
@Aspect
public class TeamRefreshAspect {
	
	@Autowired
	private TeamRefreshDataService teamRefreshDataService;
	@Autowired
	private TeamPlayerService teamPlayerService;
	@Autowired
	private PlayerService playerService;
	
	private static final Logger log = LoggerFactory.getLogger(TeamRefreshAspect.class);
	
	
//	@AfterReturning("@annotation(refresh)")
//	public void afterModifyTeam(JoinPoint joinPoint, DoTeamRefresh refresh) {
//		// Step1. 取得切面方法資訊：參數名稱，參數內容，方法資訊。
//		// ============================================================================================================
//		// 取得註解上寫的 teamIdParam 名稱，
//		// 或是路徑(@DoRefreshTeam(teamIdParam = "??參數名稱??")) 的參數名稱。
//		String paramPath = refresh.teamIdParam(); 
//		
//		// 取得 aspect 作用的方法的參數列的所有參數 args[0]:第一個參數值。
//		// 如果方法輸入的引數為：addTeamPlayer(101, 30)
//		// args[0]: 101
//		// args[1]: 30
//        Object[] args = joinPoint.getArgs(); 
//        
//        // 取得當前執行的方法本身：
//        // method.getName 取得方法名稱
//        // method.getAnnotation: 取得方法註解
//        // method.getParameterTypes: 取得參數型別
//        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//        
//        // 取得方法參數的名稱列表。
//        // 如果參數是 (Integer teamId, Integer playerId)
//        // paramNames[0]: teamId
//        // paramNames[1]: playerId
//        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
//        
//        
//        
//        // Step2. 定義 teamId，根據不同的 teamIdParam 參數內容，找出 teamId，
//        // 原因是因為這些方法的參數列不一定有 teamId，所以要依靠他們的參數找出 teamId 內容。
//        // ============================================================================================================
//        try {
//        	// 根據下面的邏輯判斷，取得各個方法可能會得到的 teamIds 
//            List<Integer> teamIds = resolveTeamId(paramPath, args, paramNames);
//            if (teamIds != null && !teamIds.isEmpty()) {
//                // 延遲執行直到 transaction commit
//            	// 確定 Transactional 都有開啟同步功能 (default會開啟)
//            	if (TransactionSynchronizationManager.isSynchronizationActive()) {
//            		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//                        @Override
//                        public void afterCommit() {
//                            for (Integer teamId : teamIds) {
//                                try {
//                                    teamRefreshDataService.AFTeamPlayerUpdate(teamId);
//                                    System.out.printf("[AOP afterCommit] TeamRefresh 成功刷新隊伍資料 teamId=%d%n", teamId);
//                                    log.info(String.format("[AOP afterCommit] TeamRefresh 成功刷新隊伍資料 teamId=%d%n", teamId));
//                                    
//                                } catch (TeamRefreshException e) {
//                                    System.err.printf("[AOP afterCommit] TeamRefresh 錯誤：teamId=%d，錯誤：%s%n", teamId, e.getMessage());
//                                    log.error(String.format("[AOP afterCommit] TeamRefresh 錯誤：teamId=%d，錯誤：%s%n", teamId, e.getMessage()));
//                                }
//                            }
//                        }
//                    });
//                } else {
//                    System.err.println("[AOP] TeamRefresh: 無法解析 teamId，未註冊 afterCommit 行為。");
//                    log.error("[AOP] TeamRefresh: 無法解析 teamId，未註冊 afterCommit 行為。");
//                }
//            }else {
//            	log.warn("目前非 Transaction 同步階段，無法註冊 afterCommit");
//            }
//                
//        }catch (Exception e) {
//        	e.printStackTrace();
//			System.err.println("[AOP] TeamRefresh 錯誤：更新失敗：" + e.getMessage());
//			log.error("[AOP] TeamRefresh 錯誤：更新失敗：" + e.getMessage());
//		}
//        
//	}// end of AfterReturning

	
	
	@AfterReturning("@annotation(refresh)")
	public void afterModifyTeam(JoinPoint joinPoint, DoTeamRefresh refresh) {
		// Step1. 取得切面方法資訊：參數名稱，參數內容，方法資訊。
		// ============================================================================================================
		// 取得註解上寫的 teamIdParam 名稱，
		// 或是路徑(@DoRefreshTeam(teamIdParam = "??參數名稱??")) 的參數名稱。
		String paramPath = refresh.teamIdParam();

		// 取得 aspect 作用的方法的參數列的所有參數 args[0]:第一個參數值。
		// 如果方法輸入的引數為：addTeamPlayer(101, 30)
		// args[0]: 101
		// args[1]: 30
		Object[] args = joinPoint.getArgs();

		// 取得當前執行的方法本身：
		// method.getName 取得方法名稱
		// method.getAnnotation: 取得方法註解
		// method.getParameterTypes: 取得參數型別
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

		// 取得方法參數的名稱列表。
		// 如果參數是 (Integer teamId, Integer playerId)
		// paramNames[0]: teamId
		// paramNames[1]: playerId
		String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

		// Step2. 定義 teamId，根據不同的 teamIdParam 參數內容，找出 teamId，
		// 原因是因為這些方法的參數列不一定有 teamId，所以要依靠他們的參數找出 teamId 內容。
		// ============================================================================================================
		try {
			List<Integer> teamIds = resolveTeamId(paramPath, args, paramNames);
			if (teamIds != null && !teamIds.isEmpty()) {
				// 取得每個 teamId
				for (Integer teamId : teamIds) {
					// 更新每個 teamId:
					try {
                      teamRefreshDataService.AFTeamPlayerUpdate(teamId);
                      System.out.printf("[AOP afterCommit] TeamRefresh 成功刷新隊伍資料 teamId=%d%n", teamId);
                      log.info(String.format("[AOP afterCommit] TeamRefresh 成功刷新隊伍資料 teamId=%d%n", teamId));
                      
	                  } catch (TeamRefreshException e) {
	                      System.err.printf("[AOP afterCommit] TeamRefresh 錯誤：teamId=%d，錯誤：%s%n", teamId, e.getMessage());
	                      log.error(String.format("[AOP afterCommit] TeamRefresh 錯誤：teamId=%d，錯誤：%s%n", teamId, e.getMessage()));
	                  }
				}
			} else {
				System.err.println("[AOP] TeamRefresh 錯誤：無法取得方法中的 teamId。");
				log.error(String.format("[AOP] TeamRefresh 錯誤：無法取得方法中的 teamId。%n"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[AOP] TeamRefresh 錯誤：更新失敗：" + e.getMessage());
			log.error("[AOP] TeamRefresh 錯誤：更新失敗：" + e.getMessage());
		}

	}// end of AfterReturning
	
	
	
	// 將判斷 teamId 的邏輯取出來。
	private List<Integer> resolveTeamId(String paramPath, Object[] args, String[] paramNames) throws TeamPlayerException, PlayerException {
		// 在某些方法中，像是更新 playerLevel 的方法，就需要多筆 teamId 全部更新。
		// 所以無論是單筆更新或是可能有多筆更新，都用陣列查詢。
		List<Integer> teamIds = new ArrayList<>();
		
		// CASE: 處理邏輯：@DoTeamRrefresh(paramPath = "teamPlayerId") -> 查 teamPlayer 查 teamId
		// FROM: TeamPlayerService: removeTeamPlayer(Integer teamPlayerId)
		// ==================================================================================================
	    if ("teamPlayerId".equals(paramPath)) {
	    	// 遍歷每個方法的參數名稱
	        for (int i = 0; i < paramNames.length; i++) {
	        	// 判斷參數列是否有等於 "teamPlayerId" 的參數，且值要是 Integer 型態。
	            if ("teamPlayerId".equals(paramNames[i]) && args[i] instanceof Integer) {
	            	// 取得 teamPlayerId 在方法中的實際數值(args[i])
	                Integer teamPlayerId = (Integer) args[i];
	                // 透過teamPlayerService從 teamPlayerId 取得 teamId，並蒐集到 teamIds 陣列中。
	                teamIds.add(teamPlayerService.findTeamPlayerById(teamPlayerId).getTeam().getId());
	            }
	        }
	        return teamIds;
	    }
	    
	    
	    // CASE: 處理邏輯：@DoTeamRrefresh(paramPath = "userId") -> 查 player 查 teamPlayer 查 teamId
	    // FROM: PlayerService: updatePlayerLevel(Integer userId, Integer level)
	    // ==================================================================================================
	    if("userId".equals(paramPath)) {
	    	// 遍歷方法的參數列的所有名稱，查詢 userId
	    	for(int i = 0; i<paramNames.length; i++) {
	    		// 取得名稱為 "userId" 且為數值型態的參數與值：
	    		if("userId".equals(paramNames[i]) && args[i] instanceof Integer) {
	    			// 取得 userId
	    			Integer userId = (Integer) args[i];
	    			// 取得 playerDTO:
	    			Integer playerId = playerService.findPlayerByUserId(userId).getId();
	    			// 找出這個 player 所參與的所有球隊：
	    			TeamsOfPlayerDTO teamsOfPlayerDTO = teamPlayerService.getTeamsFromPlayer(playerId);
	    			// 遍歷所有球隊，蒐集所有 teamId:
	    			for(TeamDTO teamDTO: teamsOfPlayerDTO.getTeamDTOs()) {
	    				teamIds.add(teamDTO.getId());
	    			}
	    		}
	    	}
	    	return teamIds;
	    }
	    
	    
	    // CASE: 處理邏輯：@DoTeamRrefresh(paramPath = "teamId") -> 直接使用 teamId
	    // FROM: TeamPlayerService: addTeamPlayer(Integer teamId, Integer playerId)
	    // ==================================================================================================
	    if("teamId".equals(paramPath)){
		    // 遍歷這個方法的每個參數列的名稱，看看是否有 "teamId" 的參數。
		    for (int i = 0; i < paramNames.length; i++) {
		    	// 如果參數列上的名稱與 @TeamIdParam(paramPath) 一樣就直接回傳 teamId
		        if (paramNames[i].equals(paramPath) && args[i] instanceof Integer) {
		            teamIds.add((Integer) args[i]);
		        }
		    }
		    return teamIds;
	    }
	    
	    
	    // 什麼都沒有。
	    return null;
	}// end of resolveTeamId
}
