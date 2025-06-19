package match.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
public class MatchArrangeController {
	
	@Autowired
	private SetPlayerMatchService setPlayerMatchService;
	
	// 每次執行加入對戰表，就將 groupNum 累加 1
	// 作為 currentMatch Map 的 key:
	public static Integer groupNum = 1;
	
	// Post 表單送出等級差距，啟動自動排序
	@PostMapping("/arrange/{teamId}")
	@Transactional
	public String arrangeBattle(
			@PathVariable Integer teamId, 
			@RequestParam Integer threshold, 
			HttpSession session, 
			Model model) throws MatchPlayerException {
		
		// Step1. 呼叫 MatchPlayers (透過 filter 或是 AOP 處理。)
		// =====================================================================================================================================
		List<MatchPlayerDTO> matchPlayers = (List<MatchPlayerDTO>) session.getAttribute("matchPlayers");
		
		
		// Step2. 執行自動編排：
		// =====================================================================================================================================
		// 執行對戰：
		List<MatchPlayerDTO> newBattle = setPlayerMatchService.arrangePlayerWithLevel(matchPlayers, threshold);
		
		// Map 會紀錄每此執行 "自動編排"，也就是執行這個 MatchArrangeServlet 時，
		// 會需要將之前還沒下場的對戰隊伍保留，直到他們結束比賽下場，
		// 因此建立 Map 時，要先從目前的 Session 判斷是否已經有隊伍進行對戰中，
		// 如果有就要用 Map.put() 的方式將新的對戰隊伍加入。
		Map<Integer, List<MatchPlayerDTO>> battlePlayers = null; 
		
		// 確認 session 是否已經有正在對戰的隊伍
		// 沒有就透過 setPlayerMatchService.arrangeBattle 建立新的 battlePlayers
		// {1: List<MatchPlayerDTO>}
		if(session.getAttribute("battlePlayers") == null) {
			battlePlayers = setPlayerMatchService.arrangeBattle(groupNum, newBattle);
		
		// 如果有就要呼叫出來，並用新的 key-value 新增。
		}else {
			battlePlayers = (Map<Integer, List<MatchPlayerDTO>>) session.getAttribute("battlePlayers");
			Map<Integer, List<MatchPlayerDTO>> newBattlePlayers = setPlayerMatchService.arrangeBattle(groupNum, newBattle);
			// 建立好新的 battlePlayer 後，新增到當前的對戰隊伍列中
			// {1: List<MatchPlayerDTO>, 2:List<MatchPlayerDTO>, 3: ...}
			battlePlayers.put(groupNum, newBattlePlayers.get(groupNum));
		}
		
		
		// Step3. 顯示對戰紀錄：
		// =====================================================================================================================================
		System.out.println("自動編排紀錄：");
		for(Integer i:battlePlayers.keySet()) {
			for(MatchPlayerDTO mp : battlePlayers.get(i)) {
				System.out.printf("隊伍編號(%s)-成員(%s)%n",
						i, mp.getUserName());
			}
		}
		
		
		// Step4. 推送與重新推送
		// =====================================================================================================================================
		// 首先要設計排序規則：
		Comparator<MatchPlayerDTO> cmpMP = (MatchPlayerDTO mp1, MatchPlayerDTO mp2) -> {
			return mp1.getTotalMatch() - mp2.getTotalMatch();
		};
		Collections.sort(matchPlayers, cmpMP);
		
		// 將隊伍儲存在 session 當中
		session.setAttribute("matchPlayers", matchPlayers);
		session.setAttribute("battlePlayers", battlePlayers);
				
		// 累加：groupNum++;
		groupNum++;
		return "redirect:/match/teamlist/" + teamId +"#arrange";
	}
	
	
	/* 手動編排系統
	 * Select 重複挑選四個球員名稱，
	 * 可以透過 @RequestParam List<String> 將 userNames 全部接收。
	 * 不用 setPlayerMatchService.arrangeBattle() 編排，
	 * 直接透過名稱將每個 MatchPlayerDTO 從 MatchPlayers 抓取出來，
	 * 與目前的 groupNum 組合成 battlePlayers，就可以呈現在 battlePlayers 區。
	 */
	@PostMapping("/arrange/manual/{teamId}")
	@Transactional
	public String manualArrange(
			@PathVariable Integer teamId,
			@RequestParam List<String> players,
			HttpSession session,
			Model model) throws MatchPlayerException {
		
		// Step 1. 檢查是否有重複名稱：
		// =====================================================================================================================================
		Set<String> check = players.stream().collect(Collectors.toSet());
		if(check.size() < 4) {
			throw new MatchPlayerException("手動編排Controller 錯誤：球員重複，重新手動編排。");
		}
	
		// Step2. 從目前的 matchPlayers 透過 usernames 取出 MatchPlayerDTO
		// =====================================================================================================================================
		// 檢查 matchPlayers 夠不夠 4 個
		// 取出 matchPlayers 
		List<MatchPlayerDTO> matchPlayers = (List<MatchPlayerDTO>) session.getAttribute("matchPlayers");
		if(matchPlayers.size() < 4) {
			throw new MatchPlayerException("手動編排錯誤：目前備戰球員不足四人，目前人數：" + matchPlayers.size());
		}
		// 依照姓名找出自訂排序的 battlePlayers
		List<MatchPlayerDTO> manualBattle = new ArrayList<>();
		for(String name : players) {
			manualBattle.add(matchPlayers.stream().filter(p->p.getUserName().equals(name)).findFirst().orElse(null));
		}
		
		
		// 手動加入球員到 Battleplayer 需要自己將 matchPlayerDTO 從 MatchPlayers 刪除
		// setPlayerMatchService.arrangeBattle 會透過 iterator 讀取 MatchPlayers，會直接改變記憶體的 MatchPlayers
		// 在這裡就需要直接對 MatchPlayer 更改。
		// 用 Iterator 遍歷並安全移除
		Iterator<MatchPlayerDTO> iterator = null;
		if(matchPlayers != null) {
			iterator = matchPlayers.iterator();
		}
		while (iterator.hasNext()) {
		    MatchPlayerDTO mp = iterator.next();
		    if (players.contains(mp.getUserName())) {
		        iterator.remove(); // 安全移除
		    }
		}
		
		
		// Step3. Map 會紀錄每此執行 "自動編排"，也就是執行這個 MatchArrangeServlet 時，
		// =====================================================================================================================================
		// 會需要將之前還沒下場的對戰隊伍保留，直到他們結束比賽下場，
		// 因此建立 Map 時，要先從目前的 Session 判斷是否已經有隊伍進行對戰中，
		// 如果有就要用 Map.put() 的方式將新的對戰隊伍加入。
		Map<Integer, List<MatchPlayerDTO>> battlePlayers = null; 
		
		// 確認 session 是否已經有正在對戰的隊伍
		// 沒有就透過 setPlayerMatchService.arrangeBattle 建立新的 battlePlayers
		// {1: List<MatchPlayerDTO>}
		if(session.getAttribute("battlePlayers") == null) {
			battlePlayers = setPlayerMatchService.arrangeBattle(groupNum, manualBattle);
		
		// 如果有就要呼叫出來，並用新的 key-value 新增。
		}else {
			battlePlayers = (Map<Integer, List<MatchPlayerDTO>>) session.getAttribute("battlePlayers");
			Map<Integer, List<MatchPlayerDTO>> newBattlePlayers = setPlayerMatchService.arrangeBattle(groupNum, manualBattle);
			// 建立好新的 battlePlayer 後，新增到當前的對戰隊伍列中
			// {1: List<MatchPlayerDTO>, 2:List<MatchPlayerDTO>, 3: ...}
			battlePlayers.put(groupNum, newBattlePlayers.get(groupNum));
		}
		
		
		// Step4. 顯示對戰紀錄：
		// =====================================================================================================================================
		System.out.println("自動編排紀錄：");
		for(Integer i:battlePlayers.keySet()) {
			for(MatchPlayerDTO mp : battlePlayers.get(i)) {
				System.out.printf("隊伍編號(%s)-成員(%s)%n",
						i, mp.getUserName());
			}
		}
		
		
		// Step5. 推送與重新推送
		// =====================================================================================================================================
		// 首先要設計排序規則：
		Comparator<MatchPlayerDTO> cmpMP = (MatchPlayerDTO mp1, MatchPlayerDTO mp2) -> {
			return mp1.getTotalMatch() - mp2.getTotalMatch();
		};
		Collections.sort(matchPlayers, cmpMP);
		
		// 將隊伍儲存在 session 當中
		session.setAttribute("matchPlayers", matchPlayers);
		session.setAttribute("battlePlayers", battlePlayers);
				
		// 累加：groupNum++;
		groupNum++;
		return "redirect:/match/teamlist/" + teamId + "#manual";
	}
	
	
	
