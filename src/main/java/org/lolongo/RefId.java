package org.lolongo;

/**
 * Ref implementation for Ref by id as String.
 *
 * @author Xavier Courangon
 */
public class RefId<T> implements Ref<T> {
    private final String id;

    public RefId(String id) {
	this.id = id;
    }

    public String getId() {
	return id;
    }

    @Override
    public int hashCode() {
	return id.hashCode();
    }

    @Override
    public boolean equals(Object ref) {
	if (ref instanceof RefId) {
	    return id.equals(((RefId<?>) ref).getId());
	} else {
	    return false;
	}
    }

    @Override
    public String toString() {
	final StringBuffer sb = new StringBuffer(getClass().getSimpleName());
	sb.append("('");
	sb.append(id);
	sb.append("')");
	return sb.toString();
    }
}
