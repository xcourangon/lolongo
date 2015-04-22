package org.lolongo;

/**
 * Exception thrown when a Ref already exists in a Context.
 * 
 * @author Xavier Courangon
 */
public class RefAlreadyExists extends RefException {

    public RefAlreadyExists(Ref< ? > ref, Context context) {
        super(ref,context);
    }
}
