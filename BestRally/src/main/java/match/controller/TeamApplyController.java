package match.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import match.exception.TeamApplicationException;
import match.exception.TeamException;
import match.exception.TeamPlayerException;
import match.exception.TeamRefreshException;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamDTO;
import match.model.dto.TeamsOfPlayerDTO;
import match.model.dto.UserCertDTO;
import match.service.TeamApplicationService;
import match.service.TeamPlayerService;
import match.service.TeamReadService;
import match.util.EmailService;

@Controller
@RequestMapping("/team")
public class TeamApplyController {
	
	@Autowired
	private TeamReadService teamReadService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private TeamPlayerService teamPlayerService;
	@Autowired
	private TeamApplicationService teamApplicationService;
	
	
	// 向球隊隊長申請
	@GetMapping("/apply/{teamId}")
	@Transactional
	public String applyTeam(
			@PathVariable Integer teamId, 
			HttpSession session, 
			Model model, 
			HttpServletRequest request
			) throws TeamException, TeamPlayerException {
		
		// 回報訊息：
		String message = null;
		
		// Step1. 確認使用者還沒參加此球隊：
		if(session.getAttribute("teamDTOs") != null) {
			List<TeamDTO> teamDTOs = (List<TeamDTO>) session.getAttribute("teamDTOs");
			
			// 確認使用者所參與的球隊是否與申請球隊一樣。
			Optional<TeamDTO>  optTeamDTO = teamDTOs.stream().filter(p->p.getId()==teamId).findAny();
			if(optTeamDTO.isPresent()) {
				 message = String.format("你已經加入此球隊(%s)編號(%s)與申請編號(%s)一樣。",
						 optTeamDTO.get().getTeamName(), optTeamDTO.get().getId(), teamId);
				
				 // 回傳錯誤訊息
				 System.err.printf("你已經加入此球隊(%s)編號(%s)與申請編號(%s)一樣。",
						 optTeamDTO.get().getTeamName(), optTeamDTO.get().getId(), teamId);
				 throw new TeamPlayerException(message);
			}else {
				System.out.println("TeamApplyController: 使用者還沒加入使用者。");
			}
		}
		
		
		// Step2. 呼叫這個隊伍的隊長(Player)資料：
		// throws TeamException，即將交給 @ExceptionHandler 處理
		TeamDTO applyTeamDTO = teamReadService.findTeamByTeamId(teamId);
		// 隊長的 email:
		String capEmail = applyTeamDTO.getPlayer().getUser().getEmail();
		
		
		// Step3. 取得登入使用者的 playerId
		// 因為會用 filter 確認進入此頁面時需要登入，且要有 playerDTO 身份，
		// 因此不用再次確認 session 當中是否有 player (UserLogin 就會加入 session)
		PlayerDTO playerDTO = (PlayerDTO) session.getAttribute("playerDTO");
		Integer playerId = playerDTO.getId();
		
		
		// Step4. 發送 email:
		// 建立申請 email 連結：
		String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
		String applyLink = baseUrl + "/team/accept/" + teamId + "?playerId=" + playerId;
		
		// 取得 User:
	    UserCertDTO userCertDTO = (UserCertDTO)session.getAttribute("userCertDTO");
		
		try {
			// Email 傳送的 Service
		    emailService.sendEmail(capEmail, applyLink);
		    System.out.println("TeamApplyController: 發送成功。");
		    
		    // 建立 teamApplication 申請資料紀錄：
		    teamApplicationService.addApplication(userCertDTO, playerDTO, applyTeamDTO);
		    System.out.println("TeamApplyController: 球隊申請紀錄儲存。");
		    
		    // 申請成功，回到列表。
		    return "redirect:/team";
		    
		// 球隊申請紀錄出問題
		}catch (TeamApplicationException ta) {
			ta.printStackTrace();
			message = String.format("申請球隊錯誤，球員(%s)申請球隊(%s)出問題：%n%(s)%n",
					userCertDTO.getUsername(), applyTeamDTO.getTeamName(), ta.getMessage());
			System.err.println(message);
			throw new TeamPlayerException(message);
			
		// email 出問題
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("申請球隊錯誤：發送申請email時發生錯誤："+applyLink +"\n"+ e.getMessage());
			message = "申請球隊錯誤：發送申請email時發生錯誤："+applyLink + "\n" + e.getMessage();
			throw new TeamPlayerException(message);
		}
	}
	
	
	
	// 隊長接收請求：
	@GetMapping("/accept/{teamId}")
	@Transactional
	public String acceptPlayer(
			@PathVariable Integer teamId, 
			@RequestParam Integer playerId,
			HttpSession session)throws TeamRefreshException, TeamPlayerException {
		
		// Step1. 確認加入球隊
		// 執行加入球隊：
		teamPlayerService.addTeamPlayer(teamId, playerId);
		
		// 將 teamApplication 紀錄刪除。
		try {
			teamApplicationService.dropApplication(teamId, playerId);
			
		} catch (TeamApplicationException e) {
			e.printStackTrace();
			System.err.println("隊長接受申請Controller，刪除申請紀錄失敗:"+e.getMessage());
			throw new TeamPlayerException("隊長接受申請Controller，刪除申請紀錄失敗:"+e.getMessage());
		}
		
		// Step2. 重新推送 Team 到目前 session：
		// 取得目前 session 中的 playerId:
		PlayerDTO playerDTO = (PlayerDTO)session.getAttribute("playerDTO");
		
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
		
		// 回首頁：(之後要改成隊長管理球隊頁面)
		return "redirect:/teamplayer/list/" + teamId;
	}
	
	
	// 使用者主頁，如果是隊長就可以進入球隊管理，
	// 管理的時候可以看到有球員申請加入球隊，
	// 這個時候可以點擊拒絕按鈕將使用者申請駁回。
	
	@GetMapping("/reject/{teamId}")
	public String rejectApply(@PathVariable Integer teamId, @RequestParam Integer applyId) {
		// 直接刪除
		teamApplicationService.dropApplication(applyId);
		// 回到原處。
		return "redirect:/teamplayer/list/" + teamId;
	}
}
