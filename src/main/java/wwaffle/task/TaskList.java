package wwaffle.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Owns and manages the user's collection of tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates a task list containing tasks loaded from storage.
     *
     * @param tasks Initial tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index Zero-based task index.
     * @return Task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Deletes and returns the task at the specified zero-based index.
     *
     * @param index Zero-based task index.
     * @return Deleted task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a read-only view suitable for display and persistence.
     *
     * @return Unmodifiable task view.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
