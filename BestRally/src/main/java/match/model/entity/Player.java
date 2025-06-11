package match.model.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Player {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="level", nullable = false)
	private Integer level; 
	
	// 一個使用者就只有一個 userID 
	@OneToOne
	@JoinColumn(name = "user_id", unique = true)
	private User user;
	
	// 每個隊伍的隊長 id 就是一個球員的 id
	@OneToMany(mappedBy = "player")
	private List<Team> teams;
	
	// 每個球員可以參加多個隊伍，所以可以有多個 teamPlayer 身份
	@OneToMany(mappedBy = "player")
	private List<TeamPlayer> teamplayers;
}
