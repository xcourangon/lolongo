package org.lolongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

public class SimpleFunctionSequencer implements FunctionSequencer {

    /* (f1,f2,f3) => [(f1),(f2),(f3)] */
    @Override
    public Collection<Entry<Function, Context>>[] sort(List<Entry<Function, Context>> functions) {
        int size = functions.size();
        @SuppressWarnings("unchecked")
        final List<Entry<Function, Context>>[] result = new ArrayList[size];
        for (int i = 0; i < size; i++) {
            result[i] = new ArrayList<>();
            result[i].add(functions.get(i));
        }
        return result;
    }

    public Collection<Function>[] sort(List<Function> functions, Context context) {
        int size = functions.size();
        @SuppressWarnings("unchecked")
        final Collection<Function>[] result = new ArrayList[size];
        for (int i = 0; i < size; i++) {
            result[i] = new ArrayList<>();
            result[i].add(functions.get(i));
        }
        return result;
    }

}
