package match.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import match.model.dto.TeamDTO;
import match.model.entity.Team;

public class TeamMapper {
	
	@Autowired
	ModelMapper modelMapper;
	
	// Entity 2 DTO
	public TeamDTO toDTO(Team team) {
		return modelMapper.map(team, TeamDTO.class);
	}
	
	// DTO 2 Entity 
	public Team toEntity(TeamDTO teamDTO){
		return modelMapper.map(teamDTO, Team.class);
	}
}
