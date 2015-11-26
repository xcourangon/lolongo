package org.lolongo;

/**
 * A Context is a container of values identified by their Ref.
 *
 * @author Xavier Courangon
 */
public interface Context {

    /**
     * @param <T> the type of the value
     * @param <R> the type of the reference
     * @param ref the reference of the value to find in this Context.
     * @return the value found identified by the ref in this Context.
     * @throws RefNotFound the reference is not found
     */
    <T, R extends Ref<T>> T get(R ref) throws RefNotFound;

    // TODO could be
    // <T, U extends T, R extends Ref<T>> void put(R ref, U value) throws
    // RefAlreadyExists;

    <T, R extends Ref<T>> void put(R ref, T value) throws RefAlreadyExists;
}
