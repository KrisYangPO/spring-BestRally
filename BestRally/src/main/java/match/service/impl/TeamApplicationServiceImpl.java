package match.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import match.exception.TeamApplicationException;
import match.mapper.TeamApplicationMapper;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamApplicationDTO;
import match.model.dto.TeamDTO;
import match.model.dto.UserCertDTO;
import match.model.entity.Player;
import match.model.entity.TeamApplication;
import match.repository.PlayerRepository;
import match.repository.TeamApplicationRepository;
import match.service.TeamApplicationService;

@Service
public class TeamApplicationServiceImpl implements TeamApplicationService {

	@Autowired
	private TeamApplicationRepository teamApplicationRepository;
	@Autowired
	private TeamApplicationMapper teamApplicationMapper;
	@Autowired
	private PlayerRepository playerRepository;
	
	
	@Override
	public List<TeamApplicationDTO> findAllApplication() {
		return teamApplicationRepository.findAll().stream().map(teamApplicationMapper::toDTO).collect(Collectors.toList());
	}
	
	@Override
	public List<TeamApplicationDTO> findByTeamId(Integer teamId) {
		List<TeamApplication> teamApplys = teamApplicationRepository.findByTeamId(teamId);
		if(teamApplys.size() < 1 || teamApplys == null) {
			throw new TeamApplicationException("查詢球隊申請Service 錯誤：查無此球隊編號：" + teamId);
		}
		return teamApplys.stream().map(teamApplicationMapper::toDTO).collect(Collectors.toList());
	}
	
	// 根據球隊編號與球員編號找出特定一筆球隊申請。
	@Override
	public TeamApplicationDTO findByTeamAndPlayerId(Integer teamId, Integer playerId) {
		Optional<TeamApplication> optTeamApply = teamApplicationRepository.findByPlayerAndTeamId(teamId, playerId);
		if(optTeamApply.isEmpty() || optTeamApply == null) {
			throw new TeamApplicationException("查詢球隊申請Service 錯誤：查無此隊伍編號："+teamId + " 球員編號：" + playerId);
		}
		return teamApplicationMapper.toDTO(optTeamApply.get());
	}

	// 新增球隊申請：
	@Override
	@Transactional
	public void addApplication(UserCertDTO userCertDTO, PlayerDTO playerDTO, TeamDTO teamDTO) throws TeamApplicationException {
		// 尋找這個 playerId 是不是已經在這個隊伍的申請名單中：
		try {
			TeamApplicationDTO teamApplyDTO = findByTeamAndPlayerId(teamDTO.getId(), playerDTO.getId());
			
		} catch (TeamApplicationException e) {
			System.out.println("TeamApplyService: 球員還沒申請加入此球隊過，可以申請。");
		}
		
		// 建立 player 實體：
		Player player = playerRepository.findById(playerDTO.getId()).get();
		
		// 建立申請：
		TeamApplication teamApply = new TeamApplication(null, userCertDTO.getId(), userCertDTO.getUsername(), userCertDTO.getEmail(), player, teamDTO.getId(), teamDTO.getTeamName());
		teamApplicationRepository.save(teamApply);
	}
	
	
	// 刪除編號
	@Override
	@Transactional
	public void dropApplication(Integer teamId, Integer playerId) throws TeamApplicationException  {
		TeamApplicationDTO teamApplyDTO = findByTeamAndPlayerId(teamId, playerId);
		if(teamApplyDTO == null) {
			throw new TeamApplicationException(String.format("查詢球隊申請Service 錯誤：查無此申請球隊(%s)與球員(%s)%n", teamId, playerId));
		}
		// 透過這個申請的編號刪除申請。
		teamApplicationRepository.deleteById(teamApplyDTO.getId());
	}
	
	
	// 刪除編號
	@Override
	@Transactional
	public void dropApplication(Integer id) throws TeamApplicationException  {
		Optional<TeamApplication> optTeamApply = teamApplicationRepository.findById(id);
		if(optTeamApply.isEmpty()) {
			throw new TeamApplicationException(String.format("查詢球隊申請Service 錯誤：查無此申請編號(%s)%n",id));
		}
		// 透過這個申請的編號刪除申請。
		teamApplicationRepository.deleteById(id);
	}
}
