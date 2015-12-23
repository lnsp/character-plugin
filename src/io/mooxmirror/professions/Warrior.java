package io.mooxmirror.professions;

import org.bukkit.ChatColor;

import io.mooxmirror.character.spells.Spell;

public class Warrior implements Profession {
    private final int MAXIMUM_RAGE = 100;
    private final double RAGE_LOSS_PER_SECOND = 5;
    private double rage;

    private long lastTick;

    private Spell[] spells;

    public Warrior() {
        rage = 0;
        lastTick = System.nanoTime();
        spells = new Spell[0];
    }

    @Override
    public String getName() {
        return "Krieger";
    }

    @Override
    public String getArmorLevel() {
        return "Schwer";
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.DARK_RED;
    }

    @Override
    public String toString() {
        return getChatString();
    }

    @Override
    public void tick() {
        long diff = System.nanoTime() - lastTick;
        double delta = diff / 1000000000.0;
        rage = Math.max(0, rage - delta * RAGE_LOSS_PER_SECOND);
    }

    @Override
    public String getResourceString() {
        return ChatColor.DARK_RED + "Wut: " + (int) rage + " / " + MAXIMUM_RAGE + ChatColor.RESET;
    }

    @Override
    public String getResourceName() {
        return "Wut";
    }

    @Override
    public boolean enoughResources(Spell spell) {
        return spell.getResourceCost() <= rage;
    }

    @Override
    public Spell[] getSpells() {
        return spells;
    }

    @Override
    public void useResources(int cost) {
        rage -= cost;
    }

}
