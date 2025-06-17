package match.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import match.exception.PlayerException;
import match.exception.TeamPlayerException;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamDTO;
import match.model.dto.TeamsOfPlayerDTO;
import match.model.dto.UserCertDTO;
import match.model.dto.UserDTO;
import match.service.PlayerService;
import match.service.TeamPlayerService;

@Controller
@RequestMapping("/player")
public class PlayerController {

	@Autowired
	private PlayerService playerService;
	@Autowired
	private TeamPlayerService teamPlayerService;
	
	// 前進 player 頁面
	@GetMapping("/register")
	public String getPlayerPage() {
		return "player_register";
	}
	
	// 註冊或是更新 player:
	@PostMapping("/register")
	public String addPlayer(@RequestParam Integer level, HttpSession session) throws PlayerException, TeamPlayerException {
		// Step1. 取得 userDTO
		UserCertDTO userCertDTO = (UserCertDTO) session.getAttribute("userCertDTO");
		
		// Step2.
		// 如果使用者沒有 playerDTO 身份，就執行新增：
		if(session.getAttribute("playerDTO") == null) {
			playerService.addPlayer(userCertDTO.getId(), level);
		}
		// 如果有就執行更新：
		else {
			playerService.updatePlayerLevel(userCertDTO.getId(), level);
		}
		
		// Step3. 重新將 playerDTO 找出來，並更新 session 資訊：
		PlayerDTO playerDTO = playerService.findPlayerByUserId(userCertDTO.getId());
		session.setAttribute("playerDTO", playerDTO);
		
		// Step4. 重新推送 Team 到目前 session：
		// 如果有 player 再看看有沒有 team 資訊
		List<TeamDTO> teamDTOs = null;
		if(playerDTO != null) {
			try {
				// 先找這個 player 的所有 teams
				TeamsOfPlayerDTO teamsOfPlayerDTO = teamPlayerService.getTeamsFromPlayer(playerDTO.getId());
				teamDTOs = teamsOfPlayerDTO.getTeamDTOs();
				session.setAttribute("teamDTOs", teamDTOs);
				
			} catch (TeamPlayerException e) {
				e.printStackTrace();
				System.err.println("PlayerController: 球員尚未加入球隊。");
			}
		}
		
		// 更新完回到使用者主頁，可以觀察 player Level 是否被更新：
		return "redirect:/user/home";
	}
}