	// 一組四人重新編排：
	@GetMapping("/arrange/{teamId}")
	@Transactional
	public String resetBattle(
			@PathVariable Integer teamId,
			@RequestParam Integer resetGroupNum,
			Model model,
			HttpSession session) throws MatchPlayerException {
		
		
		// Step1. 取得目前的備戰隊伍
		// =====================================================================================================================================
		List<MatchPlayerDTO> matchPlayers = (List<MatchPlayerDTO>) session.getAttribute("matchPlayers");
		// 取得對戰隊伍：
		Map<Integer, List<MatchPlayerDTO>> battlePlayers = (Map<Integer, List<MatchPlayerDTO>>) session.getAttribute("battlePlayers");
		
		
		// Step2. 先從對戰區移動到備戰區，再從對戰區刪除。
		// =====================================================================================================================================
		// 還原對戰區的球員後，建立新的備戰區球員 (matchPlayers)
		List<MatchPlayerDTO> resetMatchPlayers = setPlayerMatchService.returnPlayers(resetGroupNum, battlePlayers, matchPlayers);
		// 將對戰區的刪掉。
		Map<Integer, List<MatchPlayerDTO>> resetBattlePlayers = setPlayerMatchService.closeBattle(resetGroupNum, battlePlayers);
		
		
		// Step3. 重新執行對戰編排：
		// =====================================================================================================================================
		// 設定等級差距容忍程度：2
		List<MatchPlayerDTO> newBattle = setPlayerMatchService.arrangePlayerWithLevel(resetMatchPlayers, 2);
		
		// 用舊的編號重新執行對戰：
		Map<Integer, List<MatchPlayerDTO>> newBattlePlayer = setPlayerMatchService.arrangeBattle(resetGroupNum, newBattle);
		resetBattlePlayers.put(resetGroupNum, newBattlePlayer.get(resetGroupNum));
		
		
		// Step4. 顯示對戰紀錄：
		// =====================================================================================================================================
		System.out.println("重新編排成員紀錄，固定等級差距：" + 2);
		for(Integer i:battlePlayers.keySet()) {
			for(MatchPlayerDTO mp : battlePlayers.get(i)) {
				System.out.printf("隊伍編號(%s)-成員(%s)%n",
						i, mp.getUserName());
			}
		}
		
		
		// Step5. 推送與重新推送
		// =====================================================================================================================================
		// 首先要設計排序規則：
		Comparator<MatchPlayerDTO> cmpMP = (MatchPlayerDTO mp1, MatchPlayerDTO mp2) -> {
			return mp1.getTotalMatch() - mp2.getTotalMatch();
		};
		Collections.sort(resetMatchPlayers, cmpMP);
		// 重新加入 session
		session.setAttribute("matchPlayers", resetMatchPlayers);
		session.setAttribute("battlePlayers", resetBattlePlayers);
		
		return "redirect:/match/teamlist/" + teamId + "#reset_" + resetGroupNum;
	}
}
