package com.kuroinusaba.kuroinuslotplugin_test;

import com.kuroinusaba.kuroinuslotplugin_test.Commands.kuroinuslot;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class KuroinuSlotPlugin_Test extends JavaPlugin {
    public static KuroinuSlotPlugin_Test plugin;
    private Listener listener;
    public static Economy econ = null;
    public static String prefix = "§7[§6KuroinuSlot§7]§r ";

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        try {
            getCommand("kuroinuslot").setExecutor(new kuroinuslot());
            getLogger().info("kuroinuslotコマンドを登録しました。");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            this.listener = new Listeners();
        } catch (Exception e) {
            getLogger().severe("Listenersのインスタンス化に失敗しました。");
            throw new RuntimeException(e);
        }
        getServer().getPluginManager().registerEvents(this.listener, this);
        if (!setupEconomy()) {
            getLogger().severe("Vaultが見つかりませんでした。");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholder(this).register();
        }
        plugin.saveDefaultConfig();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        super.onDisable();
        getLogger().info("プラグインを停止しました。");
    }
    private static Boolean setupEconomy() {
        if (getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null){
            return false;
        }else{
            econ = rsp.getProvider();
        }
        return econ != null;
    }
    private static KuroinuSlotPlugin_Test getPlugin() {
        return plugin;
    }
}
