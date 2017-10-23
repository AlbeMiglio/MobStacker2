package com.kiwifisher.mobstacker2.loot;

import org.bukkit.Material;

public class UncommonLoot extends Loot {

    public UncommonLoot(Material material) {
        super(material, 1, 1, 0.33, false);
    }

    public UncommonLoot(Material material, short data) {
        super(material, data, 1, 1, 0.33, false);
    }

    @Override
    public double getDropChance(int looting) {

        switch (looting) {

            case 1:
                return 0.5;
            case 2:
                return 0.66;
            case 3:
                return 0.75;

        }

        return 0.33;

    }

}
