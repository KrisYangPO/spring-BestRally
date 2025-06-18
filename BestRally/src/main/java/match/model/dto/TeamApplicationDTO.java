package match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamApplicationDTO {
	
	private Integer id;
	private Integer userId;
	private String email;
	private Integer playerId;
	private Integer playerLevel;
	private Integer teamId;
	private String teamName;
}
