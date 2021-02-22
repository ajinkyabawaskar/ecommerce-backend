package in.stackroute.cplayer.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class acts as a POJO class to store the model of an user
 * 
 * @author Ajinkya
 */

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	@Column(unique = true)
	private String username;
	private String name;
	private String email;
	private String password;
	private String imagePath;
	private LocalDateTime createdAt;
	@OneToMany(cascade = CascadeType.ALL)
	private List<Product> cart;
}
