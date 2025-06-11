package match.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import match.model.entity.Player;
import match.model.entity.Team;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamPlayerDTO {

	private Integer id;
	private Team team;
	private Player player;
	private Double winRate; 
	private Integer winGame; 
	private Integer total;
	private Date joinTime; 
}
