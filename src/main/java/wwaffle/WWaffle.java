package wwaffle;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import wwaffle.command.CommandType;
import wwaffle.command.Parser;
import wwaffle.exception.WWaffleException;
import wwaffle.storage.Storage;
import wwaffle.task.Deadline;
import wwaffle.task.Event;
import wwaffle.task.Task;
import wwaffle.task.TaskList;
import wwaffle.task.Todo;
import wwaffle.ui.Ui;

/**
 * Coordinates user input, task management, and persistent storage.
 */
public class WWaffle {
    private static final String DEADLINE_SEPARATOR = " /by ";
    private static final String EVENT_START_SEPARATOR = " /from ";
    private static final String EVENT_END_SEPARATOR = " /to ";

    private final Parser parser;
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates WWaffle using the specified save file.
     *
     * @param filePath Path of the task data file.
     */
    public WWaffle(String filePath) {
        this.ui = new Ui();
        this.parser = new Parser();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList(loadTasks());
    }

    /**
     * Starts WWaffle using the default task data file.
     *
     * @param args Command-line arguments; unused by WWaffle.
     */
    public static void main(String[] args) {
        new WWaffle("./data/wwaffle.txt").run();
    }

    /**
     * Runs the command loop until the user exits.
     */
    public void run() {
        ui.showWelcome(tasks.getSize());
        String command = ui.readCommand();

        while (parser.parseCommandType(command) != CommandType.BYE) {
            try {
                executeInTerminal(command);
            } catch (WWaffleException | IOException e) {
                ui.showError(e.getMessage());
            }
            command = ui.readCommand();
        }
        ui.showExit();
    }

    /**
     * Returns the welcome message used by graphical interfaces.
     *
     * @return Welcome message containing the number of loaded tasks.
     */
    public String getWelcomeMessage() {
        return ui.getWelcomeMessage(tasks.getSize());
    }

    /**
     * Returns the current number of tasks.
     *
     * @return Number of tasks stored by WWaffle.
     */
    public int getTaskCount() {
        return tasks.getSize();
    }

    /**
     * Executes a command and returns a plain-text response for a graphical interface.
     *
     * @param command Command entered by the user.
     * @return User-facing response to the command.
     */
    public String getResponse(String command) {
        try {
            return switch (parser.parseCommandType(command)) {
                case LIST -> ui.getTaskListMessage(tasks.getTasks());
                case SORT -> ui.getTaskListMessage(sortTasks(command));
                case MARK -> ui.getTaskMarkedMessage(markTask(command, true), true);
                case UNMARK -> ui.getTaskMarkedMessage(markTask(command, false), false);
                case DELETE -> ui.getTaskDeletedMessage(deleteTask(command), tasks.getSize());
                case TODO -> ui.getTaskAddedMessage(addTodo(command), tasks.getSize());
                case DEADLINE -> ui.getTaskAddedMessage(addDeadline(command), tasks.getSize());
                case EVENT -> ui.getTaskAddedMessage(addEvent(command), tasks.getSize());
                case FIND -> ui.getMatchingTasksMessage(findTasks(command));
                case BYE -> ui.getExitMessage();
                default -> throw new WWaffleException("Unknown command.");
            };
        } catch (WWaffleException | IOException e) {
            return ui.getErrorMessage(e.getMessage());
        }
    }

    private ArrayList<Task> loadTasks() {
        try {
            return storage.load();
        } catch (IOException e) {
            ui.showLoadingError();
            return new ArrayList<>();
        }
    }

    private void executeInTerminal(String command) throws WWaffleException, IOException {
        switch (parser.parseCommandType(command)) {
            case LIST -> ui.showTaskList(tasks.getTasks());
            case SORT -> ui.showTaskList(sortTasks(command));
            case MARK -> ui.showTaskMarked(markTask(command, true), true);
            case UNMARK -> ui.showTaskMarked(markTask(command, false), false);
            case DELETE -> ui.showTaskDeleted(deleteTask(command), tasks.getSize());
            case TODO -> ui.showTaskAdded(addTodo(command), tasks.getSize());
            case DEADLINE -> ui.showTaskAdded(addDeadline(command), tasks.getSize());
            case EVENT -> ui.showTaskAdded(addEvent(command), tasks.getSize());
            case FIND -> ui.showMatchingTasks(findTasks(command));
            case BYE -> {
                // The command loop handles exiting before execution.
            }
            default -> throw new WWaffleException("Unknown command.");
        }
    }

