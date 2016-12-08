package org.lolongo.function;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.lolongo.CompositeFunction;
import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionContainer;
import org.lolongo.FunctionException;
import org.lolongo.InputBinding;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;

public class CompositePrepare extends CompositeFunction {

	@InputBinding
	private Ref<String> in;

	@OutputBinding
	private Ref<String> out;

	private Function[] componentFunctions;

	public CompositePrepare(Ref<String> in, Ref<String> out, Function... componentFunctions) {
		this.in = in;
		this.out = out;
		setComponentFunctions(componentFunctions);
	}

	public void setComponentFunctions(Function... componentFunctions) {
		this.componentFunctions = componentFunctions;
	}

	@Override
	public void prepare(FunctionContainer container, Context context) throws FunctionException, ContextException {
		for (Function function : componentFunctions) {
			container.add(function);
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("in", in).append("out", out).toString();
	};
}
