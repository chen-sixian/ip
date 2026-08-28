package wwaffle.exception;

/**
 * Represents an input error that WWaffle can explain to the user.
 */
public class WWaffleException extends Exception {
    /**
     * Creates an exception with a user-facing explanation.
     *
     * @param message Explanation of the input error.
     */
    public WWaffleException(String message) {
        super(message);
    }
}
