package org.lolongo;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

    private static FunctionSequencerBinding instance;

    public static FunctionSequencerBinding getInstance() {
        if (instance == null) {
            instance = new FunctionSequencerBinding();
        }
        return instance;
    }

    protected FunctionSequencerBinding() {
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
                if (!Collections.disjoint(DataBindingUtils.getInputBindings(context, fi), DataBindingUtils.getOutputBindings(context, fj))) {
                    p.post(pos_f[i], ">", pos_f[j]);
                    logger.debug(pos_f[i] + " > " + pos_f[j]);
                }
                if (!Collections.disjoint(DataBindingUtils.getOutputBindings(context, fi), DataBindingUtils.getInputBindings(context, fj))) {
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
