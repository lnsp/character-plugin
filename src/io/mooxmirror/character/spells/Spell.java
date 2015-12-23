package io.mooxmirror.character.spells;

import io.mooxmirror.character.Character;

public interface Spell {
    /**
     * The name of the spell.
     * @return
     */
    public String getName();

    /**
     * The description of the spell.
     * @return
     */
    public String getDescription();

    /**
     * The resource cost of the spell.
     * @return
     */
    public int getResourceCost();

    /**
     * The cooldown in nanoseconds of the spell.
     * @return
     */
    public long getCooldown();

    /**
     * Checks if the spell is off-cooldown.
     * @return
     */
    public boolean isReady();

    /**
     * The minimum level requirement.
     * @return
     */
    public int getLevel();

    /**
     * Casts the spell.
     * @param character
     */
    public void cast(Character character);
}
