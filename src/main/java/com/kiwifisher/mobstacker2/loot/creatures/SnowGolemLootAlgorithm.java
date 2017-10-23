package com.kiwifisher.mobstacker2.loot.creatures;


import com.kiwifisher.mobstacker2.loot.Loot;
import com.kiwifisher.mobstacker2.loot.LootAlgorithm;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

public class SnowGolemLootAlgorithm extends LootAlgorithm {

    public SnowGolemLootAlgorithm() {
        this.getLootArray().add(new Loot(Material.SNOW_BALL, 0, 15));
    }

    @Override
    public int getExp(Entity entity, int numberOfMobs) {
        return 0;
    }

}
