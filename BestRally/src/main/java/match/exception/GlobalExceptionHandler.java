package match.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	// User 相關 Exception:
	@ExceptionHandler(UserException.class)
	public String getUserException(UserException e, Model model, HttpSession session) {
		// 找 type 跟 message
		String type = e.getClass().getSimpleName();
		String message = e.getMessage();
		
		// 推送至 JSP
		model.addAttribute("message", String.format("[Global]%s: 錯誤訊息-%s", type, message));
		return "error_report";
	}
	
	// User 相關 Exception:
	@ExceptionHandler(UserCertException.class)
	public String getUserCertException(UserCertException e, Model model, HttpSession session) {
		// 找 type 跟 message
		String type = e.getClass().getSimpleName();
		String message = e.getMessage();
		
		// 推送至 JSP
		model.addAttribute("message", String.format("[Global]%s: 錯誤訊息-%s", type, message));
		return "error_report";
	}
	
	// PlayerException:
	@ExceptionHandler(PlayerException.class)
	public String getPlayerException(PlayerException e, Model model, HttpSession session) {
		// 找 type 跟 message
		String type = e.getClass().getSimpleName();
		String message = e.getMessage();
		
		// 推送至 JSP
		model.addAttribute("message", String.format("[Global]%s: 錯誤訊息-%s", type, message));
		return "error_report";
	}
	
	// TeamException:
	@ExceptionHandler(TeamException.class)
	public String getTeamException(TeamException e, Model model, HttpSession session) {
		// 找 type 跟 message
		String type = e.getClass().getSimpleName();
		String message = e.getMessage();
		
		// 推送至 JSP
		model.addAttribute("message", String.format("[Global]%s: 錯誤訊息-%s", type, message));
		return "error_report";
	}

	// TeamPlayerException:
	@ExceptionHandler(TeamPlayerException.class)
	public String getTeamPlayerException(TeamPlayerException e, Model model, HttpSession session) {
		// 找 type 跟 message
		String type = e.getClass().getSimpleName();
		String message = e.getMessage();
		
		// 推送至 JSP
		model.addAttribute("message", String.format("[Global]%s: 錯誤訊息-%s", type, message));
		return "error_report";
	}
	
	// RefreshException:
	@ExceptionHandler(TeamRefreshException.class)
	public String getTeamRefreshException(TeamRefreshException e, Model model, HttpSession session) {
		// 找 type 跟 message
		String type = e.getClass().getSimpleName();
		String message = e.getMessage();
		
		// 推送至 JSP
		model.addAttribute("message", String.format("[Global]%s: 錯誤訊息-%s", type, message));
		return "error_report";
	}
	
	// MatchPlayerException:
	@ExceptionHandler(MatchPlayerException.class)
	public String getMatchPlayerException(MatchPlayerException e, Model model, HttpSession session) {
		// 找 type 跟 message
		String type = e.getClass().getSimpleName();
		String message = e.getMessage();
		
		// 推送至 JSP
		model.addAttribute("message", String.format("[Global]%s: 錯誤訊息-%s", type, message));
		return "error_report";
	}
	
	// 除了自訂例外以外的 Exception
	@ExceptionHandler(Exception.class)
	public String otherException(Exception e, Model model, HttpSession session) {
		// 找 type 跟 message
		String type = e.getClass().getSimpleName();
		String message = e.getMessage();
		
		// 推送至 JSP
		model.addAttribute("message", String.format("[Global]%s: 錯誤訊息-%s", type, message));
		return "error_report";
	}
}
