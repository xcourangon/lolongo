package org.lolongo;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

public class SimpleFunctionSequencer implements FunctionSequencer {

    /* [(f1,c),(f2,c),(f3,c)] => [[(f1,c)],[(f2,c)],[(f3,c)]] */
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

    /* ([f1,f2,f3],c) => [[(f1,c)],[(f2,c)],[(f3,c)]] */
    @Override
    public Collection<Entry<Function, Context>>[] sort(List<Function> functions, Context context) {
        int size = functions.size();
        @SuppressWarnings("unchecked")
        final Collection<Entry<Function, Context>>[] result = new ArrayList[size];
        for (int i = 0; i < size; i++) {
            result[i] = new ArrayList<Entry<Function, Context>>();
            result[i].add(new AbstractMap.SimpleEntry<Function, Context>(functions.get(i), context));
        }
        return result;
    }

}
