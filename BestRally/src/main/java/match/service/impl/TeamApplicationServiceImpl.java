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
import match.model.entity.TeamApplication;
import match.repository.TeamApplicationRepository;
import match.service.TeamApplicationService;

@Service
public class TeamApplicationServiceImpl implements TeamApplicationService {

	@Autowired
	private TeamApplicationRepository teamApplicationRepository;
	@Autowired
	private TeamApplicationMapper teamApplicationMapper;
	
	
	@Override
	public List<TeamApplicationDTO> findAllApplication() {
		return teamApplicationRepository.findAll().stream().map(teamApplicationMapper::toDTO).collect(Collectors.toList());
	}
	
	@Override
	public List<TeamApplicationDTO> findApplicationByTeamId(Integer teamId) {
		List<TeamApplication> teamApplications = teamApplicationRepository.findByTeamId(teamId);
		if(teamApplications.isEmpty() || teamApplications == null) {
			throw new TeamApplicationException("查詢球隊申請Service 錯誤：查無此隊伍編號："+teamId);
		}
		return teamApplications.stream().map(teamApplicationMapper::toDTO).collect(Collectors.toList());
	}

	// 新增球隊申請：
	@Override
	@Transactional
	public void addApplication(UserCertDTO userCertDTO, PlayerDTO playerDTO, TeamDTO teamDTO) {
		// 顯找出這個球隊的所有申請：
		List<TeamApplicationDTO> teamApplyDTOs = findApplicationByTeamId(teamDTO.getId());
		
		// 尋找這個 playerId 是不是已經在這個隊伍的申請名單中：
		Boolean check = teamApplyDTOs.stream().anyMatch(p->p.getPlayerId().equals(playerDTO.getId()));
		if(check) {
			throw new TeamApplicationException(String.format("新增球隊申請失敗，球員(%s)已經申請加入球隊(%s)。", playerDTO.getUsername(), teamDTO.getTeamName()));
		}
		// 建立申請：
		TeamApplication teamApply = new TeamApplication(null, userCertDTO.getId(), userCertDTO.getEmail(), playerDTO.getId(), playerDTO.getLevel(), teamDTO.getId(), teamDTO.getTeamName());
		teamApplicationRepository.save(teamApply);
	}
	
	
	// 刪除編號
	@Override
	@Transactional
	public void dropApplication(Integer id) {
		Optional<TeamApplication> optTeamApply = teamApplicationRepository.findById(id);
		if(optTeamApply.isEmpty()) {
			throw new TeamApplicationException("查詢球隊申請Service 錯誤：查無此申請編號：" + id);
		}
		teamApplicationRepository.deleteById(id);
	}



}
