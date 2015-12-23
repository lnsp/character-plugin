package io.mooxmirror.character.channels;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import io.mooxmirror.character.Character;
import io.mooxmirror.character.CharacterPlugin;

public class ChannelListener implements Listener {
    private CharacterPlugin plugin;
    
    public ChannelListener(CharacterPlugin plugin) {
	this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
	Player player = event.getPlayer();
	Character character = plugin.getCharacter(player.getUniqueId());
	Channel channel = character.getChannel();
	
	switch (channel) {
	case GLOBAL:
	    event.setFormat(Channel.GLOBAL.getColor() + "[" + Channel.GLOBAL.getName() + "] " + ChatColor.RESET + "%s: %s");
	    break;
	case LOCAL:
	    event.setFormat(Channel.LOCAL.getColor() + "[" + Channel.LOCAL.getName() + "] " + ChatColor.RESET + "%s: %s");
	    event.getRecipients().removeIf((p) -> p.getLocation().distance(player.getLocation()) > Channel.LOCAL_RADIUS);
	    break;
	case SQUAD:
	    if (character.hasSquad()) {
		event.setFormat(Channel.SQUAD.getColor() + "[" + Channel.SQUAD.getName() + "] " + ChatColor.RESET + "%s: %s");
		event.getRecipients().removeIf((p) -> !character.getSquad().isMember(plugin.getCharacter(p.getUniqueId())));
	    } else {
		character.sendMessage(Channel.SQUAD.getColor() + "[" + Channel.SQUAD.getName() + "] " + plugin.getConfig().getString("squad.none"));
		event.setCancelled(true);
	    }
	    break;
	case GUILD:
	    if (character.hasGuild()) {
		event.setFormat(Channel.GUILD.getColor() + "[" + Channel.GUILD.getName() + "] " + ChatColor.RESET + "%s: %s");
		event.getRecipients().removeIf((p) -> !character.getGuild().isMember(plugin.getCharacter(p.getUniqueId())));
	    } else {
		character.sendMessage(Channel.GUILD.getColor() + "[" + Channel.GUILD.getName() + "] " + plugin.getConfig().getString("guild.none"));
		event.setCancelled(true);
	    }
	    break;
	}
    }
}
