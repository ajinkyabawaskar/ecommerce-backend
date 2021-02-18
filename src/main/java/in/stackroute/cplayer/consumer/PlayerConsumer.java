package in.stackroute.cplayer.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import in.stackroute.cplayer.entity.Player;

@RestController
public class PlayerConsumer {

	@Autowired
	private RestTemplate restTemplate;

	/****************
	 * constants
	 ****************************************************/

	/***
	 * Api key can be found after login into https://cricapi.com Limit: (100 Hits
	 * per day)
	 * 
	 * Note: Use your own API key
	 ***/
	private static final String API_KEY = "HUTscPOfwmMnJXhpIMiMJO5lRqz1";

	private static final String ALL_PLAYERS_URL = "https://cricapi.com/api/allPlayers";

	private static final String PLAYER_STATS_URL = "https://cricapi.com/api/playerStats?apikey=" + API_KEY + "&pid=";

	private static final String PLAYER_BY_NAME_URL = "https://cricapi.com/api/playerFinder?apikey=" + API_KEY
			+ "&name=";

	/***********************************************************************************************************************/

	/**
	 * for storing 100 players and avoid hitting to External API every time
	 ** Applicable only for fetching all players
	 */
	private List<Player> playerList = null;

	// for storing response objects
	private ResponseEntity<?> response;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * API method for getting all players from external an API: https://cricapi.com
	 * 
	 * @param session
	 * @return A list of Players with valid response
	 */
	@GetMapping("/api/players")
	public ResponseEntity<?> getAllPlayers(HttpSession session) {
		// check for valid user
		if (session != null && session.getAttribute("loggedInUserId") != null) {
			// populate the playerList during first hit
			if (playerList == null) {
				@SuppressWarnings("unchecked")
				List<Player> allPlayers = (List<Player>) restTemplate.getForObject(ALL_PLAYERS_URL, HashMap.class)
						.get("data");
				playerList = allPlayers.subList(1, 101);
			}
			response = new ResponseEntity<List<Player>>(playerList, HttpStatus.OK);
		} else {
			response = new ResponseEntity<String>("unauthorized", HttpStatus.UNAUTHORIZED);
		}
		return response;

	}

	/**
	 * API method for fetching players by their Id from an external API:
	 * https://cricapi.com
	 * 
	 * @param playerId
	 * @param session
	 * @return Player object with valid response
	 */

	@GetMapping("api/player/id/{pid}")
	public ResponseEntity<?> getPlayerById(@PathVariable("pid") String playerId, HttpSession session) {

		HashMap<String, String> playerStats = restTemplate.getForObject(PLAYER_STATS_URL + playerId, HashMap.class);
		System.out.println(playerStats);
		// check for valid playerId
		if (!playerStats.containsKey("pid")) {
			response = new ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<Player>(new Player(), HttpStatus.OK);
		}

		return response;
	}

	/**
	 * API method for fetching Players by their Name from an external API:
	 * https://cricapi.com
	 * 
	 * @param playerName
	 * @param session
	 * @return A List of Players with valid response
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("api/player/name/{pname}")
	public ResponseEntity<?> getPlayerByName(@PathVariable("pname") String playerName) {
		List<Object> data = (List<Object>) restTemplate.getForObject(PLAYER_BY_NAME_URL + playerName, HashMap.class)
				.get("data");

		int totalResults = data.size();

		// check for zero matches
		if (totalResults == 0) {
			response = new ResponseEntity<String>("No Result Found", HttpStatus.NOT_FOUND);
		} else {
			List<Player> players = new ArrayList<Player>(data.size());

			for (Object eachObj : data) {
				HashMap<String, Object> eachPlayer = (HashMap<String, Object>) eachObj;
				System.out.println(eachPlayer);
//					players.add(new Player(eachPlayer.get("pid").toString(), eachPlayer.get("name").toString()));
			}
			response = new ResponseEntity<List<Player>>(players, HttpStatus.OK);
		}

		return response;
	}

	/**
	 * API method for fetching a Player statistics by their Id from an external API:
	 * https://cricapi.com
	 * 
	 * @param playerId
	 * @param session
	 * @return PlayerStat object with valid response
	 */

	@GetMapping("api/stats/{pid}")
	public ResponseEntity<?> getPlayerStats(@PathVariable("pid") String playerId) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> playerStats = restTemplate.getForObject(PLAYER_STATS_URL + playerId, HashMap.class);
		System.out.println(playerStats);
		response = new ResponseEntity<String>("found", HttpStatus.OK);

		return response;
	}

}
