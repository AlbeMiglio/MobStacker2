package com.kiwifisher.mobstacker2.listeners;

import com.kiwifisher.mobstacker2.util.Util;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

public class MobTameListener implements Listener {

    @EventHandler
    public void onTame(EntityTameEvent event) {

        if (event.getEntity() instanceof Tameable) {

            LivingEntity entity = (LivingEntity) event.getEntity();

            Util.peelAway(entity);

        }

    }

}
