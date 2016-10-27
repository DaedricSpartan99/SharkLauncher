package atlas.numixe;

import org.bukkit.entity.Player;

public class PlayerInfo {
	
	public static final int NO_ID = -1;
	
	public PlayerInfo(Player player, String ip_address, Integer id) {
		
		this.player = player;
		this.ip_address = ip_address;
		
		if (id == null)
			this.id = NO_ID;		// id is strictly positive, NO_ID is set if input is null
		else
			this.id = id.intValue();
	}
	
	public Player player;
	public String ip_address;
	public int id;
	
	public static PlayerInfo fromByteData(Player player, byte[] data) {
		
		return null;
	}
}
