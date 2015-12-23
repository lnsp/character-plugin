package io.mooxmirror.character.guilds;

import io.mooxmirror.character.Character;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Guild {
    private Set<Character> members;
    private Set<Character> invites;
    private Character leader;
    private Set<Character> officers;

    private String name, motd;

    /**
     * Creates a new guild with the specific name and leader.
     *
     * @param name
     * @param leader
     */
    public Guild(String name, Character leader) {
        members = new HashSet<Character>();
        invites = new HashSet<Character>();
        officers = new HashSet<Character>();

        this.leader = leader;
        this.name = name;
        members.add(leader);
    }

    /**
     * The message of the day.
     *
     * @return
     */
    public String getMotd() {
        return motd;
    }

    /**
     * Sets the message of the day.
     *
     * @param motd
     */
    public void setMotd(String motd) {
        this.motd = motd;
    }

    /**
     * True if the character is a member of the guild
     *
     * @param character
     * @return
     */
    public boolean isMember(Character character) {
        return members.contains(character);
    }

    /**
     * True if the character is the guild leader.
     *
     * @param character
     * @return
     */
    public boolean isLeader(Character character) {
        return leader.equals(character);
    }

    /**
     * True if the character is a guild officer.
     *
     * @param character
     * @return
     */
    public boolean isOfficer(Character character) {
        return officers.contains(character);
    }

    /**
     * True if the character is invited to join the guild.
     *
     * @param character
     * @return
     */
    public boolean isInvited(Character character) {
        return invites.contains(character);
    }

    /**
     * Sets the character on the invite list.
     *
     * @param character
     */
    public void invite(Character character) {
        invites.add(character);
    }

    /**
     * Makes the character a member of the guild if he is invited, else returns false.
     *
     * @param character
     * @return
     */
    public boolean join(Character character) {
        if (isInvited(character)) {
            members.add(character);
            invites.remove(character);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Leaves the guild and hands the leadership over if needed.
     *
     * @param character
     */
    public void leave(Character character) {
        if (isMember(character)) {
            Optional<Character> officer = officers.stream().findFirst();
            if (isLeader(character) && officer.isPresent()) {
                this.leader = officer.get();
                officers.remove(this.leader);
            } else if (isOfficer(character)) {
                officers.remove(character);
            }
            members.remove(character);
        }
    }

    /**
     * The name of the guild.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the guild.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
