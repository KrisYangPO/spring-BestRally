package match.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import match.model.dto.TeamPlayerDTO;
import match.model.entity.TeamPlayer;

@Component
public class TeamPlayerMapper {
	
	@Autowired
	ModelMapper modelMapper;
	
	// Entity 2 DTO
	public TeamPlayerDTO toDTO(TeamPlayer teamPlayer) {
		return modelMapper.map(teamPlayer, TeamPlayerDTO.class);
	}
	
	// DTO 2 Entity 
	public TeamPlayer toEntity(TeamPlayerDTO teamPlayerDTO){
		return modelMapper.map(teamPlayerDTO, TeamPlayer.class);
	}
}
