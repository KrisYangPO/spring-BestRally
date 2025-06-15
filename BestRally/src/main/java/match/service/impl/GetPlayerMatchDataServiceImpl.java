package match.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import match.exception.TeamPlayerException;
import match.model.dto.MatchPlayerDTO;
import match.service.GetPlayerMatchDataService;
import match.service.TeamPlayerService;

@Service
public class GetPlayerMatchDataServiceImpl implements GetPlayerMatchDataService {

	// 透過 matchPlayers 的 player_id 找 PlayerDTO 物件
	@Autowired
	private TeamPlayerService teamPlayerService;	
	
	// 由 session 取得對戰結束後的 matchPlayers 所有當日參賽球員。
	@Override
	public void GetMatchResult(MatchPlayerDTO matchPlayerDTO) throws TeamPlayerException {
		// 取得 MatchPlayerDTO 的所有資訊，直接更新：
		teamPlayerService.updateTeamPlayerMatchData(
				matchPlayerDTO.getTeamId(),
				matchPlayerDTO.getPlayerId(),
				matchPlayerDTO.getWinGame(), 
				matchPlayerDTO.getTotalMatch());
	}
}
