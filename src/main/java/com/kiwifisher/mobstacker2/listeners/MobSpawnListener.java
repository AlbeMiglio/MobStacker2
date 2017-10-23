package com.kiwifisher.mobstacker2.listeners;

import com.kiwifisher.mobstacker2.MobStacker2;
import com.kiwifisher.mobstacker2.io.Settings;
import com.kiwifisher.mobstacker2.metadata.MetaTags;
import com.kiwifisher.mobstacker2.util.Util;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MobSpawnListener implements Listener {

    @EventHandler
    public void mobSpawnEvent(final CreatureSpawnEvent event) {

        final LivingEntity entity = event.getEntity();

        if (Settings.STACKING_TYPES.contains(entity.getType().name()) && Settings.SPAWN_TYPES.contains(event.getSpawnReason().name())) {

            if (Settings.MAX_PER_CHUNK.containsKey(entity.getType().name()) && Util.quantityInChunk(entity) >= Settings.MAX_PER_CHUNK.get(entity.getType().name())) {

                event.setCancelled(true);
                return;
            }

            if (Settings.BLACKLISTED_WORLDS.contains(event.getEntity().getWorld().getName())) return;


            MetaTags.initialiseMob(event);

            long delay = 0;

            if (MobStacker2.usesMythicMobs() && MetaTags.getSpawnReason(entity).equalsIgnoreCase(CreatureSpawnEvent.SpawnReason.CUSTOM.name())) {
                delay = 5L;
            }

            new BukkitRunnable() {

                int count = 0;
                int stop = Settings.SECONDS_TO_TRY_STACK * 2;

                @Override
                public void run() {

                    MobStacker2.debug("Trying to stack now as " + MetaTags.getSpawnReason(entity));
                    if (MetaTags.getSpawnReason(entity).equals("MYTHIC_MOBS")) {

                        MetaTags.setStacking(entity, false);
                        cancel();

                    }

                    if (count == 0 && entity.getType() == EntityType.MUSHROOM_COW) {
                        for (Entity ent : entity.getNearbyEntities(0.2, 0.2, 0.2)) {

                            if (!(ent instanceof LivingEntity)) return;

                            LivingEntity e = (LivingEntity) ent;

                            if (e.getType() == EntityType.COW) {

                                if (!MetaTags.getSpawnReason(e).equalsIgnoreCase(MetaTags.getSpawnReason(entity))) {
                                    MetaTags.setSpawnReason(e, MetaTags.getSpawnReason(entity));

                                }

                                if (MetaTags.isStacking(e) != MetaTags.isStacking(entity)) {
                                    MetaTags.setStacking(e, MetaTags.isStacking(entity));

                                }

                                if (MetaTags.isNoMcMMO(e) != MetaTags.isNoMcMMO(entity)) {
                                    MetaTags.setNoMcMMO(e, MetaTags.isNoMcMMO(entity));

                                }
                            }

                        }
                    }


                    /*
                    Enter if we still want to be counting, or if we have spawn on continuous search going
                     */
                    if (count < stop || stop <= 0) {

                        Util.tryStack(event);

                        if (stop > 0) {
                            count++;
                        }

                        /*
                        If we are past the count then stop
                         */
                    } else if (count >= stop) {
                        cancel();
                    }

                    /*
                    If the stop int is 0 then we we only wanted to search once
                     */
                    if (stop == 0 || entity.isDead()) {
                        cancel();
                    }

                }
            }.runTaskTimer(MobStacker2.getPlugin(), delay, 10L);

        }

    }

}
