import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class WWaffle {
    public static void main(String[] args) {
        String banner = """
                ╔════════════════════════╗
                ║        WWAFFLE         ║
                ╚════════════════════════╝
                """;

        String line = "____________________________________________________________";
        System.out.println(banner);
        System.out.println(line);
        System.out.println("Hello! I'm WWaffle");
        System.out.println("What can I do for you?");
        System.out.println(line);
        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage("./data/wwaffle.txt");
        ArrayList<Task> tasks;
        try {
            tasks = storage.load();
        } catch (IOException e) {
            System.out.println("OOPS!!! I couldn't load your saved tasks.");
            tasks = new ArrayList<>();
        }
        String command = scanner.nextLine();

        while (!command.equals("bye")) {
            System.out.println(line);
            try {
                switch (CommandType.from(command)) {
                case LIST -> {
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }
                }
                case MARK -> {
                    int taskIndex = parseTaskIndex(command, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    storage.save(tasks);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(taskIndex));
                }
                case UNMARK -> {
                    int taskIndex = parseTaskIndex(command, "unmark", tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    storage.save(tasks);
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks.get(taskIndex));
                }
                case DELETE -> {
                    int taskIndex = parseTaskIndex(command, "delete", tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    storage.save(tasks);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                }
                case TODO -> {
                    String description = command.substring("todo".length()).trim();
                    if (description.isBlank()) {
                        throw new WWaffleException("OOPS!!! The description of a todo cannot be empty.");
                    }
                    tasks.add(new Todo(description));
                    storage.save(tasks);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                }
                case DEADLINE -> {
                    String deadlineDetails = command.substring("deadline".length()).trim();
                    if (deadlineDetails.isEmpty()) {
                        throw new WWaffleException("OOPS!!! The description of a deadline cannot be empty.");
                    }
                    int byIndex = deadlineDetails.indexOf(" /by ");
                    if (byIndex < 0) {
                        throw new WWaffleException("OOPS!!! Use: deadline <description> /by <date or time>.");
                    }
                    String description = deadlineDetails.substring(0, byIndex);
                    String by = deadlineDetails.substring(byIndex + 5);
                    if (description.isBlank()) {
                        throw new WWaffleException("OOPS!!! The description of a deadline cannot be empty.");
                    }
                    if (by.isBlank()) {
                        throw new WWaffleException("OOPS!!! A deadline must have a date or time after /by.");
                    }
                    try {
                        tasks.add(new Deadline(description, by));
                    } catch (DateTimeParseException e) {
                        throw new WWaffleException(
                                "OOPS!!! Use a valid deadline date in yyyy-MM-dd format.");
                    }
                    storage.save(tasks);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                }
                case EVENT -> {
                    String eventDetails = command.substring("event".length()).trim();
                    if (eventDetails.isEmpty()) {
                        throw new WWaffleException("OOPS!!! The description of an event cannot be empty.");
                    }
                    int fromIndex = eventDetails.indexOf(" /from ");
                    if (fromIndex < 0) {
                        throw new WWaffleException(
                                "OOPS!!! Use: event <description> /from <start> /to <end>.");
                    }
                    String description = eventDetails.substring(0, fromIndex);
                    String times = eventDetails.substring(fromIndex + 7);
                    int toIndex = times.indexOf(" /to ");
                    if (toIndex < 0) {
                        throw new WWaffleException(
                                "OOPS!!! Use: event <description> /from <start> /to <end>.");
                    }
                    String from = times.substring(0, toIndex);
                    String to = times.substring(toIndex + 5);
                    if (description.isBlank()) {
                        throw new WWaffleException("OOPS!!! The description of an event cannot be empty.");
                    }
                    if (from.isBlank() || to.isBlank()) {
                        throw new WWaffleException("OOPS!!! An event must have both start and end times.");
                    }
                    tasks.add(new Event(description, from, to));
                    storage.save(tasks);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                }
                case UNKNOWN ->
                    throw new WWaffleException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                case BYE -> {
                    // The loop exits before this case can be reached.
                }
                }
            } catch (WWaffleException | IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(line);
            command = scanner.nextLine();
        }
        System.out.println(line);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(line);
    }

    /**
     * Extracts and validates the one-based task number supplied to a command.
     *
     * @param command full command entered by the user
     * @param commandName command word whose argument should be parsed
     * @param taskCount number of tasks currently stored
     * @return zero-based index of the selected task
     * @throws WWaffleException if the task number is missing, invalid, or out of range
     */
    private static int parseTaskIndex(String command, String commandName, int taskCount)
            throws WWaffleException {
        String numberText = command.substring(commandName.length()).trim();
        if (numberText.isEmpty()) {
            throw new WWaffleException("OOPS!!! Use: " + commandName + " <task number>.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            throw new WWaffleException("OOPS!!! The task number must be a whole number.");
        }

        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new WWaffleException("OOPS!!! There is no task numbered " + taskNumber + ".");
        }
        return taskIndex;
    }
}
