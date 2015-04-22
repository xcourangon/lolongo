package org.lolongo;

import java.text.MessageFormat;
import java.util.ResourceBundle;


/**
 * @author Xavier Courangon
 */
public abstract class ContextException extends Exception {

    private Context context;

    public ContextException(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getMessage() {
        final ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle");
        return MessageFormat.format(messages.getString(getClass().getSimpleName()), context);
    }
}
