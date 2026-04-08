# One Block Survival

A Minecraft Fabric mod where the entire world is a single block floating in the void. When you break it, it gives a random block or item and respawns as a new random block. Over time you accumulate resources to build a platform and survive.

## Features

- Single block spawns at coordinates (0, 64, 0)
- Breaking the block drops its resources normally
- Block immediately respawns as a new random block
- 30% chance for bonus item drops
- Progressive difficulty - better blocks become more common as you break more
- Special blocks include spawners, lava, water, and TNT
- Blocks broken counter displayed on screen

## Block Tiers

- **Common**: Dirt, Cobblestone, Oak Log, Sand, Gravel, Clay
- **Uncommon**: Iron Ore, Coal Ore, Copper Ore, Gold Ore, Lapis Ore, Pumpkin, Melon
- **Rare**: Diamond Ore, Emerald Ore, Obsidian, Glowstone, Bookshelf, Chest
- **Special**: Spawner, Lava, Water, TNT

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.16+
- Fabric API

## Installation

1. Install Fabric for Minecraft 1.21.1
2. Download the JAR from `build/libs/`
3. Place it in your `.minecraft/mods/` folder
4. Launch Minecraft with the Fabric profile

## Building

```bash
gradle build
```

The built JAR will be in `build/libs/`.

## How to Play

1. Create a new world (Superflat void recommended)
2. Navigate to coordinates (0, 64, 0)
3. Start breaking the one block!
4. Collect resources and build your survival platform
5. Watch out for special blocks like lava and TNT!
