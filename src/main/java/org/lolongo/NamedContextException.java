package org.lolongo;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author Xavier Courangon
 */
public abstract class NamedContextException extends Exception {

    private String name;

    public NamedContextException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        final ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle");
        return MessageFormat.format(messages.getString(getClass().getSimpleName()), name);
    }
}
