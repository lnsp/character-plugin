package io.mooxmirror.character.squads;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.mooxmirror.character.Character;
import io.mooxmirror.character.CharacterPlugin;
import io.mooxmirror.character.channels.Channel;

public class SquadCommandExecutor implements CommandExecutor {
    private CharacterPlugin plugin;

    public SquadCommandExecutor(CharacterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("squad")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getConfig().getString("server.command.unusable"));
            } else {
                Player player = (Player) sender;
                Character character = plugin.getCharacter(player.getUniqueId());

                if (args.length == 0) {
                    character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.cmd.error"));
                } else {
                    if (args[0].equalsIgnoreCase("new")) {
                        if (character.hasSquad()) {
                            character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.new.error"));
                        } else {
                            Squad squad = new Squad(character);
                            plugin.addSquad(squad);
                            character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.new.success"));
                        }
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        if (character.hasSquad()) {
                            Squad squad = character.getSquad();
                            squad.leave(character);

                            if (squad.size() == 0) {
                                plugin.removeSquad(squad);
                            } else {
                                squad.getMembers().forEach((m) -> m.sendChannelMessage(Channel.SQUAD, String.format(plugin.getConfig().getString("squad.leave.notify"), character)));
                            }
                            character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.leave.success"));
                        } else {
                            character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.none"));
                        }
                    } else if (args[0].equalsIgnoreCase("invite")) {
                        if (character.hasSquad()) {
                            if (args.length == 1) {
                                character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.cmd.error"));
                            } else {
                                Squad squad = character.getSquad();
                                String playerName = args[1];
                                Player invitedPlayer = plugin.getServer().getPlayer(playerName);

                                if (invitedPlayer != null) {
                                    Character invitedCharacter = plugin.getCharacter(invitedPlayer.getUniqueId());
                                    squad.invite(invitedCharacter);
                                    invitedCharacter.sendChannelMessage(Channel.SQUAD, String.format(plugin.getConfig().getString("squad.invite.notify"), player.getName()));
                                } else {
                                    character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.invite.error"));
                                }
                            }
                        } else {
                            character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.none"));
                        }
                    } else if (args[0].equalsIgnoreCase("join")) {
                        if (args.length == 1) {
                            character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.cmd.error"));
                        } else {
                            if (!character.hasSquad()) {
                                String playerName = args[1];
                                Player squadLeader = plugin.getServer().getPlayer(playerName);
                                Character squadCharacter = plugin.getCharacter(squadLeader.getUniqueId());

                                if (squadCharacter.hasSquad()) {
                                    Squad squad = squadCharacter.getSquad();

                                    if (squad.isInvited(character)) {
                                        squad.getMembers().forEach((m) -> m.sendChannelMessage(Channel.SQUAD, String.format(plugin.getConfig().getString("squad.join.notify"), character)));
                                        squad.accept(character);
                                        character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.join.success"));
                                    } else {
                                        character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.join.not.invited"));
                                    }
                                } else {
                                    character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.join.not.invited"));
                                }
                            } else {
                                character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.join.error"));
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("about")) {
                        if (character.hasSquad()) {
                            Squad squad = character.getSquad();
                            String message = String.format(plugin.getConfig().getString("squad.about.online"), squad.size()) + ChatColor.RESET;
                            for (Character member : squad.getMembers()) {
                                message += member.toString() + " ";
                            }
                            character.sendChannelMessage(Channel.SQUAD, message);
                        } else {
                            character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.none"));
                        }
                    } else if (args[0].equalsIgnoreCase("help")) {
                        if (args.length == 1) {
                            character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("server.cmd.help"));
                            character.sendMessage(new String[]{
                                    "new - " + plugin.getConfig().getString("squad.new.help"),
                                    "leave - " + plugin.getConfig().getString("squad.leave.help"),
                                    "invite - " + plugin.getConfig().getString("squad.invite.help"),
                                    "join - " + plugin.getConfig().getString("squad.join.help"),
                                    "about - " + plugin.getConfig().getString("squad.about.help")
                            });
                        } else {
                            character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("server.cmd.guide"));
                            if (args[1].equalsIgnoreCase("new")) {
                                character.sendMessage(plugin.getConfig().getString("squad.new.guide"));
                            } else if (args[1].equalsIgnoreCase("leave")) {
                                character.sendMessage(plugin.getConfig().getString("squad.leave.guide"));
                            } else if (args[1].equalsIgnoreCase("invite")) {
                                character.sendMessage(plugin.getConfig().getString("squad.invite.guide"));
                            } else if (args[1].equalsIgnoreCase("join")) {
                                character.sendMessage(plugin.getConfig().getString("squad.join.guide"));
                            } else if (args[1].equalsIgnoreCase("about")) {
                                character.sendMessage(plugin.getConfig().getString("squad.about.guide"));
                            } else {
                                character.sendChannelMessage(Channel.SQUAD, plugin.getConfig().getString("squad.cmd.error"));
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

}
