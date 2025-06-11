package match.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
	
	@Column(name="photo", nullable = true)
	private String photo;
	
	@Column(name="admin", nullable = true)
	private Boolean admin;
	
	// 一個 user 只會有個 player 身份
	@OneToOne(mappedBy = "user")
	private Player player;
}
