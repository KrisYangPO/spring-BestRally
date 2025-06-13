package match.service;

import java.util.List;
import org.springframework.stereotype.Service;
import match.exception.PlayerException;
import match.model.dto.PlayerDTO;

@Service
public interface PlayerService {
	
	// 搜尋所有 player 並且轉成 PlayerDTO
	// PlayerRepository: findAllPlayerDTOs()
	List<PlayerDTO> findAllPlayerDTOs();
	
	// 搜尋特定 userId 的 player 轉成 PlayerDTO
	// PlayerRepository: readPlayerDTOByUserId(Integer userId)
	PlayerDTO findPlayerByUserId(Integer userId) throws PlayerException;
	
	// 根據 playerId 找球員
	PlayerDTO findPlayerByPlayerId(Integer playerId) throws PlayerException;
	
	// 新增 player:
	void addPlayer(Integer userId, Integer level) throws PlayerException;
	
	// 更新 player:
	void updatePlayerLevel(Integer userId, Integer level) throws PlayerException;
	
	// 刪除 player：
	void removePlayer(Integer userId) throws PlayerException;
	
}
