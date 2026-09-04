package wwaffle.ui;

import java.util.List;
import java.util.Scanner;

import wwaffle.task.Task;

/**
 * Handles terminal input and presentation for WWaffle.
 */
public class Ui {
    private static final String RESET = "\u001B[0m";
    private static final String COLUMBIA_BLUE = "\u001B[38;2;135;175;199m";
    private static final String BLUEBERRY_BLUE = "\u001B[38;2;0;65;194m";
    private static final String ROYAL_BLUE = "\u001B[38;2;65;105;225m";
    private static final String DAY_SKY_BLUE = "\u001B[38;2;130;202;255m";
    private static final String BABY_BLUE = "\u001B[38;2;149;185;199m";
    private static final String DENIM_BLUE = "\u001B[38;2;121;186;236m";
    private static final String[] WORDMARK = {
        " __        ____        ___    _____ _____ _     _____ ",
        " \\ \\      / /\\ \\      / / \\  |  ___|  ___| |   | ____|",
        "  \\ \\ /\\ / /  \\ \\ /\\ / / _ \\ | |_  | |_  | |   |  _|  ",
        "   \\ V  V /    \\ V  V / ___ \\|  _| |  _| | |___| |___ ",
        "    \\_/\\_/      \\_/\\_/_/   \\_\\_|   |_|   |_____|_____|"
    };
    private static final String DIVIDER =
            "──────────────────────────────────────────────────────────";

    private final Scanner scanner = new Scanner(System.in);
    private final boolean useColor;

    /**
     * Creates a UI, enabling colour unless the {@code wwaffle.color} system property is false.
     */
    public Ui() {
        this.useColor = Boolean.parseBoolean(System.getProperty("wwaffle.color", "true"));
    }

    /**
     * Shows the welcome banner and number of loaded tasks.
     *
     * @param taskCount Number of tasks loaded from storage.
     */
    public void showWelcome(int taskCount) {
        System.out.println();
        for (String line : WORDMARK) {
            System.out.println(applyColor(ROYAL_BLUE, line));
        }
        System.out.println();
        System.out.println(applyColor(DAY_SKY_BLUE, " PERSONAL TASK MANAGER")
                + applyColor(BABY_BLUE, "  ·  " + taskCount + " "
                        + getTaskWord(taskCount).toUpperCase() + " READY"));
        System.out.println(applyColor(COLUMBIA_BLUE, DIVIDER));
    }

    /**
     * Shows the command prompt and reads the next line of user input.
     *
     * @return Command entered by the user.
     */
    public String readCommand() {
        System.out.print("\n" + applyColor(COLUMBIA_BLUE, "COMMAND ")
                + applyColor(DAY_SKY_BLUE, "› "));
        if (useColor) {
            System.out.print(BLUEBERRY_BLUE);
        }
        String command = scanner.nextLine();
        if (useColor) {
            System.out.print(RESET);
        }
        return command;
    }

    /**
     * Returns a concise welcome message for non-terminal interfaces.
     *
     * @param taskCount Number of tasks loaded from storage.
     * @return Plain-text welcome message.
     */
    public String getWelcomeMessage(int taskCount) {
        return "System ready. " + taskCount + " " + getTaskWord(taskCount)
                + " loaded.\nType a command below to get started.";
    }

    /**
     * Formats all tasks with their one-based list numbers.
     *
     * @param tasks Tasks to format.
     * @return Plain-text task list.
     */
    public String getTaskListMessage(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return "[i] Your task list is empty.";
        }

