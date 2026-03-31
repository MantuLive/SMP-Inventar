package de.smpi.inventar;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryManager {

    private final JavaPlugin plugin;
    private final File databaseFile;
    private YamlConfiguration database;

    // Speichert Survival-Inventare: UUID -> ItemStack Array
    private final Map<UUID, ItemStack[]> survivalInventories = new HashMap<>();
    
    // Speichert Survival-Armor: UUID -> ItemStack Array
    private final Map<UUID, ItemStack[]> survivalArmor = new HashMap<>();
    
    // Speichert Creative-Inventare: UUID -> ItemStack Array
    private final Map<UUID, ItemStack[]> creativeInventories = new HashMap<>();
    
    // Speichert Creative-Armor: UUID -> ItemStack Array
    private final Map<UUID, ItemStack[]> creativeArmor = new HashMap<>();

    public InventoryManager(JavaPlugin plugin) {
        this.plugin = plugin;
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        this.databaseFile = new File(plugin.getDataFolder(), "database.yml");
        loadDatabase();
    }
    
    private void loadDatabase() {
        if (!databaseFile.exists()) {
            database = new YamlConfiguration();
        } else {
            database = YamlConfiguration.loadConfiguration(databaseFile);
        }
    }
    
    private void saveDatabase() {
        try {
            database.save(databaseFile);
        } catch (IOException e) {
            // Silent fail
        }
    }

    public void saveInventory(Player player, GameMode fromMode) {
        UUID playerId = player.getUniqueId();
        ItemStack[] inventory = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();

        if (fromMode == GameMode.SURVIVAL || fromMode == GameMode.ADVENTURE) {
            survivalInventories.put(playerId, inventory);
            survivalArmor.put(playerId, armor);
        } else if (fromMode == GameMode.CREATIVE) {
            creativeInventories.put(playerId, inventory);
            creativeArmor.put(playerId, armor);
        }
    }

    public void loadInventory(Player player, GameMode toMode) {
        UUID playerId = player.getUniqueId();
        ItemStack[] inventory;
        ItemStack[] armor;

        if (toMode == GameMode.SURVIVAL || toMode == GameMode.ADVENTURE) {
            // Lade Survival Inventar
            inventory = survivalInventories.get(playerId);
            armor = survivalArmor.get(playerId);
        } else if (toMode == GameMode.CREATIVE) {
            // Lade Creative Inventar (oder leer falls nicht vorhanden)
            inventory = creativeInventories.get(playerId);
            armor = creativeArmor.get(playerId);
        } else {
            // Spectator oder andere Modi - kein Inventar laden
            return;
        }

        // Wende das Inventar an
        if (inventory != null) {
            player.getInventory().setContents(inventory);
        } else {
            player.getInventory().clear();
        }

        if (armor != null) {
            player.getInventory().setArmorContents(armor);
        } else {
            player.getInventory().setArmorContents(new ItemStack[4]);
        }
    }

    public void savePlayerData(UUID playerId) {
        String path = playerId.toString();

        ItemStack[] survInv = survivalInventories.get(playerId);
        ItemStack[] survArm = survivalArmor.get(playerId);
        ItemStack[] creaInv = creativeInventories.get(playerId);
        ItemStack[] creaArm = creativeArmor.get(playerId);

        if (survInv != null) {
            database.set(path + ".survival.inventory", survInv);
        }
        if (survArm != null) {
            database.set(path + ".survival.armor", survArm);
        }
        if (creaInv != null) {
            database.set(path + ".creative.inventory", creaInv);
        }
        if (creaArm != null) {
            database.set(path + ".creative.armor", creaArm);
        }

        saveDatabase();
    }

    public void loadPlayerData(UUID playerId) {
        String path = playerId.toString();

        if (!database.contains(path)) {
            return;
        }

        if (database.contains(path + ".survival.inventory")) {
            java.util.List<?> list = database.getList(path + ".survival.inventory");
            if (list != null) {
                ItemStack[] items = new ItemStack[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    items[i] = (ItemStack) list.get(i);
                }
                survivalInventories.put(playerId, items);
            }
        }
        if (database.contains(path + ".survival.armor")) {
            java.util.List<?> list = database.getList(path + ".survival.armor");
            if (list != null) {
                ItemStack[] items = new ItemStack[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    items[i] = (ItemStack) list.get(i);
                }
                survivalArmor.put(playerId, items);
            }
        }
        if (database.contains(path + ".creative.inventory")) {
            java.util.List<?> list = database.getList(path + ".creative.inventory");
            if (list != null) {
                ItemStack[] items = new ItemStack[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    items[i] = (ItemStack) list.get(i);
                }
                creativeInventories.put(playerId, items);
            }
        }
        if (database.contains(path + ".creative.armor")) {
            java.util.List<?> list = database.getList(path + ".creative.armor");
            if (list != null) {
                ItemStack[] items = new ItemStack[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    items[i] = (ItemStack) list.get(i);
                }
                creativeArmor.put(playerId, items);
            }
        }
    }

    public void saveAllInventories() {
        for (UUID playerId : survivalInventories.keySet()) {
            savePlayerData(playerId);
        }
        for (UUID playerId : creativeInventories.keySet()) {
            savePlayerData(playerId);
        }
    }

    public void clearPlayerData(UUID playerId) {
        survivalInventories.remove(playerId);
        survivalArmor.remove(playerId);
        creativeInventories.remove(playerId);
        creativeArmor.remove(playerId);
    }
}
