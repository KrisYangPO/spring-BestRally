package match;

import java.util.List;
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
	public void testPlayer() {
//		Optional<Player> optplayer = playerRepository.readPlayerByUserId(2);
		Optional<Player> optPlayer = playerRepository.findById(6);
		Player player = optPlayer.get();
		System.out.println(player);
//		
//		System.out.println(player.getUser().getUsername());
		
//		Optional<PlayerDTO> optPlayerDTO = playerRepository.readPlayerDTOByUserId(2);
//		System.out.println(optPlayerDTO.get());
		
		// 找所有 player:
//		List<PlayerDTO> playerDTOs = playerRepository.findAllPlayerDTOs();
//		for (PlayerDTO p : playerDTOs) {
//			System.out.println("球員：" + p.getUsername());
//		}
	}
}
