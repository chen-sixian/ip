/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates an incomplete deadline with the given description and due time.
     *
     * @param description description of the deadline
     * @param by due date or time, stored as text
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline in its display format.
     *
     * @return formatted deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
