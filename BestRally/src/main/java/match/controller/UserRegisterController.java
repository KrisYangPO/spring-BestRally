package match.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import match.exception.UserException;
import match.mapper.UserMapper;
import match.model.dto.UserDTO;
import match.model.dto.UserRegisterDTO;
import match.model.entity.User;
import match.service.UserService;
import match.util.Hash;
import match.util.PhotoStorage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/user")
public class UserRegisterController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserMapper userMapper;
	
	// 前往註冊頁面：register.jsp
	@GetMapping("/register")
	public String getRegister() {
		return "user_register";
	}
	
	// 取得註冊填寫表單，執行註冊(新增會員)
	@PostMapping("/register")
	public String postMethodName(
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam String email,
			@RequestParam MultipartFile photo
			) throws UserException, IOException {
		
		// Step1. photo 要以 MultipartFile 格式帶入，再轉成 byte[] 再轉 String 儲存至 DTO
		String photoFile = PhotoStorage.MultipartFileToBase64(photo);
		
		// Step2. 將更新參數帶入 userRegisterDTO
		UserRegisterDTO userRegisterDTO = new UserRegisterDTO(username, password, email, photoFile);
		
		userService.addUser(userRegisterDTO);
		return "redirect:/"; 
	}
	
}
