package org.lolongo;

/**
 * A Data is the aggregation of a value with its ref.
 * 
 * @author Xavier Courangon
 */
public class Data<T> {

	private final T value;
	private Ref<? extends T> ref;

	public Data(T value) {
		this.value = value;
	}

	public Data(Ref<? extends T> ref, T value) {
		this.ref = ref;
		this.value = value;
	}

	public Ref<? extends T> getRef() {
		return ref;
	}

	public T getValue() {
		return value;
	}
}
