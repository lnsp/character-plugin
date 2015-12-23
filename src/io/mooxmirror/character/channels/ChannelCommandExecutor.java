package io.mooxmirror.character.channels;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.mooxmirror.character.Character;
import io.mooxmirror.character.CharacterPlugin;

public class ChannelCommandExecutor implements CommandExecutor {
    private CharacterPlugin plugin;
    
    public ChannelCommandExecutor(CharacterPlugin plugin) {
	this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if (command.getName().equalsIgnoreCase("channel")) {
	    if (!(sender instanceof Player)) {
		sender.sendMessage(plugin.getConfig().getString("server.command.unusable"));
		return true;
	    }
	    
	    Player player = (Player) sender;
	    Character character = plugin.getCharacter(player.getUniqueId());
	    
	    if (args.length == 0) {
		character.sendChannelMessage(Channel.CHANNEL, plugin.getConfig().getString("channel.switch.error"));
	    } else if (args[0].equalsIgnoreCase("global")) {
		character.setChannel(Channel.GLOBAL);
		character.sendChannelMessage(Channel.CHANNEL, plugin.getConfig().getString("channel.switch.global"));
	    } else if (args[0].equalsIgnoreCase("local")) {
		character.setChannel(Channel.LOCAL);
		character.sendChannelMessage(Channel.CHANNEL, plugin.getConfig().getString("channel.switch.local"));
	    } else if (args[0].equalsIgnoreCase("squad")) {
		character.setChannel(Channel.SQUAD);
		character.sendChannelMessage(Channel.CHANNEL, plugin.getConfig().getString("channel.switch.squad"));
	    } else if (args[0].equalsIgnoreCase("guild")) {
		character.setChannel(Channel.GUILD);
		character.sendChannelMessage(Channel.CHANNEL, plugin.getConfig().getString("channel.switch.guild"));
	    } else {
		character.sendChannelMessage(Channel.CHANNEL, plugin.getConfig().getString("channel.switch.error"));
	    }
	    
	    return true;
	}
	
	return false;
    }

}
