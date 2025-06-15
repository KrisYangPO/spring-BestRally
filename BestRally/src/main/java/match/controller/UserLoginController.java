package match.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import match.exception.UserCertException;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamDTO;
import match.model.dto.UserCertDTO;
import match.service.UserCertService;
import match.service.UserLoginSessionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

/** Login Controller:
 *  invokes: userCertService, userLoginSessionService
 * 	使用者點擊登入，進入頁面，
 *  表單輸入帳號密碼，執行 userCertService 判斷。
 *  確認登入就可以將登入憑證 (userCertDTO) 儲存在 session。完成登入
 *  
 *  userLoginSessionService 檢查 user 有沒有 player 和 team 資訊
 *  無論有沒有，都直接將資訊傳向前端，這樣就可以用 filter 判斷 player 和 team 的資訊，
 *  如果沒有 player 或是 team 資料，就會回傳 null，這會由 Filter 或是 JSP 進行判斷。
 *  這裡不會產生例外是因為 "尋找不到資料" 的例外已經在 UserLoginSession 處理。
 */

@Controller
@RequestMapping("/user")
public class UserLoginController {
	
	@Autowired
	private UserCertService userCertService;
	@Autowired
	private UserLoginSessionService userLoginSessionService;
	
	// 取得登入頁面：(類似 doGet 點擊進入登入)
	// return "loging" 就是 req.getRequestDispatcher("/login.jsp").forward() 的意思。
	// 點擊登入頁面後就會執行這個 controller:getLoing() 方法到登入頁面。
	@GetMapping("/login")
	public String getLogin() {
		return "user_login";
	}
	
	// 取得表單資訊，讀取 username, password，
	@PostMapping("/login")
	public String loginGetCert(
			@RequestParam String username, 
			@RequestParam String password,
			Model model, HttpSession session) {
		
		// Step1. 登入使用者：
		// -------------------------------------------------------------------------------------------
		// 執行 userCertService 的 loginGetCert 方法，驗證使用者和密碼，
		// 如果成功執行就會回傳 UserCertDTO 物件，錯誤就回傳 UserCertException.
		try {
			UserCertDTO userCertDTO = userCertService.loginGetCert(username, password);
			
			// Step2. 檢查是否有 player 和 team 資訊：
			// -------------------------------------------------------------------------------------------
			// 檢查有沒有 Player：
			PlayerDTO playerDTO = userLoginSessionService.checkLoginPlayer(userCertDTO);
			
			// 如果有 player 再看看有沒有 team 資訊
			List<TeamDTO> teamDTOs = null;
			if(playerDTO != null) {
				teamDTOs = userLoginSessionService.checkLoginTeam(playerDTO).getTeamDTOs();
			}
			
			// Step3. 儲存資訊到 session：
			// -------------------------------------------------------------------------------------------
			// 將登入憑證(UserCertDTO)，球員身份(playerDTO)，球隊資訊(teamDTOs)儲存到當前 session，
			// 這樣就可以透過 filter 判斷登入狀態與權限。
			session.setAttribute("userCertDTO", userCertDTO);
			session.setAttribute("playerDTO", playerDTO);
			session.setAttribute("teamDTOs", teamDTOs);
		
		// 登入失敗時：
		}catch (UserCertException e) {
			e.printStackTrace();
			String message = String.format("UserLoginService: 登入失敗。%n%s", e.getMessage());
			model.addAttribute("message", message);
			return "error_report";
		}
		
		// 通知瀏覽器執行首頁
		return "redirect:/";
	}
	
	
	// 登出使用者
	@GetMapping("/logout")
	public String logout(HttpSession session, Model model) {
		
		// 先確認目前是否有人登入，如果沒有人登入就不用登出。
		// 但是也可以靠 JSP 直接判斷 session 顯示登出按鈕：
		if(session.getAttribute("userCertDTO") == null) {
			String message = "目前沒有任何人登入，登出無效。";
			model.addAttribute("message", message);
			return "error_report";
		}
		
		// 直接刪除 session
		session.invalidate();
		return "/";
	}
}
