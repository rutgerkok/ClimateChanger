package nl.rutgerkok.climatechanger;

import nl.rutgerkok.climatechanger.converter.ConverterExecutor;
import nl.rutgerkok.climatechanger.gui.GuiInformation;
import nl.rutgerkok.climatechanger.gui.Window;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;
import nl.rutgerkok.hammer.anvil.AnvilWorld;
import nl.rutgerkok.hammer.material.GlobalMaterialMap;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Little program to change the ids in a Minecraft map.
 *
 * RegionFile and the NBT classes are written by Mojang. See the headers of
 * those files for their respective licenses. All other code is public domain.
 * Do whatever you want with it.
 */
public class Startup {

    public static final String NAME = "ClimateChanger";

    public static void main(String[] args) {
        if (args.length == 0 && !GraphicsEnvironment.isHeadless()) {

            // Show gui instead
            new Window(new GuiInformation());
            return;

        }

        LineParser parser = new LineParser();
        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
            showHelp(parser);
            return;
        }

        Path levelDat = new File(args[0]).toPath();
        if (!Files.isRegularFile(levelDat) || !levelDat.getFileName().toString().equals(AnvilWorld.LEVEL_DAT_NAME)) {
            System.err.println("Please specify the path to the level.dat, as the path");
            System.err.println(levelDat + " is invalid.");
            System.exit(1);
            return;
        }

        try {
            GlobalMaterialMap materialMap = new GlobalMaterialMap();
            AnvilWorld world = new AnvilWorld(materialMap, levelDat);
            List<String> strings = Arrays.asList(args).subList(1, args.length);
            // Convert!
            List<Task> tasks = parser.parse(materialMap, strings);
            new ConverterExecutor(new ConsoleProgressUpdater(), world, tasks).convertAll();
        } catch (ParseException | InvalidTaskException e) {
            System.err.println("Invalid syntax: " + e.getMessage());
            showHelp(parser);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Cannot read " + AnvilWorld.LEVEL_DAT_NAME);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void showHelp(LineParser parser) {
        System.out.println("Usage: <path/to/level.dat> <action> [and <action> [and ...]]");
        System.out.println("<action> can be:");
        for (String helpLine : parser.getActionsHelp()) {
            System.out.print("*  ");
            System.out.println(helpLine);
        }
    }
}
