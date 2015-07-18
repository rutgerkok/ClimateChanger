package nl.rutgerkok.climatechanger.util;

/**
 * Thrown when the user tries to add an invalid task.
 *
 */
public class InvalidTaskException extends Exception {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 0L;

    public InvalidTaskException(String reason) {
        super(reason);
    }
}
