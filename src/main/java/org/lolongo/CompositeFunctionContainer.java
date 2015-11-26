package org.lolongo;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

class CompositeFunctionContainer implements Iterable<Entry<Function, Context>> {
    private final List<Entry<Function, Context>> container = new ArrayList<>();
    private final Context context;

    public CompositeFunctionContainer(Context context) {
	this.context = context;
    }

    public void add(Function function, Context context) {
	container.add(new SimpleEntry<>(function, context));
    }

    public void add(Function f) {
	container.add(new SimpleEntry<>(f, context));
    }

    @Override
    public Iterator<Entry<Function, Context>> iterator() {
	return container.iterator();
    }

    public void add(CompositeFunction compositeFunction, Context context) {
	add((FunctionContainer) compositeFunction, context);
    }

    public void add(FunctionContainer functionContainer, Context context) {
	for (final Function function : functionContainer) {
	    add(function, context);
	}
    }

}
