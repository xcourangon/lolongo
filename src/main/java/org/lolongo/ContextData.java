package org.lolongo;

/**
 * Context for Data.
 *
 * @author Xavier Courangon
 */
public class ContextData extends ContextBase {

    public void put(Data<?> data) throws RefAlreadyExists {
	put((Ref) data.getRef(), data);
    }
}
