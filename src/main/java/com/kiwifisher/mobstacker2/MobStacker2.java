package com.kiwifisher.mobstacker2;

import com.google.common.io.ByteStreams;
import com.kiwifisher.mobstacker2.commands.Commands;
import com.kiwifisher.mobstacker2.io.Settings;
import com.kiwifisher.mobstacker2.listeners.*;
import com.kiwifisher.mobstacker2.util.Util;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

public class MobStacker2 extends JavaPlugin {

    public final static String RELOAD_UUID = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    public static String LAST_UUID;
    public static boolean IS_STACKING = true;
    public static ArrayList<Chunk> BLACKLISTED_CHUNKS;
    public static WorldGuardPlugin worldGuardPlugin;
    public static com.gmail.nossr50.mcMMO mcMMOPlugin;
    public static boolean DEBUG = false;
    private static Plugin plugin;
    private static Logger logger;
    private static boolean usesMythicMobs;
    private static boolean usesClearLag;
    private final YamlConfiguration uuidYaml = new YamlConfiguration();

    public static boolean usesWG() {
        return worldGuardPlugin != null;
    }

    public static boolean usesMcMMO() {
        return mcMMOPlugin != null;
    }

    public static boolean usesMythicMobs() {
        return usesMythicMobs;
    }

    public static void log(String info) {
        logger.info(info);
    }

    public static void broadcast(String message) {
        Bukkit.broadcastMessage(message);
    }

    public static void debug(String message) {

        if (DEBUG) {

            Player kiwi = getPlugin().getServer().getPlayer("KiwiFisher");
            if (kiwi != null) {
                kiwi.sendMessage(message);
            }

        }

    }

    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        logger = getLogger();
        worldGuardPlugin = getWorldGuard();
        mcMMOPlugin = getMcMMOPlugin();
        usesMythicMobs = getServer().getPluginManager().isPluginEnabled("MythicMobs");
        usesClearLag = getServer().getPluginManager().isPluginEnabled("ClearLag");

        Settings.reload();

        loadFiles();
        registerListeners();
        registerCommands();

        BLACKLISTED_CHUNKS = new ArrayList<>();
        LAST_UUID = this.uuidYaml.getString("session-UUID");

        if (usesMcMMO()) {
            log("Hooked in to mcMMO");
        }

        if (usesWG()) {
            log("Hooked in to WorldGuard");
        }

        if (usesMythicMobs()) {
            log("Hooked in to MythicMobs");
        }

        if (usesClearLag) {
            log("Hooked in to ClearLag");
        }

        Util.reviveAllStacks();

    }

    @Override
    public void onDisable() {

        Util.serialiseAllStacks();
        this.uuidYaml.set("session-UUID", RELOAD_UUID);

        try {
            this.uuidYaml.save(getDataFolder().getAbsolutePath() + "/uuid.yml");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private com.gmail.nossr50.mcMMO getMcMMOPlugin() {

        Plugin plugin = getServer().getPluginManager().getPlugin("mcMMO");

        if (plugin == null || !(plugin instanceof com.gmail.nossr50.mcMMO)) {
            return null;
        }
        return (com.gmail.nossr50.mcMMO) plugin;
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

    private void registerCommands() {

        getCommand("mobstacker").setExecutor(new Commands());

    }

    private void registerListeners() {

        plugin.getServer().getPluginManager().registerEvents(new MobSpawnListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new MobDeathListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new MobRenameListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new MobExplodeListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new MobShearListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new MobTameListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new MobTargetListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new MobLeashListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new ChunkListeners(), this);
        plugin.getServer().getPluginManager().registerEvents(new EggLayListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new SheepDyeListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new WoolRegrowListener(), this);

        if (usesClearLag) {

            plugin.getServer().getPluginManager().registerEvents(new ClearLaggListener(), this);

        }

        if (usesMythicMobs) {

            plugin.getServer().getPluginManager().registerEvents(new MythicSpawnListener(), this);

        }

    }

    public void loadFiles() {

        loadResource(this, "config.yml");
        try {
            this.uuidYaml.load(loadResource(this, "uuid.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        File blacklistFile = new File(getDataFolder() + File.separator + "blacklisted-regions.txt");
        if (!blacklistFile.exists()) {

            try {
                blacklistFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);

        try {
            if (!resourceFile.exists() && resourceFile.createNewFile()) {
                try (InputStream in = plugin.getResource(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resourceFile;
    }
}
