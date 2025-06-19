package match.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import match.exception.MatchPlayerException;
import match.exception.TeamPlayerException;
import match.model.dto.MatchPlayerDTO;
import match.service.GetPlayerMatchDataService;

@Controller
@RequestMapping("/match/close")
public class MatchReckonController {
	
	@Autowired
	private GetPlayerMatchDataService getPlayerMatchDataService;
	
	// 點擊結算今日對戰結果
	@GetMapping("{teamId}")
	@Transactional
	public String closeGame(
			@PathVariable Integer teamId,
			Model model,
			HttpSession session
			) throws MatchPlayerException {
		
		// Step1. 取得 MatchPlayers 所有備戰區球員：
		List<MatchPlayerDTO> matchPlayers = (List<MatchPlayerDTO>)session.getAttribute("matchPlayers");
		// 一般來說，這裡面的 matchPlayer 都已經紀錄好所有的對戰數據，像是勝場，總場次，
		// 所以這裡就要想辦法將每個 matchPlayers 的數據傳到後端。
		
		// 要確認 MatchPlayers 存在
		if(matchPlayers == null) {
			throw new MatchPlayerException("MatchReckonController: 結算錯誤：備戰隊伍沒有人。今日沒有比賽。");
		}
		
		// Step2. 將每個 matchPlayer 對戰資訊紀錄至後端：
		for(MatchPlayerDTO mp: matchPlayers) {
			try {
				// 將 matchDTOs 儲存到後端。
				getPlayerMatchDataService.GetMatchResult(mp);
				
			} catch (TeamPlayerException e) {
				e.printStackTrace();
				throw new MatchPlayerException("MatchReckonController: 結算錯誤："+ mp.getUserName() +"\n"+ e.getMessage());
			}
		}
		
		// 將紀錄傳給結算畫面：
		// 進行排序：
		Comparator<MatchPlayerDTO> mpWin = (MatchPlayerDTO mp1, MatchPlayerDTO mp2) -> {
			return mp1.getOneWinRate() < mp2.getOneWinRate()?1:-1; };
			
		// 使用 Collections 進行排序
		Collections.sort(matchPlayers, mpWin);
		model.addAttribute("matchPlayers", matchPlayers);
		return "match_reckon";
	}
}
