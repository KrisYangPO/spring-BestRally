package match;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import match.exception.TeamPlayerException;
import match.model.dto.TeamDTO;
import match.model.dto.TeamsOfPlayerDTO;
import match.model.entity.Team;
import match.model.entity.TeamPlayer;
import match.repository.TeamPlayerRepository;
import match.repository.TeamRepository;
import match.service.TeamPlayerService;
import match.service.TeamReadService;

@SpringBootTest
public class TestTeam {
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private TeamPlayerRepository temaPlayerRepository;
	@Autowired
	private TeamPlayerService teamPlayerService;
	
	@Test
	public void testTeam() {
//		Optional<Team> optTeam = teamRepository.findTeamWithPlayerById(1);
		
//		List<TeamPlayer> teamPlayers = temaPlayerRepository.readTeamPlayerWithPlayerByTeamId(1);
		
		
		try {
			TeamsOfPlayerDTO teamsOfPlayerDTO = teamPlayerService.getTeamsFromPlayer(1);
			List<TeamDTO> teamDTOs = teamsOfPlayerDTO.getTeamDTOs();
			
			System.out.println(teamDTOs.size());
		} catch (TeamPlayerException e) {
			e.printStackTrace();
		}

	}
	
	
}
