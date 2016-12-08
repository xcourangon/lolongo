package org.lolongo.function;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.Function;
import org.lolongo.FunctionException;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleFunction0in1out implements Function {

	@OutputBinding
	private Ref out;

	public SimpleFunction0in1out(Ref<String> out) {
		this.out = out;
	}

	@Override
	public void execute(Context context) throws FunctionException, ContextException {
		context.put(out, "generated");
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	};
}