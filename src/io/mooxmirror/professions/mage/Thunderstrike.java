package io.mooxmirror.professions.mage;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import io.mooxmirror.character.Character;
import io.mooxmirror.character.spells.BasicSpell;

public class Thunderstrike extends BasicSpell {

    public Thunderstrike() {
        super();
        super.cooldown = 5000000000L;
        super.name = "Donnergrollen";
        super.description = "Ruft einen Blitz.";
        super.level = 1;
        super.resourceCost = 50;
    }

    @Override
    public void apply(Character character) {
        Player player = character.getPlayer();
        Block target = player.getTargetBlock(null, 30);
        Location location = target.getLocation();
        player.getWorld().strikeLightningEffect(location);
        player.getWorld().getLivingEntities().forEach((p) -> {
            if (p.getLocation().distance(location) <= 5) {
                p.damage(100, player);
            }
        });
    }


}
