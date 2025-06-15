package match.model.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class MatchPlayerDTO {
	// player 基本資訊
	private Integer playerId;
	private Integer userId;
	private String userName;
	private Integer playerLevel;
	private Integer teamId;
	
	// 自訂 constructor
	public MatchPlayerDTO(Integer playerId, Integer userId, Integer teamId, String username, Integer playerLevel) {
		this.playerId = playerId;
		this.userId = userId;
		this.teamId = teamId;
		this.userName = username;
		this.playerLevel = playerLevel;
	}
	
	// 對戰紀錄資訊：
	// 每次對戰紀錄勝負，勝 add(1), 敗則 add(0), 長度就是上場次數。
	private List<Integer> matchRecord = new ArrayList<>();
	
	// 上場次數 = 總場數，也就是 matchRecord 長度。
	private Integer totalMatch = 0;
	
	// 計算勝場數：挑選出 matchRecord 裡面為 1 的總和，就是總勝場。
	private Integer winGame = 0;
	
	
	// 將對戰紀錄加入到 matchRecord
	public void setMatchRecord(Integer score) {
		// 加入分數紀錄
		this.matchRecord.add(score);
		
		// 紀錄勝場
		this.winGame =  matchRecord
				.stream()
				.filter(m -> m == 1)
				.mapToInt(Integer::intValue)
				.sum();
		// mapToInt 將 Stream<Integer> 轉成 IntStream 才可以藉由 sum() 計算總和。
		
		// 紀錄總場次
		this.totalMatch = matchRecord.size();
	}
}
