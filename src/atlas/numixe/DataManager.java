package atlas.numixe;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import atlas.jax.Jax;
import atlas.jax.Jax.JaxInfo;

public class DataManager implements PluginMessageListener, DataStream {
	
	public static final String CHANNEL = "sharkchan";
	public static final byte SIG_INFO = 0x1;
	public static final byte SIG_DATA = 0x2;
	
	private Plugin plugin;
	
	private JaxInfo jaxInfo;
	private PlayerInfo playerInfo;
	private String currentPlayer;

	public DataManager(Plugin plugin) {
		
		this.plugin = plugin;
		
        this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, CHANNEL);
        this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, CHANNEL, this);
        
        this.jaxInfo = null;
        this.playerInfo = null;
        this.currentPlayer = null;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		
		if (!channel.equals(CHANNEL) || data.length < 2)
			return;
		
		byte signal = data[0];	// first byte is signal
		byte[] buffer = new byte[data.length - 1];
		
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = data[i+1];
		
		switch (signal) {
		
		case SIG_INFO:
			
			jaxInfo = JaxInfo.fromByteFormat(buffer);	// jax library function, native method
			playerInfo = null;
			
			break;
			
		case SIG_DATA:
			
			if (jaxInfo == null)
				return;
			
			Jax.decodeSequence(buffer, jaxInfo);	// jax library function, native method
			playerInfo = PlayerInfo.fromByteData(player, buffer);
			jaxInfo = null;
			
			break;
		
		default:
			return;
		}
	}

	@Override
	public PlayerInfo getPlayerInfo() {
		
		return playerInfo;
	}
	
	@Override
	public boolean isReadable() {
		
		return playerInfo != null;
	}
}
