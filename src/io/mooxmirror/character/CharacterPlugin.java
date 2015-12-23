package io.mooxmirror.character;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.mooxmirror.character.channels.Channel;
import io.mooxmirror.character.channels.ChannelCommandExecutor;
import io.mooxmirror.character.channels.ChannelListener;
import io.mooxmirror.character.guilds.Guild;
import io.mooxmirror.character.spells.SpellCommandExecutor;
import io.mooxmirror.character.squads.Squad;
import io.mooxmirror.character.squads.SquadCommandExecutor;
import io.mooxmirror.professions.Mage;

public class CharacterPlugin extends JavaPlugin implements Listener {
    private FileConfiguration config;
    private Map<UUID, Character> characters;

    private List<Squad> squads;
    private List<Guild> guilds;

    private CharacterCommandExecutor characterCommandExecutor;
    private SquadCommandExecutor squadCommandExecutor;
    private SpellCommandExecutor spellCommandExecutor;
    private ChannelCommandExecutor channelCommandExecutor;

    private CharacterUpdater characterUpdater;

    public Character getCharacter(UUID id) {
        return characters.get(id);
    }

    public void addSquad(Squad squad) {
        squads.add(squad);
    }

    public boolean removeSquad(Squad squad) {
        return squads.remove(squad);
    }

