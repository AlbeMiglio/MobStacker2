package com.kiwifisher.mobstacker2.util;

import com.kiwifisher.mobstacker2.MobStacker2;
import com.kiwifisher.mobstacker2.io.Settings;
import com.kiwifisher.mobstacker2.metadata.MetaTags;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.material.Colorable;

import java.util.List;

public class Util {

    static boolean canStack(LivingEntity spawnedEntity, LivingEntity existingEntity) {

        if (spawnedEntity == null || existingEntity == null) return false;

        if (MobStacker2.usesWG()) {

            RegionManager regionManager = MobStacker2.worldGuardPlugin.getRegionManager(spawnedEntity.getWorld());
            ApplicableRegionSet spawnedSet = regionManager.getApplicableRegions(spawnedEntity.getLocation());
            ApplicableRegionSet existingSet = regionManager.getApplicableRegions(existingEntity.getLocation());

            for (ProtectedRegion region : spawnedSet) {
                if (Settings.BLACKLISTED_REGIONS.contains(region.getId())) return false;
            }

            for (ProtectedRegion region : existingSet) {
                if (Settings.BLACKLISTED_REGIONS.contains(region.getId())) {

                    return false;
                }
            }

        }

        if (Settings.MAX_PER_CHUNK.containsKey(existingEntity.getType().name()) &&
                (Util.quantityInChunk(existingEntity) >= Settings.MAX_PER_CHUNK.get(existingEntity.getType().name()) ||
                        Util.quantityInChunk(spawnedEntity)
                                >= Settings.MAX_PER_CHUNK.get(spawnedEntity.
                                getType().name()))) {

            if (MetaTags.getQuantity(spawnedEntity) > MetaTags.getQuantity(existingEntity)) {

                existingEntity.remove();

            } else {
                spawnedEntity.remove();
            }

            return false;
        }


        if (MobStacker2.BLACKLISTED_CHUNKS.contains(spawnedEntity.getLocation().getChunk()) || MobStacker2.BLACKLISTED_CHUNKS.contains(existingEntity.getLocation().getChunk()))
            return false;

        if (spawnedEntity.getType() != existingEntity.getType()) return false;

        if (Settings.STACK_BY_AGE && spawnedEntity instanceof Ageable && ((Ageable) spawnedEntity).isAdult() != ((Ageable) existingEntity).isAdult())
            return false;

        if (spawnedEntity == existingEntity) return false;

        if (spawnedEntity.isDead() || existingEntity.isDead()) return false;

        if (spawnedEntity instanceof Slime && ((Slime) spawnedEntity).getSize() != ((Slime) existingEntity).getSize())
            return false;

        if (!(MetaTags.isStacking(existingEntity) && MetaTags.isStacking(spawnedEntity))) return false;

        if (!Settings.STACK_LEASHED_ANIMALS && (spawnedEntity.isLeashed() || existingEntity.isLeashed())) return false;

        if (Settings.PROTECT_TAMED_PETS && spawnedEntity instanceof Tameable && (((Tameable) spawnedEntity).isTamed() || ((Tameable) existingEntity).isTamed()))
            return false;

        if (Settings.SEPARATE_BY_COLOUR && spawnedEntity instanceof Colorable && (((Colorable) spawnedEntity).getColor() != ((Colorable) existingEntity).getColor()))
            return false;

        if (Settings.SEPARATE_BY_SHEARED && spawnedEntity instanceof Sheep && (((Sheep) spawnedEntity).isSheared() != ((Sheep) existingEntity).isSheared()))
            return false;

        if (Settings.MAX_STACK_MOBS.containsKey(spawnedEntity.getType().name()) &&
                (Settings.MAX_STACK_MOBS.get(spawnedEntity.getType().name()) == (MetaTags.getQuantity(spawnedEntity)) ||
                        Settings.MAX_STACK_MOBS.get(spawnedEntity.getType().name()) == (MetaTags.getQuantity(existingEntity)))) {

            return false;

        }

        if (!Settings.STACK_THROUGH_WALLS && !(spawnedEntity.hasLineOfSight(existingEntity))) return false;

        return true;

    }

    public static int quantityInChunk(LivingEntity entity) {

        int count = 0;

        for (Entity e : entity.getLocation().getChunk().getEntities()) {

            if (e.getType() == entity.getType() && !e.isDead()) {

                if (MetaTags.hasMetaData((LivingEntity) e) && MetaTags.isStacking((LivingEntity) e)) {

                    count += MetaTags.getQuantity((LivingEntity) e);

                } else {

                    count++;

                }
            }
        }

        return count;

    }

    public static boolean tryStack(CreatureSpawnEvent event) {
        return event.getEntity() != null && tryStack(event.getEntity());
    }

