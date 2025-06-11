package match.repository;

import java.util.List;

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
	
}
