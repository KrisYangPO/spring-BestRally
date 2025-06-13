package match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCertDTO {
	private Integer id;
	private String username;
	private String email;
	private String photo;
	private Boolean admin;
}
