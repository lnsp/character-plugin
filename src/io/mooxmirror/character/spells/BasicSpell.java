package io.mooxmirror.character.spells;

import io.mooxmirror.character.Character;

public abstract class BasicSpell implements Spell {
    protected String name, description;
    protected int resourceCost, level;
    protected long lastCast, cooldown;

    public BasicSpell() {
        this.name = "BasicSpell";
        this.description = "Basic spell";
        this.resourceCost = 0;
        this.cooldown = 0;
        this.lastCast = 0;
        this.level = 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getResourceCost() {
        return this.resourceCost;
    }

    @Override
    public long getCooldown() {
        return this.cooldown;
    }

    @Override
    public boolean isReady() {
        return System.nanoTime() - lastCast >= this.cooldown;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void cast(Character character) {
        apply(character);
        this.lastCast = System.nanoTime();
    }

    public abstract void apply(Character character);
}
