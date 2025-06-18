package match.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "team_application")
public class TeamApplication {
	
	@Id
	@Column(name= "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "user_id", nullable = false)
	private Integer userId;
	
	@Column(name = "email", nullable = true)
	private String email;
	
	@Column(name = "player_id", nullable = false)
	private Integer playerId;
	
	@Column(name = "player_level")
	private Integer playerLevel;
	
	@Column(name = "team_id", nullable = false)
	private Integer teamId;
	
	@Column(name = "team_name")
	private String teamName;
}
