package nl.rutgerkok.climatechanger.gui;

import nl.rutgerkok.climatechanger.task.ChunkTask;
import nl.rutgerkok.climatechanger.util.Consumer;
import nl.rutgerkok.climatechanger.util.Supplier;

import java.io.File;
import java.util.*;

public class GuiInformation {
    public enum UpdateType {
        ADD_ELEMENT,
        ADD_SELECTED,
        REMOVE_ELEMENT,
        REMOVE_SELECTED;
    }

    private Supplier<File> folder = new Supplier<File>() {
        @Override
        public File get() {
            return null;
        }
    };

    private final Collection<ChunkTask> selectedTasks = new ArrayList<ChunkTask>();
    private final List<Consumer<UpdateType>> taskChangeListeners = new ArrayList<>();
    private final List<ChunkTask> tasks = new ArrayList<>();

    /**
     * Adds a task to the task list.
     * 
     * @param task
     *            The task to add.
     * @throws NullPointerException
     *             If the task is null.
     */
    public void addTask(ChunkTask task) {
        tasks.add(Objects.requireNonNull(task));

        callTaskListChangeListeners(UpdateType.ADD_ELEMENT);
    }

    private void callTaskListChangeListeners(UpdateType type) {
        for (Consumer<UpdateType> listener : taskChangeListeners) {
            listener.accept(type);
        }
    }

    /**
     * Gets the region folder, may be null or invalid.
     * 
     * @return The region folder, may be null or invalid.
     */
    public File getRegionDirectory() {
        return folder.get();
    }

    /**
     * Gets all currently selected tasks.
     * 
     * @return All currently selected tasks.
     */
    public Collection<ChunkTask> getSelectedTasks() {
        return Collections.unmodifiableCollection(new HashSet<>(selectedTasks));
    }

    /**
     * Gets all tasks that must be executed. List will be immutable.
     * 
     * @return All tasks.
     */
    public List<ChunkTask> getTasks() {
        return Collections.unmodifiableList(new ArrayList<>(tasks));
    }

    /**
     * Gets whether the given tasks is selected.
     * 
     * @param task
     *            The task.
     * @return Whether the task is selected.
     * @throws IllegalArgumentException
     *             If the task is not registered.
     */
    public boolean isSelected(ChunkTask task) throws IllegalArgumentException {
        if (!tasks.contains(task)) {
            throw new IllegalArgumentException("Task is not registered");
        }
        return selectedTasks.contains(task);
    }

    /**
     * Marks all selected tasks as unselected tasks. Does nothing if no tasks
     * were selected.
     */
    public void markAllUnselected() {
        selectedTasks.clear();
    }

    /**
     * Marks a task as selected.
     * 
     * @param task
     *            The task to mark.
     * @throws IllegalArgumentException
     *             If the task is not in the {@link #getTasks() task list}.
     * @throws IllegalArgumentException
     *             If the task already exists.
     * @throws NullPointerException
     *             If task is null.
     */
    public void markSelected(ChunkTask task) throws IllegalArgumentException {
        if (!tasks.contains(task)) {
            throw new IllegalArgumentException(task + " is not in task list");
        }
        if (selectedTasks.contains(task)) {
            throw new IllegalArgumentException(task + " is already selected");
        }

        selectedTasks.add(Objects.requireNonNull(task));

        callTaskListChangeListeners(UpdateType.ADD_SELECTED);
    }

    /**
     * Marks a selected task as unselected.
     * 
     * @param task
     *            The task to mark.
     * @throws IllegalArgumentException
     *             If the task is not selected.
     */
    public void markUnselected(ChunkTask task) throws IllegalArgumentException {
        if (!tasks.contains(task)) {
            throw new IllegalArgumentException(task + " is not in task list");
        }
        if (!selectedTasks.remove(task)) {
            throw new IllegalArgumentException(task + " is not selected");
        }

        callTaskListChangeListeners(UpdateType.REMOVE_SELECTED);
    }

    /**
     * Removes a task.
     * 
     * @param task
     *            The task to remove.
     * @throws IllegalArgumentException
     *             If this task was not in the task list.
     */
    public void removeTask(ChunkTask task) throws IllegalArgumentException {
        if (!tasks.remove(task)) {
            throw new IllegalArgumentException(task + " not in task list " + tasks);
        }
        selectedTasks.remove(task);
        callTaskListChangeListeners(UpdateType.REMOVE_ELEMENT);
    }

    /**
     * Sets a supplier for the region folder. The supplier may return null or
     * invalid files. The supplier itself may not be null.
     * 
     * @param file
     *            The region folder provider.
     */
    public void setRegionDirectory(Supplier<File> file) {
        this.folder = Objects.requireNonNull(file);
    }

    /**
     * Adds a runnable that will be run after a task has been
     * added/removed/selected/unselected.
     * 
     * @param runnable
     *            The runnable to run.
     */
    public void subscribeToTaskChanges(Consumer<UpdateType> runnable) {
        taskChangeListeners.add(runnable);
    }
}
