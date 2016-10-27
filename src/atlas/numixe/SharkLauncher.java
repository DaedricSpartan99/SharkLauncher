package atlas.numixe;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SharkLauncher extends JavaPlugin {
	
	Register register;

	public void onEnable() {

		register = new Register(this);
	}
	
	public void onDisable() {
		
		register.clearAllInfo();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (!(sender instanceof Player)) {
			
			sender.sendMessage("Only a player can use SharkLauncher commands");
			return false;
		}
		
		String strcmd = cmd.getName();
		Player p = (Player)sender;
		
		if (strcmd.equalsIgnoreCase("haslauncher") || strcmd.equalsIgnoreCase("launchmode")) {
			
			if (args.length < 1) {
				
				p.sendMessage("SharkLauncher> Too few arguments");
				return false;
			}
			
			Player object = Bukkit.getServer().getPlayer(args[0]);
			p.sendMessage(object.getName() + " " + (register.hasLauncher(object) ? "" : "non ") +
					"ha loggato con il client launcher");
			
		} else if (strcmd.equalsIgnoreCase("loglaunchmode")) {
			
			String reason = null;
			
			if (args.length < 1) {
				
				p.sendMessage("SharkLauncher> Too few arguments");
				return false;
				
			} else if (args.length >= 2)
				reason = args[1];
			
			Player object = Bukkit.getServer().getPlayer(args[0]); 
			register.logPlayerMode(object, reason);	// perform log action
			
		} else {
			
			sender.sendMessage("SharkLauncher> Unknown command");
			return false;
		}
		
		return true;
	}
}
