package wwaffle.task;

/**
 * Represents a task that occurs between specified start and end times.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an incomplete event with the given description and times.
     *
     * @param description description of the event
     * @param from start date or time, stored as text
     * @param to end date or time, stored as text
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    /**
     * Returns the event in its display format.
     *
     * @return formatted event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
