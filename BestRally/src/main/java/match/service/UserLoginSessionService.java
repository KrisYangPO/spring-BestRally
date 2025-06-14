package match.service;

import match.model.dto.PlayerDTO;
import match.model.dto.TeamsOfPlayerDTO;
import match.model.dto.UserCertDTO;

public interface UserLoginSessionService {
	
	// 檢查登入使用者是否有 player 身份
	PlayerDTO checkLoginPlayer(UserCertDTO userCertDTO);
	
	// 檢查這個 playerDTO 是否有 Team 資訊，有就建立 TeamsOfPLayerDTO
	TeamsOfPlayerDTO checkLoginTeam(PlayerDTO playerDTO);
}
