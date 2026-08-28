/**
 * Interprets user commands and extracts their arguments.
 */
public class Parser {
    /**
     * Identifies the type of a command from its first word.
     *
     * @param command full command entered by the user
     * @return matching command type, or {@link CommandType#UNKNOWN}
     */
    public CommandType parseCommandType(String command) {
        String commandWord = command.split(" ", 2)[0];
        return switch (commandWord) {
        case "list" -> CommandType.LIST;
        case "mark" -> CommandType.MARK;
        case "unmark" -> CommandType.UNMARK;
        case "delete" -> CommandType.DELETE;
        case "todo" -> CommandType.TODO;
        case "deadline" -> CommandType.DEADLINE;
        case "event" -> CommandType.EVENT;
        case "bye" -> CommandType.BYE;
        default -> CommandType.UNKNOWN;
        };
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
    public int parseTaskIndex(String command, String commandName, int taskCount)
            throws WWaffleException {
        String numberText = command.substring(commandName.length()).trim();
        if (numberText.isEmpty()) {
            throw new WWaffleException("Use: " + commandName + " <task number>.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            throw new WWaffleException("The task number must be a whole number.");
        }

        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new WWaffleException("There is no task numbered " + taskNumber + ".");
        }
        return taskIndex;
    }

    /**
     * Returns the text following a command word.
     *
     * @param command full command entered by the user
     * @param commandName command word to remove
     * @return trimmed command argument
     */
    public String parseArgument(String command, String commandName) {
        return command.substring(commandName.length()).trim();
    }
}
