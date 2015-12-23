package io.mooxmirror.character.guilds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.mooxmirror.character.Character;
import io.mooxmirror.character.CharacterPlugin;

public class GuildCommandExecutor implements CommandExecutor {
    private CharacterPlugin plugin;
    
    public GuildCommandExecutor(CharacterPlugin plugin) {
	this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if (command.getName().equalsIgnoreCase("guild")) {
	    if (!(sender instanceof Player)) {
		sender.sendMessage(ChatColor.YELLOW + "[Guild] " + plugin.getConfig().getString("server.command.unusable"));
		return true;
	    }
	    
	    Player player = (Player) sender;
	    Character character = plugin.getCharacter(player.getUniqueId());
	    
	    return true;
	}
	return false;
    }

}
