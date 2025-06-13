package match.service;

import org.springframework.stereotype.Service;
import match.exception.TeamRefreshException;

@Service
public interface TeamRefreshDataService {
	
	/* AFTeamPlayerUpdate():
	 根據 player 在 teamplayer 當中新增刪除修改，需要重新計算總人數與平均級數，
	 重新計算完的數據，會由這個 TeamRefreshData 執行 AFTeamPlayerUpdate 方法。
	 在這個方法中，將會執行 service.update.TeamUpdateDataService 中的 Team 兩個欄位
	*/ 
	
	/*	AFTeamPlayerUpdate 說明這個方法只會在某個球隊的 TeamPlayer 被更改時執行，
		這個物件實體要在以下情況使用 (基本上會動到一個球隊裡的成員 這樣的動作，就需要執行這個更新)
		 1. 新增球員到這個球隊 (需要更新球員人數，平均級數)
		 2. 刪除球員到這個球隊 (需要更新球員人數，平均級數)
		 3. 更新一個球員的級數 (需要重新計算整個球隊的級數)
	 */
	
	// 需要傳入一個 teamId，並回傳布林值告訴你有沒有更新成功。
	public abstract Boolean AFTeamPlayerUpdate(Integer teamId) throws TeamRefreshException;
}
