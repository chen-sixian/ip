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
     * @param tasks initial tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public Task delete(int index) {
        return tasks.remove(index);
    }

    public int size() {
        return tasks.size();
    }

    /**
     * Returns a read-only view suitable for display and persistence.
     *
     * @return unmodifiable task view
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
