package org.lolongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class CompositeFunctionContainer implements Iterable<Entry<Function, Context>> {
  
      private static final Logger logger = LoggerFactory.getLogger(CompositeFunctionContainer.class);

    private final List<Entry<Function, Context>> container = new ArrayList<>();
    private final Context context;

    public CompositeFunctionContainer(Context context) {
	this.context = context;
    }

    public void add(Function function, Context context) {
	container.add(new SimpleEntry<>(function, context));
    }

    public void add(Function f) {
	container.add(new SimpleEntry<>(f, this.context));
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
  
  public Context getContext(Function function) {
    for (Entry<Function, Context> entry : container) {
      if(entry.getKey().equals(function)) {
        return entry.getValue();
      }
    }
    logger.error("Function {} not found in {}",function,this);
    return null;
  }
}
