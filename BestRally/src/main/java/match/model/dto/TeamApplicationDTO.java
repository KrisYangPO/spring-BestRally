package match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import match.model.entity.Player;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamApplicationDTO {
	
	private Integer id;
	private Integer userId;
	private String username;
	private String email;
	private Player player;
	private Integer teamId;
	private String teamName;
}
