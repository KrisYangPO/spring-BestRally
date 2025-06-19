package match.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import match.exception.PlayerException;
import match.model.dto.MatchPlayerDTO;
import match.model.dto.PlayerDTO;
import match.service.PlayerService;


@Controller
@RequestMapping("/match")
public class MatchPlayerController {
	
	@Autowired
	private PlayerService playerService;
	
	// 將球隊名冊人員加入備戰隊伍(matchPlayers)
	@GetMapping("/teamplayers/add/{teamId}")
	public String addMatchPlayer(
			@PathVariable Integer teamId, 
			@RequestParam Integer playerId, 
			Model model, 
			HttpSession session) throws PlayerException {
		
		// Step1. 建立對戰Session表單，並確認對戰Session表單是否已經建立好，
		// ====================================================================================================================
		// 如果已經建立好就要沿用舊的對戰 Session 表單
		
		// 建立初始的對戰表單：
		List<MatchPlayerDTO> matchPlayers = null;
		
		// 確認是否已經有對戰表單：
		if(session.getAttribute("matchPlayers") == null) {
			matchPlayers = new ArrayList<>();	
		}
		// 如過 Session 中的 matchPlayers 不是空值，
		// 表示對戰表單已經有球員加入，所以要從 Session 中取出來，
		// 給下面程式繼續加入球員：
		else {
			// 從 session 中取出 "matchPlayers" 他的資料型態是 List<PlayerDTO>
			// 需要從 session 物件轉型成：List<PlayerDTO>
			matchPlayers = (List<MatchPlayerDTO>) session.getAttribute("matchPlayers"); 
			// 設計排序規則：
			Comparator<MatchPlayerDTO> cmpMP = (MatchPlayerDTO mp1, MatchPlayerDTO mp2) -> {
				return mp1.getTotalMatch() - mp2.getTotalMatch();
				};
				
			Collections.sort(matchPlayers, cmpMP);
		}
		
		// Step2. 取得 BattlePlayers
		// ====================================================================================================================
		Map<Integer, List<MatchPlayerDTO>> battlePlayers = null;
		if(session.getAttribute("battlePlayers") != null) {
			battlePlayers = (Map<Integer, List<MatchPlayerDTO>>)session.getAttribute("battlePlayers");
		}
		
		// Step3.檢查 player 是否已經存在備戰隊伍和對戰隊伍
		// ====================================================================================================================
		// 建立 MatchPlayerDTO 身份，
		PlayerDTO playerDTO = playerService.findPlayerByPlayerId(playerId);
		
		// 在將 playerDTO 加入 matchPlayers 時，要先確認是否已經重複加入：
		boolean exists = matchPlayers.stream().anyMatch(p -> p.getPlayerId() == playerId);
		if (exists) {
		    model.addAttribute("resultTitle", "球員加入對戰失敗");
		    model.addAttribute("resultMessage", String.format("隊伍(%s)的球員(%s)已經加入備戰表中。",teamId, playerId));
		    return "result";
		}
		
		// 確認 playerDTO 是否已經加入 battlePlayers
		// playerDTO 不在 matchPlayers 列表可能是因為已經加入 battlePlayers:
		if(battlePlayers != null) {
			for(Integer i : battlePlayers.keySet()) {
				List<MatchPlayerDTO> mpList = battlePlayers.get(i);
				boolean exists2 = mpList.stream().anyMatch(p -> p.getPlayerId().equals(playerId));
				if (exists2) {
					model.addAttribute("resultTitle", "球員加入對戰失敗");
					model.addAttribute("resultMessage", String.format("隊伍(%s)的球員(%s)已經加入對戰表中。",teamId, playerId));
				    return "result";
				}
			}
		}
		
		// Step4. 建立 MatchPlayerDTO
		// ====================================================================================================================
		MatchPlayerDTO matchPlayerDTO = new MatchPlayerDTO(
				playerId, playerDTO.getUserId(), teamId, playerDTO.getUsername(), playerDTO.getLevel());
		
		// 將新的 matchPlayer 加入備戰隊伍：
		matchPlayers.add(matchPlayerDTO);
		// 重新推送 MatchPlayers 到 session：
		session.setAttribute("matchPlayers", matchPlayers);
		
		// 重新導向至: match_teamPlayerList
		return "redirect:/match/teamlist/" + teamId + "#" + playerId;
	}
	
	
	// 從備戰區刪除球員：
	@GetMapping("/teamplayers/remove/{teamId}")
	public String removeMatchPlayer(
			@PathVariable Integer teamId, @RequestParam Integer playerId,
			HttpSession session, Model model) {
		
		// 刪除備戰區與對戰區球員，重新整理：
		if(playerId == 0000) {
			session.removeAttribute("matchPlayers");
			session.removeAttribute("battlePlayers");
			return "redirect:/match/teamlist/" + teamId;
		}
		
		// 將特定 playerId 刪除：
		// 找出所有 Session 當中的 matchPlayers
		List<MatchPlayerDTO> matchPlayers = (List<MatchPlayerDTO>)session.getAttribute("matchPlayers");
		// 找到特定 MatchPlayerDTO
		MatchPlayerDTO matchPlayer = matchPlayers.stream().filter(mp -> mp.getPlayerId() == playerId).findFirst().orElse(null);
		// 找到他在 MatchPlayers 裡的哪個 index
		int idx = matchPlayers.indexOf(matchPlayer);
		// 刪除這個 index 資訊
		matchPlayers.remove(idx);
		
		// 重新載入Session：
		session.setAttribute("matchPlayers", matchPlayers);
		
		return "redirect:/match/teamlist/" + teamId + "#removePlayer";
	}
	
}
