package in.stackroute.cplayer.entity;

public class Player {

	private String playerId;
	private String playerName;

	public Player() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Player(String playerId, String playerName) {
		super();
		this.playerId = playerId;
		this.playerName = playerName;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public String toString() {
		return "Player [playerId=" + playerId + ", playerName=" + playerName + "]";
	}

}
