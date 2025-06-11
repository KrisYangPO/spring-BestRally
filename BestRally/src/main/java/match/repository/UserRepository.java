package match.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import match.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// @Query(value = "select id, username, hash_password, hash_salt, email, photo, admin from users where username =:userName", nativeQuery = true)
	// 會導致 N+1 查詢
	@Query(value = "select u from User u left join fetch u.player where u.username =:userName")
	public Optional<User> readUserByName(String userName);
	
	// 可以避免 N+1 查詢
	@EntityGraph(attributePaths = {"player"})
	@Query(value = "select u from User u")
	public List<User> findAllUsers();
}
