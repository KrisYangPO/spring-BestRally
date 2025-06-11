package match.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import match.model.dto.PlayerDTO;
import match.model.entity.Player;
import match.model.entity.User;

@Component
public class PlayerMapper {
	
	@Autowired
	ModelMapper modelMapper;
	
	// Entity 2 DTO
	public PlayerDTO toDTO(Player player) {
		return modelMapper.map(player, PlayerDTO.class);
	}
	
	// DTO 2 Entity 
	public Player toEntity(PlayerDTO playerDTO){
		return modelMapper.map(playerDTO, Player.class);
	}
	
	public Player setEntity(PlayerDTO playerDTO, User user) {
		Player player = new Player();
		
		player.setId(playerDTO.getId());
		player.setLevel(playerDTO.getLevel());
		player.setUser(user);
		
		return player;
	}
}
