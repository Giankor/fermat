package com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions;

/**
 * Created by toshiba on 08/04/2015.
 */
public class CantUpdateException extends FileSystemException {

	/**
	 *
	 */
	private static final long serialVersionUID = 2411843361343674579L;

	public static final String DEFAULT_MESSAGE = "CAN'T UPDATE FILE";

	public CantUpdateException(final String message, final Exception cause, final String context, final String possibleReason) {
		super(message, cause, context, possibleReason);
	}

	public CantUpdateException(final Exception cause, final String context, final String possibleReason) {
		super(DEFAULT_MESSAGE, cause, context, possibleReason);
	}

	public CantUpdateException(final String message, final Exception cause) {
		this(message, cause, "", "");
	}

	public CantUpdateException(final String message) {
		this(message, null);
	}

	public CantUpdateException(final Exception exception) {
		this(exception.getMessage());
		setStackTrace(exception.getStackTrace());
	}

	public CantUpdateException() {
		this(DEFAULT_MESSAGE);
	}

}
