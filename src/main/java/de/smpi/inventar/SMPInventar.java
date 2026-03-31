package de.smpi.inventar;

import org.bukkit.plugin.java.JavaPlugin;

public class SMPInventar extends JavaPlugin {

    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        // Initialize inventory manager
        inventoryManager = new InventoryManager(this);
        
        // Register event listener
        getServer().getPluginManager().registerEvents(new GameModeListener(inventoryManager, this), this);
    }

    @Override
    public void onDisable() {
        // Save all inventories before shutdown
        if (inventoryManager != null) {
            inventoryManager.saveAllInventories();
        }
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
