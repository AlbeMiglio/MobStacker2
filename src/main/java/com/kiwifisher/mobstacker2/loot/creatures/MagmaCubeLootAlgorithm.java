package com.kiwifisher.mobstacker2.loot.creatures;

import com.kiwifisher.mobstacker2.loot.Loot;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * LootAlgorithm for Magma Cubes.
 *
 * @author Jikoo
 */
public class MagmaCubeLootAlgorithm extends SlimeLootAlgorithm {

    public MagmaCubeLootAlgorithm() {
        this.getLootArray().add(new Loot(Material.MAGMA_CREAM, 1, 1.0 / 4));
    }

    @Override
    public List<ItemStack> getRandomLoot(Entity entity, int numberOfMobs, boolean playerKill, int looting) {
        if (!(entity instanceof Slime)) {
            return new ArrayList<>();
        }

        if (((Slime) entity).getSize() != 1) {
            return super.getRandomLoot(entity, numberOfMobs, playerKill, looting);
        }

        return new ArrayList<>();
    }

}