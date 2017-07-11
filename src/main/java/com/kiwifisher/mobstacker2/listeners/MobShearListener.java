package com.kiwifisher.mobstacker2.listeners;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.kiwifisher.mobstacker2.MobStacker2;
import com.kiwifisher.mobstacker2.io.Settings;
import com.kiwifisher.mobstacker2.metadata.MetaTags;
import com.kiwifisher.mobstacker2.util.Util;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class MobShearListener implements Listener {


    @EventHandler
    public void sheepShear(PlayerShearEntityEvent event) {

        if (event.getEntity() instanceof Sheep) {

            LivingEntity entity = (LivingEntity) event.getEntity();

            LivingEntity shearedEntity = Util.peelAway(entity);

        } else if (event.getEntity() instanceof MushroomCow) {

            final LivingEntity entity = (LivingEntity) event.getEntity();

            if (MetaTags.hasMetaData(entity) && MetaTags.getQuantity(entity) > 0) {

                LivingEntity shearedEntity = Util.peelAway(entity, true);


            }
        }
    }

}
