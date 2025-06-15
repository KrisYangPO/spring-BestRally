package match.util;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import match.model.dto.MatchPlayerDTO;

public class MatchTools {
	// 判斷陣列裡面的數值差異是否大於多少：
	public static Boolean checkLevelByMPs(Integer threshDiff, List<MatchPlayerDTO> battlePlayers) {
		// 透過 IntSummaryStatistics 計算所有運算：
		IntSummaryStatistics stats = battlePlayers
								.stream()
			    				.mapToInt(MatchPlayerDTO::getPlayerLevel)
			    				.summaryStatistics();
		
		// 找出最大差異數 = 最大值 - 最小值
		int maxDiff = stats.getCount() > 0 ? (stats.getMax() - stats.getMin()) : 0;
		
		// 判斷最大差異值是否小於等於差異閾值。
		return maxDiff <= threshDiff;
	}
	
	
	// 根據 level 判斷 level 陣列裡面的數值差異是否大於多少閾值：
	public static Boolean checkLevelByLevels(Integer threshDiff, List<Integer> levels) {		
		// 透過 IntSummaryStatistics 計算所有運算：
		IntSummaryStatistics stats = levels
								.stream().mapToInt(p->p)
			    				.summaryStatistics();
		
		// 找出最大差異數 = 最大值 - 最小值
		int maxDiff = stats.getCount() > 0 ? (stats.getMax() - stats.getMin()) : 0;
		
		// 判斷最大差異值是否小於等於差異閾值。
		return maxDiff <= threshDiff;
	}
	
	
	// 找出所有 playerLevel:
	// 有可能目前的 battlePlayers 是空集合，表示目前還沒有匹配對戰球員加入隊伍，
	// 會回傳空集合，這會交給 checkLevelByLevels 自行判斷。
	public static List<Integer> findAllPlayerLevels(List<MatchPlayerDTO> battlePlayers){
		return battlePlayers.stream().map(p-> p.getPlayerLevel()).collect(Collectors.toList());
	}
}
