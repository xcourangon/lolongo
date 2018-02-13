package org.lolongo.function;

import org.lolongo.CompositeFunctionDyn;
import org.lolongo.Context;
import org.lolongo.FunctionContainer;
import org.lolongo.InputBinding;
import org.lolongo.InternalRef;
import org.lolongo.OutputBinding;
import org.lolongo.Ref;

public class ToUpperAndQuoteDyn extends CompositeFunctionDyn {

	@InputBinding
	private final Ref<String> refIn;

	@OutputBinding
	private final Ref<String> refOut;

	private static final InternalRef<String> tmp = new InternalRef<String>("tmp");

	public ToUpperAndQuoteDyn(Ref<String> in, Ref<String> out) {
		refIn = in;
		refOut = out;
	}

	@Override
	public void prepare(FunctionContainer container, Context context) {
		container.add(new ToUpperCase(refIn, tmp));
		container.add(new Quote(tmp, refOut));
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
