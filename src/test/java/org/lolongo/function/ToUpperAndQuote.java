package org.lolongo.function;

import org.lolongo.CompositeFunction;
import org.lolongo.InternalRef;
import org.lolongo.Ref;

public class ToUpperAndQuote extends CompositeFunction {

    private static final InternalRef<String> tmp = new InternalRef<String>("tmp");

    public ToUpperAndQuote(Ref<String> in, Ref<String> out) {
	add(new ToUpperCase(in, tmp));
	add(new Quote(tmp, out));
    }

    @Override
    public String toString() {
	final StringBuffer sb = new StringBuffer(getClass().getSimpleName());
	sb.append(" (macro)");
	return sb.toString();
    };
}
