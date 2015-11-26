package org.lolongo;

/**
 * Context for Data.
 *
 * @author Xavier Courangon
 */
public interface ContextData extends Context {

    default void put(Data<?> data) throws RefAlreadyExists {
	put((Ref) data.getRef(), data);
    }

}
