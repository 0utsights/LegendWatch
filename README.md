# LegendWatch

A Hoplite client mod that shows a small icon next to players' nametags and in the tab list when they've crafted a legendary.

## How it works

LegendWatch listens to chat for legendary craft announcements and records who crafted what. The item's icon (or name if no icon exists) then appears next to that player's nametag above their head and in the tab list for the rest of the match. When the match ends, the data is cleared automatically.

## Features

- Shows legendary item icons on player nametags
- Shows legendary item icons in the tab list
- Toggle the mod on/off with a keybind
- Toggle icons/names mode with a separate keybind
- Both keybinds are unbound by default — set them in Options → Controls → LegendWatch

## Download

Available on CurseForge: https://legacy.curseforge.com/minecraft/mc-mods/legendwatch

## Supported Versions

| Minecraft | Fabric API |
|-----------|------------|
| 1.21.11   | ✅ |

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/)
2. Download [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download LegendWatch from [github](https://github.com/0utsights/LegendWatch/releases) OR [CurseForge](https://legacy.curseforge.com/minecraft/mc-mods/legendwatch)
4. Place both jars in your `.minecraft/mods/` folder
5. Launch Minecraft and join Hoplite

## Requirements

- Minecraft 1.21.10 or 1.21.11
- Fabric Loader 0.18.1+
- Fabric API

## Building from source

```bash
./gradlew build
```

Output jar will be in `build/libs/`.
