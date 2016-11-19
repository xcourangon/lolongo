package org.lolongo;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

public interface FunctionSequencer {

    Collection<Entry<Function, Context>>[] sort(List<Entry<Function, Context>> fonctions);
}