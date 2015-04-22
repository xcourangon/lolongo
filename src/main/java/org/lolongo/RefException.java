package org.lolongo;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Exception thrown when an Exception occurs during the use of a Ref.
 * 
 * @author Xavier Courangon
 */
public abstract class RefException extends ContextException {

    private final Ref< ? > ref;

    public RefException(Ref< ? > ref, Context context) {
        super(context);
        this.ref = ref;
    }

    public Ref< ? > getRef() {
        return ref;
    }

    @Override
    public String getMessage() {
        final ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle");
        return MessageFormat.format(messages.getString(getClass().getSimpleName()), ref, getContext());
    }
}