    public static boolean tryStack(LivingEntity entity) {

        List<Entity> nearbyEntities = entity.getNearbyEntities(Settings.X_RANGE, Settings.Y_RANGE, Settings.Z_RANGE);

        if (!MetaTags.isStacking(entity)) return false;

        if (!MobStacker2.IS_STACKING) return false;

        /*
        For all nearby entities
         */
        for (Entity e : nearbyEntities) {

            if (!(e instanceof LivingEntity)) continue;

            /*
            If they are the same type as ones we are meant to be stacking and the correct spawn reason, then let's merge
             */
            if (Settings.STACKING_TYPES.contains(entity.getType().name()) && Settings.SPAWN_TYPES.contains(MetaTags.getSpawnReason(entity))) {

                final LivingEntity spawnedEntity = entity;
                final LivingEntity existingEntity = (LivingEntity) e;

                if (canStack(spawnedEntity, existingEntity)) {

                    stack(spawnedEntity, existingEntity);

                }

            }

        }

        return false;

    }

    public static void stack(final LivingEntity dyingEntity, final LivingEntity growingEntity) {

        if (Settings.STACK_MOBS_DOWN && Settings.MOBS_TO_STACK_DOWN.contains(growingEntity.getType().name())) {

            double dyingY = dyingEntity.getLocation().getY();
            double growingY = growingEntity.getLocation().getY();

            if (growingY > dyingY) {

                stack(growingEntity, dyingEntity);
                return;

            }

        }


        int newSize = MetaTags.getQuantity(dyingEntity) + MetaTags.getQuantity(growingEntity);
        dyingEntity.remove();
        MetaTags.setQuantity(growingEntity, newSize);

        /*
        If the total is going to add up to more than the max stack, then set the new size to the max and spawn in another mob.
         */
        if (Settings.MAX_STACK_MOBS.containsKey(growingEntity.getType().name())) {

            if (MetaTags.getQuantity(growingEntity) > Settings.MAX_STACK_MOBS.get(growingEntity.getType().name())) {
                int maxSize = Settings.MAX_STACK_MOBS.get(growingEntity.getType().name());
                Chunk chunk = growingEntity.getLocation().getChunk();

                int numberOfFullStacks = MetaTags.getQuantity(growingEntity) / Settings.MAX_STACK_MOBS.get(growingEntity.getType().name());
                int remainingMobs = MetaTags.getQuantity(growingEntity) % Settings.MAX_STACK_MOBS.get(growingEntity.getType().name());

                MobStacker2.BLACKLISTED_CHUNKS.add(chunk);

                for (int i = 0; i < numberOfFullStacks; i++) {

                    LivingEntity newStack = (LivingEntity) growingEntity.getWorld().spawnEntity(growingEntity.getLocation(), growingEntity.getType());
                    cloneAttributes(newStack, growingEntity);
                    MetaTags.setQuantity(newStack, maxSize);

                }

                LivingEntity remainderStack = (LivingEntity) growingEntity.getWorld().spawnEntity(growingEntity.getLocation(), growingEntity.getType());
                cloneAttributes(remainderStack, growingEntity);
                MetaTags.setQuantity(remainderStack, remainingMobs);

                growingEntity.remove();

                if (MobStacker2.BLACKLISTED_CHUNKS.contains(chunk)) {

                    MobStacker2.BLACKLISTED_CHUNKS.remove(chunk);

                }

            }

        }

    }

    public static void cloneAttributes(LivingEntity newEntity, LivingEntity masterEntity) {

        MetaTags.setSpawnReason(newEntity, MetaTags.getSpawnReason(masterEntity));
        MetaTags.setStacking(newEntity, MetaTags.isStacking(masterEntity));
        MetaTags.setNoMcMMO(newEntity, MetaTags.isNoMcMMO(masterEntity));

        if (newEntity instanceof Sheep && masterEntity instanceof Sheep) {
            if (Settings.SEPARATE_BY_COLOUR) {
                ((Sheep) newEntity).setColor(((Sheep) masterEntity).getColor());
            }

            ((Sheep) newEntity).setSheared(((Sheep) masterEntity).isSheared());
        }

        if (newEntity instanceof Ageable && masterEntity instanceof Ageable) {
            ((Ageable) newEntity).setAge(((Ageable) masterEntity).getAge());
        }

        if (newEntity instanceof Slime && masterEntity instanceof Slime) {

            ((Slime) newEntity).setSize(((Slime) masterEntity).getSize());

        }

    }

