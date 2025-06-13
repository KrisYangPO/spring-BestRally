package match.service;

import java.util.List;

import org.springframework.stereotype.Service;

import match.exception.TeamPlayerException;
import match.model.dto.PlayersOfTeamDTO;
import match.model.dto.TeamPlayerDTO;
import match.model.dto.TeamsOfPlayerDTO;
import match.model.entity.Player;
import match.model.entity.Team;
import match.model.entity.TeamPlayer;

@Service
public interface TeamPlayerService {
	// 新增隊伍球員，通常要加入球隊球員，已經是登入且有球員身份，
	// 並且可以透過查詢球隊找到隊伍資訊，因此直接透過 Entity 加入。
	void addTeamPlayer(Team team, Player player) throws TeamPlayerException;
	
	// 尋找所有 teamPlayers
	List<TeamPlayerDTO> findAllTeamPlayers();
	
	// 透過特定 teamPlayer_id 找尋 teamPlayer
	TeamPlayer findTeamPlayerById(Integer teamPlayerId) throws TeamPlayerException;
	
	// 找尋這個隊伍(team_Id)的所有隊伍成員
	List<TeamPlayerDTO> findByTeamId(Integer teamId) throws TeamPlayerException;
	
	// 找尋這個球員(player_id)參與的所有球隊
	List<TeamPlayerDTO> findByPlayerId(Integer playerId) throws TeamPlayerException;
	
	// 更新 TeamPlayer 資訊：
	void updateTeamPlayerWinRatio(Integer teamId, Integer playerId, Double winRate) throws TeamPlayerException;
	void updateTeamPlayerMatchData(Integer teamId, Integer playerId, Integer winGame, Integer total) throws TeamPlayerException;
	
	
	// 取得這個球員(playerId)所參與的球隊資訊：
	TeamsOfPlayerDTO getTeamsFromPlayer(Integer playerId) throws TeamPlayerException;
	// 取得這個球隊(teamId)所有的參與球員資訊：
	PlayersOfTeamDTO getPlayersFromTeam(Integer teamId) throws TeamPlayerException;
}
