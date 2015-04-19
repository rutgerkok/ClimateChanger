package nl.rutgerkok.climatechanger.converter;

import nl.rutgerkok.climatechanger.ProgressUpdater;
import nl.rutgerkok.climatechanger.task.ChunkTask;
import nl.rutgerkok.climatechanger.task.PlayerDataTask;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.hammer.PlayerFile;
import nl.rutgerkok.hammer.anvil.AnvilChunk;
import nl.rutgerkok.hammer.anvil.AnvilWorld;
import nl.rutgerkok.hammer.util.Progress;
import nl.rutgerkok.hammer.util.Result;
import nl.rutgerkok.hammer.util.Visitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class converts everything. It automatically distributes the given tasks
 * to the right converters.
 *
 */
public class ConverterExecutor {

    private final List<ChunkTask> chunkTasks;
    private final List<PlayerDataTask> playerTasks;
    private final ProgressUpdater updater;
    private final AnvilWorld world;

    public ConverterExecutor(ProgressUpdater updater, AnvilWorld world, Iterable<? extends Task> tasks) {
        this.world = Objects.requireNonNull(world);
        this.updater = Objects.requireNonNull(updater);

        this.chunkTasks = filterList(tasks, ChunkTask.class);
        this.playerTasks = filterList(tasks, PlayerDataTask.class);
    }

    /**
     * Starts the conversion process.
     */
    public void convertAll() {
        try {
            updater.init();
            world.walkAnvilChunks(new Visitor<AnvilChunk>() {
                @Override
                public Result accept(AnvilChunk chunk, Progress progress) {
                    updater.update(progress);
                    Result result = Result.NO_CHANGES;
                    for (ChunkTask chunkTask : chunkTasks) {
                        result = result.getCombined(chunkTask.convertChunk(chunk));
                    }
                    return result;
                }
            });
            world.walkPlayerFiles(new Visitor<PlayerFile>() {
                @Override
                public Result accept(PlayerFile playerFile, Progress progress) {
                    updater.update(progress);
                    Result result = Result.NO_CHANGES;
                    for (PlayerDataTask playerTask : playerTasks) {
                        result = result.getCombined(playerTask.convertPlayerFile(playerFile));
                    }
                    return result;
                }
            });
            updater.complete();
        } catch (IOException e) {
            updater.failed(e);
        }
    }

    /**
     * Returns a list with only the elements present in the given iterable that
     * are of the specified class.
     *
     * @param iterable
     *            Iterable containing elements of unknown type.
     * @param clazz
     *            Type of the new list.
     * @return The list with all elements of the iterable with the given class.
     */
    private <T> List<T> filterList(Iterable<?> iterable, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        for (Object object : iterable) {
            if (clazz.isInstance(object)) {
                @SuppressWarnings("unchecked")
                // Checked using clazz.isInstance
                T tObject = (T) object;
                result.add(tObject);
            }
        }
        return result;
    }

}
