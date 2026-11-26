package exceptions;

public class DuplicateDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateDataException() {
		super("El registro ya existe en la base de datos.");
	}

	public DuplicateDataException(String message) {
		super(message);
	}

	public DuplicateDataException(String message, Throwable cause) {
		super(message, cause);
	}

}
