package match.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import match.exception.TeamException;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamDTO;
import match.service.TeamReadService;

/** filter: 檢測需要登入使用者的頁面：
 *  使用者主頁：/user/home
 *  對戰系統以下所有：/match/* 
 */

/* SpringBoot 預設會忽略 JavaWeb 內建標籤，像是 @WebFilter，
 * 因此需要在 SpringbootJpaApplication.java 中加入：@ServletComponentScan 這個標籤到他的類別宣告，
 * SpringBoot 才會讀取到 JavaWeb 的內建標籤：@WebFilter (不是 SpringBoot 則會被忽略)。
 */

@WebFilter(urlPatterns = {"/team/accept/*"})// 需要登入才能訪問的路徑。
public class TeamAcceptFilter extends HttpFilter {
	
	@Autowired
	private TeamReadService teamReadService;

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// 檢查 session 中是否有 userCertDTO
		HttpSession session = request.getSession();
		if(session.getAttribute("userCertDTO") == null) {
			response.sendRedirect("/user/login");
			return;
		}
		// 檢查 session 中是否有 playerDTO
		if(session.getAttribute("playerDTO") == null) {
			response.sendRedirect("/player");
			return;
		}
		
		// 取得申請的球隊編號：
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String uri = httpRequest.getRequestURI(); // 例如 /team/accept/1
		String[] parts = uri.split("/");
		String teamId = parts[parts.length - 1];  // 拿到最後一段為 teamId
		
		if(teamId == null || !teamId.matches("\\d+")) {
			request.setAttribute("message", "球隊編號錯誤：" + teamId);
			request.getRequestDispatcher("/WEB-INF/view/match/error_report.jsp").forward(request, response);
			return;
		}
		
		// 檢查此 player 是否為隊長：
		PlayerDTO playerDTO = (PlayerDTO) session.getAttribute("playerDTO");
		try {
			// 取得這個球員所擔任隊長的球隊。
			List<TeamDTO> teamDTOs = teamReadService.findTeamByCapId(playerDTO.getId());
			
			// 檢查使否有任何球隊。
			if(teamDTOs.isEmpty()) {
				request.setAttribute("message", String.format("目前使用者(%s)不是球隊隊長。", playerDTO.getUsername()));
				request.getRequestDispatcher("/WEB-INF/view/match/error_report.jsp").forward(request, response);
				return;
			}
			
			// 檢查申請球隊編號是否是使用者所管理的隊伍：
			Optional<TeamDTO> optTeamDTO = teamDTOs.stream().filter(p->p.getId()==Integer.parseInt(teamId)).findAny();
			if(optTeamDTO.isEmpty()) {
				request.setAttribute("message", String.format("目前使用者(%s)不是這個球隊(%s)的隊長。", playerDTO.getUsername(), teamId));
				request.getRequestDispatcher("/WEB-INF/view/match/error_report.jsp").forward(request, response);
				return;
			}
			
		} catch (TeamException e) {
			e.printStackTrace();
			request.setAttribute("message", "驗證隊長錯誤。");
			request.getRequestDispatcher("/WEB-INF/view/match/error_report.jsp").forward(request, response);
			return;
		}
		
		chain.doFilter(request, response);
	}
}
