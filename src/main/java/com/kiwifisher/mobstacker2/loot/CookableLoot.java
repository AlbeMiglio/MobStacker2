package com.kiwifisher.mobstacker2.loot;

import org.bukkit.Material;
import org.bukkit.entity.Entity;

public class CookableLoot extends Loot {

    private final Material cooked;

    public CookableLoot(Material raw, Material cooked, int minimumQuantity, int maxQuantity) {
        super(raw, minimumQuantity, maxQuantity, true);
        this.cooked = cooked;
    }

    public CookableLoot(Material raw, Material cooked, int minimumQuantity, int maxQuantity, boolean doLooting) {
        super(raw, minimumQuantity, maxQuantity, false);
        this.cooked = cooked;
    }

    @Override
    public Material getMaterial(Entity entity) {
        return entity.getFireTicks() > 0 ? cooked : super.getMaterial(entity);
    }

}