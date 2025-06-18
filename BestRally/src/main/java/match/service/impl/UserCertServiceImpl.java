package match.service.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import match.exception.UserCertException;
import match.exception.UserCertUserNotExistsException;
import match.exception.UserCertWrongPasswdException;
import match.mapper.UserMapper;
import match.model.dto.UserCertDTO;
import match.model.entity.User;
import match.repository.UserRepository;
import match.service.UserCertService;
import match.util.Hash;

@Service
public class UserCertServiceImpl implements UserCertService {

	@Autowired
	private UserRepository userRepository;
	

	@Override
	public UserCertDTO loginGetCert(String username, String rowPassword) throws UserCertException {
		
		// STEP1. 確認這個使用者存在於資料庫。
		Optional<User> optUser = userRepository.readUserByName(username);
		if(optUser.isEmpty()) {
			throw new UserCertUserNotExistsException("登入失敗：查無此使用者註冊資訊：" + username);
		}
		
		// STEP2. 確認密碼：
		// 先把使用者當初註冊時儲存的 hashSalt 取出，與輸入密碼建立 hashPassword
		User loginUser = optUser.get();
		String hashSalt = loginUser.getHashSalt();
		String loginHashPassword = Hash.getHash(rowPassword, hashSalt);
		
		// 進行判斷：
		if(!loginHashPassword.equals(loginUser.getHashPassword())) {
			throw new UserCertWrongPasswdException("登入失敗：密碼輸入錯誤，請重新嘗試。");
		}
		
		// STEP3. 根據登入資訊建立 UserCertDTO:
		
		return new UserCertDTO(loginUser.getId(),loginUser.getUsername(), loginUser.getEmail(), loginUser.getPhoto(), loginUser.getAdmin());
	}
}
