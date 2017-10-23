package com.kiwifisher.mobstacker2.metadata;

import com.gmail.nossr50.mcMMO;
import com.kiwifisher.mobstacker2.MobStacker2;
import com.kiwifisher.mobstacker2.io.Settings;
import com.kiwifisher.mobstacker2.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class MetaTags {

    public static final String QUANTITY = "ms-quantity";
    public static final String SPAWN_REASON = "ms-spawn-reason";
    public static final String IS_STACKING = "ms-is-stacking";

    /**
     * Adds the appropriate meta tags to a mob
     *
     * @param spawnEvent The spawn event of the creature to affect
     */
    public static void initialiseMob(CreatureSpawnEvent spawnEvent) {

        LivingEntity entity = spawnEvent.getEntity();

        setQuantity(entity, 1);
        setSpawnReason(entity, spawnEvent.getSpawnReason());
        setStacking(entity, true);

        if (MobStacker2.usesMcMMO() && Settings.NO_MCMMO_REASONS.contains(spawnEvent.getSpawnReason().name())) {

            setNoMcMMO(entity, true);

        } else {

            setNoMcMMO(entity, false);

        }

    }

    public static void setNoMcMMO(LivingEntity entity, boolean bool) {

        if (bool) {

            entity.setMetadata(mcMMO.entityMetadataKey, new FixedMetadataValue(MobStacker2.mcMMOPlugin, mcMMO.metadataValue));

        } else {

            if (entity.hasMetadata(mcMMO.entityMetadataKey)) {

                entity.removeMetadata(mcMMO.entityMetadataKey, MobStacker2.mcMMOPlugin);

            }

        }

    }

    public static boolean isNoMcMMO(LivingEntity entity) {

        if (entity.hasMetadata(mcMMO.entityMetadataKey)) {

            return entity.getMetadata(mcMMO.entityMetadataKey).get(0).asBoolean();

        }

        return false;

    }


    public static void setQuantity(LivingEntity entity, int quantity) {

        entity.setMetadata(QUANTITY, new FixedMetadataValue(MobStacker2.getPlugin(), quantity));
        updateName(entity);

    }

    public static int getQuantity(LivingEntity entity) {

        if (entity.hasMetadata(QUANTITY)) {


            try {
                return entity.getMetadata(QUANTITY).get(0).asInt();
            } catch (IndexOutOfBoundsException e) {

                MobStacker2.log("There was a problem getting an entities quantity");
                return -1;

            }

        }
        /*
        If no quantity is specified then we can assume is is just a single entity
         */
        return 1;
    }

    public static java.lang.String getSpawnReason(LivingEntity entity) {

        if (!entity.hasMetadata(SPAWN_REASON)) {

            setSpawnReason(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

        }

        try {
            return entity.getMetadata(SPAWN_REASON).get(0).asString();
        } catch (IndexOutOfBoundsException e) {

            MobStacker2.log("There was a problem getting an entities spawn reason");
            return "ERROR";

        }

    }

    public static void setSpawnReason(LivingEntity entity, CreatureSpawnEvent.SpawnReason reason) {

        entity.setMetadata(SPAWN_REASON, new FixedMetadataValue(MobStacker2.getPlugin(), reason.name()));

    }

    public static void setSpawnReason(LivingEntity entity, String reason) {

        entity.setMetadata(SPAWN_REASON, new FixedMetadataValue(MobStacker2.getPlugin(), reason));

    }

    public static boolean isStacking(LivingEntity entity) {

        try {

            if (entity.hasMetadata(IS_STACKING)) {

                return entity.getMetadata(IS_STACKING).get(0).asBoolean();

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            return false;

        }

        return false;
    }

    public static void setStacking(LivingEntity entity, boolean isStacking) {

        entity.setMetadata(IS_STACKING, new FixedMetadataValue(MobStacker2.getPlugin(), isStacking));
        if (!getSpawnReason(entity).equals(CreatureSpawnEvent.SpawnReason.CUSTOM.name())) {

            Util.tryStack(entity);

        }

    }

    public static boolean hasMetaData(LivingEntity entity) {

        if (entity.hasMetadata(QUANTITY) && entity.hasMetadata(SPAWN_REASON) && entity.hasMetadata(IS_STACKING)) {
            return true;
        }

        return false;

    }

    public static void updateName(LivingEntity entity) {

        if (hasMetaData(entity)) {

            if (getQuantity(entity) <= 1) {

                removeName(entity);
                return;

            }

            String newName = Settings.NAME_TEMPLATE.replace("{QTY}", getQuantity(entity) + "");
            newName = newName.replace("{TYPE}", entity.getType().name().replace("_", " "));
            newName = ChatColor.translateAlternateColorCodes('&', newName);

            entity.setCustomName(newName);
            entity.setCustomNameVisible(true);

        }

    }

    public static void removeName(LivingEntity entity) {

        entity.setCustomName("");
        entity.setCustomNameVisible(false);

    }

    public static void serialiseToName(LivingEntity entity) {

        if (hasMetaData(entity)) {

            entity.setCustomName(MobStacker2.RELOAD_UUID + "-" + getQuantity(entity) + "-" + getSpawnReason(entity) + "-" + isStacking(entity));

        }
    }

    public static void recreateFromName(LivingEntity entity) {

        if (entity.getCustomName() != null && (entity.getCustomName().contains(MobStacker2.LAST_UUID) || entity.getCustomName().contains(MobStacker2.RELOAD_UUID))) {

            String[] attributes = entity.getCustomName().split("-");

            if (attributes.length != 4) {

                removeName(entity);
                return;
            }

            int quantity = new Integer(attributes[1]);
            String spawnReason = attributes[2];
            boolean isStacking = Boolean.parseBoolean(attributes[3]);

            setStacking(entity, isStacking);
            setSpawnReason(entity, spawnReason);
            setQuantity(entity, quantity);

        }

    }

}
