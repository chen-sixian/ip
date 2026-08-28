package wwaffle.command;

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
    UNKNOWN
}
