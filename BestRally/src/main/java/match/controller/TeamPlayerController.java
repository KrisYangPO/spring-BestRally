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
import match.exception.TeamApplicationException;
import match.exception.TeamPlayerException;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamApplicationDTO;
import match.model.dto.TeamPlayerDTO;
import match.repository.TeamApplicationRepository;
import match.service.TeamApplicationService;
import match.service.TeamPlayerService;

@Controller
@RequestMapping("/teamplayer")
public class TeamPlayerController {
	
	@Autowired
	private TeamPlayerService teamPlayerService;
	@Autowired
	private TeamApplicationService teamApplicationService ;
	
	// 前往特定球隊球員列表。
	@GetMapping("/list/{teamId}")
	public String getTeamPlayerList(@PathVariable Integer teamId, Model model, HttpSession session) throws TeamPlayerException {
		// Step1. 找出這個 teamId 的 teamPlayer:
		List<TeamPlayerDTO> teamPlayerDTOs = teamPlayerService.findByTeamId(teamId);
		
		// Step2. 篩掉這個使用者的 teamPlayerDTOs:
		// 先找出自己的 playerId
		PlayerDTO playerDTO = (PlayerDTO)session.getAttribute("playerDTO");
		
		// 將不是自己的 teamPlayerDTO 保留下來。
		List<TeamPlayerDTO> noCapTeamPlayerDTOs = teamPlayerDTOs.stream()
				.filter(p->p.getPlayer().getId() != playerDTO.getId())
				.collect(Collectors.toList());
		
		// Step3. 顯示目前球隊申請：
		List<TeamApplicationDTO> teamApplicationDTOs = null;
		try {
			teamApplicationDTOs = teamApplicationService.findByTeamId(teamId);
			
		} catch (TeamApplicationException e) {
			System.err.println("使用者主頁管理球隊，目前沒有任何球員申請此球隊。");
		}
		
		// Step4. 將這個隊伍的 teamPlayer 儲存至 teamPlayerDTOs 並推送
		model.addAttribute("teamPlayerDTOs", noCapTeamPlayerDTOs);
		model.addAttribute("teamApplicationDTOs", teamApplicationDTOs);
		model.addAttribute("teamId", teamId);
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
