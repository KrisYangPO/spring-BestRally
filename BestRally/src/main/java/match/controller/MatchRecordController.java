package match.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import match.exception.MatchPlayerException;
import match.model.dto.MatchPlayerDTO;
import match.service.SetPlayerMatchService;

@Controller
@RequestMapping("/match")
public class MatchRecordController {
	
	@Autowired
	private SetPlayerMatchService setPlayerMatchService;
	
	// 輸入對戰成員的勝負後，就可以發送請求到這個方法紀錄勝負。
	@PostMapping("/record/{teamId}")
	@Transactional
	public String recordBattle(
			@PathVariable Integer teamId,
			@RequestParam Integer groupNum,
			@RequestParam Map<String, String> matchResultRaw, //@RequestParam 轉成 Map 一定是 String, String
			Model model, 
			HttpSession session
			) throws MatchPlayerException {
		
		// Step0. 處理 matchResult Map:
		// =====================================================================================================================================
		// 將 result_3=1, result_4=0 這類轉為 Map<String, Integer>
		Map<String, Integer> matchResult = matchResultRaw.entrySet().stream()
			    .filter(e -> e.getKey().startsWith("result_"))
			    .collect(Collectors.toMap(
			        Map.Entry::getKey,
			        e -> {
			            try {
			                return Integer.parseInt(e.getValue());
			            } catch (NumberFormatException ex) {
			                System.err.println("轉換對戰結果失敗: " + e);
			                return 0;
			            }
			        }
			    ));
		
		// 計算勝場總和：
		int winCount = matchResult.values().stream().mapToInt(Integer::intValue).sum();
		
		// 確認你輸入的勝利數一場只會有兩個 (雙打勝利方就是兩位)
		if(winCount != 2) {
			System.err.println("MatchRecordController: 輸入勝負錯誤，一組雙打勝利數一定是 2。" + winCount);
			String message = "MatchRecordController: 輸入勝負錯誤，一組雙打勝利數一定是 2。" + winCount;
			throw new MatchPlayerException(message);
		}
		
		
		// Step1. 取得目前的對戰備戰隊伍資訊
		// =====================================================================================================================================
		List<MatchPlayerDTO> matchPlayers = (List<MatchPlayerDTO>)session.getAttribute("matchPlayers");
		Map<Integer, List<MatchPlayerDTO>> battlePlayers = (Map<Integer, List<MatchPlayerDTO>>)session.getAttribute("battlePlayers");
		
		
		// Step2. 根據 groupNum 找出哪些人要下來並記錄。
		// =====================================================================================================================================
		// 從 battlePlayers Map 根據 groupNum Key 找尋下場的對戰隊伍(value)
		List<MatchPlayerDTO> closePlayers = battlePlayers.get(groupNum);
		
		// 將下場的對戰隊伍成員加回備戰區
		List<MatchPlayerDTO> updatedMatchPlayers = setPlayerMatchService.returnPlayers(groupNum, battlePlayers, matchPlayers);
		
		// 將對戰隊伍列表中下場的隊伍刪除。
		Map<Integer, List<MatchPlayerDTO>> updatedBattlePlayers = setPlayerMatchService.closeBattle(groupNum, battlePlayers);
		
		
		// Step3. 根據 closePlayers 裡的 PlayerId 呼叫每個人的 MatchPlayer，
		// =====================================================================================================================================
		// 將對戰數據加入他們的 MatchPlayerDTO。
		List<Integer> playerIds = closePlayers.stream().map(p->p.getPlayerId()).collect(Collectors.toList());
		
		// 遍歷每個下場球員的 id，
		for(Integer i : playerIds) {
			// 尋找更新後的備戰區的 matchPlayerDTO 是否有一致的 id
			Optional<MatchPlayerDTO> matchPlayer = updatedMatchPlayers
				.stream()
				.filter(p->p.getPlayerId() == i)
				.findFirst();
			
			// 如果在 matchPlayerDTOs 找到，就可以將數據記錄起來。
			// 數據來自 req.getParameter(result_{player_id})
			if(matchPlayer.isPresent()) {
				System.out.printf("對戰紀錄：球員(%s)對戰紀錄儲存，勝場數為：%s%n",
						matchPlayer.get().getUserName(),
						matchResult.get("result_" + i));
				// 新增到這個 MatchPlayerDTO 裡的對戰紀錄中。
				matchPlayer.get().setMatchRecord((matchResult.get("result_" + i)));
				
			}else {
				System.err.println("紀錄對戰紀錄時發生錯誤:" + matchResult.get("result_" + i));
				String message = "紀錄對戰紀錄時發生錯誤:" + matchResult.get("result_" + i);
				throw new MatchPlayerException(message);
			}
		}
		
		
		// Step4. 回傳所有資訊：
		// =====================================================================================================================================
		// 首先要設計排序規則：
		Comparator<MatchPlayerDTO> cmpMP = (MatchPlayerDTO mp1, MatchPlayerDTO mp2) -> {
			return mp1.getTotalMatch() - mp2.getTotalMatch();
			};
			
		Collections.sort(updatedMatchPlayers, cmpMP);
		session.setAttribute("matchPlayers", updatedMatchPlayers);
		session.setAttribute("battlePlayers", updatedBattlePlayers);
		
		return "redirect:/match/teamlist/" + teamId + "#arrange";
	}
}
