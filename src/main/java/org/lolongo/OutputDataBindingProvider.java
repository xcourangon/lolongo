package org.lolongo;

import java.util.Collection;

public interface OutputDataBindingProvider {

    Collection< ? extends Ref< ? >> getOutputBindings(final Context context) throws DataBindingException;
}
