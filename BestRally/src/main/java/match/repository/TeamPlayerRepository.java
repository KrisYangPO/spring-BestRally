package match.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import match.model.dto.TeamPlayerDTO;
import match.model.entity.TeamPlayer;

@Repository
public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, Integer> {
	
	// 找尋所有 teamPlayer 並轉成 DTO:
	@Query(value = """
			SELECT new match.model.dto.TeamPlayerDTO(tp.id, tp.team, tp.player, tp.winRate, tp.winGame, tp.total, tp.joinTime)
			FROM TeamPlayer tp
			""")
	public List<TeamPlayerDTO> findAllTeamPLayerDTOs();
	
	
	// 根據 playerID 找 teamPlayers List:
	// (尋找這個球員id下所有參與的隊伍紀錄)
	@Query(value = """
			SELECT new match.model.dto.TeamPlayerDTO(tp.id, tp.team, tp.player, tp.winRate, tp.winGame, tp.total, tp.joinTime)
			FROM TeamPlayer tp
			WHERE tp.player.id =:playerId
			""")
	public List<TeamPlayerDTO> readTeamPlayerByPlayerId(Integer playerId);
	
	
	// 根據 teamId 找尋 teamPlayers List:
	// (尋找這個隊伍id下所有球隊球員)
	@Query(value = """
			SELECT new match.model.dto.TeamPlayerDTO(tp.id, tp.team, tp.player, tp.winRate, tp.winGame, tp.total, tp.joinTime)
			FROM TeamPlayer tp
			WHERE tp.team.id =:teamId
			""")
	public List<TeamPlayerDTO> readTeamPlayerByTeamId(Integer teamId);
	
	
	// 執行程式，就會計算 winRate (用 native SQL 計算)
	@Modifying
	@Query(value = """
			UPDATE `team_player`
			SET win_rate = 100*(win_game*1.0/total) 
			WHERE team_id =:teamId and player_id =:playerId and total_match != 0
			""", nativeQuery = true)
	public void updateWinRate(Integer teamId, Integer playerId);
	
	
	// 執行程式，就計算 winRate (用 native SQL 計算)
	@Modifying
	@Query(value = """
			UPDATE `team_player`
			SET win_game =:winGame, total=:total
			WHERE team_id=:teamId AND player_id=:playerId AND total_match != 0
			""", nativeQuery = true)
	public void updateMatchData(Integer teamId, Integer playerId, Integer winGame, Integer total);
	
}
