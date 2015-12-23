package io.mooxmirror.character;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import io.mooxmirror.character.channels.Channel;
import io.mooxmirror.character.guilds.Guild;
import io.mooxmirror.character.purses.Purse;
import io.mooxmirror.character.spells.Spell;
import io.mooxmirror.character.squads.Squad;
import io.mooxmirror.professions.Profession;

public class Character {
    private final static int MAX_LEVEL = 100;

    private final UUID uniqueId;
    private final CharacterPlugin plugin;

    private Profession profession;
    private Map<Profession, Integer> experience;
    private Map<String, String> bindings;

    private Purse purse;
    private Squad squad;
    private Guild guild;
    private Channel channel;

    private final static int[] expTable;

    static {
        expTable = new int[MAX_LEVEL + 1];

        for (int i = 0; i <= MAX_LEVEL; i++) {
            expTable[i] = 100 * i + 3 * i * i;
        }
    }

    /**
     * Returns the player's unique id.
     *
     * @return
     */
    public UUID getPlayerId() {
        return uniqueId;
    }

    /**
     * Updates the character.
     */
    public void tick() {
        profession.tick();
    }

    /**
     * Initializes a new character bound to the specific player UUID.
     * @param playerId
     * @param plugin
     * @param profession
     * @param level
     */
    public Character(UUID playerId, CharacterPlugin plugin, Profession profession, int level) {
        this.plugin = plugin;
        this.uniqueId = playerId;
        this.profession = profession;
        this.experience = new HashMap<Profession, Integer>();
        this.bindings = new HashMap<String, String>();

        experience.put(profession, expTable[level]);
        channel = Channel.GLOBAL;
        guild = null;
        squad = null;
        purse = null;

        getPlayer().setMaxHealth(getLevel() * 10 + 10);
        getPlayer().setHealth(getPlayer().getMaxHealth());
        getPlayer().setHealthScale(20);
    }

    public Map<String, String> getBindings() {
        return bindings;
    }

    /**
     * Returns the currently active channel.
     *
     * @return
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Sets the currently active channel.
     *
     * @param channel
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * Returns the host plugin.
     *
     * @return
     */
    public CharacterPlugin getPlugin() {
        return plugin;
    }

    /**
     * Sends a message to the player.
     *
     * @param text
     */
    public void sendMessage(String text) {
        getPlayer().sendMessage(text);
    }

    /**
     * Sends a channel message to the player.
     *
     * @param channel
     * @param text
     */
    public void sendChannelMessage(Channel channel, String text) {
        getPlayer().sendMessage(channel.getColor() + "[" + channel.getName() + "] " + text);
    }

    /**
     * Sends a message to the player.
     *
     * @param text
     */
    public void sendMessage(String[] text) {
        getPlayer().sendMessage(text);
    }

    /**
     * Returns the player bound to the character.
     *
     * @return
     */
    public Player getPlayer() {
        return plugin.getServer().getPlayer(getPlayerId());
    }

    /**
     * Returns the current character level.
     *
     * @return
     */
    public int getLevel() {
        int level;
        for (level = 0; level < 100 && experience.get(profession) > expTable[level]; level++) ;
        return level;
    }

    /**
     * Returns the character's class.
     *
     * @return
     */
    public Profession getProfession() {
        return profession;
    }

    /**
     * Returns the sum of all experience.
     *
     * @return
     */
    public int getExperience() {
        return experience.get(profession);
    }

    public void changeProfession(Profession profession) {
        this.profession = profession;
        experience.putIfAbsent(profession, expTable[0]);
        getPlayer().setMaxHealth(getLevel() * 10 + 10);
        getPlayer().setHealthScale(20);
    }

    /**
     * Returns the sum of experience needed for the next level.
     *
     * @return
     */
    public int neededExperience() {
        int level = getLevel();
        if (level < 100) return expTable[level];
        else return 0;
    }

    /**
     * Adds the experience to the character. Returns true, when a new level is reached.
     *
     * @param add
     * @return level up
     */
    public boolean addExperience(int add) {
        int oldLevel = getLevel();

        int current = experience.get(profession);
        experience.put(profession, current + add);

        if (oldLevel != getLevel()) {
            getPlayer().setMaxHealth(getLevel() * 10 + 10);
            getPlayer().setHealthScale(20);
            return true;
        }
        return false;
    }

    /**
     * Adds experience, shares it and informs the character.
     *
     * @param exp
     * @param squadSharing
     */
    public void receiveExperience(int exp, boolean squadSharing) {
        if (hasSquad() && squadSharing) {
            int expPerMember = Math.max(1, exp / getSquad().size());
            for (Character member : getSquad().getMembers()) {
                if (exp <= 0) break;
                member.receiveExperience(expPerMember, false);
                exp -= expPerMember;
            }
        } else {
            sendChannelMessage(Channel.PROFESSION, String.format(plugin.getConfig().getString("profession.exp"), exp));
            if (addExperience(exp)) {
                sendChannelMessage(Channel.PROFESSION, String.format(plugin.getConfig().getString("profession.levelup"), getLevel()));
            }
        }
    }

    /**
     * Adds experience, shares it and informs the character.
     *
     * @param exp
     */
    public void receiveExperience(int exp) {
        receiveExperience(exp, true);
    }

    /**
     * True if the character has a squad
     *
     * @return
     */
    public boolean hasSquad() {
        return squad != null;
    }

    /**
     * True if the character has a guild
     *
     * @return
     */
    public boolean hasGuild() {
        return guild != null;
    }

    /**
     * The squad of the character
     *
     * @return
     */
    public Squad getSquad() {
        return squad;
    }

    /**
     * Sets the squad of the character
     *
     * @param squad
     */
    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    /**
     * The guild of the character
     *
     * @return
     */
    public Guild getGuild() {
        return guild;
    }

    /**
     * Sets the guild of the character
     *
     * @param guild
     */
    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    @Override
    public String toString() {
        return getPlayer().getName();
    }
}
