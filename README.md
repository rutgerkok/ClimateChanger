# ClimateChanger

Tool to change biome and block ids in an existing Minecraft map.

![Screenshot of the tool](http://i.imgur.com/kkvn9ZA.png)

## Usage (GUI)
Execute the command `java -jar ClimateChanger.jar`. A window should pop up.

Instead of typing the command, on most operating systems you can also right click the jar and select Open with -> Java.

Click the `Browse...` button to select the `level.dat` of a world. Add one
or more actions and click `Execute tasks`. The actions will be executed on
*all* dimensions of the world.

## Download
[See here](https://github.com/rutgerkok/ClimateChanger/releases).

### Usage (command line)
On a headless server, or just prefer to type commands? Start the program like this:

`jar -jar ClimateChanger.jar <path/to/level.dat> <action> [and <action> [and ...]]`

`<path/to/level.dat>` is the relative or absolute path to the level.dat of
the world.

`<action>` is the actions to execute. It can be one of the following:

* `changeBiome <fromId> <toId>` changes all biomes with the `<fromId>` to
  the `<toId>`. If you use `-1` as the `<fromId>` all biomes will be
  changed to the given `<toId>`, so that you end up with a one-biome world.
* `changeBlock <fromId> <fromData> <toId> <toData>` changes all blocks
  with the id and data `<fromId> <fromData>` to `<toId> <toData>`.
  `<fromId>` and `<toId>` must be valid block ids or names.

You can combine multiple actions with the syntax
`<action> and <action> and <action>`.

## Notes about block id replacement
The program allows you to use both block ids and names. Modded block names are
supported; the `level.dat` file is read for `name<->id` mappings created by
Forge Mod Loader.

Block ids and data are replaced in:

* Blocks placed in the world.
* Blocks placed in player inventories.
* Blocks placed in Ender Chests.
* Blocks placed in tile entities that use the `Items` tag (in vanilla:
  chests, dispensers, droppers, hoppers, furnaces, brewing stands)
* Blocks placed in flower pots.
* Blocks being pushed by pistons.

## License
The NBT files and the `RegionFile.java` were open sourced by Mojang in 2011
and 2012. The have a `don't do evil` license. The files have been modified
for ClimateChanger.

All other files are public domain. Do whatever you want with it.
