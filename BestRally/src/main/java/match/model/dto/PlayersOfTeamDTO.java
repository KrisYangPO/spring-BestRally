package match.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import match.model.entity.Team;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayersOfTeamDTO {
	
	// 找出這個隊伍裡的所有球員
	private Team team;
	private List<PlayerDTO> playerDTOs;
}
