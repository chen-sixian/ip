package wwaffle.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
        assert tasks != null : "Initial task list must not be null";
        assert !tasks.contains(null) : "Initial task list must not contain null tasks";
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        assert task != null : "Task to add must not be null";
        tasks.add(task);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index Zero-based task index.
     * @return Task at the specified index.
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "Task index must be validated before retrieval";
        return tasks.get(index);
    }

    /**
     * Deletes and returns the task at the specified zero-based index.
     *
     * @param index Zero-based task index.
     * @return Deleted task.
     */
    public Task delete(int index) {
        assert index >= 0 && index < tasks.size() : "Task index must be validated before deletion";
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Finds tasks whose descriptions contain the given keyword, ignoring case.
     *
     * @param keyword Text to find in task descriptions.
     * @return Matching tasks in their original order.
     */
    public List<Task> find(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matches.add(task);
            }
        }
        return Collections.unmodifiableList(matches);
    }

    /**
     * Returns a read-only view suitable for display and persistence.
     *
     * @return Unmodifiable task view.
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }
}
