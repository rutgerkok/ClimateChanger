package nl.rutgerkok.climatechanger.gui;

import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.hammer.anvil.AnvilWorld;
import nl.rutgerkok.hammer.util.Consumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class GuiInformation {
    public enum UpdateType {
        ADD_ELEMENT,
        ADD_SELECTED,
        REMOVE_ELEMENT,
        REMOVE_SELECTED;
    }

    private final Collection<Task> selectedTasks = new ArrayList<Task>();

    private final List<Consumer<UpdateType>> taskChangeListeners = new CopyOnWriteArrayList<>();
    private final List<Task> tasks = new ArrayList<>();
    private AnvilWorld world = null;
    private final List<Runnable> worldChangeListeners = new CopyOnWriteArrayList<>();

    /**
     * Adds a task to the task list.
     *
     * @param task
     *            The task to add.
     * @throws NullPointerException
     *             If the task is null.
     */
    public void addTask(Task task) {
        tasks.add(Objects.requireNonNull(task));

        callTaskListChangeListeners(UpdateType.ADD_ELEMENT);
    }

    private void callTaskListChangeListeners(UpdateType type) {
        for (Consumer<UpdateType> listener : taskChangeListeners) {
            listener.accept(type);
        }
    }

    /**
     * Gets all currently selected tasks.
     *
     * @return All currently selected tasks.
     */
    public Collection<Task> getSelectedTasks() {
        return Collections.unmodifiableCollection(new HashSet<>(selectedTasks));
    }

    /**
     * Gets all tasks that must be executed. List will be immutable.
     *
     * @return All tasks.
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(new ArrayList<>(tasks));
    }

    /**
     * Gets the world, may be null.
     *
     * @return The world, may be null.
     */
    public AnvilWorld getWorld() {
        return world;
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
    public boolean isSelected(Task task) throws IllegalArgumentException {
        if (!tasks.contains(task)) {
            throw new IllegalArgumentException("Task is not registered");
        }
        return selectedTasks.contains(task);
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
    public void markSelected(Task task) throws IllegalArgumentException {
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
    public void markUnselected(Task task) throws IllegalArgumentException {
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
    public void removeTask(Task task) throws IllegalArgumentException {
        if (selectedTasks.contains(task)) {
            markUnselected(task);
        }
        if (!tasks.remove(task)) {
            throw new IllegalArgumentException(task + " not in task list " + tasks);
        }
        callTaskListChangeListeners(UpdateType.REMOVE_ELEMENT);
    }

    /**
     * Sets the world, may be null.
     *
     * @param world
     *            The world, may be null.
     */
    public void setWorld(AnvilWorld world) {
        this.world = world;
        for (Runnable runnable : worldChangeListeners) {
            runnable.run();
        }

        // New world, remove all tasks
        for (Task task : getTasks()) {
            removeTask(task);
        }
    }

    /**
     * Adds a consumer that will be run after a task has been
     * added/removed/selected/unselected.
     *
     * @param consumer
     *            The consumer to run.
     */
    public void subscribeToTaskChanges(Consumer<UpdateType> consumer) {
        taskChangeListeners.add(consumer);
    }

    /**
     * Adds a runnable that will be run after {@link #setWorld(AnvilWorld)} has
     * been called.
     *
     * @param runnable
     *            The runnable to run.
     */
    public void subscribeToWorldChanges(Runnable runnable) {
        worldChangeListeners.add(runnable);
    }
}
