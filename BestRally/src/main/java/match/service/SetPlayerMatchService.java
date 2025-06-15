package match.service;

import java.util.List;
import java.util.Map;
import match.exception.MatchPlayerException;
import match.model.dto.MatchPlayerDTO;

//從 session 取得目前對戰球員後，就可以行自動編排。
public interface SetPlayerMatchService {
	
	// 根據球員上場次數進行編排 (不考慮球員等級)
	public abstract List<MatchPlayerDTO> supplementPlayers(List<MatchPlayerDTO> matchPlayers, List<MatchPlayerDTO> battlePlayers) throws MatchPlayerException;
	
	
	// 根據球員上場次數，還有球員程度進行編排 (考慮球員等級)
	// 並回傳一組 4 人為一組的 List<MatchPlayerDTO>
	public abstract List<MatchPlayerDTO> arrangePlayerWithLevel(List<MatchPlayerDTO> matchPlayers, Integer levelThreshold) throws MatchPlayerException;
	
	
	// 根據建立好的 List<MatchPlayerDTO> 與編號建立 Map 物件：
	public abstract Map<Integer, List<MatchPlayerDTO>> arrangeBattle(Integer groupNum, List<MatchPlayerDTO> battlePlayers);
	
	
//	// 將正在對戰的隊伍當中的球員從 session 當中暫時刪除
//	// 回傳一份更新好的 matchPlayer List
//	public abstract List<MatchPlayerDTO> releasePlayer(List<MatchPlayerDTO> battlePlayers, List<MatchPlayerDTO> matchPlayers);
	
	
	/* 對戰結束後，紀錄數據再將特定的 battlePlayers (Map<Integer, List<MatchPlayerDTO>>) 放回 matchPlayers 資訊。
	 * 執行這個方法前，要先將下場的那組 battlePlayers 從 battlePlayers Map 當中刪除，並且記錄對戰資訊 (getPlayerMatchDataService)，
	 * 刪除紀錄完之後，要將刪除掉的那組 (剛下場的 battlePlayer) 加回 session 當中的 matchPlayers，讓下一輪可以繼續換她上場。
	 */
	public abstract Map<Integer, List<MatchPlayerDTO>> closeBattle(Integer groupNum, Map<Integer, List<MatchPlayerDTO>> battlePlayers);
	
	
	 /* 同一個編號重新進行排序有時候
	  * 會遇到自動編排的組員沒有辦法進行對戰，這時候就要：
	  * 以同一個對戰組編號，重新進行自動編排。
	  * 給予當前組編號 (groupNum) 再給予 matchPlayers 一次
	  * 要在裡面進行判斷式看看哪個不能重複，或是再次編排時的順序再次變異。
	  */
	public abstract Map<Integer, List<MatchPlayerDTO>> resetArrange(Integer curGroupNum, // 找到要重新編排的隊伍
																	Map<Integer, List<MatchPlayerDTO>> battlePlayers, // 給予目前的對戰隊伍列表
																	List<MatchPlayerDTO> matchPlayers, // 重新根據 matchPlayers 當中的隊伍找
																	Integer levelThreshold) throws MatchPlayerException; // 設定重新執行編排。
	/* 在這方法中要執行上面的方法：
	 * 1. arrangePlayer 重新編排
	 * 2. arrangeBattle，根據 curGroupNum 建立對戰。
	 */
	
	
	/* 球員對戰結束後，
	 * 要將球員從 battlePlayers 刪除，並加入回 Session 的備戰球員區 matchPlayers，
	 * 原因就是要讓他們回去 matchPlayers 做下一輪的人員編排。
	 * 所以這個方法就是根據對戰結束編號取得對戰成員，將成員加回 matchPlayer ，
	 * 方法回傳新的 matchPlayer，並重新加入 session 當中的 matchPlayers。
	 * 刪除 battlePlayers 可以用這裡 Service 的方法：closeBattle 將他們從 battlePlayers 刪除。
	 * 
	 */
	public abstract List<MatchPlayerDTO> returnPlayers(
													Integer curGroupNum,
													Map<Integer, List<MatchPlayerDTO>> battlePlayers,
													List<MatchPlayerDTO> matchPlayers);
	
	
	
}
