package match.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamsOfPlayerDTO {
	
	// 一個球員所參與的球隊
	private PlayerDTO playerDTO;
	private List<TeamDTO> teamDTOs;
	
}
