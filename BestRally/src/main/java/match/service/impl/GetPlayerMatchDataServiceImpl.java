package match.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import match.exception.TeamPlayerException;
import match.model.dto.MatchPlayerDTO;
import match.service.GetPlayerMatchDataService;
import match.service.TeamPlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GetPlayerMatchDataServiceImpl implements GetPlayerMatchDataService {
	
	@Autowired
	private TeamPlayerService teamPlayerService; // 透過 matchPlayers 的 player_id 找 PlayerDTO 物件
	// Logger
	private static final Logger log = LoggerFactory.getLogger(GetPlayerMatchDataService.class);
	
	// 由 session 取得對戰結束後的 matchPlayers 所有當日參賽球員。
	@Override
	@Transactional
	public void GetMatchResult(MatchPlayerDTO matchPlayerDTO) throws TeamPlayerException {
		// 取得 MatchPlayerDTO 的所有資訊，直接更新：
		teamPlayerService.updateTeamPlayerMatchData(
				matchPlayerDTO.getTeamId(),
				matchPlayerDTO.getPlayerId(),
				matchPlayerDTO.getWinGame(), 
				matchPlayerDTO.getTotalMatch());
		
		// log 紀錄
		String message = String.format("GetPlayerMatchService:%n 更新球員(%s)本次對戰紀錄。%n勝場(%s)總場次(%s)勝率(%s)",
				matchPlayerDTO.getUserName(), matchPlayerDTO.getWinGame(), matchPlayerDTO.getTotalMatch(), matchPlayerDTO.getOneWinRate());
		
		log.info(message);
	
	}// end of GetMatchResult()
}
