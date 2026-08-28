package wwaffle.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class TaskListTest {
    @Test
    void add_newTask_increasesSizeAndStoresTask() {
        TaskList tasks = new TaskList(new ArrayList<>());
        Todo todo = new Todo("read book");

        tasks.add(todo);

        assertEquals(1, tasks.size());
        assertEquals(todo, tasks.get(0));
    }

    @Test
    void delete_existingTask_removesAndReturnsTask() {
        ArrayList<Task> initialTasks = new ArrayList<>();
        Todo todo = new Todo("read book");
        initialTasks.add(todo);
        TaskList tasks = new TaskList(initialTasks);

        Task removedTask = tasks.delete(0);

        assertEquals(todo, removedTask);
        assertEquals(0, tasks.size());
    }

    @Test
    void asList_returnedView_cannotBeModified() {
        TaskList tasks = new TaskList(new ArrayList<>());

        assertThrows(UnsupportedOperationException.class,
                () -> tasks.asList().add(new Todo("read book")));
    }
}
