package org.lolongo.function;

import org.lolongo.CompositeFunctionDyn;
import org.lolongo.Context;
import org.lolongo.ContextException;
import org.lolongo.FunctionContainer;
import org.lolongo.InputBinding;
import org.lolongo.InternalRef;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;

public class ToUpperAndQuoteDynResolve extends CompositeFunctionDyn {

	@InputBinding
	private final Ref<String> refIn;

	@OutputBinding
	private final Ref<String> refOut;

	private static final InternalRef<String> refTmp = new InternalRef<String>("tmp");

	public ToUpperAndQuoteDynResolve(Ref<String> in, Ref<String> out) {
		refIn = in;
		refOut = out;
	}

	@Override
	public void prepare(FunctionContainer container, Context context) {
		container.add(new ToUpperCase(refIn, refTmp));
	}

	@Override
	public void resolve(Context context) throws ContextException {
		final String tmp = String.format("'%s'", context.get(refTmp));
		context.put(refOut, tmp);
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer(getClass().getSimpleName());
		sb.append("(");
		sb.append("in=");
		sb.append(refIn);
		sb.append(",");
		sb.append("out=");
		sb.append(refOut);
		sb.append(")");
		return sb.toString();
	}
}
