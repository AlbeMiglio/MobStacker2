package com.kiwifisher.mobstacker2.loot;

import com.kiwifisher.mobstacker2.loot.creatures.*;

public enum AlgorithmEnum {

    BAT(EmptyAlgorithm.getInstance()),
    BLAZE(new BlazeLootAlgorithm()),
    CAVE_SPIDER(new SpiderLootAlgorithm()),
    CHICKEN(new ChickenLootAlgorithm()),
    COW(new CowLootAlgorithm()),
    CREEPER(new CreeperLootAlgorithm()),
    ENDERMAN(new EndermanLootAlgorithm()),
    ENDERMITE(new ExperienceLootAlgorithm(3, 3)),
    GHAST(new GhastLootAlgorithm()),
    // Giants always drop 5 experience.
    GIANT(new ExperienceLootAlgorithm(5, 5)),
    GUARDIAN(new GuardianLootAlgorithm()),
    HORSE(new HorseLootAlgorithm()),
    IRON_GOLEM(new IronGolemLootAlgorithm()),
    MAGMA_CUBE(new MagmaCubeLootAlgorithm()),
    // Cows and mooshrooms have the same drops.
    MUSHROOM_COW(COW.lootAlgorithm),
    // Ocelots have no drops, the animal algorithm base works.
    OCELOT(new AnimalLootAlgorithm()),
    PIG(new PigLootAlgorithm()),
    PIG_ZOMBIE(new PigZombieLootAlgorithm()),
    POLAR_BEAR(new PolarBearLootAlgorithm()),
    RABBIT(new RabbitLootAlgorithm()),
    SHEEP(new SheepLootAlgorithm()),
    // Silverfish and shulkers always drop 5 experience.
    SILVERFISH(GIANT.lootAlgorithm),
    SHULKER(GIANT.lootAlgorithm),
    SKELETON(new SkeletonLootAlgorithm()),
    SLIME(new SlimeLootAlgorithm()),
    SNOW_GOLEM(new SnowGolemLootAlgorithm()),
    // Spiders and cave spiders have the same drops.
    SPIDER(CAVE_SPIDER.lootAlgorithm),
    SQUID(new SquidLootAlgorithm()),
    VILLAGER(EmptyAlgorithm.getInstance()),
    WITCH(new WitchLootAlgorithm()),
    WITHER(new WitherLootAlgorithm()),
    // Wolves have no drops, the animal algorithm base works.
    WOLF(OCELOT.lootAlgorithm),
    ZOMBIE(new ZombieLootAlgorithm());

    private final LootAlgorithm lootAlgorithm;

    AlgorithmEnum(LootAlgorithm lootAlgorithm) {
        this.lootAlgorithm = lootAlgorithm;
    }

    public LootAlgorithm getLootAlgorithm() {
        return this.lootAlgorithm;
    }

}
