package match.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"player"})
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="username", nullable = false, unique = true)
	private String username;
	
	@Column(name="hash_password", nullable = false)
	private String hashPassword;
	
	@Column(name="hash_salt", nullable = false)
	private String hashSalt;
	
	@Column(name="email", nullable = false)
	private String email;
	
	// 如果圖片存 base64 要改成長字串(TEXT)
	@Column(name="photo", nullable = true, columnDefinition = "TEXT")
	private String photo;
	
	@Column(name="admin", nullable = true)
	private Boolean admin;
	
	// 一個 user 只會有個 player 身份
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private Player player;
}
