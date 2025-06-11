package match;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import match.model.dto.PlayerDTO;
import match.model.entity.Player;
import match.repository.PlayerRepository;

@SpringBootTest
public class TestPlayer {
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Test
	@Transactional
	public void testPlayer() {
//		Optional<Player> optplayer = playerRepository.readPlayerByUserId(2);
//		Player player = optplayer.get();
//		
//		System.out.println(player.getUser().getUsername());
		
		Optional<PlayerDTO> optPlayerDTO = playerRepository.readPlayerDTOByUserId(2);
		System.out.println(optPlayerDTO.get());
	}
}
