package nl.rutgerkok.climatechanger;

/*
 * Little program to change the ids in a Minecraft map.
 *
 * RegionFile and the NBT classes are written by Mojang. See the headers of
 * those files for their respective licenses. All other code is public domain.
 * Do whatever you want with it.
 */

import nl.rutgerkok.climatechanger.gui.Window;
import nl.rutgerkok.climatechanger.task.ChunkTask;
import nl.rutgerkok.climatechanger.task.IdChanger;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Startup {
    public static void main(String[] args) {
        if (args.length != 3) {
            if (args.length == 0 && !GraphicsEnvironment.isHeadless()) {
                // Show gui instead
                new Window();
                return;
            }
            System.out.println("Usage: <regionFolderName> <idFrom> <idTo>");
            System.out.println("idFrom can be -1 to replace all biomes");
            System.out.println("idTo can be -1 to let Minecraft recalculate the biome");
            return;
        }

        File regionFolder = new File(args[0]);
        if (!regionFolder.exists() || !regionFolder.isDirectory() || !regionFolder.getName().equals("region")) {
            System.err.println("Please specify the path to a region folder, as the path");
            System.err.println(regionFolder.getAbsolutePath() + " is invalid.");
        }

        try {
            byte from = (byte) Short.parseShort(args[1]);
            byte to = (byte) Short.parseShort(args[2]);

            // Convert!
            List<? extends ChunkTask> tasks = Arrays.asList(new IdChanger(from, to));
            new Converter(new ConsoleProgressUpdater(), new File(args[0]), tasks).convert();
        } catch (NumberFormatException e) {
            System.err.println("Invalid biome id (use numeric ids).");
        }

    }
}
