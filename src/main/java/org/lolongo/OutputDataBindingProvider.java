package org.lolongo;

import java.util.Set;

public interface OutputDataBindingProvider {

	Set<? extends Ref<?>> getOutputBindings(final Context context) throws DataBindingException;
}
