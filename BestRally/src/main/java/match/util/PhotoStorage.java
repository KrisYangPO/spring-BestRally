package match.util;

import java.io.IOException;
import java.util.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PhotoStorage {

	// 將圖片輸入的 Part 物件轉成 String 物件
	// getInputStream().readAllBytes(); 會產生 IOException。
	public static String MultipartFileToBase64(MultipartFile part) throws IOException {
		if(part == null) {
			return null;
		}
		byte[] bytes = part.getBytes(); // 可直接用 MultipartFile 的 getBytes()
		return Base64.getEncoder().encodeToString(bytes);
	}
}
