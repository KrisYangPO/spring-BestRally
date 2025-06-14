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
import match.model.dto.UserRegisterDTO;
import match.model.entity.User;
import match.repository.UserRepository;
import match.service.UserService;
import match.util.Hash;

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
	public void addUser(UserRegisterDTO userRegisterDTO) throws UserException {
		
		// 先根據 username 看看有沒有重複輸入到資料庫
		Optional<User> optUser = userRepository.readUserByName(userRegisterDTO.getUsername());
		if(optUser.isPresent()) {
			throw new UserAlreadyExistsException("UserService: 新增使用者錯誤，使用者名稱已經註冊：" + userRegisterDTO.getUsername());
		}
		
		// userRegisterDTO 只會儲存 1.username, 2. password, 3. email, 4. photo
		// 但是依舊可以將相同的屬性名稱用 ModelMapper 物件轉成 User Entity：
		
		// 1. 建立 HashSalt + HashPassword
		String hashSalt = Hash.getSalt();
		String hashPassword = Hash.getHash(userRegisterDTO.getHashPassword(), hashSalt);
		// 重新將使用者表單輸入的密碼取代成加鹽加密密碼。
		userRegisterDTO.setHashPassword(hashPassword);
		
		// 2. 將 userRegisterDTO 轉成 User Entity：
		User user = userMapper.toEntity(userRegisterDTO);
		// 設定 admin 跟 salt:
		user.setHashSalt(hashSalt);
		user.setAdmin(false);
		userRepository.save(user);
	}
	
	
	// 更新使用者資訊。
	@Override
	public void updateUser(Integer userId, UserRegisterDTO userRegisterDTO) throws UserException {
		
		try {
			// Step1. 要先確認 userId 在資料庫中有紀錄：
			User user = findByUserId(userId);
			
			// Step2. 將密碼製作成 HashPassword 加鹽密碼
			String hashSalt = user.getHashSalt();
			String hashPassword = Hash.getHash(userRegisterDTO.getHashPassword(), hashSalt);
			
			// Step3. 將更新專用的 userRegisterDTO 內容轉至給 userDTO
			user.setUsername(userRegisterDTO.getUsername());
			user.setHashPassword(hashPassword);
			user.setEmail(userRegisterDTO.getEmail());
			user.setPhoto(userRegisterDTO.getPhoto());
			
			// Step4. 執行更新。
			userRepository.save(user);
			
		} catch (UserException e) {
			e.printStackTrace();
			throw new UserNotFoundException("UserService: 更新使用者發生錯誤，查無此使用者編號："+userId + e.getMessage());
		}
	}
	
	
	/*  部分更新邏輯：
	 *  部分資料更新像是更新使用者名稱，email, password 等資訊，
	 *  最後直接 save -> userRepository.save(user);
	 *  
	 *  因此在更新這些部分內容，主要會先：
	 *  1. 確認這個 userId 可以找到資料 (findByUserId(userId))，
	 *  2. 透過找出來的 User entity，用 Setter 方式用新參數更新特定屬性。
	 *  3. 用 save() 進行判斷更新。
	 */
	
	// 更新使用者名稱
	@Override
	public void updateUserName(Integer userId, String username) throws UserException {
		try {
			User user = findByUserId(userId);
			user.setUsername(username);
			// 直接 save
			userRepository.save(user);
			
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
			// 直接 save
			userRepository.save(user);
			
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
			// 直接 save
			userRepository.save(user);
			
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
			// 直接 save
			userRepository.save(user);
			
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
