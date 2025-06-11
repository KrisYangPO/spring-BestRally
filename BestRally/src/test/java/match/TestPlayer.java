package match;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import match.model.entity.Player;
import match.repository.PlayerRepository;

@SpringBootTest
public class TestPlayer {
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Test
	public void testPlayer() {
		Player player = playerRepository.readPlayerByUserId(2).get();
		
		System.out.println(player);
	}
}
