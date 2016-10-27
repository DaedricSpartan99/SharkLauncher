package atlas.numixe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Register {
	
	/* 
	 *  Config management, info and logs
	 */
	
	Plugin plugin;
	
	public Register(Plugin plugin) {
		
		this.plugin = plugin;
		
		if (plugin.getConfig().contains("info"))
			clearAllInfo();	// reset info
		else
			plugin.getConfig().createSection("info");
		
		// instead of info, logs remain in the config until clearPlayerLogs
		
		if (!plugin.getConfig().contains("logs"))
			plugin.getConfig().createSection("logs");
	}

	public void writePlayerInfo(PlayerInfo info) {
		
		if (info == null)
			return;
		else if (info.id == PlayerInfo.NO_ID)	// do nothing if no id
			return;
		
		// write into config
		
		/*
		 *  Config info struct
		 *  
		 *  info:
		 *  	<player>:
		 *  		ip_address: <info.ip_address>
		 *  		id:  <info.id>
		 */
		
		ConfigurationSection infosection = plugin.getConfig().getConfigurationSection("info");
		
		ConfigurationSection playersection = (infosection.contains(info.player.getName())) ? 
				infosection.getConfigurationSection(info.player.getName()) : infosection.createSection(info.player.getName());
				
		if (!playersection.contains("ip_address"))
			playersection.createSection("ip_address");
		
		playersection.set("ip_address", info.ip_address);
		
		if (!playersection.contains("id"))
			playersection.createSection("id");
		
		playersection.set("id", info.id);
		
		plugin.saveConfig();
	}
	
	public void removePlayerInfo(Player player) {
		
		ConfigurationSection infosection = plugin.getConfig().getConfigurationSection("info");
		
		if (!infosection.contains(player.getName()))
			return;
		
		infosection.set(player.getName(), null);
	}
	
	public PlayerInfo loadPlayerInfo(Player player) {

		// load from config
		
		ConfigurationSection infosection = plugin.getConfig().getConfigurationSection("info");
		
		if (!infosection.contains(player.getName()))
			return null;
		
		ConfigurationSection playersection = infosection.getConfigurationSection(player.getName());
		
		String ip_address = playersection.getString("ip_address");
		int id = playersection.getInt("id");
		
		PlayerInfo info = new PlayerInfo(player, ip_address, id);
		return info;
	}
	
	public void clearAllInfo() {
		
		plugin.getConfig().set("info", null);
	}
	
	public boolean hasLauncher(Player player) {
		
		return loadPlayerInfo(player) != null;
	}
	
	public void logPlayerMode(Player player, String reason) {
		
		String newreason = "Unknown reason";
		
		if (reason != null)
			newreason = reason;
		
		String time = "";
		
		time += Calendar.getInstance().get(Calendar.HOUR) + ":";
		time += Calendar.getInstance().get(Calendar.MINUTE) + ":";
		time += String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
		
		time += "_";
		
		time += Calendar.getInstance().get(Calendar.DATE) + ".";
		time += (Calendar.getInstance().get(Calendar.MONTH) + 1) + ".";
		time += String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		
		boolean launcher = hasLauncher(player);
		
		// write into config
		
		/* Config logs struct 
		 * 
		 * 	logs:
		 * 		<player>:
		 * 			<time>:
		 * 				haslauncher: <launcher>
		 * 				reason: <newreason>
		 */
		
		ConfigurationSection logs = plugin.getConfig().getConfigurationSection("logs");
		
		ConfigurationSection playerlogs = (logs.contains(player.getName())) ? 
				logs.getConfigurationSection(player.getName()) : logs.createSection(player.getName());
				
		ConfigurationSection section = (playerlogs.contains(time)) ? 
				playerlogs.getConfigurationSection(time) : playerlogs.createSection(time);
				
		if (!section.contains("haslauncher"))
			section.createSection("haslauncher");
		
		section.set("haslauncher", launcher);
		
		if (!section.contains("reason"))
			section.createSection("reason");
		
		section.set("haslauncher", newreason);
		
		plugin.saveConfig();
	}
	
	public List<String> loadPlayerLogs(Player player) {
		
		ConfigurationSection logs = plugin.getConfig().getConfigurationSection("logs");
		
		if (!logs.contains(player.getName()))
			return null;
		
		List<String> list = new ArrayList<String>();
		
		ConfigurationSection playerlogs = logs.getConfigurationSection(player.getName());
		
		for (String date : playerlogs.getKeys(false)) {
			
			ConfigurationSection section = playerlogs.getConfigurationSection(date);
			
			String arg = player.getName() + " , ";
			arg += date.replaceAll(":", " : ").replaceAll("_", " ");
			arg += " , ";
			arg += "SharkCraft launcher: " + section.getBoolean("haslauncher") + " , ";
			arg += "Log reason: " + section.getString("reason");
			
			list.add(arg);
		}
		
		return list;
	}
	
	public void clearPlayerLogs(Player player) {
		
		ConfigurationSection logs = plugin.getConfig().getConfigurationSection("logs");
		
		if (!logs.contains(player.getName()))
			return;
		
		logs.set(player.getName(), null);
	}
	
	public void clearAllLogs() {
		
		plugin.getConfig().set("logs", null);
	}
	
	public static class PlayerInfo {
		
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
	}
}
