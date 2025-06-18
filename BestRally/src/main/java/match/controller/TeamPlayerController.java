package match.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import match.exception.TeamPlayerException;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamPlayerDTO;
import match.service.TeamPlayerService;

@Controller
@RequestMapping("/teamplayer")
public class TeamPlayerController {
	
	@Autowired
	private TeamPlayerService teamPlayerService;
	
	// 前往特定球隊球員列表。
	@GetMapping("/list/{teamId}")
	public String getTeamPlayerList(@PathVariable Integer teamId, Model model, HttpSession session) throws TeamPlayerException {
		// 找出這個 teamId 的 teamPlayer:
		List<TeamPlayerDTO> teamPlayerDTOs = teamPlayerService.findByTeamId(teamId);
		
		// 篩掉這個使用者的 teamPlayerDTOs:
		// 先找出自己的 playerId
		PlayerDTO playerDTO = (PlayerDTO)session.getAttribute("playerDTO");
		
		// 將不是自己的 teamPlayerDTO 保留下來。
		List<TeamPlayerDTO> noCapTeamPlayerDTOs = teamPlayerDTOs.stream()
				.filter(p->p.getPlayer().getId() != playerDTO.getId())
				.collect(Collectors.toList());
		
		// 將這個隊伍的 teamPlayer 
		model.addAttribute("teamPlayerDTOs", noCapTeamPlayerDTOs);
		return "teamplayer_list";
	}
	
	
	// 刪除球員：
	@DeleteMapping("/list/delete/{teamId}")
	public String deleteTeamPlayer(
			@PathVariable Integer teamId, 
			@RequestParam Integer teamPlayerId) throws TeamPlayerException {
		// 直接執行刪除：
		teamPlayerService.removeTeamPlayer(teamPlayerId);
		return "redirect:/teamplayer/list/" + teamId;
	}
}
