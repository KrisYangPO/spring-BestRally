package match.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import match.exception.MatchArrangeException;
import match.exception.MatchPlayerException;
import match.model.dto.MatchPlayerDTO;
import match.service.SetPlayerMatchService;
import match.util.MatchTools;

/* 根據自訂排序，傳出四名球員為一組，
 *  arrangeBattle 建立出組編號與成員的 Map 資料發送至前端。
 *  對戰結束後，將 Map currentPlayer 對戰中刪除。
 *  
 *  特別要注意，在執行 closeBattle 時，要將對戰紀錄儲存在各個 player 的 matchPlayerDTO 資訊裡。
 *  才可以將對戰完的球員放回 matchPlayers Session 資料，
 *  因為刪除之前要先更新數據才可以重新啟動 Session 做新一輪抽取。
 */

@Service
public class SetPlayerMatchServiceImpl implements SetPlayerMatchService {
	
	// 建立隨機數物件：
	Random random = new Random();
	
	@Override
	@Transactional
	public List<MatchPlayerDTO> supplementPlayers(List<MatchPlayerDTO> matchPlayers, List<MatchPlayerDTO> battlePlayers) throws MatchPlayerException {
		
		// 如果對戰隊伍完全沒有依照球員等級所篩選出來的球員，
		// 表示：沒有任何球員可以根據 Level threshold 與第一個對戰隊伍中加入的球員所匹配。
		if(battlePlayers.isEmpty()) {
			// 要先判斷這個球員列表目前有沒有大於等於 4 個人：
			if(matchPlayers.size() < 4) {
				System.err.println("自動編排錯誤：備戰球員不足 4 人，目前：" + matchPlayers.size() + " 人。");
				throw new MatchArrangeException("自動編排錯誤：備戰球員不足 4 人，目前：" + matchPlayers.size() + " 人。");
			}
		}
		
		
		// 1. 取對戰場數最小值 (TreeSet 進行排序)：
		Set<Integer> setCurrentTimes = new TreeSet<>();
		setCurrentTimes = matchPlayers.stream().map(p->p.getTotalMatch()).sorted().collect(Collectors.toSet());
		
		// 將所有場數種類從 Set 轉成 List 
		List<Integer> currentTimeList = new ArrayList<Integer>();
		for(Integer i : setCurrentTimes) {
			currentTimeList.add(i);
		}
		
		
		// 目前總場數的有幾種：
		int currentTimesSize = currentTimeList.size();
		
		
		// 2. 對 matchPlayers 進行排序 (根據 matchPlayer 的總場數排序)：
		Comparator<MatchPlayerDTO> sortByTotalMatch = (MatchPlayerDTO m1, MatchPlayerDTO m2) -> {return m1.getTotalMatch() - m2.getTotalMatch();};
		Collections.sort(matchPlayers, sortByTotalMatch);
		
		
		// 3. 按照場次抓取：
		// 判斷要以等於多少場次的人上去：
		// 最多只能四個
		while(battlePlayers.size() < 4 && !matchPlayers.isEmpty()) {
			boolean foundAny = false;
			
			// 不同的場次區段，如果場數最小值找不到四個，就取第二個最小值。
			for(int nextTime = 0; nextTime < currentTimesSize; nextTime ++ ) {
				System.out.println("自動編排：目前依照場次區間：" + currentTimeList.get(nextTime) + " 尋找匹配球員。");
				
				// 不用 for 回圈，用 iterator 物件：
				Iterator<MatchPlayerDTO> iterator = matchPlayers.iterator();
				
				// 遍歷每個球員：
				while(iterator.hasNext()) {
					MatchPlayerDTO mp = iterator.next();
					
					// 如果球員的總場次小於等於目前最低場數，就加入對戰。
					if (mp.getTotalMatch() <= currentTimeList.get(nextTime)) {
						battlePlayers.add(mp);
						iterator.remove();
						System.out.printf("自動編排：根據場次(%s)且不考慮程度閾值，加入球員(%s)到對戰隊伍。%n",
								currentTimeList.get(nextTime), mp.getUserName());
					}else {
						continue;
					}
					if (battlePlayers.size() == 4) {
						System.out.println("自動編排成功，目前依照場次區間：" + currentTimeList.get(nextTime) + "，列表：" + battlePlayers);
						foundAny = true;
						break;}
				}
				if (battlePlayers.size() == 4) {
					System.out.println("自動編排成功，目前依照場次區間：" + currentTimeList.get(nextTime) + "，列表：" + battlePlayers);
					foundAny = true;
					break;}
			}
			
			// 找不到任何一個匹配的球員
			if (!foundAny) {
				throw new MatchArrangeException("自動編排錯誤：所有場次間距找完還是沒有 4 個人。");
			};
		}
		return battlePlayers;
	}
	
	

