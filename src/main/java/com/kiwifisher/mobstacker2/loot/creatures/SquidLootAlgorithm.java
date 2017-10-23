package com.kiwifisher.mobstacker2.loot.creatures;

import com.kiwifisher.mobstacker2.loot.Loot;
import org.bukkit.Material;

public class SquidLootAlgorithm extends AnimalLootAlgorithm {

    public SquidLootAlgorithm() {
        this.getLootArray().add(new Loot(Material.INK_SACK, 1, 3));
    }

}