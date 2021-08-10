package panda.std;

/**
 * Wrapper exception for invocation failures. Use {@link #getCause()} to get thrown exception.
 */
public final class AttemptFailedException extends RuntimeException {

    public AttemptFailedException(Exception exception) {
        super(exception);
    }

}
