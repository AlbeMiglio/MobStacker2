package com.kiwifisher.mobstacker2.listeners;

import com.kiwifisher.mobstacker2.metadata.MetaTags;
import com.kiwifisher.mobstacker2.util.Util;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SheepDyeWoolEvent;

public class SheepDyeListener implements Listener {

    @EventHandler
    public void sheepDyeEvent(SheepDyeWoolEvent event) {

        Sheep sheep = event.getEntity();

        if (MetaTags.hasMetaData(sheep) && MetaTags.getQuantity(sheep) > 1) {

            /*
            Try increment nearby before spawning as it gets messy
             */
//            for (Entity e : sheep.getNearbyEntities(Settings.X_RANGE, Settings.Y_RANGE, Settings.Z_RANGE)) {
//
//                if (e instanceof Sheep && MetaTags.hasMetaData((LivingEntity) e) && ((Sheep) e).getColor() == event.getColor()) {
//
//                    MetaTags.setQuantity((LivingEntity) e, MetaTags.getQuantity((LivingEntity) e) + 1);
//                    Util.peelAway(sheep).remove();
//
//
//                    MobStacker2.broadcast("Merged sheep directly");
//
//                    return;
//                }
//
//            }

            MetaTags.setStacking(sheep, false);
            Sheep dyedSheep = (Sheep) Util.peelAway(sheep);
            sheep.setColor(event.getColor());
            MetaTags.setStacking(sheep, true);
            //MobStacker2.broadcast("QTY: " + MetaTags.getQuantity(dyedSheep) + ", Colour: " + event.getColor());


        } else if (MetaTags.hasMetaData(sheep)) {

            Util.tryStack(sheep);

        }

    }

}
