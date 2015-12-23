package io.mooxmirror.character.channels;

import org.bukkit.ChatColor;

public enum Channel {
    GLOBAL("/g", "Global", ChatColor.GOLD),
    LOCAL("/l", "Lokal", ChatColor.AQUA),
    SQUAD("/s", "Squad", ChatColor.BLUE),
    GUILD("/g", "Gilde", ChatColor.YELLOW),
    SPELL("", "Zauber", ChatColor.LIGHT_PURPLE),
    PROFESSION("", "Klasse", ChatColor.DARK_AQUA),
    CHANNEL("", "Chat", ChatColor.GREEN);
    
    private String shortcut, name;
    private ChatColor color;
    
    private Channel(String shortcut, String name, ChatColor color) {
	this.shortcut = shortcut;
	this.name = name;
	this.color = color;
    }
    
    public String getShortcut() {
	return shortcut;
    }
    
    public String getName() {
	return name;
    }
    
    public ChatColor getColor() {
	return color;
    }
    
    public static final int LOCAL_RADIUS = 100;
}
