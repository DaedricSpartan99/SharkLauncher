package atlas.numixe;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class DataManager implements PluginMessageListener, DataStream {
	
	public static final String CHANNEL = "sharkchan";
	private Plugin plugin;
	private byte[] currentData;

	public DataManager(Plugin plugin) {
		
		this.plugin = plugin;
		
        this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, CHANNEL);
        this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, CHANNEL, this);
        
        this.currentData = null;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		
		if (!channel.equals(CHANNEL))
			return;
		
		this.currentData = data;
	}
	
	@Override
	public byte[] getData() {
		
		return currentData;
	}
}
