package match.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import match.model.dto.TeamApplicationDTO;
import match.model.entity.TeamApplication;

@Component
public class TeamApplicationMapper {
	
	@Autowired
	ModelMapper modelMapper;
	
	// Entity 2 DTO
	public TeamApplicationDTO toDTO(TeamApplication teamApplication) {
		return modelMapper.map(teamApplication, TeamApplicationDTO.class);
	}
	
	// DTO 2 Entity 
	public TeamApplication toEntity(TeamApplicationDTO teamApplicationDTO){
		return modelMapper.map(teamApplicationDTO, TeamApplication.class);
	}
}
