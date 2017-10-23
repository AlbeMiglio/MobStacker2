package com.kiwifisher.mobstacker2.listeners;

import com.kiwifisher.mobstacker2.MobStacker2;
import com.kiwifisher.mobstacker2.io.Settings;
import com.kiwifisher.mobstacker2.loot.AlgorithmEnum;
import com.kiwifisher.mobstacker2.loot.LootAlgorithm;
import com.kiwifisher.mobstacker2.metadata.MetaTags;
import com.kiwifisher.mobstacker2.util.Util;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobDeathListener implements Listener {

    @EventHandler
    public void entityDeath(EntityDeathEvent event) {

        LivingEntity deadEntity = event.getEntity();

        if (deadEntity.getLastDamageCause() != null && deadEntity.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
            return;
        }

        if (MetaTags.hasMetaData(event.getEntity())) {

            MetaTags.removeName(deadEntity);
            int stackSize = MetaTags.getQuantity(deadEntity);
            int newQuantity = stackSize - 1;
            String spawnReason = MetaTags.getSpawnReason(deadEntity);
            boolean canStack = MetaTags.isStacking(deadEntity);
            boolean noMcMMOXP = MetaTags.isNoMcMMO(deadEntity);



            /*
            If we have any left then spawn in a replacement
             */
            if (newQuantity > 0) {

                if (Settings.KILL_WHOLE_STACK_ON_DEATH && deadEntity.getLastDamageCause() != null &&
                        Settings.KILL_FULL_STACK_REASONS
                                .contains(deadEntity
                                        .getLastDamageCause()
                                        .getCause()
                                        .name())) {

                    if (Settings.MULTIPLY_LOOT) {

                        try {
                            LootAlgorithm algorithm = AlgorithmEnum.valueOf(deadEntity.getType().name()).getLootAlgorithm();
                            Player player = event.getEntity().getKiller();
                            int looting = player != null ? player.getInventory().getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) : 0;
                            event.getDrops().addAll(algorithm.getRandomLoot(deadEntity, stackSize - 1, player != null, looting));

                            if (event.getDroppedExp() > 0) {
                                event.setDroppedExp(algorithm.getExp(deadEntity, stackSize));
                            }
                        } catch (IllegalArgumentException e) {
                            event.setDroppedExp(event.getDroppedExp() * stackSize);
                            // Rather than print a stack trace, warn about unimplemented loot.
                            MobStacker2.log(deadEntity.getType().name() + " doesn't have proportionate loot implemented - please request it be added if you need it");
                            e.printStackTrace();
                        }

                    }

                    return;

                }

                MobStacker2.BLACKLISTED_CHUNKS.add(deadEntity.getLocation().getChunk());

                LivingEntity newEntity = (LivingEntity) deadEntity.getWorld().spawnEntity(deadEntity.getLocation(), deadEntity.getType());
                Util.cloneAttributes(newEntity, deadEntity);
                MetaTags.setQuantity(newEntity, newQuantity);

                if (MobStacker2.BLACKLISTED_CHUNKS.contains(deadEntity.getLocation().getChunk())) {
                    MobStacker2.BLACKLISTED_CHUNKS.remove(deadEntity.getLocation().getChunk());

                }

                if (Settings.CARRY_OVER_FIRE && (
                        deadEntity.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE || deadEntity.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)) {

                    if (Settings.START_NEW_BURN) {

                        newEntity.setFireTicks(Settings.NEW_BURN_TICKS);

                    } else {

                        newEntity.setFireTicks(deadEntity.getFireTicks());
                    }
                }

                if (newQuantity > 1) MetaTags.updateName(newEntity);
                else MetaTags.removeName(newEntity);

            }

        }

    }

}
