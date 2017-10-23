package com.kiwifisher.mobstacker2.listeners;

import com.kiwifisher.mobstacker2.io.Settings;
import com.kiwifisher.mobstacker2.metadata.MetaTags;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class MobTargetListener implements Listener {

    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent event) {

        if (!(event.getEntity() instanceof LivingEntity)) return;

        LivingEntity entity = (LivingEntity) event.getEntity();

        if (event.getTarget() instanceof Player && MetaTags.hasMetaData(entity)) {

            if (Settings.NERF_SPAWN_REASONS.contains(MetaTags.getSpawnReason(entity))) {

                event.setCancelled(true);

            }

        }

    }

}
