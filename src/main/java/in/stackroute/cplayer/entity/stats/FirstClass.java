package in.stackroute.cplayer.entity.stats;

import javax.persistence.Entity;
import javax.persistence.Id;

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
public class FirstClass {
	@Id
	public int id;
	@JsonProperty("10")
	public String _10;
	@JsonProperty("5w")
	public String _5w;
	@JsonProperty("4w")
	public String _4w;
	@JsonProperty("SR")
	public String sR;
	@JsonProperty("Econ")
	public String econ;
	@JsonProperty("Ave")
	public String ave;
	@JsonProperty("BBM")
	public String bBM;
	@JsonProperty("BBI")
	public String bBI;
	@JsonProperty("Wkts")
	public String wkts;
	@JsonProperty("Runs")
	public String runs;
	@JsonProperty("Balls")
	public String balls;
	@JsonProperty("Inns")
	public String inns;
	@JsonProperty("Mat")
	public String mat;
	@JsonProperty("50")
	public String _50;
	@JsonProperty("100")
	public String _100;
	@JsonProperty("St")
	public String st;
	@JsonProperty("Ct")
	public String ct;
	@JsonProperty("6s")
	public String _6s;
	@JsonProperty("4s")
	public String _4s;
	@JsonProperty("BF")
	public String bF;
	@JsonProperty("HS")
	public String hS;
	@JsonProperty("NO")
	public String nO;
}