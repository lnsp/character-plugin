package io.mooxmirror.professions.mage;

import org.bukkit.Location;

import io.mooxmirror.character.Character;
import io.mooxmirror.character.spells.BasicSpell;

public class Teleport extends BasicSpell {

    public Teleport() {
        super();

        super.name = "Teleport";
        super.description = "Teleportiert dich zu dem angepeilten Ort.";
        super.cooldown = 10_000_000_000L;
        super.resourceCost = 150;
        super.level = 5;
    }

    @Override
    public void apply(Character character) {
        Location target = character.getPlayer().getTargetBlock(null, 30).getLocation();
        character.getPlayer().teleport(target.add(0, 1, 0));
    }

}
