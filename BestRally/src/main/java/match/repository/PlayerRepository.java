package match.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import match.model.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer>{
	
	@EntityGraph(attributePaths = {"user"})
	@Query(value = "Select p From Player p where p.user.id =:userId")
	public Optional<Player> readPlayerByUserId(Integer userId);

}
