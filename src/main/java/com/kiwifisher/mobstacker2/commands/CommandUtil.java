package com.kiwifisher.mobstacker2.commands;

import com.kiwifisher.mobstacker2.MobStacker2;
import com.kiwifisher.mobstacker2.io.Settings;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class CommandUtil {

    public static LivingEntity getNearestEntityInSight(Player player, int range) {

        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight((Set<Material>) null, range);

        ArrayList<Location> sight = new ArrayList<>();

        for (Block aSightBlock : sightBlock) sight.add(aSightBlock.getLocation());

        for (Location aSight : sight) {

            for (Entity entity : entities) {

                if (!(entity instanceof LivingEntity)) continue;

                if (Math.abs(entity.getLocation().getX() - aSight.getX()) < 1.3) {
                    if (Math.abs(entity.getLocation().getY() - aSight.getY()) < 1.5) {
                        if (Math.abs(entity.getLocation().getZ() - aSight.getZ()) < 1.3) {
                            return (LivingEntity) entity;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean addBlackRegion(String regionID) {

        for (World world : MobStacker2.getPlugin().getServer().getWorlds()) {

            RegionManager regionManager = MobStacker2.worldGuardPlugin.getRegionManager(world);

            if (regionManager.hasRegion(regionID) && !Settings.BLACKLISTED_REGIONS.contains(regionID)) {

                Settings.BLACKLISTED_REGIONS.add(regionID);

                File blackFile = new File(MobStacker2.getPlugin().getDataFolder() + File.separator + "blacklisted-regions.txt");

                try {

                    if (!blackFile.exists()) {
                        blackFile.createNewFile();
                    }

                    BufferedReader reader = new BufferedReader(new FileReader(blackFile));
                    boolean hasRegion = false;

                    String line;
                    while ((line = reader.readLine()) != null) {

                        if (line.equalsIgnoreCase(regionID)) {

                            hasRegion = true;
                            break;

                        }

                    }
                    reader.close();
                    if (hasRegion) return false;

                    BufferedWriter writer = new BufferedWriter(new FileWriter(blackFile, true));
                    writer.write(regionID + "\n");
                    writer.close();

                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        return false;

    }

    public static boolean removeBlackRegion(String regionID) {

        boolean wasRemoved = false;

        try {

            File inputFile = new File(MobStacker2.getPlugin().getDataFolder() + File.separator + "blacklisted-regions.txt");
            File tempFile = new File(MobStacker2.getPlugin().getDataFolder() + File.separator + "temp.txt");

            if (!inputFile.exists()) {
                inputFile.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            Settings.BLACKLISTED_REGIONS.clear();

            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if (trimmedLine.equalsIgnoreCase(regionID)) {
                    Settings.BLACKLISTED_REGIONS.remove(trimmedLine);
                    wasRemoved = true;
                    continue;
                }
                Settings.BLACKLISTED_REGIONS.add(trimmedLine);
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);

        } catch (IOException e) {
            MobStacker2.log("There was a problem editing the text files. Try checking file permissions for the system");
            e.printStackTrace();
        }

        return wasRemoved;

    }

}
