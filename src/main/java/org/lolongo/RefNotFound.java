package org.lolongo;

public class RefNotFound extends ContextException {

    static final long      serialVersionUID = 1L;

    private final Ref< ? > ref;

    public RefNotFound(Ref< ? > ref, Context context) {
        super(context);
        this.ref = ref;
    }

    public Ref< ? > getRef() {
        return ref;
    }

    @Override
    public String getMessage() {
        return "Reference " + ref + " not found in " + getContext();
    }

}
