package match.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import match.exception.TeamPlayerException;
import match.model.dto.PlayersOfTeamDTO;
import match.model.dto.TeamDTO;
import match.service.PlayerService;
import match.service.TeamPlayerService;

@Controller
@RequestMapping("/match")
public class MatchController {
	
	@Autowired
	private TeamPlayerService teamPlayerService;

	// 顯示目前的球隊：
	@GetMapping("/teamlist")
	public String getTeamList(HttpSession session, Model model) {
		// filter 確定目前 /match 網址下，
		// 所有頁面都需要有 user, player, teamDTOs 的資訊才能進入。
		List<TeamDTO> teamDTOs = (List<TeamDTO>) session.getAttribute("teamDTOs");
		model.addAttribute("teamDTOs", teamDTOs);
		return "match_teamList";
	}
	
	
	// 前往今日比賽隊伍的球隊：
	@GetMapping("/teamlist/{teamId}")
	public String getMatchTeam(@PathVariable Integer teamId, Model model, HttpSession session) throws TeamPlayerException {
		// 取得所有這個球隊的所有成員
		PlayersOfTeamDTO playersOfTeamDTO = teamPlayerService.getPlayersFromTeam(teamId);
		// 回傳資訊：
		model.addAttribute("playerOfTeamDTO", playersOfTeamDTO);
		model.addAttribute("teamId", teamId);
		
		return "match_teamplayerlist";
	}	
}
