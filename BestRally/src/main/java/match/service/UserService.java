package match.service;

import java.util.List;
import org.springframework.stereotype.Service;
import match.exception.UserException;
import match.model.dto.UserDTO;
import match.model.dto.UserRegisterDTO;
import match.model.entity.User;

@Service
public interface UserService {
	
	// 找出所有使用者
	public abstract List<UserDTO> findAllUsers();
	
	// 根據 ID 找出特定使用者
	public abstract User findByUserId(Integer userId) throws UserException;
	
	// 新增使用者
	public abstract void addUser(UserRegisterDTO userRegisterDTO)  throws UserException;
	
	// 修改使用者
	public abstract void updateUser(Integer userId, UserRegisterDTO userRegisterDTO) throws UserException;
	public abstract void updateUserName(Integer userId, String username) throws UserException;
	public abstract void updateUserPassword(Integer userId, String password) throws UserException;
	public abstract void updateUserEmail(Integer userId, String email) throws UserException;
	public abstract void updateUserPhoto(Integer userId, String photo) throws UserException;
	
	
	// 刪除姓名
	public abstract void deleteUser(Integer userId)  throws UserException;
}
