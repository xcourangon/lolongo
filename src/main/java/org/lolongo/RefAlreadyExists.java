package org.lolongo;

public class RefAlreadyExists extends ContextException {

    static final long      serialVersionUID = 1L;

    private final Ref< ? > ref;

    public RefAlreadyExists(Ref< ? > ref, Context context) {
        super(context);
        this.ref = ref;
    }

    public Ref< ? > getRef() {
        return ref;
    }

    @Override
    public String getMessage() {
        return "Reference " + ref + " already exists in " + getContext();
    }

}
