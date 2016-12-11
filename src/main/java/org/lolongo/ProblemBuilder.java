package org.lolongo;

import javax.constraints.Problem;
import javax.constraints.Var;

public interface ProblemBuilder {

	void addConstraints(Problem p, Var v1, Var v2);
}
