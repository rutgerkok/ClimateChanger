package nl.rutgerkok.climatechanger.converter;

import nl.rutgerkok.climatechanger.ProgressUpdater;
import nl.rutgerkok.climatechanger.World;
import nl.rutgerkok.climatechanger.task.ChunkTask;
import nl.rutgerkok.climatechanger.task.PlayerDataTask;
import nl.rutgerkok.climatechanger.task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * This class converts everything. It automatically distributes the given tasks
 * to the right converters.
 *
 */
public class ConverterExecutor {

    private final Collection<Converter> converters = new ArrayList<>();
    private final ProgressUpdater updater;
    private final World world;

    public ConverterExecutor(ProgressUpdater updater, World world, Iterable<? extends Task> tasks) {
        this.world = Objects.requireNonNull(world);
        this.updater = Objects.requireNonNull(updater);

        List<ChunkTask> chunkTasks = filterList(tasks, ChunkTask.class);
        if (!chunkTasks.isEmpty()) {
            converters.add(new ChunkConverter(world, chunkTasks));
        }
        List<PlayerDataTask> playerTasks = filterList(tasks, PlayerDataTask.class);
        if (!playerTasks.isEmpty()) {
            converters.add(new PlayerDataConverter(world, playerTasks));
        }
    }

    /**
     * Starts the conversion process.
     */
    public void convertAll() {
        try {
            updater.init(getUnitsToConvert());
            for (Converter converter : converters) {
                converter.convert(updater);
            }
            world.saveLevelDatIfNeeded();
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

    /**
     * Gets the sum of the units that each converter needs to convert.
     *
     * @return Total amount of units.
     * @throws IOException
     *             When an IO error occurs.
     */
    private int getUnitsToConvert() throws IOException {
        int sum = 0;
        for (Converter converter : converters) {
            sum += converter.getUnitsToConvert();
        }
        return sum;
    }

}
