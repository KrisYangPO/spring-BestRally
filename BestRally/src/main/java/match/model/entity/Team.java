package match.model.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Team {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="team_name", nullable = false, unique = true)
	private String teamName;
	
	@Column(name="avg_level", nullable = true, columnDefinition = "integer default 0")
	private Integer avgLevel;
	
	@Column(name="team_num", nullable = true, columnDefinition = "integer default 0")
	private Integer teamNum;
	
	@Column(name = "place", nullable = false)
	private String place;
	
	@Column(name = "recruit", nullable = false )
	private Boolean recruit;
	
	// 一個隊長就只會有一個隊伍。
	@ManyToOne
	@JoinColumn(name = "player_id", unique = true)
	private Player player;
	
	// 一個隊伍有多個 teamPlayer，一個 teamPlayer id 就只會有一個 team
	// teamPlayerId 不是 playerId
	@OneToMany(mappedBy = "team")
	private List<TeamPlayer> teamPlayers;
	
}
