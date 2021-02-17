package in.stackroute.cplayer.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class acts as a POJO class to store the model of an user
 * 
 * @author Ajinkya
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class User {
	/**
	 * Unique ID of user
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	/**
	 * Name of the user
	 */
	private String name;

	/**
	 * The email of user also used as username.
	 */
	private String email;

	/**
	 * The password of user, encrypted using bcrypt
	 */
	private String password;

	/**
	 * URL of the profile picture of user
	 */
	private String imagePath;

	/**
	 * Time when the user account is created.
	 */
	private LocalDateTime createdAt;

	/**
	 * OneToOne - Each user has one Favourites collection.
	 */
//	@OneToOne(cascade = CascadeType.ALL)
//	private Favourites favourite;
}
