package org.lolongo;

public class PrepareFunctionAdapter implements Function {

	private final CompositeFunctionDyn compositeFunction;
	private FunctionContainer container;

	public PrepareFunctionAdapter(final CompositeFunctionDyn compositeFunction, final FunctionContainer container) {
		this.compositeFunction = compositeFunction;
		this.container = container;
	}

	@Override
	public void execute(Context context) throws FunctionException, ContextException {
		compositeFunction.prepare(container, context);
	}

}