        StringBuilder message = new StringBuilder("[ TASKS ]");
        for (int i = 0; i < tasks.size(); i++) {
            message.append("\n").append(i + 1).append(". ").append(tasks.get(i));
        }
        return message.toString();
    }

    /**
     * Formats tasks that match a search keyword.
     *
     * @param tasks Matching tasks.
     * @return Plain-text search result.
     */
    public String getMatchingTasksMessage(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return "[i] No matching tasks found.";
        }

        StringBuilder message = new StringBuilder("[ MATCHES ]");
        for (int i = 0; i < tasks.size(); i++) {
            message.append("\n").append(i + 1).append(". ").append(tasks.get(i));
        }
        return message.toString();
    }

    /**
     * Formats confirmation that a task was added.
     *
     * @param task Added task.
     * @param taskCount Number of tasks after the addition.
     * @return Plain-text confirmation.
     */
    public String getTaskAddedMessage(Task task, int taskCount) {
        return "[+] TASK ADDED\n" + task + "\n" + taskCount + " "
                + getTaskWord(taskCount) + " total.";
    }

    /**
     * Formats confirmation that a task's completion state changed.
     *
     * @param task Updated task.
     * @param isDone Whether the task is now complete.
     * @return Plain-text confirmation.
     */
    public String getTaskMarkedMessage(Task task, boolean isDone) {
        String heading = isDone ? "[+] TASK COMPLETED" : "[+] TASK REOPENED";
        return heading + "\n" + task;
    }

    /**
     * Formats confirmation that a task was deleted.
     *
     * @param task Deleted task.
     * @param taskCount Number of tasks remaining after deletion.
     * @return Plain-text confirmation.
     */
    public String getTaskDeletedMessage(Task task, int taskCount) {
        return "[-] TASK DELETED\n" + task + "\n" + taskCount + " "
                + getTaskWord(taskCount) + " remaining.";
    }

    /**
     * Formats a user-facing error message.
     *
     * @param message Error explanation.
     * @return Plain-text error message.
     */
    public String getErrorMessage(String message) {
        return "[!] " + message;
    }

    /**
     * Returns the farewell message for non-terminal interfaces.
     *
     * @return Plain-text farewell message.
     */
    public String getExitMessage() {
        return "System offline. Goodbye!";
    }

    /**
     * Shows all tasks with their one-based list numbers.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println(applyColor(DAY_SKY_BLUE, "[i] Your task list is empty."));
            return;
        }

        System.out.println(applyColor(ROYAL_BLUE, "[ TASKS ]"));
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(applyColor(ROYAL_BLUE, "  " + (i + 1) + ". " + tasks.get(i)));
        }
    }

    /**
     * Displays tasks whose descriptions match a search keyword.
     *
     * @param tasks Matching tasks.
     */
    public void showMatchingTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println(applyColor(DAY_SKY_BLUE, "[i] No matching tasks found."));
            return;
        }

        System.out.println(applyColor(ROYAL_BLUE, "[ MATCHES ]"));
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(applyColor(ROYAL_BLUE, "  " + (i + 1) + ". " + tasks.get(i)));
        }
    }

    /**
     * Shows confirmation that a task was added.
     *
     * @param task Added task.
     * @param taskCount Number of tasks after the addition.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(applyColor(DENIM_BLUE, "[+] TASK ADDED"));
        System.out.println(applyColor(DAY_SKY_BLUE, "    " + task));
        System.out.println(applyColor(BABY_BLUE,
                "    " + taskCount + " " + getTaskWord(taskCount) + " total."));
    }

    /**
     * Shows confirmation that a task's completion state changed.
     *
     * @param task Updated task.
     * @param isDone Whether the task is now complete.
     */
    public void showTaskMarked(Task task, boolean isDone) {
        String heading = isDone ? "[+] TASK COMPLETED" : "[+] TASK REOPENED";
        System.out.println(applyColor(DENIM_BLUE, heading));
        System.out.println(applyColor(DAY_SKY_BLUE, "    " + task));
    }

    /**
     * Shows confirmation that a task was deleted.
     *
     * @param task Deleted task.
     * @param taskCount Number of tasks remaining after deletion.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(applyColor(BABY_BLUE, "[-] TASK DELETED"));
        System.out.println(applyColor(DAY_SKY_BLUE, "    " + task));
        System.out.println(applyColor(BABY_BLUE,
                "    " + taskCount + " " + getTaskWord(taskCount) + " remaining."));
    }

    /**
     * Shows a user-facing error message.
     *
     * @param message Error explanation to display.
     */
    public void showError(String message) {
        System.out.println(applyColor(DAY_SKY_BLUE, "[!] " + message));
    }

    /**
     * Shows the standard error for a failed storage load.
     */
    public void showLoadingError() {
        showError("Saved tasks could not be loaded. Starting with an empty list.");
    }

    /**
     * Shows the farewell message.
     */
    public void showExit() {
        System.out.println("\n" + applyColor(DAY_SKY_BLUE, "System offline. Goodbye!"));
    }

    private String applyColor(String ansiColor, String text) {
        return useColor ? ansiColor + text + RESET : text;
    }

    private String getTaskWord(int count) {
        return count == 1 ? "task" : "tasks";
    }
}
