package match.model.dto;

import lombok.Data;

@Data
public class UserDTO {
	
	private Integer id;
	private String username;
	private String email;
	private String photo;
	private Boolean admin;
	
}
