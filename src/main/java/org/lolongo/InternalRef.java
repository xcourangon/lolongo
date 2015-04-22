package org.lolongo;


/**
 * A Ref dedicated to remains internal while executing Functions.
 * This Ref cannot be retrieved in the execution Context.
 * 
 * @author Xavier Courangon
 */
public class InternalRef<T> extends RefId<T>{

    public InternalRef(String id) {
        super(id);
    }
}
