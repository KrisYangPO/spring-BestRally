package match.service;

import org.springframework.stereotype.Service;

import match.exception.TeamException;
import match.exception.TeamPlayerException;
import match.model.dto.TeamDTO;

@Service
public interface TeamUpdateService {
	// 新增隊伍：
	// 一定要先有 player 身份才可以建立隊伍，
	// 這個 player 建立隊伍的同時就會自動成為隊長。
	void addTeam(Integer playerId, TeamDTO teamDTO) throws TeamException, TeamPlayerException;
	
	// 更新隊伍
	void updateTeam(Integer teamId, String teamName, String place, Boolean recruit, Integer playerId) throws TeamException;
	
	// 更新隊伍名稱
	void updateTeamName(Integer teamId, String teamName) throws TeamException;
	
	// 更新隊伍場地
	void updateTeamPlace(Integer teamId, String place) throws TeamException;
	
	// 更新球隊位置
	void updateTeamRecruit(Integer teamId) throws TeamException;
	
	// 刪除球隊
	void removeTeam(Integer teamId) throws TeamException;
	
	
	// 更新隊伍數據：
	// 更新隊伍人數和隊伍平均等級：
	void updateTeamLevelAndNum(Integer teamId, Integer level, Integer num) throws TeamException;
}
