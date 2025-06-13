package match.service.impl;

import java.util.IntSummaryStatistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import match.exception.TeamException;
import match.exception.TeamPlayerException;
import match.exception.TeamRefreshException;
import match.model.dto.PlayersOfTeamDTO;
import match.service.TeamPlayerService;
import match.service.TeamRefreshDataService;
import match.service.TeamUpdateService;

/** 即時更新球隊數據，
 *  因為球員的變動，會導致 Team 數據，像是總球員人數和平均程度受到影響，
 *  變動像是：
 *  1. 新增球員到球隊：需要更新總人數與平均球員等級。
 *  2. 刪除球員：需要更新總人數與平均球員等級。
 *  3. 更新球員等級：需要更新球隊的平均球員程度。
 *  
 *  步驟會是：
 *  1. 求出這個球隊中所有球員，如果有人加入或離開這裡就會變化，但就算不變也可以求出來。
 *  2. 求出這個球隊中所有球員的等級。
 *  3. 重新計算這個隊伍的平均等級：總級數/總球員數。
 *  4. 執行 TeamUpdateService 內容。
 */


@Service
public class TeamRefreshDataServiceImpl implements TeamRefreshDataService {
	
	@Autowired
	private TeamPlayerService teamPlayerService;
	@Autowired
	private TeamUpdateService teamUpdateService;


	@Override
	public Boolean AFTeamPlayerUpdate(Integer teamId) throws TeamRefreshException {
		
		// Step1. 球隊總人數：
		// -----------------------------------------------------------------------------------
		// 找出這個球隊的所有 player:
		PlayersOfTeamDTO playersOfTeamDTO = null;
		try {
			playersOfTeamDTO =teamPlayerService.getPlayersFromTeam(teamId);
			
		} catch (TeamPlayerException e) {
			e.printStackTrace();
			throw new TeamRefreshException("TeamRefresh: 更新Team等級人數發生錯誤，無法找出球隊的所有球員："+teamId +"\n"+e.getMessage());
		}
		 
		// 總人數即為 players 人數：
		Integer newTotalMem = playersOfTeamDTO.getPlayers().size();
		
		// Step2. 計算所有球員的 level:
		// -----------------------------------------------------------------------------------
		// 直接呼叫 IntSummaryStatistics
		IntSummaryStatistics statis = playersOfTeamDTO.getPlayers().stream()
				.mapToInt(p->p.getLevel())
				.summaryStatistics();
		
		
		// Step3. 計算新的平均
		// -----------------------------------------------------------------------------------
		// 呼叫 Level 的總和:getSum()
		int newTotalLevel = (int)statis.getSum();
		// 透過新的總人數與新的球員等級，計算新的平均 Level：
		Integer avgLevel = (Integer) newTotalLevel/newTotalMem;
		
		
		// Step4. 更新數據：
		// -----------------------------------------------------------------------------------
		try {
			teamUpdateService.updateTeamLevelAndNum(teamId, avgLevel, newTotalMem);
		
		} catch (TeamException e) {
			e.printStackTrace();
			throw new TeamRefreshException(String.format("TeamRefresh: 更新Team等級人數發生錯誤。%nTeamID:%s%n新平均等級：%s%n新總球員數：%s%nerror:%s",
					teamId, avgLevel, newTotalMem, e.getMessage()));
		}	
		return true;
	}

}
