package match.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import match.exception.TeamPlayerException;
import match.model.dto.MatchPlayerDTO;
import match.model.dto.PlayerDTO;

/** 
 * 解析本次對戰紀錄：
 * 根據前端紀錄這個球隊裡，每個球員在這次對戰中的所有數據，
 * 這裡要想辦法把這個球隊的每個人的 playerDTO 呼叫出來，
 * 將本次對戰紀錄根據 playerId 儲存到 PlayerDTO 物件中，
 * 最後將每個 playerDTO 交給：TeamPlayerAFMatchService 執行 TeamPlayerUpdateService 做更新，
 * 
 * 所以可能會需要接收一個集合，紀錄本 team_id 裡的所有 player_id 的 win & total 數據。
 * 最後將各個 Player 的數據儲存在他的 PlayerDTO 當中，
 * 並呼叫 TeamPlayerAFMatchService 的方法：TeamplayerUpdateAFMatch(PlayerDTO playerDTO);
 * 將 GetPlayerMatchDataService 儲存好的 PlayerDTO 交給他。
 * 
 */ 

@Service
public interface GetPlayerMatchDataService {	
	
	/* 解析 match 結果
	 * 解析完，將每個 player 的隊賽紀錄，紀錄到 playerDTO 當中的 MatchResultDTO 當中。
	 */
	public abstract void GetMatchResult(MatchPlayerDTO matchPlayerDTO) throws TeamPlayerException ; 
	
	
//	/* 根據 GetMatchResult() 將每個 player 單次對戰紀錄紀錄在 playerDTO 中的 MatchResult 後，
//	 * 將 playerDTO 帶入這個方法，此方法將會呼叫 TeamPlayerAFMatchService，
//	 * 當中的方法：TeamplayerUpdateAFMatch(PlayerDTO) 就會執行 TeamPlayerUpdateServiceImpl
//	 * 就可以根據 MatchResult 更新數據
//	 */
//	public abstract void UpdateAFMatch(PlayerDTO playerDTO); 
}
