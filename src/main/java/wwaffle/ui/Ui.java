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

    private final Scanner scanner = new Scanner(System.in);
    private final boolean useColor;

    /**
     * Creates a UI, enabling colour unless the {@code wwaffle.color} system property is false.
     */
    public Ui() {
        this.useColor = Boolean.parseBoolean(System.getProperty("wwaffle.color", "true"));
    }

    public void showWelcome(int taskCount) {
        System.out.println(color(COLUMBIA_BLUE,
                "┌─[ WWAFFLE ]────────────────────────────────────────┐"));
        System.out.println(color(COLUMBIA_BLUE, "│") + "  "
                + color(DAY_SKY_BLUE, "System ready. ")
                + color(BABY_BLUE, taskCount + " " + taskWord(taskCount) + " loaded."));
        System.out.println(color(COLUMBIA_BLUE,
                "└────────────────────────────────────────────────────┘"));
    }

    public String readCommand() {
        System.out.print("\n" + color(COLUMBIA_BLUE, "└─> "));
        if (useColor) {
            System.out.print(BLUEBERRY_BLUE);
        }
        String command = scanner.nextLine();
        if (useColor) {
            System.out.print(RESET);
        }
        return command;
    }

    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println(color(DAY_SKY_BLUE, "[i] Your task list is empty."));
            return;
        }

        System.out.println(color(ROYAL_BLUE, "[ TASKS ]"));
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(color(ROYAL_BLUE, "  " + (i + 1) + ". " + tasks.get(i)));
        }
    }

    /**
     * Displays tasks whose descriptions match a search keyword.
     *
     * @param tasks matching tasks
     */
    public void showMatchingTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println(color(DAY_SKY_BLUE, "[i] No matching tasks found."));
            return;
        }

        System.out.println(color(ROYAL_BLUE, "[ MATCHES ]"));
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(color(ROYAL_BLUE, "  " + (i + 1) + ". " + tasks.get(i)));
        }
    }

    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(color(DENIM_BLUE, "[+] TASK ADDED"));
        System.out.println(color(DAY_SKY_BLUE, "    " + task));
        System.out.println(color(BABY_BLUE,
                "    " + taskCount + " " + taskWord(taskCount) + " total."));
    }

    public void showTaskMarked(Task task, boolean isDone) {
        String heading = isDone ? "[+] TASK COMPLETED" : "[+] TASK REOPENED";
        System.out.println(color(DENIM_BLUE, heading));
        System.out.println(color(DAY_SKY_BLUE, "    " + task));
    }

    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(color(BABY_BLUE, "[-] TASK DELETED"));
        System.out.println(color(DAY_SKY_BLUE, "    " + task));
        System.out.println(color(BABY_BLUE,
                "    " + taskCount + " " + taskWord(taskCount) + " remaining."));
    }

    public void showError(String message) {
        System.out.println(color(DAY_SKY_BLUE, "[!] " + message));
    }

    public void showLoadingError() {
        showError("Saved tasks could not be loaded. Starting with an empty list.");
    }

    public void showExit() {
        System.out.println("\n" + color(DAY_SKY_BLUE, "System offline. Goodbye!"));
    }

    private String color(String ansiColor, String text) {
        return useColor ? ansiColor + text + RESET : text;
    }

    private String taskWord(int count) {
        return count == 1 ? "task" : "tasks";
    }
}
