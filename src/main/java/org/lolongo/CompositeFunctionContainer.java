package org.lolongo;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompositeFunctionContainer extends ArrayList<Entry<Function, Context>> {

	private static final Logger logger = LoggerFactory.getLogger(CompositeFunctionContainer.class);

	private final Context context;

	public CompositeFunctionContainer(Context context) {
		this.context = context;
	}

	public void add(Function function, Context context) {
		add(new SimpleEntry<>(function, context));
	}

	public void add(Function f) {
		add(new SimpleEntry<>(f, this.context));
	}

	public void add(CompositeFunction compositeFunction, Context context) {
		for (final Function function : compositeFunction) {
			add(new ComponentFunction(compositeFunction, function), context);
		}
	}

	public void add(FunctionContainer functionContainer, Context context) {
		for (final Function function : functionContainer) {
			add(function, context);
		}
	}

	public Context getContext(Function function) {
		for (Entry<Function, Context> entry : this) {
			if (entry.getKey().equals(function)) {
				return entry.getValue();
			}
		}
		logger.error("Function {} not found in {}", function, this);
		return null;
	}
}
