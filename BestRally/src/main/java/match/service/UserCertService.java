package match.service;

import org.springframework.stereotype.Service;

import match.exception.UserCertException;
import match.model.dto.UserCertDTO;

/** 登入驗證：
 *  透過輸入 username + password 進行驗證。
 *  驗證成功後，就會發行 UserCertDTO，這就可以交給 Controller 放到 Session
 *  透過檢查 Session 當中的 UserCertDTO 就可以知道目前有沒有登入狀態。
 *  就可以建立 filter 來處以這件事。
 */

@Service
public interface UserCertService {
	
	// 使用者名稱 + 密碼就可以發行 UserCertDTO 
	public abstract UserCertDTO loginGetCert(String username, String rowPassword) throws UserCertException;
}
