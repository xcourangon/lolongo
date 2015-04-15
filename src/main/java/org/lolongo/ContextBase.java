package org.lolongo;

import java.util.HashMap;
import java.util.Map;

public class ContextBase implements Context {

    private Map content = new HashMap();

    @Override
    public <T, R extends Ref<T>> T get(R ref) throws RefNotFound {
        if (content.containsKey(ref)) {
            return (T)content.get(ref);
        } else {
            throw new RefNotFound(ref, this);
        }
    }

    @Override
    public <T, R extends Ref<T>> void put(R ref, T value) throws RefAlreadyExists {
        if (content.containsKey(ref) == false) {
            content.put(ref, value);
        } else {
            throw new RefAlreadyExists(ref, this);
        }
    }

    public String toString() {
        return "ContextBase";
    }
}
