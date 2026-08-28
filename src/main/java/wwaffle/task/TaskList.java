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
     * Finds tasks whose descriptions contain the given keyword, ignoring case.
     *
     * @param keyword text to find in task descriptions
     * @return matching tasks in their original order
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
     * @return unmodifiable task view
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
