package match.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import match.model.entity.TeamApplication;

@Repository
public interface TeamApplicationRepository extends JpaRepository<TeamApplication, Integer>{
	
	@Query(value = "Select ta from TeamApplication ta where ta.teamId =:teamId")
	public List<TeamApplication> findByTeamId(Integer teamId);

}
