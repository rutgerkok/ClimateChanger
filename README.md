# ClimateChanger

Little tool to change biome ids in an existing Minecraft map.

## Usage
Execute the command `java -jar ClimateChanger.jar` should start the JAR. A window should pop up.

Windows users can right click the jar and select Open with -> Java.

The program shouldn't be too difficult to use: just select the region folder (not the world folder), enter the biome ids and press start.

## Download
[See here](https://github.com/rutgerkok/ClimateChanger/releases).

### Command line
On a headless server, or just prefer to type commands? Start the program like this:

`jar -jar ClimateChanger.jar <regionFolder> <idFrom> <idTo>`

* `regionFolder` is the relative or absolute path to the region folder of the world.
* `idFrom` is the original biome id that will be replaced. Use the id `-1` to replace all biomes.`
* `idTo` is the new biome id.

## License
The NBT files and the `RegionFile.java` are opensourced files created by Mojang. The have a `don't do evil` license.

All other files are public domain. Do whatever you want with it.
