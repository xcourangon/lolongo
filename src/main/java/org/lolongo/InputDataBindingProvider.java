package org.lolongo;

import java.util.Collection;

public interface InputDataBindingProvider {

    Collection< ? extends Ref< ? >> getInputBindings(final Context context) throws DataBindingException;

}
