package org.lolongo.function;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.lolongo.CompositeFunction;
import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionContainer;
import org.lolongo.FunctionException;
import org.lolongo.InputBinding;
import org.lolongo.InternalRef;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;

public class CompositeResolve extends CompositeFunction {

	private static final InternalRef internal = new InternalRef<Class>(CompositeResolve.class);

	@InputBinding
	private Ref<String> in;

	@OutputBinding
	private Ref<String> out;

	private Function[] componentFunctions;

	public CompositeResolve(Ref<String> in, Ref<String> out, Function... componentFunctions) {
		this.in = in;
		this.out = out;
		setComponentFunctions(componentFunctions);
	}

	public void setComponentFunctions(Function... componentFunctions) {
		this.componentFunctions = componentFunctions;
	}

	@Override
	public void prepare(FunctionContainer container, Context context) throws FunctionException, ContextException {
	}

	@Override
	public void resolve(Context context) throws FunctionException, ContextException {
		for (Function function : componentFunctions) {
			function.execute(context);
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("in", in).append("out", out).toString();
	};
}
