package match.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import match.model.dto.UserCertDTO;
import match.model.dto.UserDTO;
import match.model.entity.User;

@Component
public class UserMapper {

    private final ModelMapping modelMapping;
	
	@Autowired
	private ModelMapper modelMapper;

    UserMapper(ModelMapping modelMapping) {
        this.modelMapping = modelMapping;
    }
	
	// Entity 2 DTO
	public UserDTO toDTO(User user) {
		return modelMapper.map(user, UserDTO.class);
	}
	
	// DTO 2 Entity 
	public User toEntity(UserDTO userDTO){
		return modelMapper.map(userDTO, User.class);
	}
	
	// User 2 UserCert
	public UserCertDTO toCert(User user) {
		return modelMapper.map(user, UserCertDTO.class);
	}
}
