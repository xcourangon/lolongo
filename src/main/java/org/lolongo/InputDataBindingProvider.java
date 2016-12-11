package org.lolongo;

import java.util.Set;

public interface InputDataBindingProvider {

	Set<? extends Ref<?>> getInputBindings(final Context context) throws DataBindingException;

}
