package org.lolongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalContext extends ContextBase {

    private static Logger logger = LoggerFactory.getLogger(InternalContext.class);

    private final Context context;
    private final boolean filter;

    public InternalContext(Context context) {
        this(context, true);
    }

    public InternalContext(Context context, boolean filter) {
        this.context = context;
        this.filter = filter;
    }

    @Override
    public <T, R extends Ref<T>> void put(R ref, T value) throws RefAlreadyExists {
        if (ref instanceof InternalRef<?> || filter == false) {
            super.put(ref, value);
        } else {
            context.put(ref, value);
        }
    }

    @Override
    public <T, R extends Ref<T>> T get(R ref) throws RefNotFound {
        try {
            return super.get(ref);
        } catch (final RefNotFound e) {
            logger.debug("{} not found in {}. Trying in the execution context {}", ref, this, context);
            try {
                return context.get(ref);
            } catch (final RefNotFound forget) {
                throw e;
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
