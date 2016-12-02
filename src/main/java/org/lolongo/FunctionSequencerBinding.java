package org.lolongo;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.constraints.Constraint;
import javax.constraints.Problem;
import javax.constraints.ProblemFactory;
import javax.constraints.Solution;
import javax.constraints.Solver;
import javax.constraints.Var;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionSequencerBinding extends SimpleFunctionSequencer {

	static final Logger logger = LoggerFactory.getLogger(FunctionSequencerBinding.class);

	private static FunctionSequencerBinding instance;

	public static FunctionSequencerBinding getInstance() {
		if (instance == null) {
			instance = new FunctionSequencerBinding();
		}
		return instance;
	}

	protected FunctionSequencerBinding() {
	}

	@SuppressWarnings("unchecked")
	private static int solve(Problem p) {

		final Var[] vars = p.getVars();
		for (final Var i : vars) {
			for (final Var j : vars) {
				if (i == j) {
					break;
				}
				final Entry<Function, Context> entry_i = (Entry<Function, Context>) i.getObject();
				final Context context_i = entry_i.getValue();
				final Function fi = entry_i.getKey();
				final Entry<Function, Context> entry_j = (Entry<Function, Context>) j.getObject();
				final Context context_j = entry_j.getValue();
				final Function fj = entry_j.getKey();

				if (!Collections.disjoint(DataBindingUtils.getInputBindings(context_i, fi), DataBindingUtils.getOutputBindings(context_j, fj))) {
					final Constraint constraint = p.post(j, "<", i);
					logger.debug(fj + " < " + fi);
				}
				if (!Collections.disjoint(DataBindingUtils.getOutputBindings(context_i, fi), DataBindingUtils.getInputBindings(context_j, fj))) {
					p.post(i, "<", j);
					logger.debug(fi + " < " + fj);
				}
				if (fi instanceof CompositeFunction) {
					final CompositeFunction comp = (CompositeFunction) fi;
					if (comp.contains(fj)) {

						p.post(j, "<", i);
						logger.debug(fj + " < " + fi);
					}
				}
				if (fj instanceof CompositeFunction) {
					final CompositeFunction comp = (CompositeFunction) fj;
					if (comp.contains(fi)) {
						p.post(i, "<", j);
						logger.debug(fj + " < " + fj);
					}
				}
			}
		}

		final Var nb_of_steps = p.variable("nb_of_steps", 1, vars.length);
		p.post(nb_of_steps, "=", p.max(vars).plus(1));
		logger.debug("declaring {}", nb_of_steps);

		final Solver solver = p.getSolver();
		final Solution solution = solver.findSolution();
		if (solution == null) {
			throw new DataBindingException("No Solution");
		}

		assert solution.getNumberOfVars() == (vars.length + 1);

		final int steps = nb_of_steps.getValue();
		logger.debug("nb of steps = " + steps);

		return steps;

	}

	@Override
	public Collection<Entry<Function, Context>>[] sort(List<Entry<Function, Context>> entries) {
		final int size = entries.size();
		// Optimization : no constraint computation if less than 2 functions
		if (size < 2) {
			logger.debug("Optimization - no constraint computation (less than 2 functions)");
			return super.sort(entries);
		}

		final int pos_max = size - 1;
		final Problem p = ProblemFactory.newProblem("Sort functions");

		final Var[] pos_f = p.variableArray("pos_f", 0, pos_max, size);

		for (int index = 0; index < size; index++) {
			pos_f[index].setObject(entries.get(index));
			logger.debug("declaring {} for {}", pos_f[index], entries.get(index));
		}

		int steps = solve(p);

		// Initialize sortedFunctions array
		final Collection<Entry<Function, Context>>[] sortedFunctions = new HashSet[steps];
		for (int s = 0; s < steps; s++) {
			sortedFunctions[s] = new HashSet<Entry<Function, Context>>();
		}

		for (final Var v : pos_f) {
			final int pos = v.getValue();
			final Entry<Function, Context> entry = (Entry<Function, Context>) v.getObject();
			sortedFunctions[pos].add(entry);
			logger.debug(v.getObject() + " = " + pos);
		}

		if (logger.isDebugEnabled()) {
			p.log(pos_f);
			log(sortedFunctions);
		}
		return sortedFunctions;
	}

	@Override
	public Collection<Function>[] sort(List<Function> functions, Context context) {
		final int size = functions.size();
		// Optimization : no constraint computation if less than 2 functions
		if (size < 2) {
			logger.debug("Optimization - no constraint computation (less than 2 functions)");
			return super.sort(functions, context);
		}

		final int pos_max = size - 1;
		final Problem p = ProblemFactory.newProblem("Sort functions");

		final Var[] pos_f = p.variableArray("pos_f", 0, pos_max, size);

		for (int index = 0; index < size; index++) {
			pos_f[index].setObject(new SimpleEntry<Function, Context>(functions.get(index), context));
			logger.debug("declaring {} for {}", pos_f[index], functions.get(index));
		}

		int steps = solve(p);

		// Initialize sortedFunctions array
		final Collection<Function>[] sortedFunctions = new ArrayList[steps];
		for (int step = 0; step < steps; step++) {
			sortedFunctions[step] = new ArrayList<>();
		}
		for (final Var v : pos_f) {
			final int pos = v.getValue();
			final Entry<Function, Context> entry = (Entry<Function, Context>) v.getObject();
			sortedFunctions[pos].add(entry.getKey());
			logger.debug(v.getObject() + " = " + pos);
		}

		if (logger.isDebugEnabled()) {
			p.log(pos_f);
			int step = 1;
			for (Collection<Function> list : sortedFunctions) {
				final StringBuilder sb = new StringBuilder("[ ");
				for (Function f : list) {
					sb.append(f);
					sb.append(" ");
				}
				sb.append("]");
				logger.debug("Step {} - {}", step++, sb.toString());
			}
		}
		return sortedFunctions;
	}

}
