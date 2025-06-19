package match.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import match.model.entity.TeamApplication;

@Repository
public interface TeamApplicationRepository extends JpaRepository<TeamApplication, Integer>{
	
	// 透過 playerId 找尋他在特定球隊的申請。
	@Query(value = """
				Select ta from TeamApplication ta 
				JOIN FETCH ta.player p
				where ta.teamId =:teamId AND p.id=:playerId
			""")
	public Optional<TeamApplication> findByPlayerAndTeamId(Integer teamId, Integer playerId);

	
	// 透過 teamId 找尋球隊所有申請。
	@Query(value = """
			Select ta from TeamApplication ta 
			JOIN FETCH ta.player p
			where ta.teamId =:teamId
		""")
	public List<TeamApplication> findByTeamId(Integer teamId);
}
