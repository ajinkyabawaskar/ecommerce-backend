package in.stackroute.cplayer.entity.stats;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class Batting {
	@Id
	public int id;

	@OneToOne
	public ListA listA;

	@OneToOne
	public FirstClass firstClass;

	@OneToOne
	@JsonProperty("T20Is")
	public T20Is t20Is;

	@OneToOne
	@JsonProperty("ODIs")
	public ODIs oDIs;

	@OneToOne
	public Tests tests;
}