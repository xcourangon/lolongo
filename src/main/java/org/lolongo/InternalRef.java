package org.lolongo;


/**
 * A Ref dedicated to remains internal while executing Functions.
 * This Ref cannot be retrieved in the execution Context.
 * 
 * @author Xavier Courangon
 */
public final class InternalRef<T> implements Ref<T> {

    private final Object ref;

    public InternalRef(Object ref) {
        this.ref = ref;
    }

    public String toString() {
        final StringBuffer bs = new StringBuffer(getClass().getSimpleName());
        bs.append("('");
        bs.append(ref);
        bs.append("')");
        return bs.toString();
    };

    @Override
    public int hashCode() {
        return ref.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other == this)
            return true;
        if (!(other instanceof InternalRef))
            return false;
        return ((InternalRef<?>)other).ref.equals(ref);
    }
}
