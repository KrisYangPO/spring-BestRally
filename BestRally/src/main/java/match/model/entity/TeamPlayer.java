package match.model.entity;

import java.sql.Date;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class TeamPlayer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;
	
	
	@ManyToOne
	@JoinColumn(name = "player_id")
	private Player player;
	
	
	@Column(name ="win_rate", nullable = true, columnDefinition = "double default 0.0")
	private Double winRate; 
	
	@Column(name = "win_game", nullable = true, columnDefinition = "integer default 0")
	private Integer winGame; 
	
	@Column(name = "total", nullable = true, columnDefinition = "integer default 0")
	private Integer total;
	
	@CreationTimestamp
	@Column(name = "join_time", updatable = false)
	private Date joinTime; 
}
