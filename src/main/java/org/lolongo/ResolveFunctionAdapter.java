package org.lolongo;

public class ResolveFunctionAdapter implements Function {

	private final CompositeFunctionDyn compositeFunction;

	public ResolveFunctionAdapter(final CompositeFunctionDyn compositeFunction) {
		this.compositeFunction = compositeFunction;
	}

	@Override
	public void execute(Context context) throws FunctionException, ContextException {
		compositeFunction.resolve(context);
	}

}
