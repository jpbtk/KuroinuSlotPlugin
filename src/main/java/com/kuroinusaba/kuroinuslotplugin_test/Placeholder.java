package com.kuroinusaba.kuroinuslotplugin_test;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Placeholder extends PlaceholderExpansion {
    private final KuroinuSlotPlugin_Test plugin;
    public Placeholder(KuroinuSlotPlugin_Test plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "Match_bou";
    }

    @Override
    public String getIdentifier() {
        return "kuroinuslotplugin";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        String name = player.getName();
        File ranking = new File("plugins/KuroinuSlotPlugin_Test/ranking.yml");
        YamlConfiguration rankingData = YamlConfiguration.loadConfiguration(ranking);
        if(params.startsWith("ranking_")) {
            // ranking_の後に数字が来ることを想定している
            String[] split = params.split("_");
            int rank = Integer.parseInt(split[1]);
            List<Integer> money = rankingData.getIntegerList("money");
            if(money.size() < rank) {
                return "データなし?";
            }
            String money2 = String.valueOf(money.get(rank - 1));
            // 3桁区切りにする
            StringBuilder sb = new StringBuilder(money2);
            for(int i = money2.length() - 3; i > 0; i -= 3) {
                sb.insert(i, ",");
            }
            return sb.toString();
        }
        if(params.startsWith("rankingname_")) {
            // rankingname_の後に数字が来ることを想定している
            String[] split = params.split("_");
            int rank = Integer.parseInt(split[1]);
            List<String> nameList = rankingData.getStringList("names");
            if(nameList.size() < rank) {
                return "データなし?";
            }
            return nameList.get(rank - 1);
        }
        if(params.startsWith("pf_")) {
            String[] split = params.split("_");
            int pf = Integer.parseInt(split[1]);
            List<Integer> usedmoney = rankingData.getIntegerList("usedmoney");
            List<Integer> money = rankingData.getIntegerList("money");
            if(usedmoney.size() < pf) {
                return "データなし?";
            }
            // 少数第3位まで表示
            return String.format("%.3f", (double)money.get(pf - 1) / usedmoney.get(pf - 1));
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
