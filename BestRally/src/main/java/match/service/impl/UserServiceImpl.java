package match.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import match.exception.UserAlreadyExistsException;
import match.exception.UserException;
import match.exception.UserNotFoundException;
import match.mapper.UserMapper;
import match.model.dto.UserDTO;
import match.model.entity.User;
import match.repository.UserRepository;
import match.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserMapper userMapper;

	// 尋找所有 user
	@Override
	public List<UserDTO> findAllUsers() {
		return userRepository.findAllUsers().stream().map(userMapper::toDTO).collect(Collectors.toList());
	}

	// 根據 id 找 user
	@Override
	public User findByUserId(Integer userId) throws UserException{
		// 先檢查 UserID 是否有紀錄資料在資料庫。
		Optional<User> optUser = userRepository.findById(userId);
		if(optUser.isEmpty()) {
			throw new UserNotFoundException("UserService: 找尋使用者失敗，查無此 userId:" + userId);
		}
		
		// 如果有東西，就將 User回傳。
		return optUser.get();
	}

	
	// 新增使用者
	@Override
	public void addUser(String username, String hashPassword, String hashSalt, String email, String photo, Boolean admin)
			throws UserException {
		// 先根據 username 看看有沒有重複輸入到資料庫
		Optional<User> optUser = userRepository.readUserByName(username);
		if(optUser != null) {
			throw new UserAlreadyExistsException("UserService: 新增使用者錯誤，使用者名稱已經註冊：" + username);
		}
		// 若 optUser 不存在，就可以加入資料庫。
		User user = new User(null, username, hashPassword, hashSalt, email, photo, admin, null);
		userRepository.save(user);
	}
	
	
	// 更新使用者資訊。
	@Override
	public void updateUser(Integer userId, User newUser) throws UserException {
		// 要先確認 userId 在資料庫中有紀錄：
		try {
			User user = findByUserId(userId);
			// 確認這個 userId 可以更新，就直接將這個 newUser 帶入。
			userRepository.save(newUser);
			
		} catch (UserException e) {
			e.printStackTrace();
			throw new UserNotFoundException("UserService: 更新使用者發生錯誤，查無此使用者編號："+userId + e.getMessage());
		}
	}
	
	
	/** 部分更新邏輯：
	 *  部分資料更新像是更新使用者名稱，email, password 等資訊，
	 *  最後依舊會交給 Service 裡的 updateUser 進行資料更新。
	 *  
	 *  因此在更新這些部分內容，主要會先：
	 *  1. 確認這個 userId 可以找到資料，
	 *  2. 透過找出來的 User entity，用 Setter 方式用新參數更新特定屬性。
	 *  3. 用 save() 進行判斷更新。
	 */
	
	// 更新使用者名稱
	@Override
	public void updateUserName(Integer userId, String username) throws UserException {
		try {
			User user = findByUserId(userId);
			user.setUsername(username);
			// 直接執行 Service:updateUser
			updateUser(userId, user);
			
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			throw new UserNotFoundException("UserService: 更新使用者名稱發生錯誤，查無此使用者編號："+userId + e.getMessage());
		}
	}

	// 更新密碼
	@Override
	public void updateUserPassword(Integer userId, String hashPassword) throws UserException {
		try {
			User user = findByUserId(userId);
			user.setHashPassword(hashPassword);
			// 直接執行 Service:updateUser
			updateUser(userId, user);
			
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			throw new UserNotFoundException("UserService: 更新使用者密碼時發生錯誤，查無此使用者編號："+userId + e.getMessage());
		}
		
	}

	// 更新 email
	@Override
	public void updateUserEmail(Integer userId, String email) throws UserException {
		try {
			User user = findByUserId(userId);
			user.setEmail(email);
			// 直接執行 Service:updateUser
			updateUser(userId, user);
			
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			throw new UserNotFoundException("UserService: 更新使用者信箱時發生錯誤，查無此使用者編號："+userId + e.getMessage());
		}
		
	}
	
	// 更新照片
	@Override
	public void updateUserPhoto(Integer userId, String photo) throws UserException {
		try {
			User user = findByUserId(userId);
			user.setPhoto(photo);
			// 直接執行 Service:updateUser
			updateUser(userId, user);
			
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			throw new UserNotFoundException("UserService: 更新使用者照片時發生錯誤，查無此使用者編號："+userId + e.getMessage());
		}
	}
	
	// 刪除
	@Override
	public void deleteUser(Integer userId) throws UserException {// 要先確認 userId 在資料庫中有紀錄：
		try {
			// 確認有這個 userId 就把他刪除。
			User user = findByUserId(userId);		
			userRepository.deleteById(userId);
			
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			throw new UserNotFoundException("UserService: 刪除使用者發生錯誤，查無此使用者編號："+userId + e.getMessage());
		}
	}

}
