package org.lolongo;


public class ContextException extends Exception {

    static final long    serialVersionUID = 1L;

    private Context context;

    public ContextException(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
