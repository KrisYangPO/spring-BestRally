package match.filter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** filter: 檢測需要登入使用者的頁面：
 *  使用者主頁：/user/home
 *  對戰系統以下所有：/match/* 
 */

/* SpringBoot 預設會忽略 JavaWeb 內建標籤，像是 @WebFilter，
 * 因此需要在 SpringbootJpaApplication.java 中加入：@ServletComponentScan 這個標籤到他的類別宣告，
 * SpringBoot 才會讀取到 JavaWeb 的內建標籤：@WebFilter (不是 SpringBoot 則會被忽略)。
 */

@WebFilter(urlPatterns = {"/team/apply/*"})// 需要登入才能訪問的路徑。
public class TeamApplyFilter extends HttpFilter {

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
		chain.doFilter(request, response);
	}
}