    private Task markTask(String command, boolean isDone) throws WWaffleException, IOException {
        String commandName = isDone ? "mark" : "unmark";
        int taskIndex = parser.parseTaskIndex(command, commandName, tasks.getSize());
        Task task = tasks.get(taskIndex);
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        storage.save(tasks.getTasks());
        return task;
    }

    private Task deleteTask(String command) throws WWaffleException, IOException {
        int taskIndex = parser.parseTaskIndex(command, "delete", tasks.getSize());
        Task removedTask = tasks.delete(taskIndex);
        storage.save(tasks.getTasks());
        return removedTask;
    }

    private Task addTodo(String command) throws WWaffleException, IOException {
        String description = parser.parseArgument(command, "todo");
        if (description.isBlank()) {
            throw new WWaffleException("A todo description cannot be empty.");
        }
        return addTask(new Todo(description));
    }

    private Task addDeadline(String command) throws WWaffleException, IOException {
        String details = parser.parseArgument(command, "deadline");
        int byIndex = details.indexOf(DEADLINE_SEPARATOR);
        if (byIndex < 0) {
            throw new WWaffleException("Use: deadline <description> /by <yyyy-MM-dd>.");
        }

        String description = details.substring(0, byIndex);
        String by = details.substring(byIndex + DEADLINE_SEPARATOR.length());
        if (description.isBlank() || by.isBlank()) {
            throw new WWaffleException("A deadline needs a description and date.");
        }

        try {
            return addTask(new Deadline(description, by));
        } catch (DateTimeParseException e) {
            throw new WWaffleException("Invalid date. Expected yyyy-MM-dd, e.g. 2026-12-02.");
        }
    }

    private Task addEvent(String command) throws WWaffleException, IOException {
        String details = parser.parseArgument(command, "event");
        int fromIndex = details.indexOf(EVENT_START_SEPARATOR);
        if (fromIndex < 0) {
            throw new WWaffleException("Use: event <description> /from <start> /to <end>.");
        }

        String description = details.substring(0, fromIndex);
        String times = details.substring(fromIndex + EVENT_START_SEPARATOR.length());
        int toIndex = times.indexOf(EVENT_END_SEPARATOR);
        if (toIndex < 0) {
            throw new WWaffleException("Use: event <description> /from <start> /to <end>.");
        }

        String from = times.substring(0, toIndex);
        String to = times.substring(toIndex + EVENT_END_SEPARATOR.length());
        if (description.isBlank() || from.isBlank() || to.isBlank()) {
            throw new WWaffleException("An event needs a description, start, and end.");
        }
        return addTask(new Event(description, from, to));
    }

    private List<Task> findTasks(String command) throws WWaffleException {
        String keyword = parser.parseArgument(command, "find");
        if (keyword.isBlank()) {
            throw new WWaffleException("Use: find <keyword>.");
        }
        return tasks.find(keyword);
    }

    /**
     * Validates the sort key, reorders tasks once, and persists their new order.
     *
     * @param command Full sort command.
     * @return Tasks in their new display order.
     * @throws WWaffleException If the sort key is unsupported or missing.
     * @throws IOException If the reordered tasks cannot be saved.
     */
    private List<Task> sortTasks(String command) throws WWaffleException, IOException {
        String sortKey = parser.parseArgument(command, "sort");
        switch (sortKey) {
            case "name" -> tasks.sortByName();
            case "status" -> tasks.sortByStatus();
            default -> throw new WWaffleException("Use: sort name or sort status.");
        }
        storage.save(tasks.getTasks());
        return tasks.getTasks();
    }

    private Task addTask(Task task) throws IOException {
        tasks.add(task);
        storage.save(tasks.getTasks());
        return task;
    }
}
