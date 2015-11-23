package org.lolongo;

public class InternalContext extends ContextBase {

    private final Context context;

    public InternalContext(Context context) {
        this.context = context;
    }

    @Override
    public <T, R extends Ref<T>> void put(R ref, T value) throws RefAlreadyExists {
        if (ref instanceof InternalRef< ? >) {
            super.put(ref, value);
        } else {
            context.put(ref, value);
        }
    }

    @Override
    public <T, R extends Ref<T>> T get(R ref) throws RefNotFound {
        try {
            return super.get(ref);
        } catch (RefNotFound _) {
            return context.get(ref);
        }
    };
}
