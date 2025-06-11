package match.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import match.model.dto.PlayerAndTeamPlayersDTO;
import match.model.dto.PlayerDTO;
import match.model.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer>{
	
//	@EntityGraph(attributePaths = {"user"})
	@Query(value = "Select p From Player p left join fetch p.user where p.user.id =:userId")
	public Optional<Player> readPlayerByUserId(Integer userId);
	
	
	// 直接將搜尋資料儲存至 PlayerDTO 當中。
	// 完全處理掉 N+1 查詢問題。
	
	// 透過 user_id 查詢 PlayerDTO
	@Query(value = """
			SELECT new match.model.dto.PlayerDTO(p.id, p.level, u.id, u.username) 
			From Player p
			JOIN p.user u
			Where u.id =:userId
			""")
	public Optional<PlayerDTO> readPlayerDTOByUserId(Integer userId);

}
