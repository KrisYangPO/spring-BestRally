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
public class TeamDTO {
	private Integer id;
	private String teamName;
	private Integer avgLevel;
	private Integer teamNum;
	private String place;
	private Boolean recruit;
	private Player player;
}
