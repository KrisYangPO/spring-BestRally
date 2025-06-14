package match.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import match.exception.TeamException;
import match.exception.TeamPlayerException;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamDTO;
import match.service.TeamReadService;
import match.service.TeamUpdateService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping("/team")
public class TeamController {
	
	@Autowired
	private TeamReadService teamReadService;
	@Autowired
	private TeamUpdateService teamUpdateService;
	
	
	// 顯示所有球隊列表
	@GetMapping
	public String getAllTeams(Model model) {
		// 直接回傳資料庫所有 team 內容：
		model.addAttribute("teamDTOs", teamReadService.findAllTeams());
		return "team_list";
	}
	
	// 建立球隊
	@PostMapping("/create")
	public String addTeam(@ModelAttribute TeamDTO teamDTO, HttpSession session) throws TeamException, TeamPlayerException {
		
		// Step1. 先取得登入使用者的 playerDTO:
		PlayerDTO playerDTO = (PlayerDTO) session.getAttribute("playerDTO");
		Integer playerId = playerDTO.getId();
		
		// Step2. 新增球隊，當中會執行新增球隊 + 新增球隊球員 + 球隊更新 Service
		teamUpdateService.addTeam(playerId, teamDTO);
		
		// 回到使用者主頁觀察剛剛建立的球隊
		return "redirect:/user/home";
	}
	
	// 更新球隊：
	// 前往更新球隊頁面：
	@GetMapping("/update")
	public String getTeamUpdate() {
		return "team_update";
	}
	
	// 執行更新：
	@PutMapping("/update/{teamId}")
	public String putMethodName(@PathVariable Integer teamId, @ModelAttribute TeamDTO teamDTO) {
		
	}
	
}
