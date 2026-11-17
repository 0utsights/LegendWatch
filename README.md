# LegendWatch

A Hoplite client mod that shows a small icon next to players' nametags and in the tab list when they've crafted a legendary.

## Download

Available on CurseForge: [https://curseforge.com/minecraft/mc-mods/legendwatch](https://www.curseforge.com/minecraft/mc-mods/legendwatch)
And Modrinth: [https://modrinth.com/mod/legendwatch](https://modrinth.com/mod/legendwatch)

## How it works

LegendWatch listens to chat for legendary craft announcements and records who crafted what. The item's icon (or name if no icon exists) then appears inside that player's nametag above their head and in the tab list for the rest of the match. When the match ends, the data is cleared automatically.

## Features

- Shows legendary item icons (or names) next to player nametags above their head
- Shows legendary item icons (or names) in the tab list
- Tracks legendary transfers — when a player is eliminated, their legendaries move to the killer's tag as predicted (shown with a `?`)
- Toggle the mod on/off with a keybind
- Toggle between icons and names-only mode with a separate keybind
- Both keybinds are unbound by default — set them in Options → Controls → LegendWatch


## Supported Versions

| Minecraft | Fabric API |
|-----------|------------|
| 1.21.11   | ✅ |
| 26.2      | ✅ |
| 26.3      | ✅ |

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/)
2. Download [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download LegendWatch from [Github](https://github.com/0utsights/LegendWatch/releases) OR [CurseForge](https://legacy.curseforge.com/minecraft/mc-mods/legendwatch)
4. Place both jars in your `.minecraft/mods/` folder
5. Launch Minecraft and join Hoplite

## Requirements

- Minecraft 1.21.11, 26.2, or 26.3
- Fabric Loader 0.18.4+ for 1.21.11; 0.19.5+ for 26.2 and 26.3
- Fabric API
- Java 21 for 1.21.11; Java 25 for 26.2 and 26.3

## Building from source

```bash
./gradlew chiseledBuild
```

Output jars will be in `versions/<minecraft-version>/build/libs/`.
To build only Minecraft 26.3, run `./gradlew :26.3:build`.
