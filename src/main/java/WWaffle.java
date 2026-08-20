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
        String[] tasks = new String[100];
        int taskCount = 0;
        String command = scanner.nextLine();

        while (!command.equals("bye")) {
            System.out.println(line);
            if (command.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("added: " + command);
            }
            System.out.println(line);
            command = scanner.nextLine();
        }
        System.out.println(line);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(line);
    }
}
