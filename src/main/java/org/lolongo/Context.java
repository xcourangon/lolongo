package org.lolongo;

/**
 * A Context is a container of values identified by their Ref.
 * 
 * @author Xavier Courangon
 */
public interface Context {

    <T,R extends Ref<T>> T get(R ref) throws RefNotFound;

  // TODO could be   
  //  <T, U extends T, R extends Ref<T>> void put(R ref, U value) throws RefAlreadyExists;

    <T,R extends Ref<T>> void put(R ref, T value) throws RefAlreadyExists;
}
