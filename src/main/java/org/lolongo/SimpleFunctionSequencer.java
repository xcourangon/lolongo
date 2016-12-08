package org.lolongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleFunctionSequencer implements FunctionSequencer {

	private static final Logger logger = LoggerFactory.getLogger(CompositeProcessor.class);

	@Override
	public Collection<Entry<Function, Context>>[] sort(List<Entry<Function, Context>> functions) {
		return pack(functions);
	}

	@Override
	public Collection<Function>[] sort(List<Function> functions, Context context) {
		return developAndPack(functions, context);
	}

	/* [(f1,c),(f2,c),(f3,c)] => [[(f1,c)],[(f2,c)],[(f3,c)]] */
	public static Collection<Entry<Function, Context>>[] pack(List<Entry<Function, Context>> entries) {
		int size = entries.size();
		@SuppressWarnings("unchecked")
		final Collection<Entry<Function, Context>>[] result = new ArrayList[size];
		for (int i = 0; i < size; i++) {
			result[i] = new ArrayList<>();
			result[i].add(entries.get(i));
		}
		if (logger.isDebugEnabled()) {
			log(result);
		}
		return result;
	}

	/* ([f1,f2,f3],c) => [[(f1,c)],[(f2,c)],[(f3,c)]] */
	public static Collection<Function>[] developAndPack(List<Function> functions, Context context) {
		int size = functions.size();
		@SuppressWarnings("unchecked")
		final Collection<Function>[] result = new ArrayList[size];
		for (int i = 0; i < size; i++) {
			result[i] = new ArrayList<Function>();
			result[i].add(functions.get(i));
		}
		if (logger.isDebugEnabled()) {
			log(result);
		}
		return result;
	}

	static <T> void log(final Collection<T>[] result) {
		int step = 1;
		for (Collection<T> list : result) {
			final StringBuilder sb = new StringBuilder("[ ");
			for (T entry : list) {
				sb.append(entry);
				sb.append(" ");
			}
			sb.append("]");
			logger.debug("Step {} [{}] - {}", step++, list.size(), sb.toString());
		}
	}
}
