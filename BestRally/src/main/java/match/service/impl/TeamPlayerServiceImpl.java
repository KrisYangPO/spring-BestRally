package match.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import match.exception.TeamPlayerException;
import match.model.dto.PlayersOfTeamDTO;
import match.model.dto.TeamPlayerDTO;
import match.model.dto.TeamsOfPlayerDTO;
import match.model.entity.Player;
import match.model.entity.Team;
import match.model.entity.TeamPlayer;
import match.repository.PlayerRepository;
import match.repository.TeamPlayerRepository;
import match.repository.TeamRepository;
import match.service.TeamPlayerService;

@Service
public class TeamPlayerServiceImpl implements TeamPlayerService {
	
	@Autowired
	private TeamPlayerRepository teamPlayerRepository;
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private TeamRepository teamRepository;
	
	
	// 新增球隊球員身份，
	// 要先確認這個球隊編號，與球員編號都有紀錄資料在資料庫。
	@Override
	public void addTeamPlayer(Team team, Player player) throws TeamPlayerException {
		// 確認 team 的 id 沒問題
		Optional<Team> optTeam = teamRepository.findById(team.getId());
		if(optTeam.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 建立球隊球員失敗，查無此隊伍編號："+team.getId());
		}
		// 確認 player 的 id 沒問題
		Optional<Player> optPlayer = playerRepository.findById(player.getId());
		if(optPlayer.isEmpty()) {
			throw new TeamPlayerException("TeamPlayerService: 建立球隊隊員失敗，查無此球員編號："+player.getId());
		}
		// 建立 teamPlayer 並儲存至資料庫。
		TeamPlayer teamPlayer = new TeamPlayer();
		teamPlayer.setPlayer(player);
		teamPlayer.setTeam(team);
		teamPlayerRepository.save(teamPlayer);
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
		return teamPlayerRepository.readTeamPlayerByTeamId(teamId);
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
		return teamPlayerRepository.readTeamPlayerByPlayerId(playerId);
	}

	
	// 更新勝率，透過 SQL 控制：當執行 updateWinRate 就計算一次 WinRate
	@Override
	public void updateTeamPlayerWinRatio(Integer teamId, Integer playerId, Double winRate) throws TeamPlayerException  {
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
	}

	@Override
	public TeamsOfPlayerDTO getTeamsFromPlayer(Integer playerId) throws TeamPlayerException {
		// Step1. 根據 playerId 找出所有 teamPlayer
		List<TeamPlayerDTO> teamPlayerDTOs = findByPlayerId(playerId);
		
		// Step2. 根據他們的 teamId 找出 Team 物件
	}

	@Override
	public PlayersOfTeamDTO getPlayersFromTeam(Integer teamId) throws TeamPlayerException {
		// TODO Auto-generated method stub
		return null;
	}

}
