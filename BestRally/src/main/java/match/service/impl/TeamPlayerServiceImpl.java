package match.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import match.exception.TeamPlayerException;
import match.exception.TeamRefreshException;
import match.mapper.TeamMapper;
import match.model.dto.PlayerDTO;
import match.model.dto.PlayersOfTeamDTO;
import match.model.dto.TeamDTO;
import match.model.dto.TeamPlayerDTO;
import match.model.dto.TeamsOfPlayerDTO;
import match.model.entity.Player;
import match.model.entity.Team;
import match.model.entity.TeamPlayer;
import match.repository.PlayerRepository;
import match.repository.TeamPlayerRepository;
import match.repository.TeamRepository;
import match.service.TeamPlayerService;
import match.service.TeamRefreshDataService;

@Service
public class TeamPlayerServiceImpl implements TeamPlayerService {
	
	@Autowired
	private TeamPlayerRepository teamPlayerRepository;
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private TeamMapper teamMapper;
	@Autowired
	private TeamRefreshDataService teamRefreshDataService;
	
	
	// 新增球隊球員身份，
	// 要先確認這個球隊編號，與球員編號都有紀錄資料在資料庫。
	@Override
	public void addTeamPlayer(Integer teamId, Integer playerId) throws TeamPlayerException, TeamRefreshException {
		// 確認 team 的 id 沒問題
		Optional<Team> optTeam = teamRepository.findById(teamId);
		if(optTeam.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 建立球隊球員失敗，查無此隊伍編號："+teamId);
		}
		// 確認 player 的 id 沒問題
		Optional<Player> optPlayer = playerRepository.findById(playerId);
		if(optPlayer.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 建立球隊隊員失敗，查無此球員編號："+playerId);
		}
		
		// 建立 teamPlayer 並儲存至資料庫。
		TeamPlayer teamPlayer = new TeamPlayer();
		teamPlayer.setPlayer(optPlayer.get());
		teamPlayer.setTeam(optTeam.get());
		teamPlayerRepository.save(teamPlayer);
		
		// 更新隊伍數據：
		try {
			teamRefreshDataService.AFTeamPlayerUpdate(teamId);
		
		}catch (TeamRefreshException e) {
			e.printStackTrace();
			throw new TeamRefreshException("TeamPlayerService: 球員加入球隊後，更新隊伍數據失敗。" + e.getMessage());
		}
	}

	
	// 查詢所有 teamPlayers 
	@Override
	public List<TeamPlayerDTO> findAllTeamPlayers() {
		return teamPlayerRepository.findAllTeamPLayerDTOs();
	}

	
	@Override
	public TeamPlayer findTeamPlayerById(Integer teamPlayerId) throws TeamPlayerException  {
		// 查詢 teamPlayerId
		Optional<TeamPlayer> optTeamPlayer = teamPlayerRepository.findById(teamPlayerId);
		if(optTeamPlayer.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 查詢球隊球員失敗，查無此球隊球員編號："+teamPlayerId);
		}
		// 回傳：
		return optTeamPlayer.get();
	}

	
	// 尋找這個隊伍裡的所有球員。
	@Override
	public List<TeamPlayerDTO> findByTeamId(Integer teamId) throws TeamPlayerException  {
		// 確認 teamId 
		Optional<Team> optTeam = teamRepository.findById(teamId);
		if(optTeam.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 查詢球隊球員失敗，查無此球隊編號："+teamId);
		}
		// 直接尋找
		List<TeamPlayerDTO> teamPlayerDTOs = teamPlayerRepository.readTeamPlayerByTeamId(teamId);
		if(teamPlayerDTOs.isEmpty() || teamPlayerDTOs == null) {
			throw new TeamPlayerException("TeamPlayerService: 查詢球隊球員失敗，球隊沒有任何人加入："+teamId);
		}
		return teamPlayerDTOs;
	}

	
	// 找尋這個球員所參與的球隊。
	@Override
	public List<TeamPlayerDTO> findByPlayerId(Integer playerId) throws TeamPlayerException  {
		// 確認 playerId
		Optional<Player> optPlayer = playerRepository.findById(playerId);
		if(optPlayer.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 查詢球隊球員失敗，查無此球員編號："+playerId);
		}
		// 直接尋找
		List<TeamPlayerDTO> teamPlayerDTOs = teamPlayerRepository.readTeamPlayerByPlayerId(playerId);
		if(teamPlayerDTOs.isEmpty() || teamPlayerDTOs == null) {
			throw new TeamPlayerException("TeamPlayerService: 查詢球隊球員失敗，球員編號沒有加入任何球隊："+playerId);
		}
		return teamPlayerDTOs;
	}

	
	// 更新勝率，透過 SQL 控制：當執行 updateWinRate 就計算一次 WinRate
	@Override
	public void updateTeamPlayerWinRatio(Integer teamId, Integer playerId) throws TeamPlayerException  {
		// Step1. 確認 teamId 
		Optional<Team> optTeam = teamRepository.findById(teamId);
		if(optTeam.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 更新勝率失敗，查無此球隊編號："+teamId);
		}
		// Step2. 確認 playerId
		Optional<Player> optPlayer = playerRepository.findById(playerId);
		if(optPlayer.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 更新勝率失敗，查無此球員編號："+playerId);
		}
		// Step3. 執行更新方法：
		teamPlayerRepository.updateWinRate(teamId, playerId);
	}

	
	// 更新勝場與總場次。
	@Override
	public void updateTeamPlayerMatchData(Integer teamId, Integer playerId, Integer winGame, Integer total) throws TeamPlayerException  {
		// Step1. 確認 teamId 
		Optional<Team> optTeam = teamRepository.findById(teamId);
		if(optTeam.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 更新對戰數據失敗，查無此球隊編號："+teamId);
		}
		// Step2. 確認 playerId
		Optional<Player> optPlayer = playerRepository.findById(playerId);
		if(optPlayer.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 更新對戰數據失敗，查無此球員編號："+playerId);
		}
		// Step3. 執行更新方法
		teamPlayerRepository.updateMatchData(teamId, playerId, winGame, total);
		
		// Step4. 直接更新 winRate:
		updateTeamPlayerWinRatio(teamId, playerId);
	}

	
	// 取得這個球員(playerId)所參與的球隊資訊：
	@Override
	public TeamsOfPlayerDTO getTeamsFromPlayer(Integer playerId) throws TeamPlayerException {
		// Step1. 根據 playerId 找出所有 teamPlayer
		List<TeamPlayerDTO> teamPlayerDTOs = findByPlayerId(playerId);
		
		// Step2. 求出 playerId 的 Player Entity:
		// 因為 Step1 會驗證 PlayerId，這裡直接轉 Entity。
		// readPlayerDTOByPlayerId 可以取得 PlayerDTO 而非 Entity
		PlayerDTO playerDTO = playerRepository.readPlayerDTOByPlayerId(playerId).get();
		
		// Step3. 根據他們 TeamPlayer 的 team 屬性找出 Team 物件，並轉成 DTO
		List<TeamDTO> teamDTOs = teamPlayerDTOs.stream()
				.map(p->p.getTeam())
				.map(teamMapper::toDTO)
				.collect(Collectors.toList());
		
		// Step4. 將這個 player 身份儲存至 TeamsOfPlayerDTO
		return new TeamsOfPlayerDTO(playerDTO, teamDTOs);
	}
	
	
	// 取得這個球隊(teamId)所有的參與球員資訊：
	@Override
	public PlayersOfTeamDTO getPlayersFromTeam(Integer teamId) throws TeamPlayerException {
		// Step1. 根據 teamId 找出所有 teamPlayer，
		// 每個 teamPlayer 就會是這球隊的所有球員。
		// findByTeamId(teamId) 會檢測 teamId 所以下面直接 .get()
		List<TeamPlayerDTO> teamPlayerDTOs = findByTeamId(teamId);
		
		// Step2. 呼叫這個 teamId 的 Team Entity，
		TeamDTO teamDTO = teamMapper.toDTO(teamRepository.findById(teamId).get());
		
		// Step3. 根據這個球隊的所有 teamPlayers 找出 player data:
		List<Player> players = teamPlayerDTOs.stream()
				.map(p->p.getPlayer())
				.collect(Collectors.toList());
		
		// Step4. 注入 PlayersOfTeamDTO:
		return new PlayersOfTeamDTO(teamDTO, players);
	}
}
