package org.lolongo;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.constraints.Problem;
import javax.constraints.ProblemFactory;
import javax.constraints.Solution;
import javax.constraints.Solver;
import javax.constraints.Var;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionSequencerBinding extends SimpleFunctionSequencer {

	static final Logger logger = LoggerFactory.getLogger(FunctionSequencerBinding.class);

	private ProblemBuilder problemBuilder;

	public FunctionSequencerBinding() {
	}

	public FunctionSequencerBinding(ProblemBuilder problemBuilder) {
		this.problemBuilder = problemBuilder;
	}

	@SuppressWarnings("unchecked")
	private int solve(Problem p) {

		// DataBinding are cached
		final Map<Entry<Function, Context>, Set<Ref<?>>> inputBindingsCache = new HashMap<>();
		final Map<Entry<Function, Context>, Set<Ref<?>>> outputBindingsCache = new HashMap<>();

		final Var[] vars = p.getVars();
		for (final Var i : vars) {
			for (final Var j : vars) {

				// so self constraint
				if (i == j) {
					break;
				}

				final Entry<Function, Context> entry_i = (Entry<Function, Context>) i.getObject();
				final Entry<Function, Context> entry_j = (Entry<Function, Context>) j.getObject();

				// DataBinding are cached
				Set<Ref<?>> inputBindings_i = inputBindingsCache.get(entry_i);
				if (inputBindings_i == null) {
					inputBindings_i = DataBindingUtils.getInputBindings(entry_i);
					inputBindingsCache.put(entry_i, inputBindings_i);
				}
				Set<Ref<?>> outputBindings_i = outputBindingsCache.get(entry_i);
				if (outputBindings_i == null) {
					outputBindings_i = DataBindingUtils.getOutputBindings(entry_i);
					outputBindingsCache.put(entry_i, outputBindings_i);
				}
				Set<Ref<?>> inputBindings_j = inputBindingsCache.get(entry_j);
				if (inputBindings_j == null) {
					inputBindings_j = DataBindingUtils.getInputBindings(entry_j);
					inputBindingsCache.put(entry_j, inputBindings_j);
				}
				Set<Ref<?>> outputBindings_j = outputBindingsCache.get(entry_j);
				if (outputBindings_j == null) {
					outputBindings_j = DataBindingUtils.getOutputBindings(entry_j);
					outputBindingsCache.put(entry_j, outputBindings_j);
				}

				// Native binding rules
				if (!Collections.disjoint(inputBindings_i, outputBindings_j)) {
					p.post(j, "<", i);
					logger.debug(entry_j.getKey() + " < " + entry_i.getKey());
				}
				if (!Collections.disjoint(outputBindings_i, inputBindings_j)) {
					p.post(i, "<", j);
					logger.debug(entry_i.getKey() + " < " + entry_j.getKey());
				}

				// Delegate additional constraints management
				if (problemBuilder != null) {
					problemBuilder.addConstraints(p, i, j);
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

		p.log(vars);
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
			log(sortedFunctions);
		}
		return sortedFunctions;
	}

}
