import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Coordinates user input, task management, and persistent storage.
 */
public class WWaffle {
    private final Parser parser;
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates WWaffle using the specified save file.
     *
     * @param filePath path of the task data file
     */
    public WWaffle(String filePath) {
        this.ui = new Ui();
        this.parser = new Parser();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList(loadTasks());
    }

    public static void main(String[] args) {
        new WWaffle("./data/wwaffle.txt").run();
    }

    /**
     * Runs the command loop until the user exits.
     */
    public void run() {
        ui.showWelcome(tasks.size());
        String command = ui.readCommand();

        while (parser.parseCommandType(command) != CommandType.BYE) {
            try {
                execute(command);
            } catch (WWaffleException | IOException e) {
                ui.showError(e.getMessage());
            }
            command = ui.readCommand();
        }
        ui.showExit();
    }

    private ArrayList<Task> loadTasks() {
        try {
            return storage.load();
        } catch (IOException e) {
            ui.showLoadingError();
            return new ArrayList<>();
        }
    }

    private void execute(String command) throws WWaffleException, IOException {
        switch (parser.parseCommandType(command)) {
        case LIST -> ui.showTaskList(tasks.asList());
        case MARK -> markTask(command, true);
        case UNMARK -> markTask(command, false);
        case DELETE -> deleteTask(command);
        case TODO -> addTodo(command);
        case DEADLINE -> addDeadline(command);
        case EVENT -> addEvent(command);
        case UNKNOWN -> throw new WWaffleException("Unknown command.");
        case BYE -> {
            // The command loop handles exiting before execution.
        }
        }
    }

    private void markTask(String command, boolean isDone) throws WWaffleException, IOException {
        String commandName = isDone ? "mark" : "unmark";
        int taskIndex = parser.parseTaskIndex(command, commandName, tasks.size());
        Task task = tasks.get(taskIndex);
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        storage.save(tasks.asList());
        ui.showTaskMarked(task, isDone);
    }

    private void deleteTask(String command) throws WWaffleException, IOException {
        int taskIndex = parser.parseTaskIndex(command, "delete", tasks.size());
        Task removedTask = tasks.delete(taskIndex);
        storage.save(tasks.asList());
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    private void addTodo(String command) throws WWaffleException, IOException {
        String description = parser.parseArgument(command, "todo");
        if (description.isBlank()) {
            throw new WWaffleException("A todo description cannot be empty.");
        }
        addTask(new Todo(description));
    }

    private void addDeadline(String command) throws WWaffleException, IOException {
        String details = parser.parseArgument(command, "deadline");
        int byIndex = details.indexOf(" /by ");
        if (byIndex < 0) {
            throw new WWaffleException("Use: deadline <description> /by <yyyy-MM-dd>.");
        }

        String description = details.substring(0, byIndex);
        String by = details.substring(byIndex + 5);
        if (description.isBlank() || by.isBlank()) {
            throw new WWaffleException("A deadline needs a description and date.");
        }

        try {
            addTask(new Deadline(description, by));
        } catch (DateTimeParseException e) {
            throw new WWaffleException("Invalid date. Expected yyyy-MM-dd, e.g. 2026-12-02.");
        }
    }

    private void addEvent(String command) throws WWaffleException, IOException {
        String details = parser.parseArgument(command, "event");
        int fromIndex = details.indexOf(" /from ");
        if (fromIndex < 0) {
            throw new WWaffleException("Use: event <description> /from <start> /to <end>.");
        }

        String description = details.substring(0, fromIndex);
        String times = details.substring(fromIndex + 7);
        int toIndex = times.indexOf(" /to ");
        if (toIndex < 0) {
            throw new WWaffleException("Use: event <description> /from <start> /to <end>.");
        }

        String from = times.substring(0, toIndex);
        String to = times.substring(toIndex + 5);
        if (description.isBlank() || from.isBlank() || to.isBlank()) {
            throw new WWaffleException("An event needs a description, start, and end.");
        }
        addTask(new Event(description, from, to));
    }

    private void addTask(Task task) throws IOException {
        tasks.add(task);
        storage.save(tasks.asList());
        ui.showTaskAdded(task, tasks.size());
    }
}
