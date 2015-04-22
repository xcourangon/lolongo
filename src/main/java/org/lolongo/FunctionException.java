package org.lolongo;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Exception thrown when an Exception occurs during the execution of a Function.
 * 
 * @author Xavier Courangon
 */
public class FunctionException extends Exception {

    private final Function function;

    public FunctionException(Function function, Exception cause) {
        super(cause.getMessage(), cause);
        this.function = function;
    }

    public FunctionException(Function function, String message) {
        super(message);
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

    public String getMessage() {
        final ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle");
        return MessageFormat.format(messages.getString(getClass().getSimpleName()), function, super.getMessage());
    }
}
