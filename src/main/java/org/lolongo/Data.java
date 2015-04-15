package org.lolongo;

public class Data<T> {

    private Ref<T> ref;
    private T      value;

    public Data(Ref<T> ref, T value) {
        this.ref = ref;
        this.value = value;
    }
}
