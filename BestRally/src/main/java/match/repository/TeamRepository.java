package match.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import match.model.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

}
