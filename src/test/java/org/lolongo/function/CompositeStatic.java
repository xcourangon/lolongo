package org.lolongo.function;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.lolongo.CompositeFunction;
import org.lolongo.Function;
import org.lolongo.InputBinding;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;

public class CompositeStatic extends CompositeFunction {

	@InputBinding
	private Ref<String> in;

	@OutputBinding
	private Ref<String> out;

	public CompositeStatic(Ref<String> in, Ref<String> out, Function... componentFunctions) {
		this.in = in;
		this.out = out;
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
