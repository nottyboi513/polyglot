/*******************************************************************************
 * This file is part of the Polyglot extensible compiler framework.
 *
 * Copyright (c) 2000-2008 Polyglot project group, Cornell University
 * Copyright (c) 2006-2008 IBM Corporation
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This program and the accompanying materials are made available under
 * the terms of the Lesser GNU Public License v2.0 which accompanies this
 * distribution.
 * 
 * The development of the Polyglot project has been supported by a
 * number of funding sources, including DARPA Contract F30602-99-1-0533,
 * monitored by USAF Rome Laboratory, ONR Grant N00014-01-1-0968, NSF
 * Grants CNS-0208642, CNS-0430161, and CCF-0133302, an Alfred P. Sloan
 * Research Fellowship, and an Intel Research Ph.D. Fellowship.
 *
 * See README for contributors.
 ******************************************************************************/

package polyglot.frontend.goals;

import java.util.*;

import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.frontend.Scheduler;
import polyglot.types.TypeSystem;
import polyglot.visit.TypeChecker;

/**
 * a <code>TypeChecked</code> is reached after typechecking.
 */
public class TypeChecked extends VisitorGoal {
	public static Goal create(Scheduler scheduler, Job job, TypeSystem ts,
			NodeFactory nf) {
		return scheduler.internGoal(new TypeChecked(job, ts, nf));
	}

	protected TypeChecked(Job job, TypeSystem ts, NodeFactory nf) {
		super(job, new TypeChecker(job, ts, nf));
	}

	public Collection prerequisiteGoals(Scheduler scheduler) {
		List l = new ArrayList();
		l.add(scheduler.Disambiguated(job));
		l.addAll(super.prerequisiteGoals(scheduler));
		return l;
	}

	// public Collection corequisiteGoals(Scheduler scheduler) {
	// List l = new ArrayList();
	// l.add(scheduler.ConstantsChecked(job));
	// l.addAll(super.corequisiteGoals(scheduler));
	// return l;
	// }
}
