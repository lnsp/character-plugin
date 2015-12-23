package io.mooxmirror.character.spells;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.mooxmirror.character.Character;
import io.mooxmirror.character.CharacterPlugin;
import io.mooxmirror.character.channels.Channel;

public class SpellCommandExecutor implements CommandExecutor {
    private CharacterPlugin plugin;

    public SpellCommandExecutor(CharacterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spell")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getConfig().getString("server.command.unusable"));
                return true;
            }

            Player player = (Player) sender;
            Character character = plugin.getCharacter(player.getUniqueId());

            if (args.length == 0) {
                character.sendChannelMessage(Channel.SPELL, plugin.getConfig().getString("spell.error"));
            } else {
                if (args[0].equalsIgnoreCase("cast")) {
                    if (args.length == 1) {
                        character.sendChannelMessage(Channel.SPELL, plugin.getConfig().getString("spell.error"));
                    } else {
                        String[] result = character.getProfession().castSpell(character, args[1]);
                        if (result != null) {
                            character.sendChannelMessage(Channel.SPELL, String.format(plugin.getConfig().getString("spell.cast.success"), result[0], result[1]));
                        } else {
                            character.sendChannelMessage(Channel.SPELL, plugin.getConfig().getString("spell.cast.error"));
                        }
                    }
                } else if (args[0].equalsIgnoreCase("bind")) {
                    if (args.length == 1) {
                        character.sendChannelMessage(Channel.SPELL, plugin.getConfig().getString("spell.error"));
                    } else {
                        String direction = "right";
                        if (args.length == 3) {
                            direction = args[2];
                        }
                        ItemStack item = player.getItemInHand();
                        character.getBindings().put(item.getType().toString() + "-" + direction, args[1]);
                        character.sendChannelMessage(Channel.SPELL, plugin.getConfig().getString("spell.bind.success"));
                    }
                } else if (args[0].equalsIgnoreCase("clear")) {
                    character.getBindings().clear();
                    character.sendChannelMessage(Channel.SPELL, plugin.getConfig().getString("spell.clear"));
                } else if (args[0].equalsIgnoreCase("help")) {
                    //  TODO:
                } else {
                    character.sendChannelMessage(Channel.SPELL, plugin.getConfig().getString("spell.error"));
                }
            }
            return true;
        }

        return false;
    }
}