	@Override
	@Transactional
	public List<MatchPlayerDTO> arrangePlayerWithLevel(List<MatchPlayerDTO> matchPlayers, Integer levelThreshold) throws MatchPlayerException {
		
		System.out.println("自動編排：目前備戰人數：" + matchPlayers.size());
		
		// 要先判斷這個球員列表目前有沒有大於等於 4 個人：
		if(matchPlayers.size() < 4) {
			System.err.println("自動編排錯誤：備戰球員不足 4 人，目前：" + matchPlayers.size() + " 人。");
			throw new MatchArrangeException("自動編排錯誤：備戰球員不足 4 人，目前：" + matchPlayers.size() + " 人。");
		}
		
		// 1. 取對戰場數最小值 (TreeSet 進行排序)：
		Set<Integer> setCurrentTimes = new TreeSet<>();
		setCurrentTimes = matchPlayers.stream().map(p->p.getTotalMatch()).sorted().collect(Collectors.toSet());
		
		// 將所有場數種類從 Set 轉成 List 
		List<Integer> currentTimeList = new ArrayList<Integer>();
		for(Integer i : setCurrentTimes) {
			currentTimeList.add(i);
		}
		System.out.println("自動編排：目前所有人的上場次數：" + currentTimeList);
		// 目前總場數的有幾種：
		int currentTimesSize = currentTimeList.size();
		
		
		// 2. 對 matchPlayers 進行排序 (根據 matchPlayer 的總場數排序)：
		Comparator<MatchPlayerDTO> sortByTotalMatch = (MatchPlayerDTO m1, MatchPlayerDTO m2) -> {return m1.getTotalMatch() - m2.getTotalMatch();};
		Collections.sort(matchPlayers, sortByTotalMatch);
		
		
		// 3. 按照場次抓取：
		// 對戰人員暫存：
		List<MatchPlayerDTO> battlePlayers = new ArrayList<>();
		
		// 判斷要以等於多少場次的人上去：
		// 最多只能四個
		while(battlePlayers.size() < 4 && !matchPlayers.isEmpty()) {
			boolean foundAny = false;
			
			// 不同的場次區段，如果場數最小值找不到四個，就取第二個最小值。
			for(int nextTime = 0; nextTime < currentTimesSize; nextTime ++ ) {
				System.out.printf("自動編排：目前依照場次區間(%s)，與浮動程度閾值(0~%s)尋找匹配球員。%n", 
									currentTimeList.get(nextTime),
									levelThreshold);
				
				// 不用 for 回圈，用 iterator 物件：
				Iterator<MatchPlayerDTO> iterator = matchPlayers.iterator();
				
				// 遍歷每個球員：
				while(iterator.hasNext()) {
					MatchPlayerDTO mp = iterator.next();
					
					// 如果球員的總場次小於等於目前最低場數，就加入對戰。
					if (mp.getTotalMatch() <= currentTimeList.get(nextTime)) {
						// 目前對戰隊伍的球員 Levels
						List<Integer> levels = MatchTools.findAllPlayerLevels(battlePlayers);
						
						// 接著判斷該球員 Level：
						Integer thisLevel = mp.getPlayerLevel();
						levels.add(thisLevel);
						
						// 設定 playerLevel 的隨機閾值：
						// 如果 levelThreshold ==0 就會觸發：IllegalArgumentException
						// 這裡說明如果 levelThreshold 給的值是零，則直接將 threshold 設為 0。
						// 如果給的值是 3 則讓他找 0~3 的值 random.nextInt()
						int threshold = (levelThreshold <= 0) ? 0 : random.nextInt(levelThreshold + 1);
						// 避免 levelThreshold 為 0 時拋出 IllegalArgumentException
						
						// 判斷目前的對戰隊伍程度差異是否太大。
						if(MatchTools.checkLevelByLevels(threshold, levels)) {
							
							// 加入對戰隊伍。 
							battlePlayers.add(mp);
							// 從備戰隊伍中刪除。
							iterator.remove();
							foundAny = true;	
							System.out.printf("自動編排：根據場次(%s)與程度閾值(%s)，加入球員(%s)到對戰隊伍。%n",
									currentTimeList.get(nextTime), threshold, mp.getUserName());
						
						// 如果不行就找下一個球員
						}else {
							System.out.printf("---目前找不到匹配閾值(%s)的球員，繼續尋找---%n", threshold);
							continue;
						}
						// 如果在這個場次範圍內找到四個
						if (battlePlayers.size() == 4) {
							System.out.printf("自動編排成功，目前依照場次區間：%s，程度差異閾值：%s，得到結果：%s%n",
									currentTimeList.get(nextTime), 
									levelThreshold,
									battlePlayers);
							foundAny = true;
							break;
						}
					}
				}
				// 判斷在所有場次內，找到符合場次與球員等級的 4 個球員。
				// 如果在這個場次範圍內找到四個
				if (battlePlayers.size() == 4) {
					System.out.println("自動編排成功，目前依照場次區間：" + currentTimeList.get(nextTime) + "，列表：" + battlePlayers);
					foundAny = true;
					break;
				}
			}
			
			// 找不到任何一個匹配的球員
			if (!foundAny) {
				System.err.println("自動編排: 根據場次與程度無法滿足四名球員，執行根據場次排序的編排 (不考慮球員程度)。");
				try {
					// 執行依據球員場次排序，不根據球員程度抓取的動作
					List<MatchPlayerDTO> battlePlayersNoLevel = supplementPlayers(matchPlayers, battlePlayers);
					System.out.println("自動編排成功 (不根據球員等級排序)：" + battlePlayersNoLevel);
					return battlePlayersNoLevel;
					
				} catch (MatchArrangeException e) {
					e.printStackTrace();
					System.err.println("自動編排錯，無法順利進行自動編排：" + e.getMessage());
					throw new MatchArrangeException("自動編排錯，無法順利進行自動編排：" + e.getMessage());
				}
			}
		}
		return battlePlayers;
	}
	
	
	
