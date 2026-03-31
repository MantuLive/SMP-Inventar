package de.smpi.inventar;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GameModeListener implements Listener {

    private final InventoryManager inventoryManager;
    private final JavaPlugin plugin;

    public GameModeListener(InventoryManager inventoryManager, JavaPlugin plugin) {
        this.inventoryManager = inventoryManager;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        if (!player.hasPermission("smpinventar.use")) {
            return;
        }
        
        inventoryManager.loadPlayerData(player.getUniqueId());
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            inventoryManager.loadInventory(player, player.getGameMode());
        }, 1L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        
        if (!player.hasPermission("smpinventar.use")) {
            return;
        }
        
        GameMode fromMode = player.getGameMode();
        GameMode toMode = event.getNewGameMode();

        if (fromMode == toMode) {
            return;
        }

        inventoryManager.saveInventory(player, fromMode);

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            inventoryManager.loadInventory(player, toMode);
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        if (!player.hasPermission("smpinventar.use")) {
            return;
        }
        
        inventoryManager.saveInventory(player, player.getGameMode());
        inventoryManager.savePlayerData(player.getUniqueId());
        // Entferne aus RAM
        inventoryManager.clearPlayerData(player.getUniqueId());
    }
}
