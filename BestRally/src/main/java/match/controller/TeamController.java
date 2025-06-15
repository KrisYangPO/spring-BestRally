package match.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import org.springframework.web.bind.annotation.PathVariable;

/** 球隊 CRUD
 *  新增，更新，查詢，刪除。
 */

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
		// Step1. 先取得登入使用者的 playerDTO
		// 誰建立球隊誰就是隊長。
		PlayerDTO playerDTO = (PlayerDTO) session.getAttribute("playerDTO");
		Integer playerId = playerDTO.getId();
		
		// Step2. 新增球隊，當中會執行新增球隊 + 新增球隊球員 + 球隊更新 Service
		teamUpdateService.addTeam(playerId, teamDTO);
		
		// 回到使用者主頁觀察剛剛建立的球隊
		return "redirect:/user/home";
	}
	
	// 更新球隊：
	// 前往更新球隊頁面：
	@GetMapping("/update/{teamId}")
	public String getTeamUpdate(@PathVariable Integer teamId, Model model) throws TeamException {
		// 尋找特定 teamId
		TeamDTO teamDTO = teamReadService.findTeamByTeamId(teamId);
		model.addAttribute("teamDTO", teamDTO);
		
		// 將目標 teamId 傳給 JSP 使用
		return "team_update";
	}
	
	// 執行更新：
	@PutMapping("/update/{teamId}")
	public String updateTeam(
			@PathVariable Integer teamId, 
			@RequestParam String teamName,
			@RequestParam String place,
			@RequestParam Boolean recruit,
			@RequestParam Integer playerId) throws TeamException {
		
		// 這裡不能直接帶入 session Player，因為可能要更新隊長。
		// 更新：
		teamUpdateService.updateTeam(teamId, teamName, place, recruit, playerId);
		
		// 更新完回主頁觀察：
		return "redirect:/user/home";
	}
	
	// 刪除球隊
	@DeleteMapping("/delete/{teamId}")
	public String deleteTeam(@PathVariable Integer teamId) throws TeamException {
		teamUpdateService.removeTeam(teamId);
		// 回首頁
		return "redirect:/";
	}
}
