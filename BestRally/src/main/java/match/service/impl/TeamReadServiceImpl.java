package match.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import match.exception.TeamException;
import match.exception.TeamNotFoundException;
import match.mapper.TeamMapper;
import match.model.dto.TeamDTO;
import match.model.entity.Player;
import match.model.entity.Team;
import match.repository.PlayerRepository;
import match.repository.TeamRepository;
import match.service.TeamReadService;

@Service
public class TeamReadServiceImpl implements TeamReadService {
	
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private TeamMapper teamMapper;

	// 找出所有球隊
	@Override
	public List<TeamDTO> findAllTeams() {
		return teamRepository.findAllTeamDTOs();
	}

	@Override
	public TeamDTO findTeamByTeamId(Integer teamId) throws TeamException {
		// 先確認這個 teamId 有註冊 team 資料到資料庫。
		Optional<Team> optTeam = teamRepository.findById(teamId);
		if(optTeam.isEmpty()) {
			throw new TeamNotFoundException("TeamService: 查詢隊伍失敗，查無此隊伍編號："+teamId);
		}
		return teamMapper.toDTO(optTeam.get());
	}
	
	@Override
	public List<TeamDTO> findTeamByCapId(Integer playerId) throws TeamException{
		// 先確認這個 playerId 有存在 Player 資料庫：
		Optional<Player> optPlayer = playerRepository.findById(playerId);
		if(optPlayer.isEmpty()) {
			throw new TeamException("TeamService: 查詢隊伍失敗，查無此球員編號："+playerId);
		}
		return teamRepository.findTeamByCapId(playerId);
	}

	@Override
	public TeamDTO findTeamByName(String teamName) throws TeamException {
		// 確認這個 teamName 找得到
		Optional<TeamDTO> optTeam = teamRepository.findTeamByName(teamName);
		if(optTeam.isEmpty()) {
			throw new TeamNotFoundException("TeamService: 查詢隊伍失敗，查無此球隊名稱："+teamName);
		}
		return optTeam.get();
	}

	@Override
	public List<TeamDTO> findTeamByRecruit(Boolean recruit) {
		return teamRepository.findTeamByRecruit(recruit);
	}

	@Override
	public List<TeamDTO> findTeamByPlace(String place) {
		return teamRepository.findTeamByPlace(place);
	}

	@Override
	public List<TeamDTO> findTeamLevelAbove(Integer level) throws TeamException {
		List<TeamDTO> teams = teamRepository.findTeamLevelAbove(level);
		if(teams.isEmpty()) {
			throw new TeamException("TeamService: 查詢隊伍失敗，沒有匹配隊伍(等級高於："+ level+")");
		}
		return teams;
	}

	@Override
	public List<TeamDTO> findTeamLevelBelow(Integer level) throws TeamException {
		List<TeamDTO> teams = teamRepository.findTeamLevelBelow(level);
		if(teams.isEmpty()) {
			throw new TeamException("TeamService: 查詢隊伍失敗，沒有匹配隊伍(等級低於："+ level+")");
		}
		return teams;
	}

}