    @SuppressWarnings("deprecation")
    public static void carbonCopy(LivingEntity newEntity, LivingEntity masterEntity) {

        cloneAttributes(newEntity, masterEntity);

        if (masterEntity instanceof Horse && newEntity instanceof Horse) {

            Horse newHorse = (Horse) newEntity;
            Horse masterHorse = (Horse) masterEntity;

            newHorse.setCarryingChest(masterHorse.isCarryingChest());
            newHorse.setColor(masterHorse.getColor());
            newHorse.setDomestication(masterHorse.getDomestication());
            newHorse.setJumpStrength(masterHorse.getJumpStrength());
            newHorse.setMaxDomestication(masterHorse.getMaxDomestication());
            newHorse.setStyle(masterHorse.getStyle());
            newHorse.setVariant(masterHorse.getVariant());

        }

        MetaTags.setQuantity(newEntity, MetaTags.getQuantity(masterEntity));
        MetaTags.setSpawnReason(newEntity, MetaTags.getSpawnReason(masterEntity));
        MetaTags.setStacking(newEntity, MetaTags.isStacking(masterEntity));

    }

    public static void respawn(LivingEntity entity) {

        carbonCopy((LivingEntity) entity.getWorld().spawnEntity(entity.getLocation(), entity.getType()), entity);
        entity.remove();
    }

    public static LivingEntity peelAway(LivingEntity entity) {

        return peelAway(entity, false);

    }

    public static LivingEntity peelAway(LivingEntity entity, boolean forceSpawnAgain) {

        if (entity instanceof Sheep || entity instanceof MushroomCow) {

            MobStacker2.BLACKLISTED_CHUNKS.add(entity.getLocation().getChunk());

        }

        LivingEntity newEntity;

        if (forceSpawnAgain && MetaTags.getQuantity(entity) == 1) {

            newEntity = (LivingEntity) entity.getWorld().spawnEntity(entity.getLocation(), entity.getType());
            Util.cloneAttributes(newEntity, entity);

            newEntity.remove();

            entity = newEntity;


        } else if (MetaTags.getQuantity(entity) > 1) {

            int newQuantity = MetaTags.getQuantity(entity) - 1;

                /*
                newEntity is the continuation of the stack. It is not the sheep that was shorn.
                 */
            newEntity = (LivingEntity) entity.getWorld().spawnEntity(entity.getLocation(), entity.getType());
            Util.cloneAttributes(newEntity, entity);

            MetaTags.setSpawnReason(newEntity, MetaTags.getSpawnReason(entity));
            MetaTags.setQuantity(newEntity, newQuantity);
            MetaTags.setQuantity(entity, 1);

        }

        tryStack(entity);

        if ((entity instanceof Sheep || entity instanceof MushroomCow) && MobStacker2.BLACKLISTED_CHUNKS.contains(entity.getLocation().getChunk())) {

            MobStacker2.BLACKLISTED_CHUNKS.remove(entity.getLocation().getChunk());

        }

        /*
        Returns the new single entity, not the stack
         */
        return entity;

    }

    public static void reviveAllStacks() {

        for (World world : MobStacker2.getPlugin().getServer().getWorlds()) {

            if (Settings.BLACKLISTED_WORLDS.contains(world.getName())) continue;

            for (Entity e : world.getEntities()) {

                if (e instanceof LivingEntity) {

                    MetaTags.recreateFromName((LivingEntity) e);

                }

            }

        }

    }

    public static void serialiseAllStacks() {

        for (World world : MobStacker2.getPlugin().getServer().getWorlds()) {

            if (Settings.BLACKLISTED_WORLDS.contains(world.getName())) continue;

            for (Entity e : world.getEntities()) {

                if (e instanceof LivingEntity && MetaTags.hasMetaData((LivingEntity) e)) {

                    MetaTags.serialiseToName((LivingEntity) e);

                }

            }

        }

    }

    public static String countAllStacks(boolean remove) {

        int stackCount = 0;
        int mobCount = 0;

        for (World world : MobStacker2.getPlugin().getServer().getWorlds()) {

            for (Entity e : world.getEntities()) {

                if (e instanceof LivingEntity && MetaTags.hasMetaData((LivingEntity) e) && MetaTags.getQuantity((LivingEntity) e) > 1) {

                    stackCount++;
                    mobCount += MetaTags.getQuantity((LivingEntity) e);
                    if (remove) e.remove();

                }

            }

        }

        return mobCount + "-" + stackCount;

    }

    /*
    Has 1/20th of a second delay bc mythic mobs slow af
     */
    public static boolean isMythicMob(LivingEntity entity) {

        if (MobStacker2.usesMythicMobs() && MetaTags.hasMetaData(entity) && !MetaTags.getSpawnReason(entity).equals("MYTHIC_MOBS"))
            return false;


        /*
        Only check the registry as a last resort bc it takes ages to update.
         */
        if (MythicMobs.inst().getMobManager().getMythicMobInstance(entity) != null) {
            return true;
        }

        return false;
    }

}
