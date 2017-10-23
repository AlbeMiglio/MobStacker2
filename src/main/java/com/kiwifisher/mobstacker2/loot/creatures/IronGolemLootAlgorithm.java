package com.kiwifisher.mobstacker2.loot.creatures;

import com.kiwifisher.mobstacker2.loot.Loot;
import com.kiwifisher.mobstacker2.loot.LootAlgorithm;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

public class IronGolemLootAlgorithm extends LootAlgorithm {

    public IronGolemLootAlgorithm() {
        this.getLootArray().add(new Loot(Material.IRON_INGOT, 3, 5, false));
        this.getLootArray().add(new Loot(Material.RED_ROSE, 0, 2, false));
    }

    @Override
    public int getExp(Entity entity, int numberOfMobs) {
        return 0;
    }

}