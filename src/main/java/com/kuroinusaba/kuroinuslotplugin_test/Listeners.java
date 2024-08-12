package com.kuroinusaba.kuroinuslotplugin_test;

import net.milkbowl.vault.economy.Economy;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.kuroinusaba.kuroinuslotplugin_test.KuroinuSlotPlugin_Test.econ;
import static com.kuroinusaba.kuroinuslotplugin_test.KuroinuSlotPlugin_Test.plugin;
import static java.lang.Math.round;

public class Listeners implements Listener {
    String prefix = plugin.getConfig().getString("prefix");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
        YamlConfiguration dataconfig = YamlConfiguration.loadConfiguration(data);
        if (!data.exists()) {
            dataconfig.set("slot", 0);
        }
        try {
            dataconfig.save(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
        YamlConfiguration dataconfig = YamlConfiguration.loadConfiguration(data);
        if (!data.exists()) {
            dataconfig.set("slot", 0);
        }
        try {
            dataconfig.save(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
        YamlConfiguration dataconfig = YamlConfiguration.loadConfiguration(data);
        if (!data.exists()) {
            return;
        }
        try {
            dataconfig.save(data);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if (e.getBlock().getType().toString().equals("LEVER")) {
            if (dataconfig.getInt("slot") == 4) {
                player.sendMessage(prefix + "§a§lスロットを設置しました。");
                player.sendMessage(prefix + "§a§l次に看板を設置してください。");
                File placedslot = new File("plugins/KuroinuSlotPlugin_Test/placedslot/" + dataconfig.getString("slotname") + ".yml");
                YamlConfiguration placedslotconfig = YamlConfiguration.loadConfiguration(placedslot);
                placedslotconfig.set("lever", e.getBlock().getLocation());
                try {
                    placedslotconfig.save(placedslot);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    dataconfig.set("slot", 5);
                    dataconfig.save(data);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (e.getBlock().getType().toString().equals("OAK_SIGN") || e.getBlock().getType().toString().equals("OAK_WALL_SIGN")) {
            if (dataconfig.getInt("slot") == 5) {
                player.sendMessage(prefix + "§a§l看板を設置しました。");
                player.sendMessage(prefix + "§e§lスロットが完成しました！");
                File placedslot = new File("plugins/KuroinuSlotPlugin_Test/placedslot/" + dataconfig.getString("slotname") + ".yml");
                YamlConfiguration placedslotconfig = YamlConfiguration.loadConfiguration(placedslot);
                placedslotconfig.set("sign", e.getBlock().getLocation());
                placedslotconfig.set("stock", dataconfig.getInt("defaultstock"));
                placedslotconfig.set("freespin", 0);
                Sign sign = (Sign) e.getBlock().getState();
                sign.setLine(0, "§a§l====================");
                sign.setLine(1, "§a§l[" + dataconfig.getString("displayname") + "§a§l]");
                sign.setLine(2, "§a§lストック: " + dataconfig.getInt("defaultstock"));
                sign.setLine(3, "§a§l====================");
                try {
                    placedslotconfig.save(placedslot);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    dataconfig.set("slot", 0);
                    dataconfig.set("pslot", "");
                    dataconfig.set("slotname", "");
                    dataconfig.save(data);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent e) {
        if (!(e.getEntity().getType().toString().equals("ITEM_FRAME"))) {
            return;
        }
        Player player = e.getPlayer();
        String frame = String.valueOf(e.getEntity());
        File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
        YamlConfiguration dataconfig = YamlConfiguration.loadConfiguration(data);
        if (!data.exists()) {
            return;
        }
        try {
            dataconfig.save(data);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if (dataconfig.getInt("slot") == 0) {
            if (!(player.hasPermission("admin"))) {
                return;
            }
            player.sendMessage(prefix + "§c§lスロットを作成してください。");
            player.sendMessage(prefix + "§c§lスロットを作成するには、/slot create <スロット名> <スロットの名前>を実行してください。");
            return;
        }
        if (dataconfig.getInt("slot") <= 3 && dataconfig.getInt("slot") >= 1) {
            player.sendMessage(prefix + "§a§l" + dataconfig.getInt("slot") + "§r§a番目の額縁を設置しました。");
            if (dataconfig.getInt("slot") == 3) {
                player.sendMessage(prefix + "§a§l次にレバーを設置してください。");
            }
            File placedslot = new File("plugins/KuroinuSlotPlugin_Test/placedslot/" + dataconfig.getString("slotname") + ".yml");
            YamlConfiguration placedslotconfig = YamlConfiguration.loadConfiguration(placedslot);
            placedslotconfig.set("itemframe" + dataconfig.getInt("slot"), e.getEntity().getLocation());
            try {
                placedslotconfig.save(placedslot);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                dataconfig.set("slot", dataconfig.getInt("slot") + 1);
                dataconfig.save(data);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType().toString().equals("LEVER")) {
                File playerdata = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
                YamlConfiguration playerdataconfig = YamlConfiguration.loadConfiguration(playerdata);
                File slot = new File("plugins/KuroinuSlotPlugin_Test/placedslot");
                for (File file : slot.listFiles()) {
                    YamlConfiguration slotconfig = YamlConfiguration.loadConfiguration(file);
                    if (slotconfig.get("lever") == null) {
                        continue;
                    }
                    if (slotconfig.get("lever").equals(e.getClickedBlock().getLocation())) {
                        if (slotconfig.get("sign") == null) {
                            player.sendMessage(prefix + "§c§lスロットが不正です。");
                            return;
                        }
                        if (slotconfig.get("itemframe1") == null) {
                            player.sendMessage(prefix + "§c§lスロットが不正です。");
                            return;
                        }
                        if (slotconfig.get("itemframe2") == null) {
                            player.sendMessage(prefix + "§c§lスロットが不正です。");
                            return;
                        }
                        if (slotconfig.get("itemframe3") == null) {
                            player.sendMessage(prefix + "§c§lスロットが不正です。");
                            return;
                        }
                        if (slotconfig.get("on") == null) {
                            slotconfig.set("on", false);
                            try {
                                slotconfig.save(file);
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            if (!(slotconfig.getBoolean("on") == true)) {
                                player.sendMessage(prefix + "§c§lスロットが起動していません。");
                                return;
                            }
                        }
                        File slotdata = new File("plugins/KuroinuSlotPlugin_Test/slots/" + slotconfig.getString("slotname") + ".yml");
                        YamlConfiguration slotdataconfig = YamlConfiguration.loadConfiguration(slotdata);
                        if (slotdataconfig.get("coin.type") == null) {
                            player.sendMessage(prefix + "§c§lスロットが不正です。");
                            return;
                        }
                        if (slotdataconfig.get("coin.amount") == null) {
                            player.sendMessage(prefix + "§c§lスロットが不正です。");
                            return;
                        }
                        if (slotdataconfig.get("coin.type").equals("item")) {
                            if (slotdataconfig.get("coin.item") == null) {
                                player.sendMessage(prefix + "§c§lスロットが不正です。");
                                return;
                            }
                        }
                        if (slotdataconfig.get("coin.type").equals("money")) {
                            // プレイヤーのお金が足りているか確認
                            if (econ.getBalance(player) < slotdataconfig.getInt("coin.amount")) {
                                player.sendMessage(prefix + "§c§lお金が足りません。");
                                return;
                            }
                            if (playerdataconfig.getBoolean("isPlaying")) {
                                return;
                            }
                            if (slotconfig.getInt("freespin") == 0) {
                                EconomyResponse withdraw = econ.withdrawPlayer(player, slotdataconfig.getInt("coin.amount"));
                                if (withdraw.transactionSuccess()) {
                                    player.sendMessage(String.format("§e%s円支払いました", econ.format(withdraw.amount)).replace("$", ""));
                                    playerdataconfig.set("isPlaying", true);
                                    try {
                                        playerdataconfig.save(playerdata);
                                    } catch (Exception ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    YamlConfiguration slotdata2 = YamlConfiguration.loadConfiguration(slotdata);
                                    File slotconfig2 = new File("plugins/KuroinuSlotPlugin_Test/placedslot/" + slotconfig.getString("slotname") + ".yml");
                                    YamlConfiguration slotconfig3 = YamlConfiguration.loadConfiguration(slotconfig2);
                                    roll(player, slotdata2, slotconfig, file);
                                } else {
                                    player.sendMessage(prefix + "§c§lお金を支払えませんでした。");
                                    return;
                                }
                            } else {
                                slotconfig.set("freespin", slotconfig.getInt("freespin") - 1);
                                try {
                                    slotconfig.save(file);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                                player.sendMessage(prefix + "§e§l無料でスロットを回します。");
                                playerdataconfig.set("isPlaying", true);
                                try {
                                    playerdataconfig.save(playerdata);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                                YamlConfiguration slotdata2 = YamlConfiguration.loadConfiguration(slotdata);
                                File slotconfig2 = new File("plugins/KuroinuSlotPlugin_Test/placedslot/" + slotconfig.getString("slotname") + ".yml");
                                YamlConfiguration slotconfig3 = YamlConfiguration.loadConfiguration(slotconfig2);
                                roll(player, slotdata2, slotconfig, file);
                            }
                        } else if (slotdataconfig.get("coin.type").equals("item")) {
                            ItemStack item = new ItemStack(Material.getMaterial(slotdataconfig.getString("coin.item")));
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(slotdataconfig.getString("coin.itemname"));
                            meta.setLore(Arrays.asList(slotdataconfig.getString("coin.itemlore")));
                            item.setItemMeta(meta);
                            if (player.getInventory().containsAtLeast(item, slotdataconfig.getInt("coin.amount"))) {
                                player.getInventory().removeItem(item);
                                player.sendMessage(prefix + "§e§l" + slotdataconfig.getInt("coin.amount") + "§r§e個の" + slotdataconfig.getString("coin.itemname") + "§r§eを支払いました。");
                            } else {
                                player.sendMessage(prefix + "§c§lアイテムを支払えませんでした。");
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void roll(Player player, YamlConfiguration slotdata, YamlConfiguration placedslotconfig, File placedslotdata) {
        File playerdata = new File("plugins/KuroinuSlotPlugin_Test/playerdata/" + player.getUniqueId() + ".yml");
        YamlConfiguration playerdataconfig = YamlConfiguration.loadConfiguration(playerdata);
        int symbolnum = slotdata.getConfigurationSection("symbol").getKeys(false).size();
        int slotnum = slotdata.getConfigurationSection("slot").getKeys(false).size();
        int tick = slotdata.getInt("tick");
        int delay = slotdata.getInt("delay");
        Location loc1 = placedslotconfig.getLocation("itemframe1");
        Location loc2 = placedslotconfig.getLocation("itemframe2");
        Location loc3 = placedslotconfig.getLocation("itemframe3");
        int bunbo = slotdata.getInt("bunbo");
        int percentage = round(bunbo / slotdata.getInt("percentage"));
        int random = new Random().nextInt(percentage) + 1;

        int frameroll1 = new Random().nextInt(symbolnum) + 1;
        int frameroll2 = new Random().nextInt(symbolnum) + 1;
        int frameroll3 = new Random().nextInt(symbolnum) + 1;
        int finalFrameroll1 = 0;
        int finalFrameroll2 = 0;
        int finalFrameroll3 = 0;
        if (random == 1) {
            int bunnsuu = 1;
            for (int i = 1; i <= slotnum; i++) {
                bunnsuu = bunnsuu + (100 / slotdata.getInt("slot." + i + ".percentage"));
            }
            // bunnsuuを四捨五入する
            bunnsuu = round(bunnsuu);
            int random2 = new Random().nextInt(bunnsuu);
            if (random2 == 0) {
                random2 = 1;
            }
            int kari = 0;
            for (int i = 1; i <= slotnum; i++) {
                if (kari < random2 && random2 <= kari + (100 / slotdata.getInt("slot." + i + ".percentage"))) {
                    List<Integer> list = slotdata.getIntegerList("slot." + i + ".pattern");
                    finalFrameroll1 = list.get(0);
                    finalFrameroll2 = list.get(1);
                    finalFrameroll3 = list.get(2);
                    kari = kari + (100 / slotdata.getInt("slot." + i + ".percentage"));
                } else {
                    random2 = random2 - round(100 / slotdata.getInt("slot." + i + ".percentage"));
                }
            }
        } else {
            frameroll3 = frameroll2 - 1;
            if (frameroll3 == 0) {
                frameroll3 = frameroll3 + 2;
            }
        }
        for (int i = 1; i <= tick; i++) {
            int finalI = i;
            int finalDelay = delay;
            int finalI1 = i;
            int finalRandom = random;
            int finalfinalFrameroll1 = finalFrameroll1;
            int finalfinalFrameroll2 = finalFrameroll2;
            int finalfinalFrameroll3 = finalFrameroll3;
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {

                    int frameroll1 = new Random().nextInt(symbolnum) + 1;
                    int frameroll2 = new Random().nextInt(symbolnum) + 1;
                    int frameroll3 = new Random().nextInt(symbolnum) + 1;
                    if (loc1.getNearbyEntities(0.5, 0.5, 0.5).size() == 0) {
                        if (player.hasPermission("admin")) {
                            player.sendMessage(prefix + "§c§l1§r§c番目のアイテムフレームが見つかりませんでした。");
                        }

                    } else {
                        List<Entity> entities = (List<Entity>) loc1.getNearbyEntities(0.5, 0.5, 0.5);
                        for (Entity entity : entities) {
                            if (entity instanceof ItemFrame) {
                                ItemFrame itemframe = (ItemFrame) entity;
                                ItemStack item1 = slotdata.getItemStack("symbol." + frameroll1);
                                itemframe.setItem(item1);
                            }
                        }
                        List<Entity> entities2 = (List<Entity>) loc2.getNearbyEntities(0.5, 0.5, 0.5);
                        for (Entity entity : entities2) {
                            if (entity instanceof ItemFrame) {
                                ItemFrame itemframe = (ItemFrame) entity;
                                ItemStack item2 = slotdata.getItemStack("symbol." + frameroll2);
                                itemframe.setItem(item2);
                            }
                        }
                        List<Entity> entities3 = (List<Entity>) loc3.getNearbyEntities(0.5, 0.5, 0.5);
                        for (Entity entity : entities3) {
                            if (entity instanceof ItemFrame) {
                                ItemFrame itemframe = (ItemFrame) entity;
                                ItemStack item3 = slotdata.getItemStack("symbol." + frameroll3);
                                itemframe.setItem(item3);
                            }
                        }
                        Sound sound = Sound.valueOf(slotdata.getString("rollsound"));
                        // ワールドに音を鳴らす
                        loc1.getWorld().playSound(loc1, sound, 1, 1);
                        List<String> actions = slotdata.getStringList("slot." + finalfinalFrameroll1 + ".action");
                        String name = slotdata.getString("slot." + finalfinalFrameroll1 + ".name");
                        int rewardmoney = slotdata.getInt("slot." + finalfinalFrameroll1 + ".rewardmoney");
                        if (finalI1 == tick) {
                            // finalFrameroll1がnullの場合
                            if (finalfinalFrameroll1 != 0) {
                                entities = (List<Entity>) loc1.getNearbyEntities(0.5, 0.5, 0.5);
                                for (Entity entity : entities) {
                                    if (entity instanceof ItemFrame) {
                                        ItemFrame itemframe = (ItemFrame) entity;
                                        ItemStack item1 = slotdata.getItemStack("symbol." + finalfinalFrameroll1);
                                        itemframe.setItem(item1);
                                    }
                                }
                                entities2 = (List<Entity>) loc2.getNearbyEntities(0.5, 0.5, 0.5);
                                for (Entity entity : entities2) {
                                    if (entity instanceof ItemFrame) {
                                        ItemFrame itemframe = (ItemFrame) entity;
                                        ItemStack item2 = slotdata.getItemStack("symbol." + finalfinalFrameroll2);
                                        itemframe.setItem(item2);
                                    }
                                }
                                entities3 = (List<Entity>) loc3.getNearbyEntities(0.5, 0.5, 0.5);
                                for (Entity entity : entities3) {
                                    if (entity instanceof ItemFrame) {
                                        ItemFrame itemframe = (ItemFrame) entity;
                                        ItemStack item3 = slotdata.getItemStack("symbol." + finalfinalFrameroll3);
                                        itemframe.setItem(item3);
                                    }
                                }
                            }
                        }
                    }
                }
            }, delay * i);
        }
        for (int i = 1; i <= tick; i++) {
            int finalI = i;
            int finalDelay = delay;
            int finalI1 = i;
            int finalRandom = random;
            int finalfinalFrameroll1 = finalFrameroll1;
            int finalfinalFrameroll2 = finalFrameroll2;
            int finalfinalFrameroll3 = finalFrameroll3;
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    int frameroll2 = new Random().nextInt(symbolnum) + 1;
                    int frameroll3 = new Random().nextInt(symbolnum) + 1;
                    if (loc1.getNearbyEntities(0.5, 0.5, 0.5).size() == 0) {
                        if (player.hasPermission("admin")) {
                            player.sendMessage(prefix + "§c§l1§r§c番目のアイテムフレームが見つかりませんでした。");
                        }

                    } else {
                        List<Entity> entities2 = (List<Entity>) loc2.getNearbyEntities(0.5, 0.5, 0.5);
                        for (Entity entity : entities2) {
                            if (entity instanceof ItemFrame) {
                                ItemFrame itemframe = (ItemFrame) entity;
                                ItemStack item2 = slotdata.getItemStack("symbol." + frameroll2);
                                itemframe.setItem(item2);
                            }
                        }
                        List<Entity> entities3 = (List<Entity>) loc3.getNearbyEntities(0.5, 0.5, 0.5);
                        for (Entity entity : entities3) {
                            if (entity instanceof ItemFrame) {
                                ItemFrame itemframe = (ItemFrame) entity;
                                ItemStack item3 = slotdata.getItemStack("symbol." + frameroll3);
                                itemframe.setItem(item3);
                            }
                        }
                        Sound sound = Sound.valueOf(slotdata.getString("rollsound"));
                        // ワールドに音を鳴らす
                        loc1.getWorld().playSound(loc1, sound, 1, 1);
                        List<String> actions = slotdata.getStringList("slot." + finalfinalFrameroll1 + ".action");
                        String name = slotdata.getString("slot." + finalfinalFrameroll1 + ".name");
                        int rewardmoney = slotdata.getInt("slot." + finalfinalFrameroll1 + ".rewardmoney");
                        if (finalI1 == tick) {
                            // finalFrameroll1がnullの場合
                            if (finalfinalFrameroll1 != 0) {
                                entities2 = (List<Entity>) loc2.getNearbyEntities(0.5, 0.5, 0.5);
                                for (Entity entity : entities2) {
                                    if (entity instanceof ItemFrame) {
                                        ItemFrame itemframe = (ItemFrame) entity;
                                        ItemStack item2 = slotdata.getItemStack("symbol." + finalfinalFrameroll2);
                                        itemframe.setItem(item2);
                                    }
                                }
                                entities3 = (List<Entity>) loc3.getNearbyEntities(0.5, 0.5, 0.5);
                                for (Entity entity : entities3) {
                                    if (entity instanceof ItemFrame) {
                                        ItemFrame itemframe = (ItemFrame) entity;
                                        ItemStack item3 = slotdata.getItemStack("symbol." + finalfinalFrameroll3);
                                        itemframe.setItem(item3);
                                    }
                                }
                            }
                        }
                    }
                }
            }, delay * i);
        }
        for (int i = 1; i <= tick; i++) {
            int finalI = i;
            int finalDelay = delay;
            int finalI1 = i;
            int finalRandom = random;
            int finalfinalFrameroll3 = finalFrameroll3;
            int finalFrameroll = finalFrameroll1;
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    int frameroll3 = new Random().nextInt(symbolnum) + 1;
                    int finalFrameroll3 = 0;
                    if (finalRandom == 1) {
                        int bunnsuu = 1;
                        for (int i = 1; i <= slotnum; i++) {
                            bunnsuu = bunnsuu + (100 / slotdata.getInt("slot." + i + ".percentage"));
                        }
                        // bunnsuuを四捨五入する
                        bunnsuu = round(bunnsuu);
                        int random2 = new Random().nextInt(bunnsuu);
                        if (random2 == 0) {
                            random2 = 1;
                        }
                        int kari = 0;
                        for (int i = 1; i <= slotnum; i++) {
                            if (kari < random2 && random2 <= kari + (100 / slotdata.getInt("slot." + i + ".percentage"))) {
                                List<Integer> list = slotdata.getIntegerList("slot." + i + ".pattern");
                                finalFrameroll3 = list.get(2);
                                kari = kari + (100 / slotdata.getInt("slot." + i + ".percentage"));
                            } else {
                                random2 = random2 - round(100 / slotdata.getInt("slot." + i + ".percentage"));
                            }
                        }
                    } else {
                        frameroll3 = frameroll2 - 1;
                        if (frameroll3 == 0) {
                            frameroll3 = frameroll3 + 2;
                        }
                    }
                    if (loc1.getNearbyEntities(0.5, 0.5, 0.5).size() == 0) {
                        if (player.hasPermission("admin")) {
                            player.sendMessage(prefix + "§c§l1§r§c番目のアイテムフレームが見つかりませんでした。");
                        }

                    } else {
                        List<Entity> entities3 = (List<Entity>) loc3.getNearbyEntities(0.5, 0.5, 0.5);
                        for (Entity entity : entities3) {
                            if (entity instanceof ItemFrame) {
                                ItemFrame itemframe = (ItemFrame) entity;
                                ItemStack item3 = slotdata.getItemStack("symbol." + frameroll3);
                                itemframe.setItem(item3);
                            }
                        }
                        Sound sound = Sound.valueOf(slotdata.getString("rollsound"));
                        // ワールドに音を鳴らす
                        loc1.getWorld().playSound(loc1, sound, 1, 1);
                        List<String> actions = slotdata.getStringList("slot." + finalFrameroll + ".action");
                        String name = slotdata.getString("slot." + finalFrameroll + ".name");
                        int rewardmoney = slotdata.getInt("slot." + finalFrameroll + ".rewardmoney");
                        if (finalI1 == tick) {
                            // finalFrameroll1がnullの場合
                            if (finalFrameroll != 0) {
                                entities3 = (List<Entity>) loc3.getNearbyEntities(0.5, 0.5, 0.5);
                                for (Entity entity : entities3) {
                                    if (entity instanceof ItemFrame) {
                                        ItemFrame itemframe = (ItemFrame) entity;
                                        ItemStack item3 = slotdata.getItemStack("symbol." + finalFrameroll3);
                                        itemframe.setItem(item3);
                                    }
                                }
                                player.sendMessage(prefix + "§e§lおめでとうございます！" + name + "§e§lです！");
                                for (String action : actions) {
                                    // sound:から始まる場合、そのサウンドを再生する
                                    if (action.contains("sound:")) {
                                        // soundname:soundpitchという形式の場合、soundnameをsoundとして、soundpitchをsoundpitchとして取得する
                                        String soundname = action.replace("sound:", "");
                                        float soundpitch = Float.parseFloat(soundname.split(":")[1]);
                                        soundname = soundname.split(":")[0];
                                        Sound sound2 = Sound.valueOf(soundname);
                                        loc2.getWorld().playSound(loc2, sound2, 1, soundpitch);
                                    }
                                    // stock:から始まる場合
                                    if (action.contains("stock:")) {
                                        String stock = action.replace("stock:", "");
                                        if (stock.contains("add")) {
                                            stock = stock.replace("add", "");
                                            int stocknum = Integer.parseInt(stock);
                                            int stocknum2 = placedslotconfig.getInt("stock");
                                            int stocknum3 = stocknum2 + stocknum;
                                            placedslotconfig.set("stock", stocknum3);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (stock.contains("remove")) {
                                            stock = stock.replace("remove", "");
                                            int stocknum = Integer.parseInt(stock);
                                            int stocknum2 = placedslotconfig.getInt("stock");
                                            int stocknum3 = stocknum2 - stocknum;
                                            placedslotconfig.set("stock", stocknum3);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (stock.contains("set")) {
                                            stock = stock.replace("set", "");
                                            int stocknum = Integer.parseInt(stock);
                                            placedslotconfig.set("stock", stocknum);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (stock.contains("reset")) {
                                            placedslotconfig.set("stock", 0);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (stock.contains("payout")) {
                                            int stocknum = placedslotconfig.getInt("stock");
                                            int newstocknum = slotdata.getInt("defaultstock");
                                            placedslotconfig.set("stock", newstocknum);
                                            rewardmoney = rewardmoney + stocknum;
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    // b:から始まる場合、そのメッセージを全員に送信する
                                    if (action.contains("b:")) {
                                        String message = action.replace("b:", "");
                                        message = message.replace("&", "§");
                                        message = message.replace("%player%", player.getName());
                                        message = message.replace("%rewardmoney%", rewardmoney + "");
                                        Bukkit.broadcastMessage(prefix + message);
                                    }
                                    // c:から始まる場合、そのコマンドを実行する
                                    if (action.contains("c:")) {
                                        String command = action.replace("c:", "");
                                        command = command.replace("%player%", player.getName());
                                        command = command.replace("%rewardmoney%", rewardmoney + "");
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                    }
                                    // s:から始まる場合、プレイヤーにメッセージを送信する
                                    if (action.contains("s:")) {
                                        String message = action.replace("s:", "");
                                        message = message.replace("&", "§");
                                        message = message.replace("%player%", player.getName());
                                        message = message.replace("%rewardmoney%", rewardmoney + "");
                                        player.sendMessage(prefix + message);
                                    }
                                    // slot:から始まる場合、そのスロットに切り替える
                                    if (action.contains("slot:")) {
                                        String slotname = action.replace("slot:", "");
                                        File slot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + slotname + ".yml");
                                        YamlConfiguration slotdata2 = YamlConfiguration.loadConfiguration(slot);
                                        if (slot.exists()) {
                                            placedslotconfig.set("slotname", slotname);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                                player.sendMessage(prefix + "§a§l" + slotname + "§f§lスロットに切り替えました。");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            player.sendMessage(prefix + "§c§l" + slotname + "§f§lスロットは存在しません。");
                                        }
                                    }
                                    // freespin:から始まる場合
                                    if (action.contains("freespin:")) {
                                        String freespin = action.replace("freespin:", "");
                                        if (freespin.contains("add")) {
                                            freespin = freespin.replace("add", "");
                                            int freespinnum = Integer.parseInt(freespin);
                                            int freespinnum2 = placedslotconfig.getInt("freespin");
                                            int freespinnum3 = freespinnum2 + freespinnum;
                                            placedslotconfig.set("freespin", freespinnum3);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (freespin.contains("remove")) {
                                            freespin = freespin.replace("remove", "");
                                            int freespinnum = Integer.parseInt(freespin);
                                            int freespinnum2 = placedslotconfig.getInt("freespin");
                                            int freespinnum3 = freespinnum2 - freespinnum;
                                            placedslotconfig.set("freespin", freespinnum3);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (freespin.contains("set")) {
                                            freespin = freespin.replace("set", "");
                                            int freespinnum = Integer.parseInt(freespin);
                                            placedslotconfig.set("freespin", freespinnum);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (freespin.contains("reset")) {
                                            placedslotconfig.set("freespin", 0);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        String variable = action.split(":")[0];
                                        String value = action.split(":")[1];
                                        if (variable.contains("add")) {
                                            value = value.replace("add", "");
                                            int variablenum = Integer.parseInt(variable);
                                            int variablenum2 = placedslotconfig.getInt(value);
                                            int variablenum3 = variablenum2 + variablenum;
                                            placedslotconfig.set(value, variablenum3);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (variable.contains("remove")) {
                                            value = value.replace("remove", "");
                                            int variablenum = Integer.parseInt(variable);
                                            int variablenum2 = placedslotconfig.getInt(value);
                                            int variablenum3 = variablenum2 - variablenum;
                                            placedslotconfig.set(value, variablenum3);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (variable.contains("set")) {
                                            value = value.replace("set", "");
                                            int variablenum = Integer.parseInt(variable);
                                            placedslotconfig.set(value, variablenum);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                                // プレイヤーにお金を渡す
                                EconomyResponse r = econ.depositPlayer(player, rewardmoney);
                                if (r.transactionSuccess()) {
                                    player.sendMessage(String.format("§e%s円受け取りました", econ.format(r.amount)).replace("$", ""));
                                } else {
                                    player.sendMessage(String.format("§c§lお金を獲得できませんでした。獲得できなかったお金:%s円", econ.format(r.amount)));
                                }
                                File ranking = new File("plugins/KuroinuSlotPlugin_Test/ranking.yml");
                                if (!ranking.exists()) {
                                    try {
                                        ranking.createNewFile();
                                        List<String> names = new ArrayList<>();
                                        List<Integer> money = new ArrayList<>();
                                        List<Integer> usedmoney = new ArrayList<>();
                                        YamlConfiguration rankingconfig = YamlConfiguration.loadConfiguration(ranking);
                                        rankingconfig.set("names", names);
                                        rankingconfig.set("money", money);
                                        rankingconfig.set("usedmoney", usedmoney);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                YamlConfiguration rankingconfig = YamlConfiguration.loadConfiguration(ranking);
                                List<String> names = rankingconfig.getStringList("names");
                                List<Integer> money = rankingconfig.getIntegerList("money");
                                List<Integer> usedmoney = rankingconfig.getIntegerList("usedmoney");
                                if (names.contains(player.getName())) {
                                    // namesのindexを取得
                                    int index = names.indexOf(player.getName());
                                    // moneyのindexがない場合
                                    if (money.size() < index) {
                                        money.add(rewardmoney);
                                        usedmoney.add(slotdata.getInt("coin.amount"));
                                    }
                                    // moneyのindexにrewardmoneyを追加
                                    money.set(index, money.get(index) + rewardmoney);
                                    // usedmoneyのindexにslotdata.getInt("coin.amount")を追加
                                    usedmoney.set(index, usedmoney.get(index) + slotdata.getInt("coin.amount"));
                                    // moneyをmoney3に全て追加
                                    List<Integer> money3 = new ArrayList<>();
                                    money3.addAll(money);
                                    // moneyを降順に並び替え
                                    money.sort(Comparator.reverseOrder());
                                    for (int i = 0; i < money.size(); i++) {
                                        int money2 = money.get(i);
                                        int money4 = money3.get(i);
                                        if (money2 != money4) {
                                            names.remove(index);
                                            names.add(i, player.getName());
                                            int money5 = usedmoney.get(index);
                                            usedmoney.remove(index);
                                            usedmoney.add(i, money5);
                                            break;
                                        }
                                    }
                                    // もしnames、moneyの数が11以上だったら、一番下を削除
                                    if (names.size() > 10) {
                                        names.remove(10);
                                    }
                                    if (money.size() > 10) {
                                        money.remove(10);
                                    }
                                    if (usedmoney.size() > 10) {
                                        usedmoney.remove(10);
                                    }
                                    // namesとmoneyを保存
                                    rankingconfig.set("names", names);
                                    rankingconfig.set("money", money);
                                    rankingconfig.set("usedmoney", usedmoney);
                                } else {
                                    names.add(player.getName());
                                    money.add(rewardmoney);
                                    usedmoney.add(slotdata.getInt("coin.amount"));
                                    // namesのindexを取得
                                    int index = names.indexOf(player.getName());
                                    // moneyのindexがない場合
                                    if (money.size() < index) {
                                        money.add(rewardmoney);
                                        usedmoney.add(slotdata.getInt("coin.amount"));
                                    }
                                    // moneyのindexにrewardmoneyを追加
                                    money.set(index, money.get(index) + rewardmoney);
                                    // usedmoneyのindexにslotdata.getInt("coin.amount")を追加
                                    usedmoney.set(index, usedmoney.get(index) + slotdata.getInt("coin.amount"));
                                    // moneyをmoney3に全て追加
                                    List<Integer> money3 = new ArrayList<>();
                                    money3.addAll(money);
                                    // moneyを降順に並び替え
                                    money.sort(Comparator.reverseOrder());
                                    for (int i = 0; i < money.size(); i++) {
                                        int money2 = money.get(i);
                                        int money4 = money3.get(i);
                                        if (money2 != money4) {
                                            names.remove(index);
                                            names.add(i, player.getName());
                                            int money5 = usedmoney.get(index);
                                            usedmoney.remove(index);
                                            usedmoney.add(i, money5);
                                            break;
                                        }
                                    }
                                    // もしnames、moneyの数が11以上だったら、一番下を削除
                                    if (names.size() > 10) {
                                        names.remove(10);
                                    }
                                    if (money.size() > 10) {
                                        money.remove(10);
                                    }
                                    if (usedmoney.size() > 10) {
                                        usedmoney.remove(10);
                                    }
                                    // namesとmoneyを保存
                                    rankingconfig.set("names", names);
                                    rankingconfig.set("money", money);
                                    rankingconfig.set("usedmoney", usedmoney);
                                }
                                try {
                                    rankingconfig.save(ranking);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                player.sendMessage(prefix + "§4外れました");
                                int stocknum = slotdata.getInt("stockgain");
                                int stocknum2 = placedslotconfig.getInt("stock");
                                int stocknum3 = stocknum2 + stocknum;
                                placedslotconfig.set("stock", stocknum3);
                                try {
                                    placedslotconfig.save(placedslotdata);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                File ranking = new File("plugins/KuroinuSlotPlugin_Test/ranking.yml");
                                YamlConfiguration rankingconfig = YamlConfiguration.loadConfiguration(ranking);
                                List<String> names = rankingconfig.getStringList("names");
                                List<Integer> money = rankingconfig.getIntegerList("money");
                                List<Integer> usedmoney = rankingconfig.getIntegerList("usedmoney");
                                if (names.contains(player.getName())) {
                                    // namesのindexを取得
                                    int index = names.indexOf(player.getName());
                                    // moneyのindexがない場合
                                    if (money.size() < index) {
                                        money.add(0);
                                        usedmoney.add(slotdata.getInt("coin.amount"));
                                    }
                                    // usedmoneyのindexにslotdata.getInt("coin.amount")を追加
                                    usedmoney.set(index, usedmoney.get(index) + slotdata.getInt("coin.amount"));
                                    List<Integer> money3 = money;
                                    // moneyを降順に並び替え
                                    money.sort(Comparator.reverseOrder());
                                    for (int i = 0; i < money.size(); i++) {
                                        int money2 = money.get(i);
                                        int money4 = money3.get(i);
                                        if (money2 != money4) {
                                            names.remove(index);
                                            names.add(i, player.getName());
                                            break;
                                        }
                                    }
                                    // もしnames、moneyの数が11以上だったら、一番下を削除
                                    if (names.size() > 10) {
                                        names.remove(10);
                                    }
                                    if (money.size() > 10) {
                                        money.remove(10);
                                    }
                                    if (usedmoney.size() > 10) {
                                        usedmoney.remove(10);
                                    }
                                    // namesとmoneyを保存
                                    rankingconfig.set("names", names);
                                    rankingconfig.set("money", money);
                                    rankingconfig.set("usedmoney", usedmoney);
                                } else {
                                    names.add(player.getName());
                                    money.add(0);
                                    usedmoney.add(slotdata.getInt("coin.amount"));
                                    // namesのindexを取得
                                    int index = names.indexOf(player.getName());
                                    // usedmoneyのindexにslotdata.getInt("coin.amount")を追加
                                    usedmoney.set(index, usedmoney.get(index) + slotdata.getInt("coin.amount"));
                                    List<Integer> money3 = money;
                                    // moneyを降順に並び替え
                                    money.sort(Comparator.reverseOrder());
                                    for (int i = 0; i < money.size(); i++) {
                                        int money2 = money.get(i);
                                        int money4 = money3.get(i);
                                        if (money2 != money4) {
                                            names.remove(index);
                                            names.add(i, player.getName());
                                            break;
                                        }
                                    }
                                    // もしnames、moneyの数が11以上だったら、一番下を削除
                                    if (names.size() > 10) {
                                        names.remove(10);
                                    }
                                    if (money.size() > 10) {
                                        money.remove(10);
                                    }
                                    if (usedmoney.size() > 10) {
                                        usedmoney.remove(10);
                                    }
                                    // namesとmoneyを保存
                                    rankingconfig.set("names", names);
                                    rankingconfig.set("money", money);
                                    rankingconfig.set("usedmoney", usedmoney);
                                }
                                try {
                                    rankingconfig.save(ranking);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            // stock0action:の処理
                            List<String> stock0actions = slotdata.getStringList("stock0action");
                            int slotstock = placedslotconfig.getInt("stock");
                            if (slotstock > 0) {
                                stock0actions = null;
                            }
                            if (stock0actions != null) {
                                for (String action : stock0actions) {
                                    // sound:から始まる場合、そのサウンドを再生する
                                    if (action.contains("sound:")) {
                                        // soundname:soundpitchという形式の場合、soundnameをsoundとして、soundpitchをsoundpitchとして取得する
                                        String soundname = action.replace("sound:", "");
                                        float soundpitch = Float.parseFloat(soundname.split(":")[1]);
                                        soundname = soundname.split(":")[0];
                                        Sound sound2 = Sound.valueOf(soundname);
                                        loc2.getWorld().playSound(loc2, sound2, 1, soundpitch);
                                    }
                                    // stock:から始まる場合
                                    if (action.contains("stock:")) {
                                        String stock = action.replace("stock:", "");
                                        if (stock.contains("add")) {
                                            stock = stock.replace("add", "");
                                            int stocknum = Integer.parseInt(stock);
                                            int stocknum2 = placedslotconfig.getInt("stock");
                                            int stocknum3 = stocknum2 + stocknum;
                                            placedslotconfig.set("stock", stocknum3);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (stock.contains("remove")) {
                                            stock = stock.replace("remove", "");
                                            int stocknum = Integer.parseInt(stock);
                                            int stocknum2 = placedslotconfig.getInt("stock");
                                            int stocknum3 = stocknum2 - stocknum;
                                            placedslotconfig.set("stock", stocknum3);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (stock.contains("set")) {
                                            stock = stock.replace("set", "");
                                            int stocknum = Integer.parseInt(stock);
                                            placedslotconfig.set("stock", stocknum);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (stock.contains("reset")) {
                                            placedslotconfig.set("stock", 0);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (stock.contains("payout")) {
                                            int stocknum = placedslotconfig.getInt("stock");
                                            int newstocknum = slotdata.getInt("defaultstock");
                                            placedslotconfig.set("stock", newstocknum);
                                            rewardmoney = rewardmoney + stocknum;
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    // b:から始まる場合、そのメッセージを全員に送信する
                                    if (action.contains("b:")) {
                                        String message = action.replace("b:", "");
                                        message = message.replace("&", "§");
                                        message = message.replace("%player%", player.getName());
                                        message = message.replace("%rewardmoney%", rewardmoney + "");
                                        Bukkit.broadcastMessage(prefix + message);
                                    }
                                    // c:から始まる場合、そのコマンドを実行する
                                    if (action.contains("c:")) {
                                        String command = action.replace("c:", "");
                                        command = command.replace("%player%", player.getName());
                                        command = command.replace("%rewardmoney%", rewardmoney + "");
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                    }
                                    // s:から始まる場合、プレイヤーにメッセージを送信する
                                    if (action.contains("s:")) {
                                        String message = action.replace("s:", "");
                                        message = message.replace("&", "§");
                                        message = message.replace("%player%", player.getName());
                                        message = message.replace("%rewardmoney%", rewardmoney + "");
                                        player.sendMessage(prefix + message);
                                    }
                                    // slot:から始まる場合、そのスロットに切り替える
                                    if (action.contains("slot:")) {
                                        String slotname = action.replace("slot:", "");
                                        File slot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + slotname + ".yml");
                                        YamlConfiguration slotdata2 = YamlConfiguration.loadConfiguration(slot);
                                        if (slot.exists()) {
                                            placedslotconfig.set("slotname", slotname);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                                player.sendMessage(prefix + "§a§l" + slotname + "§f§lスロットに切り替えました。");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            player.sendMessage(prefix + "§c§l" + slotname + "§f§lスロットは存在しません。");
                                        }
                                    }
                                    // freespin:から始まる場合
                                    if (action.contains("freespin:")) {
                                        String freespin = action.replace("freespin:", "");
                                        if (freespin.contains("add")) {
                                            freespin = freespin.replace("add", "");
                                            int freespinnum = Integer.parseInt(freespin);
                                            int freespinnum2 = placedslotconfig.getInt("freespin");
                                            int freespinnum3 = freespinnum2 + freespinnum;
                                            placedslotconfig.set("freespin", freespinnum3);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (freespin.contains("remove")) {
                                            freespin = freespin.replace("remove", "");
                                            int freespinnum = Integer.parseInt(freespin);
                                            int freespinnum2 = placedslotconfig.getInt("freespin");
                                            int freespinnum3 = freespinnum2 - freespinnum;
                                            placedslotconfig.set("freespin", freespinnum3);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (freespin.contains("set")) {
                                            freespin = freespin.replace("set", "");
                                            int freespinnum = Integer.parseInt(freespin);
                                            placedslotconfig.set("freespin", freespinnum);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (freespin.contains("reset")) {
                                            placedslotconfig.set("freespin", 0);
                                            try {
                                                placedslotconfig.save(placedslotdata);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                            Location signloc = placedslotconfig.getLocation("sign");
                            Block signblock = signloc.getBlock();
                            Sign sign = (Sign) signblock.getState();
                            sign.setLine(0, "§9§l==========");
                            sign.setLine(1, "§0§l[§f" + slotdata.getString("displayname").replace("&", "§") + "§0§l]");
                            sign.setLine(2, "§0§lstock:§a§l" + placedslotconfig.getInt("stock"));
                            sign.setLine(3, "§9§l==========");
                            sign.update();
                            File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
                            YamlConfiguration dataYml = YamlConfiguration.loadConfiguration(data);
                            dataYml.set("isPlaying", false);
                            try {
                                dataYml.save(data);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }, delay * i);
        }
    }
}
