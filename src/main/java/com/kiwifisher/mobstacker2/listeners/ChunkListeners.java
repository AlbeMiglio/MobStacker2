package com.kiwifisher.mobstacker2.listeners;

import com.kiwifisher.mobstacker2.metadata.MetaTags;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListeners implements Listener {

    @EventHandler
    public void mobUnloadEvent(ChunkUnloadEvent event) {

        for (Entity entity : event.getChunk().getEntities()) {

            if (entity instanceof LivingEntity && MetaTags.hasMetaData((LivingEntity) entity)) {

                MetaTags.serialiseToName((LivingEntity) entity);
            }

        }

    }

    @EventHandler
    public void mobLoadEvent(ChunkLoadEvent event) {


        for (Entity entity : event.getChunk().getEntities()) {

            if (!(entity instanceof LivingEntity)) continue;

            MetaTags.recreateFromName((LivingEntity) entity);

        }

    }

}
