package com.kiwifisher.mobstacker2.loot.creatures;

import com.kiwifisher.mobstacker2.loot.Loot;
import com.kiwifisher.mobstacker2.loot.LootAlgorithm;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

public class BlazeLootAlgorithm extends LootAlgorithm {

    public BlazeLootAlgorithm() {
        this.getLootArray().add(new Loot(Material.BLAZE_ROD, 0, 1));
    }

    @Override
    public int getExp(Entity entity, int numberOfMobs) {
        return 10 * numberOfMobs;
    }

}
