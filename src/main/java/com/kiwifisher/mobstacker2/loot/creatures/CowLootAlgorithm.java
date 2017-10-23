package com.kiwifisher.mobstacker2.loot.creatures;

import com.kiwifisher.mobstacker2.loot.CookableLoot;
import com.kiwifisher.mobstacker2.loot.Loot;
import org.bukkit.Material;

public class CowLootAlgorithm extends AnimalLootAlgorithm {

    public CowLootAlgorithm() {
        this.getLootArray().add(new CookableLoot(Material.RAW_BEEF, Material.COOKED_BEEF, 1, 1));
        this.getLootArray().add(new Loot(Material.LEATHER, 0, 2));
    }

}