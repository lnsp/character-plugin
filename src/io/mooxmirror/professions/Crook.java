package io.mooxmirror.professions;

import org.bukkit.ChatColor;

import io.mooxmirror.character.spells.Spell;

public class Crook implements Profession {

    @Override
    public String getName() {
        return "Gauner";
    }

    @Override
    public String getArmorLevel() {
        return "Keine";
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.DARK_GRAY;
    }

    @Override
    public String toString() {
        return getChatString();
    }

    @Override
    public void tick() {
    }

    @Override
    public String getResourceString() {
        return ChatColor.GRAY + "Keine" + ChatColor.RESET;
    }

    @Override
    public String getResourceName() {
        return "Keine";
    }

    @Override
    public boolean enoughResources(Spell spell) {
        return false;
    }

    @Override
    public Spell[] getSpells() {
        return new Spell[]{};
    }

    @Override
    public void useResources(int cost) {
    }


}
