package match.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import match.exception.TeamException;
import match.exception.TeamNotFoundException;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamDTO;
import match.model.entity.Team;
import match.repository.PlayerRepository;
import match.repository.TeamRepository;
import match.service.TeamUpdateService;

@Service
public class TeamUpdateServiceImpl implements TeamUpdateService{
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private PlayerRepository playerRepository;

	// 新增球隊
	@Override
	public void addTeam(Integer playerId, String teamName, String place, Boolean recruit) throws TeamException {
		// 檢查這個 playerId 是否存在 player 資料庫，
		Optional<PlayerDTO> optPlayerDTO = playerRepository.readPlayerDTOByPlayerId(playerId);
		if(optPlayerDTO.isEmpty()) {
			throw new TeamNotFoundException("TeamService: 新增隊伍失敗，查無此球員編號:"+playerId);
		}
		// 檢查這個 teamName 是否已經註冊 team 資料庫，
		Optional<TeamDTO> optTeam = teamRepository.findTeamByName(teamName);
		if(optTeam.isPresent()) {
			throw new TeamNotFoundException("TeamService: 新增隊伍失敗，查無此球隊名稱："+teamName);
		}
		
		// 手動直接將內容輸入：
		Team team = new Team();
		team.setPlace(place);
		team.setPlayer(playerRepository.findById(playerId).get());
		team.setRecruit(recruit);
		team.setTeamName(teamName);
		// 進行更新：
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
