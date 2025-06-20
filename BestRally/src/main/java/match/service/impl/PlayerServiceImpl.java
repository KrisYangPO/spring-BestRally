package match.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import match.annotation.DoTeamRefresh;
import match.exception.PlayerException;
import match.exception.PlayerNotFoundException;
import match.exception.TeamPlayerException;
import match.exception.TeamRefreshException;
import match.mapper.PlayerMapper;
import match.model.dto.PlayerDTO;
import match.model.dto.TeamDTO;
import match.model.dto.TeamPlayerDTO;
import match.model.dto.TeamsOfPlayerDTO;
import match.model.entity.Player;
import match.model.entity.User;
import match.repository.PlayerRepository;
import match.repository.UserRepository;
import match.service.PlayerService;
import match.service.TeamPlayerService;
import match.service.TeamRefreshDataService;

@Service
public class PlayerServiceImpl implements PlayerService{
	
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private UserRepository userRepository;
	// 將 playerDTO 轉 Player Entity 需要特別方式執行：
	@Autowired
	private PlayerMapper playerMapper;
	@Autowired
	private TeamPlayerService teamPlayerService;
	@Autowired
	private TeamRefreshDataService teamRefreshDataService; 

	
	// 尋找所有球員
	@Override
	public List<PlayerDTO> findAllPlayerDTOs() {
		return playerRepository.findAllPlayerDTOs();
	}

