package org.lolongo;


/**
 * A Data is the aggregation of a value with its ref.
 * 
 * @author Xavier Courangon
 */
public class Data<T> {

    private Ref<T> ref;
    private T      value;

    public Data(Ref<T> ref, T value) {
        this.ref = ref;
        this.value = value;
    }

    public Ref<T> getRef() {
        return ref;
    }

    public T getValue() {
        return value;
    }
}
