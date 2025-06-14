package match.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import match.exception.PlayerException;
import match.model.dto.UserDTO;
import match.service.PlayerService;

@Controller
@RequestMapping("/player")
public class PlayerController {

	@Autowired
	private PlayerService playerService;
	
	// 前進 player 頁面
	@GetMapping
	public String getPlayerPage() {
		return "player";
	}
	
	// 註冊 player:
	@PostMapping("/register")
	public String addPlayer(@RequestParam Integer level, HttpSession session) throws PlayerException {
		// 取得 userDTO
		UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
		
		// 如果使用者沒有 playerDTO 身份，就執行新增：
		if(session.getAttribute("playerDTO") == null) {
			playerService.addPlayer(userDTO.getId(), level);
		}
		// 如果有就執行更新：
		else {
			playerService.updatePlayerLevel(userDTO.getId(), level);
		}
		// 更新完回到使用者主頁，可以觀察 player Level 是否被更新：
		return "redirect:/user/home";
	}
}
