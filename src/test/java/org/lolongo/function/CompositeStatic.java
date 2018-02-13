package org.lolongo.function;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.lolongo.CompositeFunction;
import org.lolongo.Function;

public class CompositeStatic extends CompositeFunction {

	public CompositeStatic(Function... componentFunctions) {
		setComponentFunctions(componentFunctions);
	}

	public void setComponentFunctions(Function... componentFunctions) {
		for (Function function : componentFunctions) {
			add(function);
		}
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
