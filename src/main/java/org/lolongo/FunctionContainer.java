package org.lolongo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A container of Functions.
 *
 * @author Xavier Courangon
 */
public class FunctionContainer implements Iterable<Function> {

	private static Logger logger = LoggerFactory.getLogger(FunctionContainer.class);

	final List<Function> functions = new ArrayList<>();

	public FunctionContainer() {
	}

	// public FunctionContainer(Collection<Function> functions) {
	// for (final Function function : functions) {
	// this.functions.add(function);
	// }
	// }

	public void add(Function function) {
		if (function == null) {
			throw new IllegalArgumentException("function is null");
		} else {
			logger.debug("adding Function {} into {}", function, this);
			functions.add(function);
		}
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer(getClass().getSimpleName());
		return sb.toString();
	}

	@Override
	public Iterator<Function> iterator() {
		return functions.iterator();
	};
}