	// 根據 userId 找尋球員
	@Override
	public PlayerDTO findPlayerByUserId(Integer userId) throws PlayerException {
		Optional<PlayerDTO> optPlayerDTO = playerRepository.readPlayerDTOByUserId(userId);
		if(optPlayerDTO.isEmpty()) {
			throw new PlayerNotFoundException("PlayerService: 查詢球員失敗，查無此使用者編號:"+userId);
		}
		return optPlayerDTO.get();
	}

	
	// 透過 playerId 找球員。
	@Override
	public PlayerDTO findPlayerByPlayerId(Integer playerId) throws PlayerException {
		Optional<PlayerDTO> optPlayerDTO = playerRepository.readPlayerDTOByPlayerId(playerId);
		if(optPlayerDTO.isEmpty()) {
			throw new PlayerNotFoundException("PlayerService: 查詢球員失敗，查無此球員編號:"+playerId);
		}
		return optPlayerDTO.get();
	}
	
	
	// 新增球員身份，根據 userId 並設定 level
	@Override
	public void addPlayer(Integer userId, Integer level) throws PlayerException {
		
		// 要先確認這個 userId 沒有 Player 身份：
		Optional<PlayerDTO> optPlayerDTO = playerRepository.readPlayerDTOByUserId(userId);
		if(optPlayerDTO.isPresent()) {
			throw new PlayerException("PlayerService: 新增球員身份失敗，使用者已經有球員身份："+userId);
		}
		// 這個 userId 在 player 表格中尋找不到，可能這個 userId 根本不存在，
		// 因此要另外用 UserRepository find.byId 尋找，順便建立 Player 建構子。
		Optional<User> optUser = userRepository.findById(userId);
		if(optUser.isEmpty()) {
			throw new PlayerException("PlayerService: 新增球員身份失敗，使用者不存在："+userId);
		}
		// 建立 player 身份：
		Player player = new Player(null, level, optUser.get());
		playerRepository.save(player);
	}

	
	// 更新球員數據。
	@Override
	@Transactional
	@DoTeamRefresh(teamIdParam = "userId")
	public void updatePlayerLevel(Integer userId, Integer level) throws PlayerException {
		// Step1. 先確認 userId 是否存在：
		PlayerDTO playerDTO = findPlayerByUserId(userId);
		// 將參數帶入更新 level:
		playerDTO.setLevel(level);
		
		// Step2. 確認 userId:
		Optional<User> optUser = userRepository.findById(userId);
		if(optUser.isEmpty()) {
			throw new PlayerException("PlayerService: 更新球員失敗，使用者不存在："+userId);
		}
		User user = optUser.get();
		
		// Step3. 透過 setEntity 將 playerDTO 資訊與 User Entity 組合成 Player Entity.
		Player player = playerMapper.setEntity(playerDTO, user);
		playerRepository.save(player);
		
//		// Step4. 更新球隊數據：
//		// 確認是否有加入球隊：
//		try {
//			List<TeamPlayerDTO> teamPlayers = teamPlayerService.findByPlayerId(playerDTO.getId());
//		
//			// 更新這個 player 參加球隊的數據：
//			// TeamsOfPlayerDTO 取得這個 player 所參加的所有球隊。
//			TeamsOfPlayerDTO teamsOfPlayerDTO = teamPlayerService.getTeamsFromPlayer(playerDTO.getId());
//			// 遍歷每個球員的 teamDTO，呼叫他們的 teamId 進行 teamRefreshDataService 動作。
//			for(TeamDTO team: teamsOfPlayerDTO.getTeamDTOs()) {
//				try {
//					// 執行球隊更新：
//					Boolean check = teamRefreshDataService.AFTeamPlayerUpdate(team.getId());
//					if(check) {
//						System.out.printf("TeamRefresh: 更新球員(%s)的球隊(%s)的數據成功。%n",playerDTO.getUsername(), team.getTeamName());
//					}
//				// 更新球隊數據有問題：
//				}catch (TeamRefreshException e) {
//					e.printStackTrace();
//					throw new PlayerException("PlayerService: 更新球員等級後，更新球隊數據錯誤：" + team.getTeamName());
//				}
//			}
//		// 找不到 player 編號。
//		} catch (TeamPlayerException e) {
//			e.printStackTrace();
//			System.err.println("PlayerService: 此球員尚未加入球隊，不需要更新球隊資訊："+playerDTO.getUsername()+"\n" + e.getMessage());
//		}
	}

	
	// 刪除球員。
	@Override
	@Transactional
	public void removePlayer(Integer userId) throws PlayerException {
		// 先確認 userId 是否存在：
		PlayerDTO playerDTO = findPlayerByUserId(userId);
		
		
		// 確認是否有加入球隊：
		try {
			List<TeamPlayerDTO> teamPlayers = teamPlayerService.findByPlayerId(playerDTO.getId());
			// 不能透過 userId 刪除，要用 playerId 刪除。
			Integer playerId = playerDTO.getId();
			
			// 如果 playerId 沒有找到隊伍隊員，表示此球員沒有加入任何球隊，
			// 有加入(else)就要更新他們的數據。
			if(teamPlayers == null || teamPlayers.isEmpty() || teamPlayers.size() < 1) {
				System.out.println("PlayerService: 此球員尚未加入球隊，不需要更新球隊資訊："+playerDTO.getUsername());
				// 直接進行刪除。
				playerRepository.deleteById(playerId);
				return; 
			}
			
			// 刪除之前，先將球員所參與的球隊儲存到 TeamsOfPlayerDTO。
			// TeamsOfPlayerDTO 取得這個 player 所參加的所有球隊。
			TeamsOfPlayerDTO teamsOfPlayerDTO = teamPlayerService.getTeamsFromPlayer(playerDTO.getId());
			
			// 要先將數據刪除後，才可以進行更新。
			// ================================================================================================================
			playerRepository.deleteById(playerId);
			// ================================================================================================================
			
			// 遍歷每個球員的 teamDTO，呼叫他們的 teamId 進行 teamRefreshDataService 動作。
			for(TeamDTO team: teamsOfPlayerDTO.getTeamDTOs()) {
				try {
					// 執行球隊更新：
					Boolean check = teamRefreshDataService.AFTeamPlayerUpdate(team.getId());
					if(check) {
						System.out.printf("TeamRefresh: 更新球員(%s)的球隊(%s)的數據成功。%n",playerDTO.getUsername(), team.getTeamName());
					}
				// 更新球隊數據有問題：
				}catch (TeamRefreshException e) {
					e.printStackTrace();
					throw new PlayerException("PlayerService: 更新球員等級後，更新球隊數據錯誤：" + team.getTeamName());
				}
			}
		// 找不到 player 編號。
		} catch (TeamPlayerException e) {
			e.printStackTrace();
			throw new PlayerException("PlayerService: 查詢此 player 編號錯誤："+playerDTO.getId());
		}
	}
}
