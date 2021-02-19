package in.stackroute.cplayer.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import in.stackroute.cplayer.entity.Player;
import in.stackroute.cplayer.entity.PlayerList;

@RestController
@RequestMapping("/player")
public class PlayerController {

	private HashMap<String, Player> cachedPlayers = new HashMap<String, Player>();
	private HashMap<String, List<Player>> cachedSearchResults = new HashMap<String, List<Player>>();

	private final String CRICAPI_PROTOCOL = "https";
	private final String CRICAPI_HOST = "cricapi.com";
	private final String CRICAPI_PLAYER_BY_ID_RESOURCE = "api/playerStats";
	private final String CRICAPI_PLAYER_BY_NAME_RESOURCE = "api/playerFinder";
	private final String CRICAPI_KEY = "HUTscPOfwmMnJXhpIMiMJO5lRqz1";

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Bean
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();
	}

	@GetMapping("/id/{pid}")
	public ResponseEntity<?> getPlayerById(@PathVariable("pid") String playerId) {

		// Player to be returned
		Player player;

		// If player already exists in cache, then serve from cache
		if (cachedPlayers.containsKey(playerId)) {
			player = cachedPlayers.get(playerId);
		}

		// If player is not present in cache, then get the player from API and save it
		// to cache
		else {
			// get player from CricApi using webclient
			player = webClientBuilder.build().post()
					.uri(builder -> builder.scheme(CRICAPI_PROTOCOL).host(CRICAPI_HOST)
							.path(CRICAPI_PLAYER_BY_ID_RESOURCE).queryParam("apikey", CRICAPI_KEY)
							.queryParam("pid", playerId).build())
					.retrieve().bodyToMono(Player.class).block();

			// to avoid cache overflowing memory, limit the number of players cached
			if (cachedPlayers.size() < 100) {
				// add player to cache only if cache still has sufficient memory
				cachedPlayers.put(playerId, player);
			}
		}

		return new ResponseEntity<Player>(player, HttpStatus.OK);
	}

	@GetMapping("/name/{pname}")
	public ResponseEntity<?> getPlayerByName(@PathVariable("pname") String playerName) {

		// search results to be returned
		List<Player> searchResults;

		// if this query is already searched for, then serve from cached results
		if (cachedSearchResults.containsKey(playerName)) {
			searchResults = cachedSearchResults.get(playerName);
		}

		// if this is a new query, then get the results from the API and cache them
		else {

			// get search results
			PlayerList players = webClientBuilder.build().post()
					.uri(builder -> builder.scheme(CRICAPI_PROTOCOL).host(CRICAPI_HOST)
							.path(CRICAPI_PLAYER_BY_NAME_RESOURCE).queryParam("apikey", CRICAPI_KEY)
							.queryParam("name", playerName).build())
					.retrieve().bodyToMono(PlayerList.class).block();
			searchResults = players.getData();

			// if the size of cache is under limit
			if (cachedSearchResults.size() < 100) {
				cachedSearchResults.put(playerName, searchResults);
			}

		}

		return new ResponseEntity<List<Player>>(searchResults, HttpStatus.OK);
	}

}