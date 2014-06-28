package nl.rutgerkok.climatechanger.util;

/**
 * Thrown when the user tries to add an invalid task.
 *
 */
public class InvalidTaskException extends Exception {
    public InvalidTaskException(String reason) {
        super(reason);
    }
}
