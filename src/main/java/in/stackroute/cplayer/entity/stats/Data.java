package in.stackroute.cplayer.entity.stats;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Data {
	@Id
	public int id;
	@OneToOne
	public Bowling bowling;
	@OneToOne
	public Batting batting;
}