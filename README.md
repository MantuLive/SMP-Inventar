# SMP-Inventar Plugin

Ein Paper Plugin für Minecraft 1.21+ das separate Inventare für Creative und Survival Modi bereitstellt.

## Features

- **Separate Inventare**: Creative Mode erhält ein eigenes leeres Inventar
- **Automatisches Speichern**: Beim Wechsel zwischen GameModes wird das Inventar automatisch gespeichert
- **Automatisches Laden**: Beim Zurückwechseln wird das vorherige Inventar wiederhergestellt
- **Armor-Support**: Auch Rüstungen werden separat gespeichert
- **Persistent**: Inventare bleiben auch nach Logout und Server-Neustart erhalten
- **Lautlos**: Keine Chat-Nachrichten oder Benachrichtigungen
- **Keine Befehle**: Funktioniert vollautomatisch

## Funktionsweise

1. **Survival → Creative**: Das Survival-Inventar wird gespeichert, das Creative-Inventar wird geladen (oder leer falls neu)
2. **Creative → Survival**: Das Creative-Inventar wird gespeichert, das Survival-Inventar wird wiederhergestellt
3. **Logout/Login**: Inventare werden beim Verlassen gespeichert und beim Betreten wiederhergestellt
4. **Server-Neustart**: Alle Inventare bleiben in YAML-Dateien erhalten

## Installation

1. Baue das Plugin mit Maven:
   ```bash
   mvn clean package
   ```

2. Die fertige JAR-Datei findest du in `target/SMP-Inventar-1.0.0.jar`

3. Kopiere die JAR-Datei in den `plugins` Ordner deines Paper-Servers

4. Starte den Server neu oder lade das Plugin mit `/reload confirm`

## Anforderungen

- **Minecraft Version**: 1.21 oder neuer
- **Server Software**: Paper (oder Forks wie Purpur, Pufferfish)
- **Java Version**: 21 oder neuer

## Speicherung

Das Plugin speichert die Inventare in YAML-Dateien unter `plugins/SMP-Inventar/inventories/`. 
Jeder Spieler erhält eine eigene Datei (UUID.yml) die beide Inventare (Creative & Survival) enthält.

## Konfiguration

Das Plugin benötigt keine weitere Konfiguration und funktioniert direkt nach der Installation.

## Entwicklung

### Projekt-Struktur
```
SMP-Inventar/
├── src/
│   └── main/
│       ├── java/de/smpi/inventar/
│       │   ├── SMPInventar.java          # Main Plugin Klasse
│       │   ├── InventoryManager.java     # Inventar-Verwaltung
│       │   └── GameModeListener.java     # Event Listener
│       └── resources/
│           └── plugin.yml                # Plugin Metadaten
└── pom.xml                               # Maven Konfiguration
```

### Bauen

Mit Maven:
```bash
mvn clean package
```

Die kompilierte JAR-Datei wird in `target/` erstellt.

## Support

Bei Problemen oder Fragen erstelle bitte ein Issue im GitHub Repository.

## Lizenz

Dieses Projekt ist Open Source. Verwendung auf eigene Verantwortung.