    public void initConfig() {
        config = getConfig();

        config.addDefault("server.introduction", "Willkommen, %s, in der magischen Welt von Adrion.");
        config.addDefault("server.motd", "Willkommen zurück, %s.");
        config.addDefault("server.command.unusable", "Dieser Befehl ist nur von Spielern nutzbar.");
        config.addDefault("server.cmd.help", "---- HILFE ----");
        config.addDefault("server.cmd.guide", "---- GUIDE ----");
        config.addDefault("server.cmd.about", "---- ÜBER ----");

        config.addDefault("profession.about.level", "Dein aktuelles Level: ");
        config.addDefault("profession.about.experience", "Benötigte Erfahrungspunkte: ");
        config.addDefault("profession.about.basic", "Deine Klasse: ");

        config.addDefault("profession.cmd.error", "Bitte werde etwas genauer. Nutze /profession help [option] um mehr Hilfe zu erhalten.");
        config.addDefault("profession.about.help", "Lerne mehr über deine Klasse, deine Erfahrungspunkte und dein Level.");
        config.addDefault("profession.about.guide", "Nutze about um deinen aktuellen Klassenfortschritt abzufragen.\nBsp.: /profession about");
        config.addDefault("profession.change.error", "Diese Klasse existiert nicht.");
        config.addDefault("profession.change.success", "Deine Klasse wurde erfolgreich geändert.");
        config.addDefault("profession.change.help", "Lerne mehr über das ändern deiner Klasse.");
        config.addDefault("profession.change.guide", "Nutze change um deine aktuelle Klasse zu ändern.\nDeine bisher erspielten Erfahrungspunkte bleiben in der anderen Klasse erhalten.");
        config.addDefault("profession.spells.help", "Lerne mehr über deine Fähigkeiten und wie du sie nutzen kannst.");
        config.addDefault("profession.spells.guide", "Nutze spells um dir einen Überblick über deine Fähigkeiten zu verschaffen.\nDir werden alle bisher freigeschalteten Fähigkeiten deiner Klasse aufgelistet.");
        config.addDefault("profession.spells.title", "---- ZAUBER ----");
        config.addDefault("profession.spells.list", "%3d | %16s | %4d");
        config.addDefault("profession.craft.help", "Lerne mehr über deine handwerklichen Fertigkeiten und wie du sie nutzen kannst.");
        config.addDefault("profession.craft.guide", "Nutze craft um dir einen Überblick über deine handwerklichen Fertigkeiten zu verschaffen.\nDir werden alle bisher freigeschalteten Rezepte und Gegenstände aufgelistet.");

        config.addDefault("squad.cmd.error", "Bitte werde etwas genauer. Nutze /squad help [option] um mehr Hilfe zu erhalten.");
        config.addDefault("squad.new.help", "Lerne mehr über das Erstellen von Squads.");
        config.addDefault("squad.new.guide", "Nutze new um neue Squads zu formen, um besser zusammen mit deinen Freunden zu spielen.");
        config.addDefault("squad.new.success", "Du hast erfolgreich einen Squad erstellt.");
        config.addDefault("squad.new.error", "Du musst deinen aktuellen Squad verlassen, bevor du einen neuen Squad erstellen kannst.");
        config.addDefault("squad.leave.help", "Lerne mehr über das Verlassen von Squads.");
        config.addDefault("squad.leave.guide", "Nutze leave um einen nicht mehr genutzen Squad zu verlassen.\nDu verlässt einen Squad automatisch, wenn du dich ausloggst.");
        config.addDefault("squad.leave.success", "Du hast erfolgreich den Squad verlassen.");
        config.addDefault("squad.leave.error", "Du bist in keinem Squad.");
        config.addDefault("squad.leave.notify", "%s hat den Squad verlassen.");
        config.addDefault("squad.join.help", "Lerne mehr über das Betreten von Squads.");
        config.addDefault("squad.join.guide", "Nutze join um einen Squad eines anderen Spielers beizutreten.\nAllerdings musst du zuvor eine Einladung bekommen haben.");
        config.addDefault("squad.join.not.invited", "Du musst vorher eine Einladung erhalten haben um dem Squad dieses Spielers beizutreten.");
        config.addDefault("squad.join.error", "Du musst vorher deinen Squad verlassen haben, bevor du einem neuen beitreten kannst.");
        config.addDefault("squad.join.success", "Du bist dem Squad erfolgreich beigetreten.");
        config.addDefault("squad.join.notify", "%s ist dem Squad beigetreten.");
        config.addDefault("squad.invite.help", "Lerne mehr über das Einladen von Spielern in Squads.");
        config.addDefault("squad.invite.guide", "Nutze invite um neue Freunde in deinen Squad einzuladen.");
        config.addDefault("squad.invite.error", "Der Spieler wurde nicht gefunden.");
        config.addDefault("squad.invite.success", "Der Spieler wurde eingeladen.");
        config.addDefault("squad.invite.notify", "%s lädt dich ein, seinem Squad beizutreten.");
        config.addDefault("squad.none", "Du bist in keinem Squad.");
        config.addDefault("squad.about.help", "Lerne mehr über deinen Squad.");
        config.addDefault("squad.about.guide", "Nutze about um die aktuellen Spielerzahl und eine Übersicht aller Spieler deines Squads zu bekommen.");
        config.addDefault("squad.about.error", "Du bist in keinem Squad.");
        config.addDefault("squad.about.online", "Derzeit sind %d Spieler online: ");

        config.addDefault("spell.cast.help", "Lerne mehr über das Anwenden von Fähigkeiten.");
        config.addDefault("spell.cast.guide", "Nutze cast um Fähigkeiten aufzurufen. Du kannst den Aufruf auch an Gegenstände binden.");
        config.addDefault("spell.cast.error", "Du kannst diesen Zauber nicht wirken.");
        config.addDefault("spell.cast.success", "Du hast %s für %s gewirkt.");
        config.addDefault("spell.bind.help", "Lerne mehr über das Binden von Fähigkeiten.");
        config.addDefault("spell.bind.guide", "Nutze bind um Fähigkeiten an Gegenstände in deinem Inventar zu binden.");
        config.addDefault("spell.bind.success", "Der Zauber wurde an den Gegenstand gebunden.");
        config.addDefault("spell.error", "Bitte werde etwas genauer. Nutze /spell help [option] um mehr Hilfe zu erhalten.");
        config.addDefault("spell.clear", "Alle Bindungen wurden gelöscht.");

        config.addDefault("channel.switch.global", "Du sprichst nun im " + Channel.GLOBAL.getColor() + "globalen Raum.");
        config.addDefault("channel.switch.local", "Du sprichst nun im " + Channel.LOCAL.getColor() + "lokalen Raum.");
        config.addDefault("channel.switch.squad", "Du sprichst nun im " + Channel.SQUAD.getColor() + "Squad-Raum.");
        config.addDefault("channel.switch.guild", "Du sprichst nun im " + Channel.GUILD.getColor() + "Gilden-Raum.");
        config.addDefault("channel.switch.error", "Beim Wechseln des Raumes ist ein Fehler aufgetreten.");

        config.addDefault("profession.exp", "Du erhältst %d Erfahrungspunkte.");
        config.addDefault("profession.levelup", "Du bist auf Level %d aufgestiegen.");

        config.addDefault("guild.none", "Du bist in keiner Gilde.");
    }

