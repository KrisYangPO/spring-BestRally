package match.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import match.exception.PlayerException;
import match.exception.PlayerNotFoundException;
import match.mapper.PlayerMapper;
import match.model.dto.PlayerDTO;
import match.model.entity.Player;
import match.model.entity.User;
import match.repository.PlayerRepository;
import match.repository.UserRepository;
import match.service.PlayerService;

@Service
public class PlayerServiceImpl implements PlayerService{
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	// 將 playerDTO 轉 Player Entity 需要特別方式執行：
	@Autowired
	private PlayerMapper playerMapper;

	
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
	public void updatePlayerLevel(Integer userId, Integer level) throws PlayerException {
		// 先確認 userId 是否存在：
		PlayerDTO playerDTO = findPlayerByUserId(userId);
		// 將參數帶入更新 level:
		playerDTO.setLevel(level);
		
		// 確認 userId:
		Optional<User> optUser = userRepository.findById(userId);
		if(optUser.isEmpty()) {
			throw new PlayerException("PlayerService: 更新球員失敗，使用者不存在："+userId);
		}
		User user = optUser.get();
		
		// 透過 setEntity 將 playerDTO 資訊與 User Entity 組合成 Player Entity.
		Player player = playerMapper.setEntity(playerDTO, user);
		
		playerRepository.save(player);
	}

	// 刪除球員。
	@Override
	public void removePlayer(Integer userId) {
		// 先確認 userId 是否存在：
		PlayerDTO playerDTO = findPlayerByUserId(userId);
		
		// 不能透過 userId 刪除，要用 playerId 刪除。
		Integer playerId = playerDTO.getId();
		playerRepository.deleteById(playerId);
	}
}
