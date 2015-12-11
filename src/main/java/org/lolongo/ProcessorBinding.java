package org.lolongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.constraints.Problem;
import javax.constraints.ProblemFactory;
import javax.constraints.Solution;
import javax.constraints.Solver;
import javax.constraints.Var;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessorBinding extends ProcessorBase {

    static final Logger logger = LoggerFactory.getLogger(ProcessorBinding.class);

    @Override
    public void execute(Context context) throws FunctionException, ContextException {
	final Collection<Function>[] steps = sort(functions, context);
	for (final Collection<Function> step : steps) {
	    super.execute(step, context);
	}
    }

    static protected Collection<Function>[] sort(Collection<Function> functions, Context context)
	    throws DataBindingException {

	final int size = functions.size();
	if (size == 0) {
	    return new ArrayList[0];
	}

	// Optimization : no constraint computation if only one function
	if (size == 1) {
	    logger.debug("Optimization - no constraint computation (only one function)");
	    final Set<Function> sortedFunctions[] = new HashSet[1];
	    sortedFunctions[0] = new HashSet<Function>();
	    sortedFunctions[0].addAll(functions);
	    return sortedFunctions;
	}

	final int pos_max = size - 1;
	final Problem p = ProblemFactory.newProblem("Sort functions");

	final Var[] pos_f = new Var[size];

	final Var nb_of_steps = p.variable("nb_of_steps", 1, size);

	int i = 0;
	for (final Function fi : functions) {
	    pos_f[i] = p.variable("pos_f[" + i + "]", 0, pos_max);
	    pos_f[i].setObject(fi);
	    logger.debug("declaring " + pos_f[i] + "(" + pos_f[i].getObject() + ")");
	    int j = 0;
	    for (final Function fj : functions) {
		if (j == i) {
		    break;
		}
		if (!Collections.disjoint(DataBindingUtils.getInputBindings(context, fi),
			DataBindingUtils.getOutputBindings(context, fj))) {
		    p.post(pos_f[i], ">", pos_f[j]);
		    logger.debug(pos_f[i] + " > " + pos_f[j]);
		}
		if (!Collections.disjoint(DataBindingUtils.getOutputBindings(context, fi),
			DataBindingUtils.getInputBindings(context, fj))) {
		    p.post(pos_f[i], "<", pos_f[j]);
		    logger.debug(pos_f[i] + " < " + pos_f[j]);
		}
		j++;
	    }
	    i++;
	}
	p.post(nb_of_steps, "=", p.max(pos_f).plus(1));
	logger.debug("declaring {}", nb_of_steps);

	final Solver solver = p.getSolver();
	final Solution solution = solver.findSolution();
	if (solution == null) {
	    throw new DataBindingException("No Solution");
	}

	assert solution.getNumberOfVars() == (size + 1);

	// Initialize sortedFunctions array
	logger.debug("nb of steps = " + nb_of_steps.getValue());
	final int steps = nb_of_steps.getValue();
	final Set<Function> sortedFunctions[] = new HashSet[steps];
	for (int s = 0; s < steps; s++) {
	    sortedFunctions[s] = new HashSet<Function>();
	}

	int f = 0;
	for (final Function function : functions) {
	    final int pos = pos_f[f].getValue();
	    sortedFunctions[pos].add(function);
	    logger.debug(pos_f[f].getObject() + " = " + pos_f[f].getValue());
	    f++;
	}

	if (logger.isDebugEnabled()) {
	    int stepIndex = 0;
	    for (final Set<Function> step : sortedFunctions) {
		logger.debug("Step " + stepIndex + " => " + step);
		stepIndex++;
	    }
	}
	return sortedFunctions;
    }

}