	// 抓取到四個球員後，就與目前的場次與對戰球員建立 Map
	@Override
	public Map<Integer, List<MatchPlayerDTO>> arrangeBattle(Integer groupNum, List<MatchPlayerDTO> matchPlayers) {
		Map<Integer, List<MatchPlayerDTO>> battle = new TreeMap<>();
		battle.put(groupNum, matchPlayers);
		
		return battle;
	}

	
	// 隊伍比賽結束後，就可以將這組對戰隊伍從對戰隊伍編排 Map 當中刪除，
	// 回傳更新後的 battlePlayers Map 列表。
	@Override
	public Map<Integer, List<MatchPlayerDTO>> closeBattle(Integer groupNum, Map<Integer, List<MatchPlayerDTO>> battlePlayers) {
		battlePlayers.remove(groupNum);
		return battlePlayers;
	}

	
	// 用舊的編排號碼，與重新編排一次的對戰隊伍組合。
	@Override
	public Map<Integer, List<MatchPlayerDTO>> resetArrange(
			Integer curGroupNum,
			Map<Integer, List<MatchPlayerDTO>> battlePlayers, 
			List<MatchPlayerDTO> matchPlayers, 
			Integer levelThreshold)  throws MatchPlayerException {
		
		// 先刪除不要的對戰組合：
		battlePlayers.remove(curGroupNum);
		
		// 重新執行編排：
		List<MatchPlayerDTO> players = arrangePlayerWithLevel(matchPlayers, levelThreshold);
		// 用舊的序號與新的編排組合。
		battlePlayers.put(curGroupNum, players);
		
		return battlePlayers;
	}


	// 對戰結束後，就要將對戰隊伍中的球員放回 Session 的備戰區隊伍 (matchPlayers)。
	@Override
	public List<MatchPlayerDTO> returnPlayers(
			Integer curGroupNum, 
			Map<Integer, List<MatchPlayerDTO>> battlePlayers,
			List<MatchPlayerDTO> matchPlayers) {
		
		// 取得對戰隊伍球員的 MatchPlayers，直接加回去。
		for (MatchPlayerDTO mp : battlePlayers.get(curGroupNum)) {
			matchPlayers.add(mp);
		}
		// 重新設定 session 當中的 matchPlayers 備戰區球員。
		return matchPlayers;
	}
}
