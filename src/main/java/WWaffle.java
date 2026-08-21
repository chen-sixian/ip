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
        ArrayList<Task> tasks = new ArrayList<>();
        String command = scanner.nextLine();

        while (!command.equals("bye")) {
            System.out.println(line);
            try {
                if (command.equals("list")) {
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = parseTaskIndex(command, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(taskIndex));
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = parseTaskIndex(command, "unmark", tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks.get(taskIndex));
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    int taskIndex = parseTaskIndex(command, "delete", tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("todo")) {
                    throw new WWaffleException("OOPS!!! The description of a todo cannot be empty.");
                } else if (command.startsWith("todo ")) {
                    String description = command.substring(5);
                    if (description.isBlank()) {
                        throw new WWaffleException("OOPS!!! The description of a todo cannot be empty.");
                    }
                    tasks.add(new Todo(description));
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("deadline")) {
                    throw new WWaffleException("OOPS!!! The description of a deadline cannot be empty.");
                } else if (command.startsWith("deadline ")) {
                    String deadlineDetails = command.substring(9);
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
                    tasks.add(new Deadline(description, by));
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("event")) {
                    throw new WWaffleException("OOPS!!! The description of an event cannot be empty.");
                } else if (command.startsWith("event ")) {
                    String eventDetails = command.substring(6);
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
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else {
                    throw new WWaffleException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (WWaffleException e) {
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
