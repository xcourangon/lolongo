package org.lolongo;

public interface Context {

    <T,R extends Ref<T>> T get(R ref) throws RefNotFound;

    <T,R extends Ref<T>> void put(R ref, T value) throws RefAlreadyExists;
}
