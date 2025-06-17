package match.service;

import java.util.List;
import org.springframework.stereotype.Service;
import match.exception.TeamException;
import match.model.dto.TeamDTO;

@Service
public interface TeamReadService {
	
	// 尋找所有 team 轉成 teamDTO
	List<TeamDTO> findAllTeams();
	
	// 用 teamId 找球隊：
	TeamDTO findTeamByTeamId(Integer teamId) throws TeamException;
	// 用 playerId (隊長編號)找球隊 (一個隊長可能會有多個球隊)：
	List<TeamDTO> findTeamByCapId(Integer playerId) throws TeamException;
	// 用 teamName 找球隊：
	TeamDTO findTeamByName(String teamName) throws TeamException;
	// 用 recruit 找球隊：
	List<TeamDTO> findTeamByRecruit(Boolean recruit) throws TeamException;
	// 用地區找球隊：
	List<TeamDTO> findTeamByPlace(String place) throws TeamException;
	
	// 篩選平均等級高於多少的球隊：
	List<TeamDTO> findTeamLevelAbove(Integer level) throws TeamException;
	// 篩選平均等級低於多少的球隊：
	List<TeamDTO> findTeamLevelBelow(Integer level) throws TeamException;
}
