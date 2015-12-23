package io.mooxmirror.professions;

import org.bukkit.ChatColor;

import io.mooxmirror.character.spells.Spell;

public class Ranger implements Profession {
    private final int MAXIMUM_ENERGY = 100;
    private final double ENERGY_PER_SECOND = 25.00;
    private double energy;

    private long lastTick;

    private Spell[] spells;

    public Ranger() {
        energy = MAXIMUM_ENERGY;
        lastTick = System.nanoTime();
        spells = new Spell[0];
    }

    @Override
    public String getName() {
        return "Waldl�ufer";
    }

    @Override
    public String getArmorLevel() {
        return "Mittel";
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.DARK_GREEN;
    }

    @Override
    public String toString() {
        return getChatString();
    }

    @Override
    public void tick() {
        long diff = System.nanoTime() - lastTick;
        double delta = diff / 1000000000.0;
        energy = Math.min(MAXIMUM_ENERGY, energy + delta * ENERGY_PER_SECOND);
    }

    @Override
    public String getResourceString() {
        return ChatColor.GOLD + "Energie: " + (int) energy + " / " + MAXIMUM_ENERGY + ChatColor.RESET;
    }

    @Override
    public String getResourceName() {
        return "Energie";
    }

    @Override
    public boolean enoughResources(Spell spell) {
        return spell.getResourceCost() <= energy;
    }

    @Override
    public Spell[] getSpells() {
        return spells;
    }

    @Override
    public void useResources(int cost) {
        energy -= cost;
    }

}
