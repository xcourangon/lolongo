package org.lolongo;

import org.lolongo.Context;

import java.text.MessageFormat;

public class ConsistencyException extends ContextException {

    private Ref< ? >  ref;
    private Data< ? > data;

    public <T> ConsistencyException(Context context, Ref<T> ref, Data<T> data) {
        super(context);
        this.ref = ref;
        this.data = data;
    }

    public Ref< ? > getRef() {
        return ref;
    }

    public Data< ? > getData() {
        return data;
    }

    public String getMessage() {
        return MessageFormat.format(messages.getString(getClass().getSimpleName()), context, ref, data.getRef(), data.getValue());
    }
}
