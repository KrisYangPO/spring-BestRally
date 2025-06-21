package match.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import match.annotation.DoTeamRefresh;
import match.exception.TeamPlayerAlreadyExistException;
import match.exception.TeamPlayerException;
import match.exception.TeamPlayerNotFoundException;
import match.exception.TeamRefreshException;
import match.mapper.TeamMapper;
import match.mapper.TeamPlayerMapper;
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
	private TeamPlayerMapper teamPlayerMapper;
	@Autowired
	private TeamRefreshDataService teamRefreshDataService;
	
	/* Entity Manager:
	 * 因為更新項目是 Java 物件：MatchPlayerDTO，
	 * 並不是 @Entity 無法觸法 JPA Entity Manager 對他進行自動儲存至資料庫，
	 * 就算呼叫方法取得資訊 JPA 也會不知道該怎麼做，
	 * 因此需要額外在更新方法中加入 EntityManager，並在最後加入 flush 動作，
	 * 就可強制將資料儲存在資料庫。
	 */
	
	@Autowired
	private EntityManager entityManager; 
	
	
	// 新增球隊球員身份，
	// 要先確認這個球隊編號，與球員編號都有紀錄資料在資料庫。
	@Override
	@Transactional
	@DoTeamRefresh(teamIdParam = "teamId")
	public void addTeamPlayer(Integer teamId, Integer playerId) throws TeamPlayerException, TeamRefreshException {
		
		// Step1. 確認 team 的 id 沒問題
		Optional<Team> optTeam = teamRepository.findById(teamId);
		if(optTeam.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 建立球隊球員失敗，查無此隊伍編號："+teamId);
		}
		// 確認 player 的 id 沒問題
		Optional<Player> optPlayer = playerRepository.findById(playerId);
		if(optPlayer.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 建立球隊隊員失敗，查無此球員編號："+playerId);
		}
		
		// Step2. 檢查這個 playerId 是否已經在球隊中：
		// 找出這個 player 所參與的球隊：
		try {
			TeamsOfPlayerDTO teamsOfPlayerDTO = getTeamsFromPlayer(playerId);
			List<TeamDTO> teamDTOs = teamsOfPlayerDTO.getTeamDTOs();
			
			/* 確認這個 teamId 是否存在球員所加入的球隊：
			 * 這裡要強制傳出 Exception，因為在 Step2 檢查中，
			 * 是要檢查這個 player 是否已經加入球隊，如果有就不能加入，
			 * 所以要傳出例外終止這個程式。
			 */
			boolean exists = teamDTOs.stream().anyMatch(t -> t.getId().equals(teamId));
			if(exists) {
				String message = String.format("TeamPlayerService: 新增球隊球員失敗，這個球員(%s)已經加入球隊(%s)了",
						optPlayer.get().getUser().getUsername(), optTeam.get().getTeamName());
				System.err.println(message);
				throw new TeamPlayerAlreadyExistException(message);
			}
		/* 但是前面在執行 TeamsOfPlayerDTO teamsOfPlayerDTO = getTeamsFromPlayer(playerId);
		 * 可能會遇到 “這個球員還沒加入任何球隊” 的狀況，所以他在執行這段時會產生例外拋出，
		 * 但這是實務上有可能遇到的事，因此要用 try-catch 在這裡將 Exception 處理掉，
		 * 不要透過例外傳出，導致“因為使用者沒有加入任何球隊”，而中止 addTeamPlayer。
		 */
		} catch (TeamPlayerNotFoundException e) {
			e.printStackTrace();
			System.err.print("TeamPlayerService: 球員尚未加入任何球隊。");
		}
		
		// Step3. 建立 teamPlayer 並儲存至資料庫。
		TeamPlayer teamPlayer = new TeamPlayer();
		teamPlayer.setPlayer(optPlayer.get());
		teamPlayer.setTeam(optTeam.get());
		teamPlayerRepository.save(teamPlayer);
		
//		// Step4. 更新隊伍數據：
//		try {
//			teamRefreshDataService.AFTeamPlayerUpdate(teamId);
//			System.out.printf("TeamPlayerService: 新稱球員後(%s)，更新球隊(%s)成功。%n", playerId, teamId);
//		
//		}catch (TeamRefreshException e) {
//			e.printStackTrace();
//			System.err.printf("TeamPlayerService: 球員加入球隊後，更新隊伍數據失敗。%n%s%n" + e.getMessage());
//			throw new TeamRefreshException("TeamPlayerService: 球員加入球隊後，更新隊伍數據失敗。" + e.getMessage());
//		}
	}
	
	
	// 刪除球員：
	@Override
	@DoTeamRefresh(teamIdParam = "teamId")
	public void removeTeamPlayer(Integer teamPlayerId, Integer teamId) throws TeamPlayerException {
		// Step1. 先查詢這個球隊球員：
		TeamPlayer teamPlayer = findTeamPlayerById(teamPlayerId);
//		// Step2. 取得這個球隊球員的 teamId:
//		Integer teamId = teamPlayer.getTeam().getId();
		// Step3. 刪除球員：
		teamPlayerRepository.deleteById(teamPlayerId);
		
//		// Step4. 執行球隊更新：
//		try {
//			// 重新計算球隊資訊
//			teamRefreshDataService.AFTeamPlayerUpdate(teamId);
//			System.out.printf("TeamPlayerService: 刪除球隊球員後(%s)，更新球隊(%s)成功。%n", teamPlayerId, teamId);
//			
//		} catch (TeamRefreshException e) {
//			e.printStackTrace();
//			String message = "TeamPlayerService: 刪除球隊球員後，更新球隊資訊出問題：" + e.getMessage();
//			System.err.println(message);
//			throw new TeamPlayerException(message);
//		}
	}
	
	
	// 查詢所有 teamPlayers 
	@Override
	public List<TeamPlayerDTO> findAllTeamPlayers() {
		return teamPlayerRepository.findAllTeamPLayerDTOs();
	}

	
	@Override
	public TeamPlayer findTeamPlayerById(Integer teamPlayerId) throws TeamPlayerException  {
		// 查詢 teamPlayerId
		Optional<TeamPlayer> optTeamPlayer = teamPlayerRepository.readTeamPlayerWithPlayerById(teamPlayerId);
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
		// 直接尋找，並轉成 DTO 
		List<TeamPlayerDTO> teamPlayersDTO = teamPlayerRepository.readTeamPlayerWithPlayerByTeamId(teamId)
				.stream().map(teamPlayerMapper::toDTO).collect(Collectors.toList());
		
		if(teamPlayersDTO.isEmpty() || teamPlayersDTO == null) {
			throw new TeamPlayerException("TeamPlayerService: 查詢球隊球員失敗，球隊沒有任何人加入："+teamId);
		}
		return teamPlayersDTO;
	}

	
	// 找尋這個球員所參與的球隊。
	@Override
	public List<TeamPlayerDTO> findByPlayerId(Integer playerId) throws TeamPlayerNotFoundException  {
		// 確認 playerId
		Optional<Player> optPlayer = playerRepository.findById(playerId);
		if(optPlayer.isEmpty()) {
			throw new TeamPlayerNotFoundException("TeamPlayerService: 查詢球隊球員失敗，查無此球員編號："+playerId);
		}
		// 直接尋找
		List<TeamPlayerDTO> teamPlayerDTOs = teamPlayerRepository.readTeamPlayerByPlayerId(playerId);
		if(teamPlayerDTOs.isEmpty() || teamPlayerDTOs == null) {
			throw new TeamPlayerNotFoundException("TeamPlayerService: 查詢球隊球員失敗，球員編號沒有加入任何球隊："+playerId);
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
	@Transactional
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
		
		// 強制 flush 寫入 DB
		// 因為更新對象是 MatchPlayerDTO 他並不是 Entity，
		// 所以可能無法觸法 Entity Manager
	    entityManager.flush();
	}

	
	// 取得這個球員(playerId)所參與的球隊資訊：
	@Override
	public TeamsOfPlayerDTO getTeamsFromPlayer(Integer playerId) throws TeamPlayerNotFoundException {
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
		List<TeamPlayerDTO> teamPlayersDTO = findByTeamId(teamId);
		
		// Step2. 呼叫這個 teamId 的 Team Entity，
		TeamDTO teamDTO = teamMapper.toDTO(teamRepository.findById(teamId).get());
		
		// Step3. 根據這個球隊的所有 teamPlayers 找出 player data:
		List<Player> players = teamPlayersDTO.stream()
				.map(p->p.getPlayer())
				.collect(Collectors.toList());
		
		// Step4. 注入 PlayersOfTeamDTO:
		return new PlayersOfTeamDTO(teamDTO, players);
	}
}
