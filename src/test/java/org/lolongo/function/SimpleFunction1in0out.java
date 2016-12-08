package org.lolongo.function;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionException;
import org.lolongo.InputBinding;
import org.lolongo.Ref;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleFunction1in0out implements Function {

	@InputBinding
	private Ref<?> in;

	public <T> SimpleFunction1in0out(Ref<T> in) {
		this.in = in;
	}

	@Override
	public void execute(Context context) throws FunctionException, ContextException {
		context.get(in);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	};
}