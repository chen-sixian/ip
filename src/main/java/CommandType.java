/**
 * Represents the supported categories of user commands.
 */
public enum CommandType {
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    BYE,
    UNKNOWN;

    /**
     * Identifies a command from its first word.
     *
     * @param command full command entered by the user
     * @return matching command type, or {@link #UNKNOWN} if unsupported
     */
    public static CommandType from(String command) {
        String commandWord = command.split(" ", 2)[0];
        return switch (commandWord) {
            case "list" -> LIST;
            case "mark" -> MARK;
            case "unmark" -> UNMARK;
            case "delete" -> DELETE;
            case "todo" -> TODO;
            case "deadline" -> DEADLINE;
            case "event" -> EVENT;
            case "bye" -> BYE;
            default -> UNKNOWN;
        };
    }
}
