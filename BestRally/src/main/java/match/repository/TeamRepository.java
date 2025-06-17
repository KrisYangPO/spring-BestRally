package match.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import match.model.dto.TeamDTO;
import match.model.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
	
	// 找尋所有隊伍，並轉成 TeamDTO
	@Query(value = """
			Select new match.model.dto.TeamDTO(t.id, t.teamName, t.avgLevel, t.teamNum, t.place, t.recruit, t.player)
			From Team t
			""")
	public List<TeamDTO> findAllTeamDTOs();
	
	
	// 根據 playerId (隊長) 找尋隊伍
	// 取得 playerId 後可以透過 playerId 找尋這個 id 是否為隊長
	@Query(value = """
			Select new match.model.dto.TeamDTO(t.id, t.teamName, t.avgLevel, t.teamNum, t.place, t.recruit, t.player)
			From Team t
			Where t.player.id =:playerId
			""")
	public List<TeamDTO> findTeamByCapId(Integer playerId);
	
	
//	// 可能會導致 N+1 查詢。
//	// 根據隊伍編號(teamId)搜尋隊伍所有資訊，
//	@EntityGraph(attributePaths = {"player", "player.user"})
//	@Query(value = "Select t from Team t where t.id =:teamId")
//	public Optional<Team> findTeamWithPlayerById(Integer teamId);
	
	
	// 不會產生 N+1 查詢。
	// 根據隊伍編號(teamId)搜尋隊伍所有資訊
	@Query("""
		    SELECT t FROM Team t
		    JOIN FETCH t.player p
		    JOIN FETCH p.user
		    WHERE t.id = :teamId
		""")
	public Optional<Team> findTeamWithPlayerById(Integer teamId);
	
	
	// 透過隊伍名稱尋找隊伍：
	@Query(value = """
			Select new match.model.dto.TeamDTO(t.id, t.teamName, t.avgLevel, t.teamNum, t.place, t.recruit, t.player)
			From Team t
			Where t.teamName =:teamName
			""")
	public Optional<TeamDTO> findTeamByName(String teamName);
	
	
	// 根據招募與否尋找隊伍：
	@Query(value = """
			Select new match.model.dto.TeamDTO(t.id, t.teamName, t.avgLevel, t.teamNum, t.place, t.recruit, t.player)
			From Team t
			Where t.recruit =:recruit
			""")
	public List<TeamDTO> findTeamByRecruit(Boolean recruit);
	
	
	// 透過隊伍名稱尋找隊伍：
	@Query(value = """
			Select new match.model.dto.TeamDTO(t.id, t.teamName, t.avgLevel, t.teamNum, t.place, t.recruit, t.player)
			From Team t
			Where t.place =:place
			""")
	public List<TeamDTO> findTeamByPlace(String place);
	
	
	// 根據球員等級篩選球隊：
	// 篩選平均等級高於多少的球隊
	@Query(value = """
			Select new match.model.dto.TeamDTO(t.id, t.teamName, t.avgLevel, t.teamNum, t.place, t.recruit, t.player)
			From Team t
			Where t.avgLevel >:level
			""")
	public List<TeamDTO> findTeamLevelAbove(Integer level);
	
	
	// 篩選平均等級低於多少的球隊
	@Query(value = """
			Select new match.model.dto.TeamDTO(t.id, t.teamName, t.avgLevel, t.teamNum, t.place, t.recruit, t.player)
			From Team t
			Where t.avgLevel <:level
			""")
	public List<TeamDTO> findTeamLevelBelow(Integer level);
	
	
	// 自動轉換 recruit 功能：
	// 只要執行這個功能，就向 SQL 發送切換 recruit 布林值的結果。
	@Modifying
	@Query(value = "update `team` SET recruit = NOT recruit where team_id =:teamId", nativeQuery = true)
	public void updateRecruit(Integer teamId);
	
}
