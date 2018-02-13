package org.lolongo.function;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.lolongo.CompositeFunctionDyn;
import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionContainer;
import org.lolongo.FunctionException;

public class CompositeResolve extends CompositeFunctionDyn {

	private Function[] componentFunctions;

	public CompositeResolve(Function... componentFunctions) {
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
		return new ToStringBuilder(this).toString();
	};
}
