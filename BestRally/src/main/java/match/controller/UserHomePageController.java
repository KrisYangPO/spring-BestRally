package match.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import match.exception.UserException;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamDTO;
import match.model.dto.UserDTO;
import match.model.dto.UserRegisterDTO;
import match.service.UserService;
import match.util.PhotoStorage;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


/** UserHomePage 顯示使用者資訊
 *  使用者主頁會顯示 user 的資料
 *  user 的球員資訊。
 *  user 參加球隊的隊伍資訊，
 *  
 *  這裡判斷：如果使用者 playerId==capId (teamDTO.player().getId) 
 *  就表示這個隊伍的隊長是這個使用者，並顯示這個球隊更新資訊的連結。
 */

@Controller
@RequestMapping("/user")
public class UserHomePageController {
	
	@Autowired
	private UserService userService;
	
	// 取得主頁資訊
	@GetMapping("/home")
	public String getUserHome(HttpSession session, Model model) {
		
		// 照理來說在執行 UserLoginController 時，已經將 UserCertDTO, PlayerDTO, TeamDTOs 傳入 Session，
		// 因此這裡不用再次從 session 中呼叫任何資訊，
		
		// 但是需要根據這個使用者是否為 "隊長" 判斷球隊欄位是否要加入隊伍更新數據的內容。
		// 因此要將 team 隊長 id 取出，看看隊長 id 與這個使用者的 playerId 是否一致。
		// 將這個隊長為此使用者的隊伍編號蒐集起來。
		if(session.getAttribute("playerDTO") != null && session.getAttribute("teamDTOs") != null) {
			
			// Step1. 呼叫使用者的 playerId:
			PlayerDTO playerDTO = (PlayerDTO) session.getAttribute("playerDTO");
			// 呼叫使用者所參與的球隊:
			List<TeamDTO> teamDTOs = (List<TeamDTO>) session.getAttribute("teamDTOs");
			
			// Step2. 判斷使用者 playerId 是否等於球隊隊長id:
			Set<Integer> captainTeamIds = teamDTOs.stream()
					.filter(p->p.getPlayer().getId() == playerDTO.getId()) // 如果隊長id == 使用者playerId 就是隊長。
					.map(p->p.getId()) // 將這個隊伍的 teamId 存到 set 當中。
					.collect(Collectors.toSet());
			
			// 將隊長資訊推送到 JSP 
			session.setAttribute("captainTeamIds", captainTeamIds);
		}

		// 推送 session 內容與 captainTeamIds 到使用者主頁 (user_home.jsp)。
		return "user_home";
	}
	
	
	// 前往 UserUpdate 連結：
	/* 在更新表單時，可以顯示初始值，因此整個表單不用全部更新，只要更新你想要的就可以送出。
	 * 顯示表單時，可以利用 JSP 的 ModelAttribute 顯示 UserDTO，
	 * 因此這裡需要從 session 當中找出 UserDTO，並推送至 user_update
	 */
	@GetMapping("/home/update")
	public String getUpdatePage(HttpSession session, Model model) {
		
		// 1. 取得 UserDTO 
		UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
		
		// 2. 直接發送：
		model.addAttribute("userDTO", userDTO);
		
		// 重新回到更新頁，以免還有其他要更新。
		return "user_update";
	}
	
	
	// 取得更新表單：
	@PutMapping("/home/update")
	public String updateUser(
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam String email,
			@RequestParam MultipartFile photo,
			HttpSession session
			) throws UserException, IOException {
		
		// Step1. photo 要以 MultipartFile 格式帶入，再轉成 byte[] 再轉 String 儲存至 DTO
		String photoFile = PhotoStorage.MultipartFileToBase64(photo);
		
		// Step2. 建立新的更新資料：
		UserRegisterDTO userRegisterDTO = new UserRegisterDTO(username, password, email, photoFile);
		
		// Step3. 取得 UserId
		UserDTO userDTO = (UserDTO)session.getAttribute("userDTO");
		Integer userId = userDTO.getId();
		// 執行更新
		userService.updateUser(userId, userRegisterDTO);
		
		// 2. 註冊成功，回到首頁。
		return "redirect:/"; 
	}
	
}
