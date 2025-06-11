package match;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import match.model.entity.Player;
import match.model.entity.User;
import match.repository.PlayerRepository;
import match.repository.UserRepository;
import match.util.Hash;

@SpringBootTest
public class TestUserPlayer {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Test
	public void addUser() throws Exception {
		
//		String name = "POPO";
//		String password = "1234";
//		String email = "POPO1234@gmail.com";
//		Boolean admin = false;
//		
//		String hashSalt = Hash.getSalt();
//		String HashPassword = Hash.getHash(password, hashSalt);
//		
//		User user = new User(null, name, HashPassword, hashSalt, email, null, admin, null);
//		userRepository.save(user);
//		User user2 = userRepository.findById(2).get();
//		User user = userRepository.readUserByName("POPO").get();
//		
//		user.setUsername("POPO Yang");
//
//		userRepository.save(user);
		
//		User user2 = userRepository.findById(2).get();
//		System.out.println(user.getUsername());
		List<User> users = userRepository.findAllUsers();
		System.out.println(users);
		
	}
}
