package exceptions;

public class UserIdInUseException extends Exception {
	public UserIdInUseException() {
        super("User ID is in use");
    }
}
