package in.stackroute.cplayer.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import in.stackroute.cplayer.entity.stats.Data;
import in.stackroute.cplayer.entity.stats.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class Player {
	@Id
	public int pid;
	public String profile;
	public String imageURL;
	public String battingStyle;
	public String bowlingStyle;
	public String majorTeams;
	public String currentAge;
	public String born;
	public String fullName;
	public String name;
	public String country;
	public String playingRole;
	public String v;
	@OneToOne
	public Data data;
	public int ttl;
	@OneToOne
	public Provider provider;
	public int creditsLeft;
}
