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
public class Bowling {
	@Id
	public int id;

	@OneToOne
	public ListA listA;

	@OneToOne
	public FirstClass firstClass;

	@JsonProperty("T20Is")
	@OneToOne
	public T20Is t20Is;

	@JsonProperty("ODIs")
	@OneToOne
	public ODIs oDIs;

	@OneToOne
	public Tests tests;
}