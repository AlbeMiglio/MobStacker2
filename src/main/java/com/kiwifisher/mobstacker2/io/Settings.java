package com.kiwifisher.mobstacker2.io;

import com.kiwifisher.mobstacker2.MobStacker2;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Settings {

    public static ArrayList<String> BLACKLISTED_REGIONS = getBlackRegions();
    private static FileConfiguration config = MobStacker2.getPlugin().getConfig();
    public static String NAME_TEMPLATE = config.getString("stack-naming");
    public static int X_RANGE = config.getInt("stack-range.x");
    public static int Y_RANGE = config.getInt("stack-range.y");
    public static int Z_RANGE = config.getInt("stack-range.z");
    public static int SECONDS_TO_TRY_STACK = config.getInt("seconds-to-try-stack");
    public static boolean STACK_NAMED_MOBS = config.getBoolean("stack-custom-named-mobs");
    public static boolean STACK_BY_AGE = config.getBoolean("stack-by-age");
    public static boolean STACK_LEASHED_ANIMALS = config.getBoolean("stack-leased-animals");
    public static boolean PROTECT_TAMED_PETS = config.getBoolean("protect-tamed");
    public static boolean KILL_WHOLE_STACK_ON_DEATH = config.getBoolean("kill-whole-stack-on-death.enable");
    public static boolean MULTIPLY_LOOT = config.getBoolean("kill-whole-stack-on-death.multiply-loot");
    public static List<String> KILL_FULL_STACK_REASONS = config.getStringList("kill-whole-stack-on-death.reasons");
    public static boolean CREEPER_KILLS_STACK = config.getBoolean("exploding-creeper-kills-stack");
    public static boolean MAGNIFY_CREEPER_EXPLOSION = config.getBoolean("magnify-stack-explosion.enable");
    public static int MAX_EXPLOSION_SIZE = config.getInt("magnify-stack-explosion.max-creeper-explosion-size");
    public static boolean STACK_MOBS_DOWN = config.getBoolean("stack-mobs-down.enable");
    public static List<String> MOBS_TO_STACK_DOWN = config.getStringList("stack-mobs-down.mob-types");
    public static boolean SEPARATE_BY_COLOUR = config.getBoolean("separate-stacks-by-color");
    public static boolean LEASH_FULL_STACK = config.getBoolean("leash-whole-stack");
    public static boolean SEPARATE_BY_SHEARED = config.getBoolean("separate-by-sheared");
    public static boolean CARRY_OVER_FIRE = config.getBoolean("carry-over-fire.enabled");
    public static boolean START_NEW_BURN = config.getBoolean("carry-over-fire.start-new-burn");
    public static int NEW_BURN_TICKS = config.getInt("carry-over-fire.new-burn-ticks");
    public static HashMap<String, Integer> MAX_STACK_MOBS = getMaxStacks();
    public static List<String> BLACKLISTED_WORLDS = config.getStringList("blacklist-world");
    public static List<String> NERF_SPAWN_REASONS = config.getStringList("mob-nerfing");
    public static List<String> NO_MCMMO_REASONS = config.getStringList("no-mcmmo-exp");
    public static boolean RELOAD_STACKS = config.getBoolean("load-existing-stacks.enabled");
    public static List<String> RELOAD_STACKS_TYPES = config.getStringList("load-existing-stacks.mob-types");
    public static List<String> STACKING_TYPES = getMobTypes();
    public static List<String> SPAWN_TYPES = getSpawnReasons();
    public static HashMap<String, Integer> MAX_PER_CHUNK = getMaxPerChunk();
    public static boolean STACK_THROUGH_WALLS = config.getBoolean("stack-through-walls");
    public static boolean FULL_STACK_LOOTING = config.getBoolean("kill-whole-stack-on-death.looting");
    public static boolean CLEAR_LAG = config.getBoolean("clear-lag");

    private static ArrayList<String> getBlackRegions() {

        ArrayList<String> blackRegions = new ArrayList<>();

        File inputFile = new File(MobStacker2.getPlugin().getDataFolder() + File.separator + "blacklisted-regions.txt");

        try {
            if (!inputFile.exists()) inputFile.createNewFile();

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                blackRegions.add(trimmedLine);
            }

        } catch (IOException ignored) {
        }

        return blackRegions;

    }

    private static HashMap<String, Integer> getMaxStacks() {
        HashMap<String, Integer> hash = new HashMap<>();

        for (String mob : config.getConfigurationSection("max-stack-sizes").getKeys(false)) {
            hash.put(mob, config.getInt("max-stack-sizes." + mob));
        }
        return hash;
    }

    private static List<String> getMobTypes() {

        List<String> mobs = new ArrayList<>();

        for (String mob : config.getConfigurationSection("stack-mob-type").getKeys(false)) {

            if (config.getBoolean("stack-mob-type." + mob)) {
                mobs.add(mob);
            }

        }

        return mobs;

    }

    private static HashMap<String, Integer> getMaxPerChunk() {

        HashMap<String, Integer> limitsHash = new HashMap<>();

        for (String mob : config.getConfigurationSection("max-mobs-per-chunk").getKeys(false)) {

            limitsHash.put(mob, config.getInt("max-mobs-per-chunk." + mob));

        }

        return limitsHash;

    }

    private static List<String> getSpawnReasons() {

        List<String> reasons = new ArrayList<>();

        for (String reason : config.getConfigurationSection("stack-spawn-method").getKeys(false)) {

            if (config.getBoolean("stack-spawn-method." + reason)) {
                reasons.add(reason);
            }

        }

        return reasons;

    }


    public static void reload() {
        MobStacker2.getPlugin().reloadConfig();
        config = MobStacker2.getPlugin().getConfig();

        NAME_TEMPLATE = config.getString("stack-naming");
        X_RANGE = config.getInt("stack-range.x");
        Y_RANGE = config.getInt("stack-range.y");
        Z_RANGE = config.getInt("stack-range.z");
        SECONDS_TO_TRY_STACK = config.getInt("seconds-to-try-stack");
        STACK_NAMED_MOBS = config.getBoolean("stack-custom-named-mobs");
        STACK_BY_AGE = config.getBoolean("stack-by-age");
        STACK_LEASHED_ANIMALS = config.getBoolean("stack-leashed-animals");
        PROTECT_TAMED_PETS = config.getBoolean("protect-tamed");
        KILL_WHOLE_STACK_ON_DEATH = config.getBoolean("kill-whole-stack-on-death.enable");
        MULTIPLY_LOOT = config.getBoolean("kill-whole-stack-on-death.multiply-loot");
        KILL_FULL_STACK_REASONS = config.getStringList("kill-whole-stack-on-death.reasons");
        CREEPER_KILLS_STACK = config.getBoolean("exploding-creeper-kills-stack");
        MAGNIFY_CREEPER_EXPLOSION = config.getBoolean("magnify-stack-explosion.enable");
        MAX_EXPLOSION_SIZE = config.getInt("magnify-stack-explosion.max-creeper-explosion-size");
        STACK_MOBS_DOWN = config.getBoolean("stack-mobs-down.enable");
        MOBS_TO_STACK_DOWN = config.getStringList("stack-mobs-down.mob-types");
        SEPARATE_BY_COLOUR = config.getBoolean("separate-stacks-by-color");
        LEASH_FULL_STACK = config.getBoolean("leash-whole-stack");
        SEPARATE_BY_SHEARED = config.getBoolean("separate-by-sheared");
        CARRY_OVER_FIRE = config.getBoolean("carry-over-fire.enabled");
        START_NEW_BURN = config.getBoolean("carry-over-fire.start-new-burn");
        NEW_BURN_TICKS = config.getInt("carry-over-fire.new-burn-ticks");
        MAX_STACK_MOBS = getMaxStacks();
        BLACKLISTED_WORLDS = config.getStringList("blacklist-world");
        NERF_SPAWN_REASONS = config.getStringList("mob-nerfing");
        NO_MCMMO_REASONS = config.getStringList("no-mcmmo-exp");
        RELOAD_STACKS = config.getBoolean("load-existing-stacks.enabled");
        RELOAD_STACKS_TYPES = config.getStringList("load-existing-stacks.mob-types");
        STACKING_TYPES = getMobTypes();
        SPAWN_TYPES = getSpawnReasons();
        MAX_PER_CHUNK = getMaxPerChunk();
        STACK_THROUGH_WALLS = config.getBoolean("stack-through-walls");
        BLACKLISTED_REGIONS = getBlackRegions();
        CLEAR_LAG = config.getBoolean("clear-lag");
        FULL_STACK_LOOTING = config.getBoolean("kill-whole-stack-on-death.looting");
    }


}
