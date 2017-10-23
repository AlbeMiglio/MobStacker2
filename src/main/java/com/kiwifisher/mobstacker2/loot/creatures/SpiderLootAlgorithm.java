package com.kiwifisher.mobstacker2.loot.creatures;

import com.kiwifisher.mobstacker2.loot.Loot;
import com.kiwifisher.mobstacker2.loot.LootAlgorithm;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class SpiderLootAlgorithm extends LootAlgorithm {

    private final List<Loot> dropArrayList = new ArrayList<>();

    public SpiderLootAlgorithm() {
        dropArrayList.add(new Loot(Material.STRING, 0, 2));
        dropArrayList.add(new Loot(Material.SPIDER_EYE, 1, 1.0 / 3));
    }

    @Override
    public int getExp(Entity entity, int numberOfMobsWorth) {
        return 5 * numberOfMobsWorth;
    }

}
