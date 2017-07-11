package com.kiwifisher.mobstacker2.loot.creatures;

import com.kiwifisher.mobstacker2.loot.CookableLoot;
import com.kiwifisher.mobstacker2.loot.Loot;
import com.kiwifisher.mobstacker2.loot.LootAlgorithm;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PigLootAlgorithm  extends AnimalLootAlgorithm {

    public PigLootAlgorithm() {
        this.getLootArray().add(new CookableLoot(Material.PORK, Material.GRILLED_PORK, 1, 3));
    }

}
