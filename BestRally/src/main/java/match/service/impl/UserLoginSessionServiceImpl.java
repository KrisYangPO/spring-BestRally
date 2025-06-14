package match.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import match.exception.PlayerException;
import match.exception.TeamPlayerException;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamsOfPlayerDTO;
import match.model.dto.UserCertDTO;
import match.service.PlayerService;
import match.service.TeamPlayerService;
import match.service.UserLoginSessionService;

@Service
public class UserLoginSessionServiceImpl implements UserLoginSessionService{
	
	@Autowired
	private PlayerService playerService;
	@Autowired
	private TeamPlayerService teamPlayerService;

	
	// 檢查使用者是否有 Player 身份：
	@Override
	public PlayerDTO checkLoginPlayer(UserCertDTO userCertDTO) {
		try {
			// 透過 UserID 找出 playerDTO
			return playerService.findPlayerByUserId(userCertDTO.getId());
		
		// 如果使用者沒有 player 身份，就回傳 null
		}catch (PlayerException e) {
			System.err.println("登入Session：使用者沒有Player身份。\n" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	
	// 檢查使用者是否有 Team：
	@Override
	public TeamsOfPlayerDTO checkLoginTeam(PlayerDTO playerDTO) {
		try {
			// 透過 playerId 尋找 teamPlayer，就會是球隊球員。
			return teamPlayerService.getTeamsFromPlayer(playerDTO.getId());
		
		// 如果出錯或是找不到就直接出例外捕捉
		} catch (TeamPlayerException e) {
			e.printStackTrace();
			System.err.println("登入Session：使用者沒有加入任何球隊。\n" + e.getMessage());
			return null; 
		}
	}

}
