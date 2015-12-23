package io.mooxmirror.professions;

import org.bukkit.ChatColor;

import io.mooxmirror.character.spells.Spell;

public class Priest implements Profession {
    private final int MAXIMUM_MANA = 1000;
    private final double MANA_PER_SECOND = 50.00;
    private double mana;

    private long lastTick;

    private Spell[] spells;

    public Priest() {
        mana = MAXIMUM_MANA;
        lastTick = System.nanoTime();
        spells = new Spell[0];
    }

    @Override
    public String getName() {
        return "Priester";
    }

    @Override
    public String getArmorLevel() {
        return "Leicht";
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.DARK_AQUA;
    }

    @Override
    public String toString() {
        return getChatString();
    }

    @Override
    public void tick() {
        long diff = System.nanoTime() - lastTick;
        double delta = diff / 1000000000.0;
        mana = Math.min(MAXIMUM_MANA, delta * MANA_PER_SECOND);
    }

    @Override
    public String getResourceString() {
        return ChatColor.BLUE + "Mana: " + (int) mana + " / " + MAXIMUM_MANA + ChatColor.RESET;
    }

    @Override
    public String getResourceName() {
        return "Mana";
    }

    @Override
    public boolean enoughResources(Spell spell) {
        return spell.getResourceCost() <= mana;
    }

    @Override
    public Spell[] getSpells() {
        return spells;
    }

    @Override
    public void useResources(int cost) {
        mana -= cost;
    }

}
