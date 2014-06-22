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

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class Startup {
    public static void main(String[] args) {
        if (args.length == 0 && !GraphicsEnvironment.isHeadless()) {

            // Show gui instead
            new Window();
            return;

        }

        LineParser parser = new LineParser();
        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
            showHelp(parser);
            return;
        }

        File regionFolder = new File(args[0]);
        if (!regionFolder.exists() || !regionFolder.isDirectory() || !regionFolder.getName().equals("region")) {
            System.err.println("Please specify the path to a region folder, as the path");
            System.err.println(regionFolder.getAbsolutePath() + " is invalid.");
            System.exit(1);
            return;
        }

        try {
            List<String> strings = Arrays.asList(args).subList(1, args.length);
            // Convert!
            List<ChunkTask> tasks = parser.parse(strings);
            new Converter(new ConsoleProgressUpdater(), new File(args[0]), tasks).convert();
        } catch (ParseException e) {
            System.err.println("Invalid syntax: " + e.getMessage());
            showHelp(parser);
            System.exit(1);
        }
    }

    private static void showHelp(LineParser parser) {
        System.out.println("Usage: <regionFolder> <action> [and <action> [and ...]]");
        System.out.println("<action> can be:");
        for (String helpLine : parser.getActionsHelp()) {
            System.out.print("*  ");
            System.out.println(helpLine);
        }
    }
}
