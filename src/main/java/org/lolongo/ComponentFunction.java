package org.lolongo;

import java.util.Collection;

class ComponentFunction implements Function, DataBindingProvider {

	private CompositeFunction parent;
	private Function componentFunction;

	ComponentFunction(CompositeFunction parent, Function componentFunction) {
		this.parent = parent;
		this.componentFunction = componentFunction;
	}

	Function getFunction() {
		return componentFunction;
	}

	boolean hasParent(CompositeFunction compositeFunction) {
		return compositeFunction == parent;
	}

	@Override
	public void execute(Context context) throws FunctionException, ContextException {
		componentFunction.execute(context);
	}

	@Override
	public Collection<? extends Ref<?>> getInputBindings(Context context) throws DataBindingException {
		return DataBindingUtils.getInputBindings(context, componentFunction);
	}

	@Override
	public Collection<? extends Ref<?>> getOutputBindings(Context context) throws DataBindingException {
		return DataBindingUtils.getOutputBindings(context, componentFunction);
	}

	@Override
	public String toString() {
		return componentFunction.toString();
	}
}
