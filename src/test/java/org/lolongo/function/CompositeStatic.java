package org.lolongo.function;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.lolongo.CompositeFunction;
import org.lolongo.Function;
import org.lolongo.Ref;

public class CompositeStatic extends CompositeFunction {

	public CompositeStatic(Ref<String> in, Ref<String> out, Function... componentFunctions) {
		setComponentFunctions(componentFunctions);
	}

	public void setComponentFunctions(Function... componentFunctions) {
		for (Function function : componentFunctions) {
			add(function);
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}
}
