package io.mooxmirror.character.squads;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.mooxmirror.character.Character;

public class Squad {
    private Set<Character> members;
    private Set<Character> invites;

    /**
     * Creates a new squad.
     *
     * @param leader
     */
    public Squad(Character leader) {
        members = new HashSet<Character>();
        invites = new HashSet<Character>();
        members.add(leader);
        leader.setSquad(this);
    }

    public Set<Character> getMembers() {
        return members;
    }

    /**
     * Kicks the member from the group.
     *
     * @param character
     * @return
     */
    public boolean leave(Character character) {
        if (isMember(character)) {
            members.remove(character);
            character.setSquad(null);
            return true;
        }
        return false;
    }

    /**
     * Accepts the invitation if it exists. Returns false if the invitation does not exists.
     *
     * @param character
     * @return
     */
    public boolean accept(Character character) {
        if (invites.contains(character)) {
            invites.remove(character);
            members.add(character);
            character.setSquad(this);
            return true;
        }
        return false;
    }

    /**
     * Invites the character into the squad.
     *
     * @param character
     */
    public void invite(Character character) {
        if (!invites.contains(character)) invites.add(character);
    }

    /**
     * Returns if the character is a member of the squad.
     *
     * @param character
     * @return
     */
    public boolean isMember(Character character) {
        return members.contains(character);
    }

    /**
     * Returns if the character is invited to the squad.
     */
    public boolean isInvited(Character character) {
        return invites.contains(character);
    }

    /**
     * Returns the size of the squad.
     *
     * @return
     */
    public int size() {
        return members.size();
    }

    /**
     * Cleans current invitations.
     */
    public void clearInvites() {
        invites.clear();
    }
}
