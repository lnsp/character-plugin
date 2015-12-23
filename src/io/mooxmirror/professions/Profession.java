package io.mooxmirror.professions;

import org.bukkit.ChatColor;

import io.mooxmirror.character.Character;
import io.mooxmirror.character.spells.Spell;

public interface Profession {
    /**
     * Returns the name of the profession.
     *
     * @return
     */
    public String getName();

    /**
     * Returns the armor level of the profession.
     *
     * @return
     */
    public String getArmorLevel();

    /**
     * Returns the unique chat color of the profession.
     *
     * @return
     */
    public ChatColor getChatColor();

    /**
     * Generates a chat-formatted profession string.
     *
     * @return
     */
    public default String getChatString() {
        return getChatColor() + getName() + " [" + getArmorLevel() + "]" + ChatColor.RESET;
    }

    /**
     * Updates the profession, regenerating resources etc.
     */
    public void tick();

    /**
     * Returns a string representing the current resource amount.
     *
     * @return
     */
    public String getResourceString();

    /**
     * Returns the resource name.
     *
     * @return
     */
    public String getResourceName();

    /**
     * Checks if the profession has enough resources to cast the spell.
     *
     * @param spell
     * @return
     */
    public boolean enoughResources(Spell spell);

    /**
     * Returns an array with all available profession spells.
     *
     * @return
     */
    public Spell[] getSpells();

    /**
     * Casts a spell. Returns a string array with the spell name and the resource cost if successful, else null.
     *
     * @param character
     * @param name
     * @return
     */
    public default String[] castSpell(Character character, String name) {
        for (Spell spell : getSpells()) {
            if (spell.getName().equalsIgnoreCase(name) && spell.getLevel() <= character.getLevel() && spell.isReady() && enoughResources(spell)) {
                spell.cast(character);
                character.getProfession().useResources(spell.getResourceCost());
                return new String[]{spell.getName(), spell.getResourceCost() + " " + character.getProfession().getResourceName()};
            }
        }

        return null;
    }

    /**
     * Uses up resources.
     *
     * @param cost
     */
    public void useResources(int cost);
}
