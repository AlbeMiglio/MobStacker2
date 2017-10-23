package com.kiwifisher.mobstacker2.commands;

import com.kiwifisher.mobstacker2.MobStacker2;
import com.kiwifisher.mobstacker2.commands.permissions.Permissions;
import com.kiwifisher.mobstacker2.io.Settings;
import com.kiwifisher.mobstacker2.metadata.MetaTags;
import com.kiwifisher.mobstacker2.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player) {

            /*
            Player Commands
             */
            Player player = (Player) commandSender;

            /*
            If it is a mobstacker command then follow
             */
            if (command.getLabel().toLowerCase().equalsIgnoreCase("mobstacker")) {

                if (args.length == 1 && args[0].equalsIgnoreCase("inspect")) {

                    if (!player.hasPermission(Permissions.INSPECT)) {
                        player.sendMessage(ChatColor.RED + "You don't have access to that command");
                        return true;
                    }

                    inspectCommand(player);
                    return true;

                } else if (args.length == 2 && args[0].equalsIgnoreCase("stacking")) {

                    if (!player.hasPermission(Permissions.CAN_STACK)) {
                        player.sendMessage(ChatColor.RED + "You don't have access to that command");
                        return true;
                    }

                    canStackCommand(player, args);
                    return true;

                } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

                    if (!player.hasPermission(Permissions.RELOAD)) {
                        player.sendMessage(ChatColor.RED + "You don't have access to that command");
                        return true;
                    }

                    player.sendMessage(ChatColor.GREEN + "Reloaded the config for MobStacker");
                    Settings.reload();
                    return true;

                } else if (args.length == 2 && args[0].equalsIgnoreCase("quantity") && args[1].matches("[0-9]+")) {

                    if (!player.hasPermission(Permissions.SET_QUANTITY)) {
                        player.sendMessage(ChatColor.RED + "You don't have access to that command");
                        return true;
                    }

                    int quantity = new Integer(args[1]);

                    setQuantityCommand(player, quantity);
                    return true;

                } else if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {

                    if (!player.hasPermission(Permissions.CLEAR)) {
                        player.sendMessage(ChatColor.RED + "You don't have access to that command");
                        return true;
                    }

                    String result = Util.countAllStacks(true);
                    String[] resultArray = result.split("-");

                    player.sendMessage(ChatColor.YELLOW + resultArray[0] + " mobs were removed across " + resultArray[1] + " stack" + (new Integer(resultArray[1]) > 1 ? "s" : ""));
                    return true;

                } else if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {

                    if (!player.hasPermission(Permissions.TOGGLE_STACKING)) {
                        player.sendMessage(ChatColor.RED + "You don't have access to that command");
                        return true;
                    }

                    player.sendMessage(ChatColor.YELLOW + "Mob stacking is now " + (toggleStacking() ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
                    return true;

                } else if (args.length == 1 && args[0].equalsIgnoreCase("trystack")) {

                    if (!player.hasPermission(Permissions.TRY_STACK)) {
                        player.sendMessage(ChatColor.RED + "You don't have access to that command");
                        return true;
                    }

                    LivingEntity selectedEntity = CommandUtil.getNearestEntityInSight(player, 30);

                    if (selectedEntity == null) {

                        player.sendMessage(ChatColor.RED + "No mob stack was selected. Please look at the stack and try again");

                    } else if (MetaTags.hasMetaData(selectedEntity)) {

                        Util.tryStack(selectedEntity);
                        player.sendMessage(ChatColor.YELLOW + "Trying to re-stack this mob");

                    }

                    return true;

                } else if (args.length == 3 && args[0].equalsIgnoreCase("region")) {

                    if (!MobStacker2.usesWG() && (player.hasPermission(Permissions.ADD_REGION) || player.hasPermission(Permissions.REMOVE_REGION))) {
                        player.sendMessage(ChatColor.RED + "WorldGuard could not be hooked in to. Please check you have it installed properly");
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("add")) {

                        if (!player.hasPermission(Permissions.ADD_REGION)) {
                            player.sendMessage(ChatColor.RED + "You don't have access to that command");
                            return true;
                        }

                        String regionID = args[2];

                        player.sendMessage((CommandUtil.addBlackRegion(regionID) ? ChatColor.GREEN + "Region " + "\"" + regionID + "\"" + " was added to the blacklist" :
                                ChatColor.RED + "Region \"" + regionID + "\"" + " doesn't exist or is already blacklisted!"));
                        return true;

                    }
                    if (args[1].equalsIgnoreCase("remove")) {

                        if (!player.hasPermission(Permissions.REMOVE_REGION)) {
                            player.sendMessage(ChatColor.RED + "You don't have access to that command");
                            return true;
                        }

                        String regionID = args[2];

                        player.sendMessage((CommandUtil.removeBlackRegion(regionID) ? ChatColor.GREEN + "Region " + "\"" + regionID + "\"" + " was removed from the blacklist" :
                                ChatColor.RED + "Region \"" + regionID + "\"" + " doesn't exist or isn't blacklisted"));
                        return true;

                    }

                } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {

                    if (!player.hasPermission(Permissions.HELP)) {
                        player.sendMessage(ChatColor.RED + "You don't have access to that command");
                        return true;
                    }

                    String helpString = "";

                    helpString += ChatColor.YELLOW + "Aliases: " + ChatColor.GREEN + "mobstacker, mstacker, ms" + "\n";
                    if (player.hasPermission(Permissions.TOGGLE_STACKING))
                        helpString += ChatColor.YELLOW + "Toggle: " + ChatColor.GREEN + "/mobstacker toggle" + "\n";
                    if (player.hasPermission(Permissions.RELOAD))
                        helpString += ChatColor.YELLOW + "Reload: " + ChatColor.GREEN + "/mobstacker reload" + "\n";
                    if (player.hasPermission(Permissions.INSPECT))
                        helpString += ChatColor.YELLOW + "Inspect: " + ChatColor.GREEN + "/mobstacker inspect" + "\n";
                    if (player.hasPermission(Permissions.CAN_STACK))
                        helpString += ChatColor.YELLOW + "Stacking: " + ChatColor.GREEN + "/mobstacker stacking <true || false>" + "\n";
                    if (player.hasPermission(Permissions.SET_QUANTITY))
                        helpString += ChatColor.YELLOW + "Quantity: " + ChatColor.GREEN + "/mobstacker quantity <qty>" + "\n";
                    if (player.hasPermission(Permissions.CLEAR))
                        helpString += ChatColor.YELLOW + "Clear: " + ChatColor.GREEN + "/mobstacker clear" + "\n";
                    if (player.hasPermission(Permissions.TRY_STACK))
                        helpString += ChatColor.YELLOW + "Try Stack Again: " + ChatColor.GREEN + "/mobstacker trystack" + "\n";
                    if (MobStacker2.usesWG())
                        helpString += ChatColor.YELLOW + "Regions: " + ChatColor.GREEN + "/mobstacker region <add || remove> <regionName>" + "\n";

                    player.sendMessage(helpString);
                    return true;


                } else if (args.length == 1 && args[0].equalsIgnoreCase("debug") && player.getUniqueId().toString().equals("4935d826-0325-4e25-951b-ae8231fcac6d")) {

                    MobStacker2.DEBUG = !MobStacker2.DEBUG;
                    player.sendMessage(MobStacker2.DEBUG ? ChatColor.GREEN + "Debug mode activated" : ChatColor.RED + "Debug mode exited");
                    return true;

                } else if (args.length == 1 && args[0].equalsIgnoreCase("stats")) {

                    if (!player.hasPermission(Permissions.STATS)) {
                        player.sendMessage(ChatColor.RED + "You don't have access to that command");
                        return true;
                    }

                    String result = Util.countAllStacks(false);
                    String[] resultArray = result.split("-");
                    int stacks = new Integer(resultArray[1]);
                    int mobCount = new Integer(resultArray[0]);

                    double percentSaving = ((float) (mobCount - stacks) / (float) mobCount) * 100;
                    String percentString = String.format("%.02f", percentSaving) + "%";

                    player.sendMessage(ChatColor.YELLOW + "Stacks: " + ChatColor.GREEN + stacks +
                            ChatColor.YELLOW + "\nMobs in stacks: " + ChatColor.GREEN + mobCount +
                            ChatColor.YELLOW + " \nEntity savings: " + ChatColor.GREEN + percentString);

                    return true;

                }

            }


        } else if (commandSender instanceof ConsoleCommandSender) {

            if (command.getLabel().equalsIgnoreCase("mobstacker")) {

                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    Settings.reload();
                    MobStacker2.log("Reloaded the config for MobStacker");
                    return true;

                } else if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {

                    MobStacker2.log("Mob stacking is now " + (toggleStacking() ? "enabled" : "disabled"));
                    return true;


                } else if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {

                    String result = Util.countAllStacks(true);
                    String[] resultArray = result.split("-");
                    int stacks = new Integer(resultArray[1]);

                    MobStacker2.log(resultArray[0] + " mobs were removed across " + resultArray[1] + " stack" + (stacks != 1 ? "s" : ""));
                    return true;

                }
            }
        }

        return false;
    }

    private boolean inspectCommand(Player player) {

        LivingEntity selectedEntity = CommandUtil.getNearestEntityInSight(player, 30);

        if (selectedEntity == null) {

            player.sendMessage(ChatColor.RED + "No mob stack was selected. Please look at the stack and try again");
            return false;

        } else {

            player.sendMessage(ChatColor.YELLOW + "Type: " + ChatColor.GREEN + selectedEntity.getType().name() + "\n" +
                    ChatColor.YELLOW + "Quantity: " + ChatColor.GREEN + MetaTags.getQuantity(selectedEntity) + "\n" +
                    ChatColor.YELLOW + "Spawn Reason: " + ChatColor.GREEN + MetaTags.getSpawnReason(selectedEntity) + "\n" +
                    ChatColor.YELLOW + "Is Stacking: " + (MetaTags.isStacking(selectedEntity) ? ChatColor.GREEN + "true" : ChatColor.RED + "false"));

            if (MobStacker2.usesMcMMO()) {

                player.sendMessage(ChatColor.YELLOW + "mcMMO XP: " + (MetaTags.isNoMcMMO(selectedEntity) ? ChatColor.RED + "false" : ChatColor.GREEN + "true"));

            }

            return true;

        }

    }

    private boolean setQuantityCommand(Player player, int quantity) {

        LivingEntity entity = CommandUtil.getNearestEntityInSight(player, 30);

        if (entity == null) {
            player.sendMessage(ChatColor.RED + "No mob stack was selected. Please look at the stack and try again");
            return false;
        }

        if (MetaTags.hasMetaData(entity)) {

            MetaTags.setQuantity(entity, quantity);
            player.sendMessage(ChatColor.GREEN + "New quantity set");
            return true;

        }
        return false;
    }

    private boolean canStackCommand(Player player, String[] args) {

        LivingEntity selectedEntity = CommandUtil.getNearestEntityInSight(player, 30);

        if (selectedEntity == null) {
            player.sendMessage(ChatColor.RED + "No mob stack was selected. Please look at the stack and try again");
            return true;
        }

        if (args[1].equalsIgnoreCase("true")) {
            MetaTags.setStacking(selectedEntity, true);

        } else if (args[1].equalsIgnoreCase("false")) {
            MetaTags.setStacking(selectedEntity, false);
        }

        player.sendMessage(MetaTags.isStacking(selectedEntity) ?
                ChatColor.YELLOW + "This " + selectedEntity.getType().name() + " stack is now stacking" :
                ChatColor.YELLOW + "This " + selectedEntity.getType().name() + " stack is no longer stacking");

        return true;

    }

    private boolean toggleStacking() {

        MobStacker2.IS_STACKING = !MobStacker2.IS_STACKING;
        return MobStacker2.IS_STACKING;
    }
}
