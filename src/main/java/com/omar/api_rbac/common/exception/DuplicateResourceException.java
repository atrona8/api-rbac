 
package com.omar.api_rbac.common.exception;

public class DuplicateResourceException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String field;
    private final String value;

    public DuplicateResourceException(String field, String value) {
        super(String.format("%s '%s' is already taken", field, value));
        this.field = field;
        this.value = value;
    }

    public String getField() { return field; }
    public String getValue() { return value; }
}