package in.stackroute.cplayer.entity;

public class PlayerStat {

	private String playerId;
	private String playerName;
	private String playerCountry;
	private String playingRole;

	public PlayerStat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlayerStat(String playerId, String playerName, String playerCountry, String playingRole) {
		super();
		this.playerId = playerId;
		this.playerName = playerName;
		this.playerCountry = playerCountry;
		this.playingRole = playingRole;
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

	public String getCountry() {
		return playerCountry;
	}

	public void setCountry(String playerCountry) {
		this.playerCountry = playerCountry;
	}

	public String getPlayingRole() {
		return playingRole;
	}

	public void setPlayingRole(String playingRole) {
		this.playingRole = playingRole;
	}

	@Override
	public String toString() {
		return "PlayerStat [playerId=" + playerId + ", playerName=" + playerName + ", playerCountry=" + playerCountry
				+ ", playingRole=" + playingRole + "]";
	}

}
