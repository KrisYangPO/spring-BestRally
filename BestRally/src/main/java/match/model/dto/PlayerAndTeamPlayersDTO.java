package match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import match.model.entity.Player;
import match.model.entity.TeamPlayer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerAndTeamPlayersDTO {
	
	private Player player;
	private TeamPlayer teamplayer;
	
}
