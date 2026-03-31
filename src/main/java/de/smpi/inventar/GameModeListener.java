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
        
        // Nur für Spieler mit Permission
        if (!player.hasPermission("smpinventar.use")) {
            return;
        }
        
        // Lade gespeicherte Inventare
        inventoryManager.loadPlayerData(player.getUniqueId());
        
        // Lade das richtige Inventar basierend auf aktuellem GameMode
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            inventoryManager.loadInventory(player, player.getGameMode());
        }, 1L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        
        // Nur für Spieler mit Permission
        if (!player.hasPermission("smpinventar.use")) {
            return;
        }
        
        GameMode fromMode = player.getGameMode();
        GameMode toMode = event.getNewGameMode();

        // Ignoriere wenn der GameMode gleich bleibt
        if (fromMode == toMode) {
            return;
        }

        // Speichere das aktuelle Inventar
        inventoryManager.saveInventory(player, fromMode);

        // Lade das Inventar für den neuen GameMode
        // Wird verzögert ausgeführt, damit der GameMode-Wechsel bereits erfolgt ist
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            inventoryManager.loadInventory(player, toMode);
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // Nur für Spieler mit Permission
        if (!player.hasPermission("smpinventar.use")) {
            return;
        }
        
        // Speichere aktuelles Inventar
        inventoryManager.saveInventory(player, player.getGameMode());
        // Speichere auf Festplatte
        inventoryManager.savePlayerData(player.getUniqueId());
        // Entferne aus RAM
        inventoryManager.clearPlayerData(player.getUniqueId());
    }
}
