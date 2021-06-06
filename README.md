# ClimateChanger

This program was created originally to change the biome ids of your map. Since then, it has been expanded to make it a more general batch-editor for chunks. Only one chunk is loaded at a time, so the program has a small memory footprint.

Current features include:

* Change biome ids.
* Change block ids. Not only blocks placed in the world are replaced, but also blocks in chests, inventories and many other places. See the section below.
* Remove chunks that have been loaded for less than X minutes.
* Spawn ores in existing chunks.

![Screenshot of the tool](http://i.imgur.com/kkvn9ZA.png)

## Download
[See here](https://github.com/rutgerkok/ClimateChanger/releases).

## Compiling
Make sure you have installed [Maven](https://maven.apache.org/).

First, compile [Hammer](https://github.com/rutgerkok/Hammer). This library is required
for ClimateChanger to compile.
Then, download the source code of ClimateChanger (see the sidebar) and run the command
`mvn install` in the directory you have just downloaded ClimateChanger to. You should
end up with a runnable JAR file in the newly created `target` directory.

## Usage
Before you use any of the features, please read the notes about the feature below.

### Gui
Execute the command `java -jar ClimateChanger.jar`. A window should pop up.

Instead of typing the command, on most operating systems you can also right click the jar and select Open with -> Java.

Click the `Browse...` button to select the `level.dat` of a world. Add one
or more actions and click `Execute tasks`. The actions will be executed on
*all* dimensions of the world.

### Command line
On a headless server, or just prefer to type commands? Start the program like this:

`jar -jar ClimateChanger.jar <path/to/level.dat> <action> [and <action> [and ...]]`

`<path/to/level.dat>` is the relative or absolute path to the level.dat of
the world.

`<action>` is the actions to execute. It can be one of the following:

* `changeBiome <fromId> <toId>` changes all biomes with the `<fromId>` to
  the `<toId>`. If you use `-1` as the `<fromId>` all biomes will be
  changed to the given `<toId>`, so that you end up with a one-biome world.
* `changeBlock <fromNamespacedId> <toNamespacedId>` changes all blocks
  with the first id to the second.
* `spawnOre <namespacedId> <maxRadius> <attemptsPerChunk> <chancePerAttempt> <minAltitude> <maxAltitude> <spawnInBlock;anotherBlock,...>` spawns ores of the given type in each chunk.
* `deleteoldchunks <minutesPlayed>` will delete all chunks that have been loaded for less than `<minutesPlayed>`.

You can combine multiple actions with the syntax
`<action> and <action> and <action>`.

## Biome id replacement
This is the original feature of the program. It simply replaces the biome in the world. This is useful if you want to change the climate of a region: grass color, mobs spawning and weather will change. Actual blocks won't be changed.

## Block id replacement
The program allows you to use both block ids and names. Modded block names are
supported; the `level.dat` file is read for `name<->id` mappings created by
Forge Mod Loader.

Block ids and data are replaced in:

* Blocks placed in the world.
* Blocks placed in player inventories.
* Blocks placed in Ender Chests.
* Blocks placed in tile entities that use the `Items` tag (in vanilla:
  chests, dispensers, droppers, hoppers, furnaces, brewing stands)
* Blocks placed in entities that use the `Items` tag (in vanilla:
  horses, minecarts with chest)
* Blocks placed in entities that use the `Item` tag (in vanilla:
  items on the ground, item frames)
* Blocks being pushed by pistons.

## Ore spawning
Ores always spawn in stone. Chance per attempt is always 100. Please note that
in vanilla emeralds only spawn in the Extreme Hills biome.

In vanilla Minecraft attempts per chunk is called spawn tries. In the mod Terrain Control attempts per chunk is called frequency and chance per attempt is called rarity.

| Ore       | Max radius | Attempts per chunk | Altitude |
| --------- | ---------- | ------------------ | -------- |
| Dirt      | 33         | 10                 | 0 - 256  |
| Gravel    | 33         | 8                  | 0 - 256  |
| Granite   | 33         | 10                 | 0 - 80   |
| Diorite   | 33         | 10                 | 0 - 80   |
| Andesite  | 33         | 10                 | 0 - 80   |
| Coal      | 17         | 20                 | 0 - 128  |
| Iron      | 9          | 20                 | 0 - 64   |
| Gold      | 9          | 2                  | 0 - 32   |
| Redstone  | 8          | 8                  | 0 - 16   |
| Diamond   | 8          | 1                  | 0 - 16   |
| Lapis     | 8          | 1                  | 0 - 32   |
| Emerald   | 6          | 1                  | 4 - 32   |
| Tuff      | 33         | 1                  | 0 - 16   |
| Deepslate | 64         | 2                  | 0 - 16   |

## Sign conversion
Minecraft 1.8 doewsn't import the Minecraft 1.7 signs correctly. For example, the text `[Private]`, used by Lockette, gets read as a JSON-array with one element `Private`. Result: the first line of your sign now reads `Private` and the container is no longer protected!

If you have not opened your map on a 1.8 server yet you can use this program to convert all your signs correctly. If you have already opened your map on 1.8, this program can fix the signs. The program can detect which signs are imported for 1.7, so you only need to specify that `Private` should be `[Private]` and the program will do the rest. Signs created in 1.8 are not affected.

## License
The NBT files and the `RegionFile.java` were open sourced by Mojang in 2011
and 2012. The have a `don't do evil` license. The files have been modified
for ClimateChanger.

All other files are public domain. Do whatever you want with it.
