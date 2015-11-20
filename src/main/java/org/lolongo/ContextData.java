package org.lolongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Context for Data.
 * 
 * @author Xavier Courangon
 */
public interface ContextData extends Context {

    default void put(Data<?> data) throws RefAlreadyExists {
        put((Ref)data.getRef(), data);
    }

}
