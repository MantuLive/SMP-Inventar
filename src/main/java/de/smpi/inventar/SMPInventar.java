package de.smpi.inventar;

import org.bukkit.plugin.java.JavaPlugin;

public class SMPInventar extends JavaPlugin {

    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        inventoryManager = new InventoryManager(this);
        
        getServer().getPluginManager().registerEvents(new GameModeListener(inventoryManager, this), this);
    }

    @Override
    public void onDisable() {
        if (inventoryManager != null) {
            inventoryManager.saveAllInventories();
        }
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
