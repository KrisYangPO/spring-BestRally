package match.service;

import java.util.List;

import org.springframework.stereotype.Service;

import match.model.dto.PlayerDTO;
import match.model.dto.TeamApplicationDTO;
import match.model.dto.TeamDTO;
import match.model.dto.UserCertDTO;

@Service
public interface TeamApplicationService {
	
	// 找出所有的申請
	List<TeamApplicationDTO> findAllApplication();
	
	// 搜尋特定隊伍的所有申請
	List<TeamApplicationDTO> findByTeamId(Integer teamId);
	
	// 尋找特定球員在隊伍的申請
	TeamApplicationDTO findByTeamAndPlayerId(Integer teamId, Integer playerId);
	
	// 新增申請
	void addApplication(UserCertDTO userCertDTO, PlayerDTO playerDTO, TeamDTO teamDTO);
	
	// 根據id刪除申請
	void dropApplication(Integer teamId, Integer playerId);
	void dropApplication(Integer id);
	
}
