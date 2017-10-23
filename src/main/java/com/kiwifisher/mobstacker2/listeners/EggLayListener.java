package com.kiwifisher.mobstacker2.listeners;

import com.kiwifisher.mobstacker2.MobStacker2;
import com.kiwifisher.mobstacker2.metadata.MetaTags;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EggLayListener implements Listener {

    @EventHandler
    public void eggLay(ItemSpawnEvent event) {

        Item droppedItem = event.getEntity();
        ItemStack droppedItemStack = droppedItem.getItemStack();

        if (droppedItemStack.getType() != Material.EGG) return;

        List<Entity> nearbyEntities = droppedItem.getNearbyEntities(0.01, 0.3, 0.01);

        LivingEntity closestChicken = null;
        double closestChickenDistance = 100;

        for (Entity entity : nearbyEntities) {

            if (entity instanceof Chicken) {

                if (droppedItem.getLocation().distance(entity.getLocation()) < closestChickenDistance) {

                    closestChicken = (Chicken) entity;
                    closestChickenDistance = droppedItem.getLocation().distance(entity.getLocation());

                }

            }

        }

        if (closestChicken == null) return;

        MobStacker2.debug("EGG LAY: Has Meta: " + MetaTags.hasMetaData(closestChicken) + " QTY: " + MetaTags.getQuantity(closestChicken));
        if (MetaTags.hasMetaData(closestChicken) && MetaTags.getQuantity(closestChicken) > 1) {

            droppedItemStack.setAmount(MetaTags.getQuantity(closestChicken));

        }

    }

}
