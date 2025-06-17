package match.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import match.exception.TeamException;
import match.exception.TeamNotFoundException;
import match.exception.TeamPlayerException;
import match.exception.TeamRefreshException;
import match.mapper.TeamMapper;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamDTO;
import match.model.entity.Player;
import match.model.entity.Team;
import match.repository.PlayerRepository;
import match.repository.TeamRepository;
import match.service.TeamPlayerService;
import match.service.TeamUpdateService;

@Service
public class TeamUpdateServiceImpl implements TeamUpdateService{
	
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private TeamPlayerService teamPlayerService;
	@Autowired
	private TeamMapper teamMapper;

	// 新增球隊
	@Override
	public void addTeam(Integer playerId, TeamDTO teamDTO)
			throws TeamException, TeamPlayerException, TeamRefreshException {
		
		// Step1. 檢查資料庫內容
		// 檢查這個 playerId 是否存在 player 資料庫，
		Optional<PlayerDTO> optPlayerDTO = playerRepository.readPlayerDTOByPlayerId(playerId);
		if(optPlayerDTO.isEmpty()) {
			throw new TeamNotFoundException("TeamService: 新增隊伍失敗，查無此球員編號:"+playerId);
		}
		// 檢查這個 teamName 是否已經註冊 team 資料庫，
		Optional<TeamDTO> optTeam = teamRepository.findTeamByName(teamDTO.getTeamName());
		if(optTeam.isPresent()) {
			throw new TeamNotFoundException("TeamService: 新增隊伍失敗，球隊名稱已註冊："+teamDTO.getTeamName());
		}
		
		// Step2. 將 TeamDTO 轉 Team entity
		Team team = teamMapper.toEntity(teamDTO);
		
		// Step3. 手動直接將內容輸入：
		team.setPlayer(playerRepository.findById(playerId).get());
		// 進行更新：
		teamRepository.save(team);
		
		// Step4. 同時新增 TeamPlayer 資料內容。
		// 接著直接新增 teamPlayer 到這個 team 當中，
		// 因為 team.player 就是隊長，也就是創立這個球隊的人。
		teamPlayerService.addTeamPlayer(team.getId(), playerId);
	}
	
	
	// 更新隊伍資訊
	@Override
	public void updateTeam(Integer teamId, String teamName, String place, Boolean recruit, Integer playerId) throws TeamException {
		// 先確認這個 teamId 有註冊 team 資料到資料庫。
		Optional<Team> optTeam = teamRepository.findById(teamId);
		if(optTeam.isEmpty()) {
			throw new TeamNotFoundException("TeamService: 更新隊伍錯誤，查無此隊伍編號："+teamId);
		}
		// 確認 playerId
		Optional<Player> optPlayer = playerRepository.findById(playerId);
		if(optPlayer.isEmpty()) {
			throw new TeamException("TeamService: 更新隊伍失敗，查無此隊長編號："+playerId);
		}
		
		// 直接更新：
		Team team = optTeam.get();
		team.setTeamName(teamName);
		team.setPlace(place);
		team.setRecruit(recruit);
		team.setPlayer(optPlayer.get());
		
		teamRepository.save(team);
	}

	
	// 部分更新：更新球隊名稱
	@Override
	public void updateTeamName(Integer teamId, String teamName) throws TeamException  {
		// 先確認這個 teamId 有註冊 team 資料到資料庫。
		Optional<Team> optTeam = teamRepository.findById(teamId);
		if(optTeam.isEmpty()) {
			throw new TeamNotFoundException("TeamService: 更新隊伍名稱失敗，查無此隊伍編號："+teamId);
		}
		
		// 檢查這個 teamName 是否已經註冊 team 資料庫，不能重複更新
		Optional<TeamDTO> checkName = teamRepository.findTeamByName(teamName);
		if(checkName.isPresent()) {
			throw new TeamNotFoundException("TeamService: 更新隊伍名稱失敗，球隊名稱已經被使用："+teamName);
		}
		// 進行更新：
		Team team = optTeam.get();
		team.setTeamName(teamName);
		teamRepository.save(team);
	}
	
	// 部分更新：更新球隊場地
	@Override
	public void updateTeamPlace(Integer teamId, String place) throws TeamException  {
		// 先確認這個 teamId 有註冊 team 資料到資料庫。
		Optional<Team> optTeam = teamRepository.findById(teamId);
		if(optTeam.isEmpty()) {
			throw new TeamNotFoundException("TeamService: 更新隊伍場地失敗，查無此隊伍編號："+teamId);
		}
		// 進行更新：
		Team team = optTeam.get();
		team.setPlace(place);
		teamRepository.save(team);
	}
	
	// 部分更新：更新球隊招募狀態
	@Override
	public void updateTeamRecruit(Integer teamId) throws TeamException  {
		// 先確認這個 teamId 有註冊 team 資料到資料庫。
		Optional<Team> optTeam = teamRepository.findById(teamId);
		if(optTeam.isEmpty()) {
			throw new TeamNotFoundException("TeamService: 更新隊伍場地失敗，查無此隊伍編號："+teamId);
		}
		// SQL 語法表示: 執行 updateRecruit() 就會將 recruit 的布林值結果切換
		teamRepository.updateRecruit(teamId);	
	}
	
	// 刪除球隊
	@Override
	public void removeTeam(Integer teamId) throws TeamException  {
		// 先確認這個 teamId 有註冊 team 資料到資料庫。
		Optional<Team> optTeam = teamRepository.findById(teamId);
		if(optTeam.isEmpty()) {
			throw new TeamNotFoundException("TeamService: 刪除隊伍場地失敗，查無此隊伍編號："+teamId);
		}
		// 刪除球隊
		teamRepository.deleteById(teamId);
	}
	
	// 進行 TeamDataRefresh 時會用到的方法：
	@Override
	public void updateTeamLevelAndNum(Integer teamId, Integer level, Integer num) throws TeamException  {
		// 先確認這個 teamId 有註冊 team 資料到資料庫。
		Optional<Team> optTeam = teamRepository.findById(teamId);
		if(optTeam.isEmpty()) {
			throw new TeamNotFoundException("TeamService: 刪除隊伍場地失敗，查無此隊伍編號："+teamId);
		}
		// 呼叫 team Entity 並更新
		Team team = optTeam.get();
		team.setAvgLevel(level);
		team.setTeamNum(num);
		teamRepository.save(team);
	}
}
