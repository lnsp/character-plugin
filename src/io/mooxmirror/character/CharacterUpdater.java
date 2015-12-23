package io.mooxmirror.character;

import org.bukkit.scheduler.BukkitRunnable;

public class CharacterUpdater extends BukkitRunnable {
    private CharacterPlugin plugin;

    public CharacterUpdater(CharacterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.allCharacters().forEach((c) -> c.tick());
    }

}
