package match.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import match.exception.TeamApplicationException;
import match.exception.UserException;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamDTO;
import match.model.dto.UserCertDTO;
import match.service.TeamApplicationService;
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
	@Autowired
	private TeamApplicationService teamApplicationService;
	
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
			
			// Step3. 挑出這個 teamId 的申請列表，並且計算總和：
			Map<Integer, Integer> teamApplys = new HashMap<>();
			for(Integer id:captainTeamIds) {
				// 計算申請人數：
				int count = 0;
				try {
					count = teamApplicationService.findByTeamId(id).size();
				} catch (TeamApplicationException e) {
					System.err.println("userHome: 球隊目前沒有人申請。");
				}
				teamApplys.put(id, count);
			}
			
			// 將隊長資訊推送到 JSP 
			session.setAttribute("captainTeamIds", captainTeamIds);
			session.setAttribute("teamApplys", teamApplys);
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
		UserCertDTO userCertDTO = (UserCertDTO) session.getAttribute("userCertDTO");
		// 2. 直接發送：
		model.addAttribute("userCertDTO", userCertDTO);
		// 重新回到更新頁，以免還有其他要更新。
		return "user_update";
	}
	
	
	@PostMapping("/home/update")
	public String updateUserInfo(
	        @RequestParam(required = false) String username,
	        @RequestParam(required = false) String rowPassword,
	        @RequestParam(required = false) String email,
	        @RequestParam(required = false) MultipartFile photofile,
	        HttpSession session
	    ) throws UserException, IOException {
		
		// Step1. 取得登入者的 userId
	    UserCertDTO userCertDTO = (UserCertDTO) session.getAttribute("userCertDTO");
	    Integer userId = userCertDTO.getId();

	    // Step2. 判斷哪個資料需要被更新。
	    if (username != null && !username.isBlank()) {
	        userService.updateUserName(userId, username);
	        userCertDTO.setUsername(username);
	        session.setAttribute("userCertDTO", userCertDTO);
	    }

	    if (rowPassword != null && !rowPassword.isBlank()) {
	        userService.updateUserPassword(userId, rowPassword);
	    }

	    if (email != null && !email.isBlank()) {
	        userService.updateUserEmail(userId, email);
	        userCertDTO.setEmail(email);
	        session.setAttribute("userCertDTO", userCertDTO);
	    }

	    if (photofile != null && !photofile.isEmpty()) {
	        String photo = PhotoStorage.MultipartFileToBase64(photofile);
	        userService.updateUserPhoto(userId, photo);
	        userCertDTO.setPhoto(photo);
	        session.setAttribute("userCertDTO", userCertDTO);
	    }

	    return "redirect:/user/home";
	}
}
