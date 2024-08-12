package com.kuroinusaba.kuroinuslotplugin_test.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kuroinusaba.kuroinuslotplugin_test.KuroinuSlotPlugin_Test.plugin;

public class kuroinuslot implements CommandExecutor, TabCompleter {
    String prefix = plugin.getConfig().getString("prefix");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!(player.hasPermission("admin"))) {
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(prefix + "§c引数が足りません。");
            player.sendMessage(prefix + "§c/kuroinuslot helpを参照してください。");
        } else if (args[0].equals("help")) {
            player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
            player.sendMessage(prefix + "§e§l/kuroinuslot help §f§l: §a§lこのヘルプを表示します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot create §f§l: §a§lスロットを作成します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot edit §f§l: §a§lスロットを編集します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot start §f§l: §a§lスロットを開始します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot stop §f§l: §a§lスロットを停止します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot reload §f§l: §a§lコンフィグを再読み込みします。");
            player.sendMessage(prefix + "§e§l/kuroinuslot info §f§l: §a§lプラグインの情報を表示します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot version §f§l: §a§lプラグインのバージョンを表示します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot debug §f§l: §a§lデバッグモードを切り替えます。");
            player.sendMessage(prefix + "§e§l/kuroinuslot test §f§l: §a§lテスト用のコマンドです。");
            player.sendMessage(prefix + "§e§l =================================");
        } else if (args[0].equals("create")) {
            if (args.length == 1) {
                player.sendMessage(prefix + "§c引数が足りません。");
                player.sendMessage(prefix + "§c/kuroinuslot create helpを参照してください。");
            } else if (args.length == 2) {
                if (args[1].equals("help")) {
                    player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
                    player.sendMessage(prefix + "§e§l/kuroinuslot create help §f§l: §a§lこのヘルプを表示します。");
                    player.sendMessage(prefix + "§e§l/kuroinuslot create <スロット名> §f§l: §a§lスロットを作成します。");
                    player.sendMessage(prefix + "§e§l =================================");
                } else {
                    File newSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                    YamlConfiguration newSlotYml = new YamlConfiguration();
                    File dataFolder = new File("plugins/KuroinuSlotPlugin_Test/slots");
                    if (!dataFolder.exists()) {
                        dataFolder.mkdir();
                    }
                    try {
                        ItemStack item1 = new ItemStack(Material.DIAMOND);
                        ItemMeta item1Meta = item1.getItemMeta();
                        item1Meta.setDisplayName("§e§lダイヤモンド");
                        item1.setItemMeta(item1Meta);
                        ItemStack item2 = new ItemStack(Material.GOLD_INGOT);
                        ItemMeta item2Meta = item2.getItemMeta();
                        item2Meta.setDisplayName("§e§l金インゴット");
                        item2.setItemMeta(item2Meta);
                        ItemStack reward1 = new ItemStack(Material.DIAMOND);
                        ItemMeta reward1Meta = reward1.getItemMeta();
                        reward1Meta.setDisplayName("§e§lダイヤモンド");
                        reward1.setItemMeta(reward1Meta);
                        ItemStack reward2 = new ItemStack(Material.GOLD_INGOT);
                        ItemMeta reward2Meta = reward2.getItemMeta();
                        reward2Meta.setDisplayName("§e§l金インゴット");
                        reward2.setItemMeta(reward2Meta);
                        newSlot.createNewFile();
                        List<Integer> pattern = new ArrayList<>();
                        pattern.add(1);
                        pattern.add(1);
                        pattern.add(1);
                        List<Integer> pattern2 = new ArrayList<>();
                        pattern2.add(2);
                        pattern2.add(2);
                        pattern2.add(2);
                        List<String> action1 = new ArrayList<>();
                        action1.add("stock:payout");
                        action1.add("b:&6&l%player%&f&lがスロットでダイヤモンド揃い！%rewardmoney%円を獲得しました！");
                        action1.add("s:§e§l%rewardmoney%円獲得！");
                        List<String> action2 = new ArrayList<>();
                        action2.add("stock:add1000");
                        action2.add("b:&6&l%player%&f&lがスロットで金インゴット揃い！ストックが1000増えました！");
                        action2.add("s:§e§lストックが1000増えました！");
                        newSlotYml.set("name", args[1]);
                        newSlotYml.set("displayname", "§e§l" + args[1]);
                        newSlotYml.set("tick", 40);
                        newSlotYml.set("delay", 1);
                        newSlotYml.set("defaultstock", 1000);
                        newSlotYml.set("stockgain", 80);
                        newSlotYml.set("percentage", 10);
                        newSlotYml.set("symbol.1", item1);
                        newSlotYml.set("symbol.2", item2);
                        newSlotYml.set("coin.type", "money");
                        newSlotYml.set("coin.amount", 100);
                        newSlotYml.set("coin.item", null);
                        newSlotYml.set("slot." + 1 + ".name", "§e§lダイヤモンド");
                        newSlotYml.set("slot." + 1 + ".reward." + 1, reward1);
                        newSlotYml.set("slot." + 1 + ".rewardmoney", 1000);
                        newSlotYml.set("slot." + 1 + ".percentage", 10);
                        newSlotYml.set("slot." + 1 + ".action", action1);
                        newSlotYml.set("slot." + 1 + ".pattern", pattern);
                        newSlotYml.set("slot." + 2 + ".name", "§e§l金インゴット");
                        newSlotYml.set("slot." + 2 + ".reward." + 1, reward2);
                        newSlotYml.set("slot." + 2 + ".rewardmoney", 1000);
                        newSlotYml.set("slot." + 2 + ".percentage", 20);
                        newSlotYml.set("slot." + 2 + ".action", action2);
                        newSlotYml.set("slot." + 2 + ".pattern", pattern2);
                        newSlotYml.set("stock0action", new ArrayList<>());
                        newSlotYml.set("startsound", "ENTITY_PLAYER_LEVELUP");
                        newSlotYml.set("rollsound", "BLOCK_NOTE_BLOCK_PLING");
                        newSlotYml.save(newSlot);
                        player.sendMessage(prefix + "§e§lスロットを作成しました。");
                    } catch (Exception e) {
                        player.sendMessage(prefix + "§cスロットの作成に失敗しました。");
                        e.printStackTrace();
                    }
                }
            } else {
                player.sendMessage(prefix + "§c引数が間違っています。");
                player.sendMessage(prefix + "§c/kuroinuslot helpを参照してください。");
            }
            return false;
        } else if (args[0].equals("edit")) {
            if (args[1].equals("help")) {
                player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
                player.sendMessage(prefix + "§e§l/kuroinuslot edit help §f§l: §a§lこのヘルプを表示します。");
                player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> §f§l: §a§lスロットを編集します。");
                player.sendMessage(prefix + "§e§l =================================");
            } else {
                File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                if (args[2].equals("coin")) {
                    if (args[3].equals("type")) {
                        editSlotYml.set("coin.type", args[4]);
                        try {
                            editSlotYml.save(editSlot);
                            player.sendMessage(prefix + "§e§lコインの種類を変更しました。");
                        } catch (Exception e) {
                            player.sendMessage(prefix + "§cコインの種類の変更に失敗しました。");
                            e.printStackTrace();
                        }
                    } else if (args[3].equals("amount")) {
                        // int型に変換する
                        editSlotYml.set("coin.amount", Integer.parseInt(args[4]));
                        try {
                            editSlotYml.save(editSlot);
                            player.sendMessage(prefix + "§e§lコインの量を変更しました。");
                        } catch (Exception e) {
                            player.sendMessage(prefix + "§cコインの量の変更に失敗しました。");
                            e.printStackTrace();
                        }
                    } else if (args[3].equals("item")) {
                        editSlotYml.set("coin.item", args[4]);
                        try {
                            editSlotYml.save(editSlot);
                            player.sendMessage(prefix + "§e§lコインのアイテムを変更しました。");
                        } catch (Exception e) {
                            player.sendMessage(prefix + "§cコインのアイテムの変更に失敗しました。");
                            e.printStackTrace();
                        }
                    } else if (args[3].equals("help")) {
                        player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> coin help §f§l: §a§lこのヘルプを表示します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> coin type <種類> §f§l: §a§lコインの種類を変更します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> coin amount <量> §f§l: §a§lコインの量を変更します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> coin item <アイテム> §f§l: §a§lコインのアイテムを変更します。");
                        player.sendMessage(prefix + "§e§l =================================");
                    } else {
                        player.sendMessage(prefix + "§c引数が間違っています。");
                        player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                    }
                } else if (args[2].equals("slot")) {
                    if (args[4].equals("reward")) {
                        if (args[6].equals("money")) {
                            editSlotYml.set("slot." + args[3] + ".reward." + args[5] + ".money", Integer.parseInt(args[7]));
                            try {
                                editSlotYml.save(editSlot);
                                player.sendMessage(prefix + "§e§lスロットの報酬を変更しました。");
                            } catch (Exception e) {
                                player.sendMessage(prefix + "§cスロットの報酬の変更に失敗しました。");
                                e.printStackTrace();
                            }
                        } else if (args[6].equals("item")) {
                            ItemStack item = player.getInventory().getItemInMainHand();
                            editSlotYml.set("slot." + args[3] + ".reward." + args[5], item);
                            try {
                                editSlotYml.save(editSlot);
                                player.sendMessage(prefix + "§e§lスロットの報酬を変更しました。");
                            } catch (Exception e) {
                                player.sendMessage(prefix + "§cスロットの報酬の変更に失敗しました。");
                                e.printStackTrace();
                            }
                        } else {
                            player.sendMessage(prefix + "§c引数が間違っています。");
                            player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                        }
                    } else if (args[4].equals("rewardmoney")) {
                        if (args.length == 6) {
                            // int型に変換する
                            editSlotYml.set("slot." + args[3] + ".rewardmoney", Integer.parseInt(args[5]));
                            try {
                                editSlotYml.save(editSlot);
                                player.sendMessage(prefix + "§e§lスロットの報酬金額を変更しました。");
                            } catch (Exception e) {
                                player.sendMessage(prefix + "§cスロットの報酬金額の変更に失敗しました。");
                                e.printStackTrace();
                            }
                        } else {
                            player.sendMessage(prefix + "§c引数が間違っています。");
                            player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                        }
                    } else if (args[4].equals("percentage")) {
                        if (args.length == 6) {
                            // int型に変換する
                            editSlotYml.set("slot." + args[3] + ".percentage", Integer.parseInt(args[5]));
                            try {
                                editSlotYml.save(editSlot);
                                player.sendMessage(prefix + "§e§lスロットの確率を変更しました。");
                            } catch (Exception e) {
                                player.sendMessage(prefix + "§cスロットの確率の変更に失敗しました。");
                                e.printStackTrace();
                            }
                        } else {
                            player.sendMessage(prefix + "§c引数が間違っています。");
                            player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                        }
                    } else if (args[4].equals("help")) {
                        player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> slot help §f§l: §a§lこのヘルプを表示します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> slot reward <報酬名> money <金額> §f§l: §a§lスロットの報酬を変更します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> slot reward <報酬名> item <アイテム> <量> §f§l: §a§lスロットの報酬を変更します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> slot percentage <確率> §f§l: §a§lスロットの確率を変更します。");
                        player.sendMessage(prefix + "§e§l =================================");
                    } else if (args[4].equals("action")) {
                        List<String> actionList = editSlotYml.getStringList("slot." + args[3] + ".action");
                        if (args[5].equals("add")) {
                            if (args[6].equals("command")) {
                                if (args.length >= 8) {
                                    actionList.add("c:" + args[7]);
                                    editSlotYml.set("slot." + args[3] + ".action", actionList);
                                    try {
                                        editSlotYml.save(editSlot);
                                        player.sendMessage(prefix + "§e§lスロットのアクションを追加しました。");
                                    } catch (Exception e) {
                                        player.sendMessage(prefix + "§cスロットのアクションの追加に失敗しました。");
                                        e.printStackTrace();
                                    }
                                } else {
                                    player.sendMessage(prefix + "§c引数が間違っています。");
                                    player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                                }
                            } else if (args[6].equals("broadcast")) {
                                if (args.length >= 8) {
                                    actionList.add("b:" + args[7]);
                                    editSlotYml.set("slot." + args[3] + ".action", actionList);
                                    try {
                                        editSlotYml.save(editSlot);
                                        player.sendMessage(prefix + "§e§lスロットのアクションを追加しました。");
                                    } catch (Exception e) {
                                        player.sendMessage(prefix + "§cスロットのアクションの追加に失敗しました。");
                                        e.printStackTrace();
                                    }
                                } else {
                                    player.sendMessage(prefix + "§c引数が間違っています。");
                                    player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                                }
                            } else if (args[6].equals("stock")) {
                                if (args[7].equals("add")) {
                                    if (args.length == 9) {
                                        actionList.add("s:add" + args[8]);
                                        editSlotYml.set("slot." + args[3] + ".action", actionList);
                                        try {
                                            editSlotYml.save(editSlot);
                                            player.sendMessage(prefix + "§e§lスロットのアクションを追加しました。");
                                        } catch (Exception e) {
                                            player.sendMessage(prefix + "§cスロットのアクションの追加に失敗しました。");
                                            e.printStackTrace();
                                        }
                                    } else {
                                        player.sendMessage(prefix + "§c引数が間違っています。");
                                        player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                                    }
                                } else if (args[7].equals("remove")) {
                                    if (args.length == 9) {
                                        actionList.add("s:remove" + args[8]);
                                        editSlotYml.set("slot." + args[3] + ".action", actionList);
                                        try {
                                            editSlotYml.save(editSlot);
                                            player.sendMessage(prefix + "§e§lスロットのアクションを削除しました。");
                                        } catch (Exception e) {
                                            player.sendMessage(prefix + "§cスロットのアクションの削除に失敗しました。");
                                            e.printStackTrace();
                                        }
                                    } else {
                                        player.sendMessage(prefix + "§c引数が間違っています。");
                                        player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                                    }
                                } else if (args[7].equals("set")) {
                                    if (args.length == 9) {
                                        actionList.add("s:set" + args[8]);
                                        editSlotYml.set("slot." + args[3] + ".action", actionList);
                                        try {
                                            editSlotYml.save(editSlot);
                                            player.sendMessage(prefix + "§e§lスロットのアクションを変更しました。");
                                        } catch (Exception e) {
                                            player.sendMessage(prefix + "§cスロットのアクションの変更に失敗しました。");
                                            e.printStackTrace();
                                        }
                                    } else {
                                        player.sendMessage(prefix + "§c引数が間違っています。");
                                        player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                                    }
                                } else {
                                    player.sendMessage(prefix + "§c引数が間違っています。");
                                    player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                                }
                            } else if (args[6].equals("help")) {
                                player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
                                player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> slot action add command <コマンド> §f§l: §a§lスロットのアクションを追加します。");
                                player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> slot action add message <メッセージ> §f§l: §a§lスロットのアクションを追加します。");
                                player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> slot action add stock <ストック名> §f§l: §a§lスロットのアクションを追加します。");
                                player.sendMessage(prefix + "§e§l =================================");
                            } else {
                                player.sendMessage(prefix + "§c引数が間違っています。");
                                player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                            }
                        }
                    } else if (args[4].equals("pattern")) {
                        List<Integer> patternList = new ArrayList<>();
                        if (args.length == 8) {
                            patternList.add(Integer.parseInt(args[5]));
                            patternList.add(Integer.parseInt(args[6]));
                            patternList.add(Integer.parseInt(args[7]));
                            editSlotYml.set("slot." + args[3] + ".pattern", patternList);
                            try {
                                editSlotYml.save(editSlot);
                                player.sendMessage(prefix + "§e§lスロットのパターンを変更しました。");
                            } catch (Exception e) {
                                player.sendMessage(prefix + "§cスロットのパターンの変更に失敗しました。");
                                e.printStackTrace();
                            }
                        } else {
                            player.sendMessage(prefix + "§c引数が間違っています。");
                            player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                        }
                    } else {
                        player.sendMessage(prefix + "§c引数が間違っています。");
                        player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                    }
                } else if (args[2].equals("symbol")) {
                    if (args[4].equals("handitem")) {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        editSlotYml.set("symbol." + args[3], item);
                        editSlotYml.set("slot." + args[3] + ".percentage", 100);
                        editSlotYml.set("slot." + args[3] + ".action", new ArrayList<>());
                        editSlotYml.set("slot." + args[3] + ".pattern", new ArrayList<>(Arrays.asList(Integer.parseInt(args[3]), Integer.parseInt(args[3]), Integer.parseInt(args[3]))));
                        editSlotYml.set("slot." + args[3] + ".rewardmoney", 0);
                        try {
                            editSlotYml.save(editSlot);
                            player.sendMessage(prefix + "§e§lスロットのシンボルを変更しました。");
                        } catch (Exception e) {
                            player.sendMessage(prefix + "§cスロットのシンボルの変更に失敗しました。");
                            e.printStackTrace();
                        }
                    } else if (args[4].equals("help")) {
                        player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> symbol help §f§l: §a§lこのヘルプを表示します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> symbol item <アイテム> §f§l: §a§lスロットのシンボルを変更します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> symbol name <名前> §f§l: §a§lスロットのシンボルを変更します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> symbol glow <true/false> §f§l: §a§lスロットのシンボルを変更します。");
                        player.sendMessage(prefix + "§e§l =================================");
                    } else {
                        player.sendMessage(prefix + "§c引数が間違っています。");
                        player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                    }
                } else if (args[2].equals("rollsound")) {
                    if (args.length == 4) {
                        if (args[3] != null) {
                            editSlotYml.set("rollsound", args[3]);
                            try {
                                editSlotYml.save(editSlot);
                                player.sendMessage(prefix + "§e§lスロットの回転音を変更しました。");
                            } catch (Exception e) {
                                player.sendMessage(prefix + "§cスロットの回転音の変更に失敗しました。");
                                e.printStackTrace();
                            }
                        } else {
                            player.sendMessage(prefix + "§c引数が間違っています。");
                            player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                        }
                    }
                } else if (args[2].equals("percentage")) {
                    if (args.length == 4) {
                        int percentage = Integer.parseInt(args[3]);
                        if (percentage >= 0 && percentage <= 100) {
                            editSlotYml.set("slot." + args[3] + ".percentage", percentage);
                            try {
                                editSlotYml.save(editSlot);
                                player.sendMessage(prefix + "§e§lスロットの確率を変更しました。");
                            } catch (Exception e) {
                                player.sendMessage(prefix + "§cスロットの確率の変更に失敗しました。");
                                e.printStackTrace();
                            }
                        } else {
                            player.sendMessage(prefix + "§c引数が間違っています。");
                            player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                        }
                    }
                }
            }
        } else if (args[0].equals("info")) {
            player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
            player.sendMessage(prefix + "§e§l説明: §a§lスロットマシンを作成するプラグインです。");
            player.sendMessage(prefix + "§e§l作者: §a§ljpbtk");
            player.sendMessage(prefix + "§e§lバージョン: §a§l" + plugin.getDescription().getVersion());
            player.sendMessage(prefix + "§e§l =================================");
        } else if (args[0].equals("reload")) {
            if (args.length == 1) {
                plugin.reloadConfig();
                player.sendMessage(prefix + "§e§lコンフィグをリロードしました。");
            } else {
                File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                // ファイルが存在するか確認する
                if (editSlot.exists()) {
                    try {
                        editSlotYml.save(editSlot);
                        player.sendMessage(prefix + "§e§lスロットファイルをリロードしました。");
                    } catch (Exception e) {
                        player.sendMessage(prefix + "§cスロットファイルのリロードに失敗しました。");
                        e.printStackTrace();
                    }
                } else {
                    player.sendMessage(prefix + "§cスロットファイルが存在しません。");
                }
            }
        } else if (args[0].equals("version")) {
            player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
            player.sendMessage(prefix + "§e§lバージョン: §a§l" + plugin.getDescription().getVersion());
            player.sendMessage(prefix + "§e§l =================================");
        } else if (args[0].equals("place")) {
            if (args.length == 1) {
                player.sendMessage(prefix + "§c引数が間違っています。");
                player.sendMessage(prefix + "§c/kuroinuslot place helpを参照してください。");
            } else if (args[1].equals("help")) {
                player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
                player.sendMessage(prefix + "§e§l/kuroinuslot place help §f§l: §a§lこのヘルプを表示します。");
                player.sendMessage(prefix + "§e§l/kuroinuslot place <スロット名> <スロットの名前> §f§l: §a§lスロットを設置します。");
                player.sendMessage(prefix + "§e§l =================================");
            } else {
                if (args.length == 3) {
                    File placeSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                    YamlConfiguration placeSlotYml = YamlConfiguration.loadConfiguration(placeSlot);
                    // ファイルが存在するか確認する
                    if (placeSlot.exists()) {
                        try {
                            File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
                            YamlConfiguration dataYml = YamlConfiguration.loadConfiguration(data);
                            // ファイルが存在するか確認する
                            if (!data.exists()) {
                                dataYml.createSection("slot");
                                dataYml.createSection("pslot");
                                dataYml.createSection("slotname");
                                dataYml.set("slot", 1);
                                dataYml.set("pslot", args[1]);
                                dataYml.set("slotname", args[2]);
                                dataYml.set("isPlaying", false);
                                dataYml.save(data);
                            } else {
                                dataYml.set("slot", 1);
                                dataYml.set("pslot", args[1]);
                                dataYml.set("slotname", args[2]);
                                dataYml.save(data);
                            }
                            File placedSlot = new File("plugins/KuroinuSlotPlugin_Test/placedslot/" + args[2] + ".yml");
                            YamlConfiguration placedSlotYml = YamlConfiguration.loadConfiguration(placedSlot);
                            placedSlotYml.createSection("slotname");
                            placedSlotYml.set("slotname", args[1]);
                            placedSlotYml.set("on", true);
                            placedSlotYml.save(placedSlot);
                            player.sendMessage(prefix + "§e§l額縁を3つ設置してください。");
                        } catch (Exception e) {
                            player.sendMessage(prefix + "§cスロットの設置に失敗しました。");
                            e.printStackTrace();
                        }
                    } else {
                        player.sendMessage(prefix + "§cスロットファイルが存在しません。");
                    }
                } else {
                    player.sendMessage(prefix + "§c引数が間違っています。");
                    player.sendMessage(prefix + "§c/kuroinuslot place helpを参照してください。");
                }
            }
        } else if (args[0].equals("debug")) {
            if (args[1].equals("isPlaying")) {
                if (args[2].equals("false")) {
                    if (args.length == 4) {
                        Player target = Bukkit.getPlayer(args[3]);
                        File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + target.getUniqueId() + ".yml");
                        YamlConfiguration dataYml = YamlConfiguration.loadConfiguration(data);
                        dataYml.set("isPlaying", false);
                        try {
                            dataYml.save(data);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        player.sendMessage(prefix + "§e§l対象のプレイヤーのスロットを強制終了しました。");
                    } else if (args.length == 3) {
                        File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
                        YamlConfiguration dataYml = YamlConfiguration.loadConfiguration(data);
                        dataYml.set("isPlaying", false);
                        try {
                            dataYml.save(data);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        player.sendMessage(prefix + "§e§lスロットを強制終了しました。");
                    }
                } else if (args[2].equals("true")) {
                    if (args.length == 4) {
                        Player target = Bukkit.getPlayer(args[3]);
                        File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + target.getUniqueId() + ".yml");
                        YamlConfiguration dataYml = YamlConfiguration.loadConfiguration(data);
                        dataYml.set("isPlaying", true);
                        try {
                            dataYml.save(data);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        player.sendMessage(prefix + "§e§l対象のプレイヤーのスロットを強制開始しました。");
                    } else if (args.length == 3) {
                        File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
                        YamlConfiguration dataYml = YamlConfiguration.loadConfiguration(data);
                        dataYml.set("isPlaying", true);
                        try {
                            dataYml.save(data);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        player.sendMessage(prefix + "§e§lスロットを強制開始しました。");
                    }
                } else {
                    player.sendMessage(prefix + "§c引数が間違っています。");
                    player.sendMessage(prefix + "§c/kuroinuslot debug isPlaying <true/false> <プレイヤー名>を参照してください。");
                }
            }
        } else if (args[0].equals("setstock")) {
            if (args.length == 3) {
                File data = new File("plugins/KuroinuSlotPlugin_Test/placedslot/" + args[1] + ".yml");
                YamlConfiguration dataYml = YamlConfiguration.loadConfiguration(data);
                int stock = Integer.parseInt(args[2]);
                dataYml.set("stock", stock);
                try {
                    dataYml.save(data);
                    player.sendMessage(prefix + "§e§lストックを設定しました。");
                } catch (Exception e) {
                    player.sendMessage(prefix + "§cストックの設定に失敗しました。");
                    e.printStackTrace();
                }
            } else {
                player.sendMessage(prefix + "§c引数が間違っています。");
                player.sendMessage(prefix + "§c/kuroinuslot stock set <ストック名>を参照してください。");
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equals("kuroinuslot")) {
            if (args.length == 1) {
                List<String> tab = new ArrayList<>();
                tab.add("help");
                tab.add("create");
                tab.add("edit");
                tab.add("place");
                tab.add("start");
                tab.add("stop");
                tab.add("reload");
                tab.add("version");
                tab.add("debug");
                tab.add("info");
                tab.add("setstock");
                tab.add("test");
                return tab;
            } else if (args.length == 2) {
                if (args[0].equals("create")) {
                    List<String> tab = new ArrayList<>();
                    tab.add("help");
                    tab.add("<スロット名>");
                    return tab;
                } else if (args[0].equals("edit")) {
                    List<String> tab = new ArrayList<>();
                    tab.add("help");
                    File dataFolder = new File("plugins/KuroinuSlotPlugin_Test/slots");
                    if (dataFolder.exists()) {
                        File[] files = dataFolder.listFiles();
                        for (File file : files) {
                            tab.add(file.getName().replace(".yml", ""));
                        }
                    }
                    return tab;
                } else if (args[0].equals("reload")) {
                    File dataFolder = new File("plugins/KuroinuSlotPlugin_Test/slots");
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFolder);
                    List<String> tab = new ArrayList<>();
                    if (dataFolder.exists()) {
                        File[] files = dataFolder.listFiles();
                        for (File file : files) {
                            tab.add(file.getName().replace(".yml", ""));
                        }
                    }
                    return tab;
                } else if (args[0].equals("place")) {
                    List<String> tab = new ArrayList<>();
                    tab.add("help");
                    File dataFolder = new File("plugins/KuroinuSlotPlugin_Test/slots");
                    if (dataFolder.exists()) {
                        File[] files = dataFolder.listFiles();
                        for (File file : files) {
                            tab.add(file.getName().replace(".yml", ""));
                        }
                    }
                    return tab;
                } else if (args[0].equals("debug")) {
                    List<String> tab = new ArrayList<>();
                    tab.add("isPlaying");
                    return tab;
                } else if (args[0].equals("setstock")) {
                    List<String> tab = new ArrayList<>();
                    File dataFolder = new File("plugins/KuroinuSlotPlugin_Test/placedslot");
                    if (dataFolder.exists()) {
                        File[] files = dataFolder.listFiles();
                        for (File file : files) {
                            tab.add(file.getName().replace(".yml", ""));
                        }
                    }
                    return tab;
                }
            } else if (args.length == 3) {
                if (args[0].equals("place")) {
                    List<String> tab = new ArrayList<>();
                    tab.add("help");
                    tab.add("<スロットの区別名称(被ってはいけません)>");
                    return tab;
                } else if (args[0].equals("edit")) {
                    List<String> tab = new ArrayList<>();
                    tab.add("symbol");
                    tab.add("coin");
                    tab.add("slot");
                    tab.add("rollsound");
                    tab.add("percentage");
                    return tab;
                } else if (args[0].equals("debug")) {
                    if (args[1].equals("isPlaying")) {
                        List<String> tab = new ArrayList<>();
                        tab.add("true");
                        tab.add("false");
                        return tab;
                    }
                } else if (args[0].equals("setstock")) {
                    List<String> tab = new ArrayList<>();
                    tab.add("<ストック数>");
                    return tab;
                }
            } else if (args.length == 4) {
                if (args[0].equals("edit")) {
                    if (args[2].equals("coin")) {
                        List<String> tab = new ArrayList<>();
                        tab.add("help");
                        tab.add("type");
                        tab.add("amount");
                        tab.add("item");
                        return tab;
                    } else if (args[2].equals("slot")) {
                        List<String> tab = new ArrayList<>();
                        // yml内のslotの項目数を取得する
                        File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                        YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                        int slotNum = editSlotYml.getConfigurationSection("slot").getKeys(false).size();
                        for (int i = 1; i <= slotNum; i++) {
                            tab.add(String.valueOf(i));
                        }
                        return tab;
                    } else if (args[2].equals("symbol")) {
                        List<String> tab = new ArrayList<>();
                        File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                        YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                        int slotNum = editSlotYml.getConfigurationSection("symbol").getKeys(false).size();
                        for (int i = 1; i <= slotNum; i++) {
                            tab.add(String.valueOf(i));
                        }
                        return tab;
                    } else if (args[2].equals("rollsound")) {
                        List<String> tab = new ArrayList<>();
                        for (Sound sound : Sound.values()) {
                            tab.add(sound.toString());
                        }
                        return tab;
                    }
                } else if (args[0].equals("debug")) {
                    List<String> tab = new ArrayList<>();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        tab.add(player.getName());
                    }
                    return tab;
                }
            } else if (args.length == 5) {
                if (args[0].equals("edit")) {
                    if (args[2].equals("coin")) {
                        if (args[3].equals("type")) {
                            List<String> tab = new ArrayList<>();
                            tab.add("money");
                            tab.add("item");
                            return tab;
                        } else if (args[3].equals("amount")) {
                            List<String> tab = new ArrayList<>();
                            for (int i = 1; i <= 64; i++) {
                                tab.add(String.valueOf(i));
                            }
                            return tab;
                        } else if (args[3].equals("item")) {
                            List<String> tab = new ArrayList<>();
                            for (Material material : Material.values()) {
                                tab.add(material.toString());
                            }
                            return tab;
                        }
                    } else if (args[2].equals("slot")) {
                        List<String> tab = new ArrayList<>();
                        tab.add("help");
                        tab.add("action");
                        tab.add("reward");
                        tab.add("percentage");
                        tab.add("pattern");
                        return tab;
                    } else if (args[2].equals("symbol")) {
                        List<String> tab = new ArrayList<>();
                        tab.add("handitem");
                        return tab;
                    }
                }
            } else if (args.length == 6) {
                if (args[0].equals("edit")) {
                    if (args[2].equals("slot")) {
                        if (args[4].equals("reward")) {
                            List<String> tab = new ArrayList<>();
                            File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                            YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                            int slotNum = editSlotYml.getConfigurationSection("slot." + args[3] + ".reward").getKeys(false).size();
                            for (int i = 1; i <= slotNum; i++) {
                                tab.add(String.valueOf(i));
                            }
                            return tab;
                        } else if (args[4].equals("percentage")) {
                            List<String> tab = new ArrayList<>();
                            for (int i = 1; i <= 100; i++) {
                                tab.add(String.valueOf(i));
                            }
                            return tab;
                        } else if (args[4].equals("pattern")) {
                            List<String> tab = new ArrayList<>();
                            File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                            YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                            int slotNum = editSlotYml.getConfigurationSection("symbol").getKeys(false).size();
                            for (int i = 1; i <= slotNum; i++) {
                                tab.add(String.valueOf(i));
                            }
                            return tab;
                        } else if (args[4].equals("action")) {
                            List<String> tab = new ArrayList<>();
                            tab.add("help");
                            tab.add("add");
                            tab.add("remove");
                            return tab;
                        }
                    } else if (args[2].equals("symbol")) {
                        if (args[4].equals("item")) {
                            List<String> tab = new ArrayList<>();
                            for (Material material : Material.values()) {
                                tab.add(material.toString());
                            }
                            return tab;
                        } else if (args[4].equals("name")) {
                            List<String> tab = new ArrayList<>();
                            tab.add("<表示させたい名前>");
                            return tab;
                        } else if (args[4].equals("glow")) {
                            List<String> tab = new ArrayList<>();
                            tab.add("true");
                            tab.add("false");
                            return tab;
                        }
                    }
                }
            } else if (args.length == 7) {
                if (args[0].equals("edit")) {
                    if (args[2].equals("slot")) {
                        if (args[4].equals("reward")) {
                            List<String> tab = new ArrayList<>();
                            tab.add("money");
                            tab.add("item");
                            return tab;
                        } else if (args[4].equals("pattern")) {
                            List<String> tab = new ArrayList<>();
                            File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                            YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                            int slotNum = editSlotYml.getConfigurationSection("symbol").getKeys(false).size();
                            for (int i = 1; i <= slotNum; i++) {
                                tab.add(String.valueOf(i));
                            }
                            return tab;
                        } else if (args[4].equals("action")) {
                            if (args[5].equals("add")) {
                                List<String> tab = new ArrayList<>();
                                tab.add("help");
                                tab.add("command");
                                tab.add("broadcast");
                                tab.add("stock");
                                return tab;
                            } else if (args[5].equals("remove")) {
                                List<String> tab = new ArrayList<>();
                                File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                                YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                                int slotNum = editSlotYml.getConfigurationSection("slot." + args[3] + ".action").getKeys(false).size();
                                for (int i = 1; i <= slotNum; i++) {
                                    tab.add(String.valueOf(i));
                                }
                                return tab;
                            }
                        }
                    }
                }
            } else if (args.length == 8) {
                if (args[0].equals("edit")) {
                    if (args[2].equals("slot")) {
                        if (args[4].equals("reward")) {
                            if (args[6].equals("money")) {
                                List<String> tab = new ArrayList<>();
                                for (int i = 1; i <= 64; i++) {
                                    tab.add(String.valueOf(i));
                                }
                                return tab;
                            }
                        } else if (args[4].equals("pattern")) {
                            List<String> tab = new ArrayList<>();
                            File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                            YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                            int slotNum = editSlotYml.getConfigurationSection("symbol").getKeys(false).size();
                            for (int i = 1; i <= slotNum; i++) {
                                tab.add(String.valueOf(i));
                            }
                            return tab;
                        } else if (args[4].equals("action")) {
                            if (args[6].equals("command")) {
                                List<String> tab = new ArrayList<>();
                                tab.add("<コマンド>");
                                return tab;
                            } else if (args[6].equals("broadcast")) {
                                List<String> tab = new ArrayList<>();
                                tab.add("<メッセージ>");
                                return tab;
                            } else if (args[6].equals("stock")) {
                                List<String> tab = new ArrayList<>();
                                tab.add("add");
                                tab.add("remove");
                                tab.add("set");
                                return tab;
                            }
                        }
                    }
                }
            } else if (args.length == 9) {
                if (args[0].equals("edit")) {
                    if (args[2].equals("slot")) {
                        if (args[4].equals("action")) {
                            if (args[6].equals("stock")) {
                                if (args[7].equals("add")) {
                                    List<String> tab = new ArrayList<>();
                                    tab.add("<数字>");
                                    return tab;
                                } else if (args[7].equals("remove")) {
                                    List<String> tab = new ArrayList<>();
                                    tab.add("<数字>");
                                    return tab;
                                } else if (args[7].equals("set")) {
                                    List<String> tab = new ArrayList<>();
                                    tab.add("<数字>");
                                    return tab;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}

