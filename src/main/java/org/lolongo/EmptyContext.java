package org.lolongo;

import org.lolongo.Ref;
import org.lolongo.RefAlreadyExists;
import org.lolongo.RefNotFound;

/**
 * Implementation of a Context always empty.
 * No value can be put into.
 * No Ref can be found.
 * This class is singleton.
 * 
 * @author Xavier Courangon
 */
public final class EmptyContext implements Context {

    private static final Context instance = new EmptyContext();

    private EmptyContext() {
    }

    public static Context getInstance() {
        return instance;
    }

    @Override
    public <T,R extends Ref<T>> T get(R ref) throws RefNotFound {
        throw new RefNotFound(ref, this);
    }

    @Override
     public   <T,R extends Ref<T>> void put(R ref, T value) throws RefAlreadyExists {
        throw new IllegalStateException("This context must remain empty");
    }

	 @Override
    public String toString() {
      return "EmptyContext";
    }

}
