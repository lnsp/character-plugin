package io.mooxmirror.character;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.mooxmirror.character.channels.Channel;
import io.mooxmirror.character.spells.Spell;
import io.mooxmirror.professions.Berserk;
import io.mooxmirror.professions.Mage;
import io.mooxmirror.professions.Priest;
import io.mooxmirror.professions.Ranger;
import io.mooxmirror.professions.Thief;
import io.mooxmirror.professions.Warlock;
import io.mooxmirror.professions.Warrior;

public class CharacterCommandExecutor implements CommandExecutor {
    private CharacterPlugin plugin;

    public CharacterCommandExecutor(CharacterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("profession")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getConfig().getString("server.command.unusable"));
                return true;
            }

            Player player = (Player) sender;
            Character character = plugin.getCharacter(player.getUniqueId());

            if (args.length == 0) {
                character.sendChannelMessage(Channel.PROFESSION, plugin.getConfig().getString("profession.cmd.error"));
            } else if (args[0].equalsIgnoreCase("about")) {
                character.sendChannelMessage(Channel.PROFESSION, plugin.getConfig().getString("server.cmd.about"));
                character.sendMessage(new String[]{
                        plugin.getConfig().getString("profession.about.basic") + character.getProfession().toString(),
                        plugin.getConfig().getString("profession.about.level") + character.getLevel(),
                        plugin.getConfig().getString("profession.about.experience") + character.getExperience()
                                + " / " + character.neededExperience(),
                });
            } else if (args[0].equalsIgnoreCase("change")) {
                if (args.length == 1) {
                    character.sendChannelMessage(Channel.PROFESSION, plugin.getConfig().getString("profession.cmd.error"));
                } else {
                    if (args[1].equalsIgnoreCase("warrior")) {
                        character.changeProfession(new Warrior());
                    } else if (args[1].equalsIgnoreCase("berserk")) {
                        character.changeProfession(new Berserk());
                    } else if (args[1].equalsIgnoreCase("ranger")) {
                        character.changeProfession(new Ranger());
                    } else if (args[1].equalsIgnoreCase("thief")) {
                        character.changeProfession(new Thief());
                    } else if (args[1].equalsIgnoreCase("warlock")) {
                        character.changeProfession(new Warlock());
                    } else if (args[1].equalsIgnoreCase("mage")) {
                        character.changeProfession(new Mage());
                    } else if (args[1].equalsIgnoreCase("priest")) {
                        character.changeProfession(new Priest());
                    } else {
                        character.sendChannelMessage(Channel.PROFESSION, plugin.getConfig().getString("profession.change.error"));
                        return true;
                    }

                    character.sendChannelMessage(Channel.PROFESSION, plugin.getConfig().getString("profession.change.success"));
                }
            } else if (args[0].equalsIgnoreCase("spells")) {
                character.sendChannelMessage(Channel.PROFESSION, plugin.getConfig().getString("profession.spells.title"));

                for (Spell spell : character.getProfession().getSpells()) {
                    character.sendMessage(String.format(plugin.getConfig().getString("profession.spells.list"), spell.getLevel(), spell.getName(), spell.getResourceCost()));
                }

            } else if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 1) {
                    character.sendChannelMessage(Channel.PROFESSION, plugin.getConfig().getString("server.cmd.help"));
                    character.sendMessage(new String[]{
                            "about - " + plugin.getConfig().getString("profession.about.help"),
                            "change - " + plugin.getConfig().getString("profession.change.help"),
                            "craft - " + plugin.getConfig().getString("profession.craft.help"),
                            "spells - " + plugin.getConfig().getString("profession.spells.help")
                    });
                } else {
                    character.sendChannelMessage(Channel.PROFESSION, plugin.getConfig().getString("server.cmd.guide"));
                    if (args[1].equalsIgnoreCase("about")) {
                        character.sendMessage(plugin.getConfig().getString("profession.about.guide"));
                    } else if (args[1].equalsIgnoreCase("change")) {
                        character.sendMessage(plugin.getConfig().getString("profession.change.guide"));
                    } else if (args[1].equalsIgnoreCase("craft")) {
                        character.sendMessage(plugin.getConfig().getString("profession.craft.guide"));
                    } else if (args[1].equalsIgnoreCase("spells")) {
                        character.sendMessage(plugin.getConfig().getString("profession.spells.guide"));
                    } else {
                        character.sendChannelMessage(Channel.PROFESSION, plugin.getConfig().getString("profession.cmd.error"));
                    }
                }
            }

            return true;
        } else if (cmd.getName().equalsIgnoreCase("stats")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getConfig().getString("server.command.unusable"));
                return true;
            }

            Player player = (Player) sender;
            Character character = plugin.getCharacter(player.getUniqueId());

            character.sendMessage(ChatColor.DARK_RED + "HP: " + (int) player.getHealth() + " / " + (int) player.getMaxHealth() + "\n" + character.getProfession().getResourceString());

            return true;
        }

        return false;
    }

}
