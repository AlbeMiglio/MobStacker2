package com.kiwifisher.mobstacker2.listeners;

import com.kiwifisher.mobstacker2.MobStacker2;
import com.kiwifisher.mobstacker2.io.Settings;
import com.kiwifisher.mobstacker2.metadata.MetaTags;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class MobExplodeListener implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {

        if (event.getEntity() instanceof LivingEntity && MetaTags.hasMetaData((LivingEntity) event.getEntity())) {

            LivingEntity entity = (LivingEntity) event.getEntity();

            if (Settings.CREEPER_KILLS_STACK) {

                if (Settings.MAGNIFY_CREEPER_EXPLOSION) {

                    int explosionSize = Settings.MAX_EXPLOSION_SIZE;
                    MobStacker2.debug("Max explosion size: " + explosionSize);

                    /*
                    If it should be smaller than the max size then reassign it
                     */
                    if (MetaTags.getQuantity(entity) < explosionSize) {

                        explosionSize = MetaTags.getQuantity(entity);

                    }

                    MobStacker2.debug("Creating explosion size " + explosionSize);

                    entity.remove();
                    entity.getWorld().createExplosion(entity.getLocation(), explosionSize);
                    return;
                }

                entity.remove();

            }

            MobStacker2.debug("Creeper stack lowered by 1");

            event.setCancelled(true);
            entity.getWorld().createExplosion(entity.getLocation(), 2);
            MetaTags.setQuantity(entity, MetaTags.getQuantity(entity) - 1);


        }

    }

}