    @Override
    public void onEnable() {
        initConfig();

        characters = new HashMap<UUID, Character>();
        squads = new ArrayList<Squad>();
        guilds = new ArrayList<Guild>();

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new ChannelListener(this), this);

        characterCommandExecutor = new CharacterCommandExecutor(this);
        squadCommandExecutor = new SquadCommandExecutor(this);
        channelCommandExecutor = new ChannelCommandExecutor(this);
        spellCommandExecutor = new SpellCommandExecutor(this);

        getCommand("profession").setExecutor(characterCommandExecutor);
        getCommand("stats").setExecutor(characterCommandExecutor);
        getCommand("squad").setExecutor(squadCommandExecutor);
        getCommand("channel").setExecutor(channelCommandExecutor);
        getCommand("spell").setExecutor(spellCommandExecutor);

        characterUpdater = new CharacterUpdater(this);
        characterUpdater.runTaskTimer(this, 0, 25);
    }

    @Override
    public void onDisable() {

    }

    public Collection<Character> allCharacters() {
        return characters.values();
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        Character character;

        if (characters.containsKey(uniqueId)) {
            character = characters.get(uniqueId);
            event.setJoinMessage(ChatColor.GREEN + String.format(config.getString("server.motd"), player.getName()));
        } else {
            character = new Character(uniqueId, this, new Mage(), 5);
            characters.put(uniqueId, character);
            event.setJoinMessage(ChatColor.GREEN + String.format(config.getString("server.introduction"), player.getName()));
        }

        player.setCustomName(player.getName() + ChatColor.GOLD + " [" + character.getLevel() + "]");
        player.setCustomNameVisible(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        Character character = getCharacter(uniqueId);

        // Leave all squads
        if (character.hasSquad()) {
            Squad squad = character.getSquad();
            squad.leave(character);

            if (squad.size() == 0) {
                squads.remove(squad);
            } else {
                squad.getMembers().forEach((m) -> m.sendMessage(ChatColor.BLUE + "[Squad] " + String.format(getConfig().getString("squad.leave.notify"), character)));
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Character damager = getCharacter(event.getDamager().getUniqueId());
            if (event.getEntity() instanceof Player) {
                Character victim = getCharacter(event.getEntity().getUniqueId());
                if (damager.getPlayer() == victim.getPlayer() || (damager.hasSquad() && damager.getSquad().isMember(victim))) {
                    event.setCancelled(true);
                    return;
                } else {
                    if (victim.getPlayer().getHealth() - event.getFinalDamage() <= 0) {
                        int exp = 30 + damager.getLevel() * 3;
                        damager.receiveExperience(exp);
                    }
                }
            } else if (event.getEntity() instanceof LivingEntity) {
                LivingEntity e = (LivingEntity) event.getEntity();

                if (e instanceof Animals) {
                    if (e.getHealth() - event.getFinalDamage() <= 0) {
                        int exp = (int) (10 + damager.getLevel() * 0.1);
                        damager.receiveExperience(exp);
                    }
                } else if (e instanceof Monster) {
                    if (e.getHealth() - event.getFinalDamage() <= 0) {
                        int exp = (int) (20 + damager.getLevel());
                        damager.receiveExperience(exp);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Character dead = getCharacter(((Player) event.getEntity()).getUniqueId());
        event.setDeathMessage(String.format(getConfig().getString("server.event.death"), dead.getPlayer().getName()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        Character character = getCharacter(player.getUniqueId());

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            String obj = player.getItemInHand().getType() + "-right";
            if (character.getBindings().containsKey(obj)) {
                getServer().dispatchCommand(player, "spell cast " + character.getBindings().get(obj));
            }
        } else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            String obj = player.getItemInHand().getType() + "-left";
            if (character.getBindings().containsKey(obj)) {
                getServer().dispatchCommand(player, "spell cast " + character.getBindings().get(obj));
            }
        }
    }
}
